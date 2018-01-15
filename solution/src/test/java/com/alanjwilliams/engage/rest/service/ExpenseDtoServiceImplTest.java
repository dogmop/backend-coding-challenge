package com.alanjwilliams.engage.rest.service;

import com.alanjwilliams.engage.entity.Expense;
import com.alanjwilliams.engage.exception.BadRequestException;
import com.alanjwilliams.engage.rest.dto.ExpenseDto;
import com.alanjwilliams.engage.service.CurrencyService;
import com.alanjwilliams.engage.util.VatCalculationUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Test class for {@link ExpenseDtoServiceImpl}.
 */
@PrepareForTest(ExpenseDtoServiceImpl.class)
@RunWith(PowerMockRunner.class)
public class ExpenseDtoServiceImplTest {

    @Mock
    CurrencyService currencyService;

    @InjectMocks
    ExpenseDtoServiceImpl expenseDtoService;

    @Before
    public void setUp() throws Exception {
        mockStatic(VatCalculationUtil.class);
    }

    @Test
    public void toExpense() throws Exception {
        // Given an expense DTO
        ExpenseDto expenseDto = generateExpenseDto(LocalDate.of(2018, 2, 1), "10.00", "Test 1");
        // When converted to an Expense
        Expense expense = expenseDtoService.toExpense(expenseDto);
        // Then the values of the expense are correctly set from the expense DTO.
        assertExpenseValues(expense, expenseDto.getDate(), new BigDecimal(expenseDto.getAmount()), expenseDto.getReason());
        // And the currency service is not called
        verify(currencyService, never()).convert(any(), any(), any());
        // And VAT calculation is called.
        verifyStatic(times(1));
        VatCalculationUtil.calculateVat(eq(new BigDecimal(expenseDto.getAmount())));
    }

    @Test(expected = BadRequestException.class)
    public void toExpense_amountComma() throws Exception {
        // Given an expense DTO with a comma in the amount as a decimal delimiter
        ExpenseDto expenseDto = generateExpenseDto(LocalDate.of(2018, 2, 1), "10,00", "Test amountComma");
        // When converted to an Expense
        expenseDtoService.toExpense(expenseDto);
        // Then a bad request exception is thrown
    }

    @Test
    public void toExpense_amountOnly() throws Exception {
        // Given an expense DTO with only an amount value set.
        ExpenseDto expenseDto = generateExpenseDto(null, "10.00", null);
        // When converted to an Expense
        Expense expense = expenseDtoService.toExpense(expenseDto);
        // Then the values of the expense are correctly set from the expense DTO.
        assertExpenseValues(expense, expenseDto.getDate(), new BigDecimal(expenseDto.getAmount()), expenseDto.getReason());
        // And the currency service is not called
        verify(currencyService, never()).convert(any(), any(), any());
        // And VAT calculation is called.
        verifyStatic(times(1));
        VatCalculationUtil.calculateVat(eq(new BigDecimal(expenseDto.getAmount())));
    }

    @Test
    public void toExpense_null() throws Exception {
        // When null is converted to an Expense
        Expense expense = expenseDtoService.toExpense(null);
        // Then a null expense is returned
        assertNull(expense);
    }

    @Test(expected = BadRequestException.class)
    public void toExpense_nullValues() throws Exception {
        // Given an expense DTO with null values
        ExpenseDto expenseDto = generateExpenseDto(null, null, null);
        // When converted to an Expense
        expenseDtoService.toExpense(expenseDto);
        // Then a bad request exception is thrown
    }

    @Test
    public void toExpense_EUR() throws Exception {
        // Given an expense DTO with a comma in the amount as a delimiter and a currency code following
        ExpenseDto expenseDto = generateExpenseDto(LocalDate.of(2018, 2, 1), "10,00 EUR", "Test EUR");
        // And an expected amount returned from the currency service.
        BigDecimal expectedAmount = new BigDecimal("8.50");
        when(currencyService.convert(eq(new BigDecimal("10.00")), eq("EUR"), eq("GBP"))).thenReturn(expectedAmount);
        // When converted to an Expense
        Expense expense = expenseDtoService.toExpense(expenseDto);
        // Then the values of the expense are correctly set from the expense DTO.
        assertExpenseValues(expense, expenseDto.getDate(), new BigDecimal("8.50"), expenseDto.getReason());
        // And the currency service is called.
        verify(currencyService, times(1)).convert(eq(new BigDecimal("10.00")), eq("EUR"), eq("GBP"));
        // And VAT calculation is called.
        verifyStatic(times(1));
        VatCalculationUtil.calculateVat(eq(expectedAmount));
    }

    @Test
    public void toExpense_Currency() throws Exception {
        // Given an expense DTO with a currency code set
        ExpenseDto expenseDto = generateExpenseDto(LocalDate.of(2018, 2, 1), "10.00 EUR", "Test Currency");
        // And an expected amount returned from the currency service.
        BigDecimal expectedAmount = new BigDecimal("8.50");
        when(currencyService.convert(eq(new BigDecimal("10.00")), eq("EUR"), eq("GBP"))).thenReturn(expectedAmount);
        // When converted to an Expense
        Expense expense = expenseDtoService.toExpense(expenseDto);
        // Then the values of the expense are correctly set from the expense DTO.
        assertExpenseValues(expense, expenseDto.getDate(), new BigDecimal("8.50"), expenseDto.getReason());
        // And the currency service is called.
        verify(currencyService, times(1)).convert(eq(new BigDecimal("10.00")), eq("EUR"), eq("GBP"));
        // And VAT calculation is called.
        verifyStatic(times(1));
        VatCalculationUtil.calculateVat(eq(expectedAmount));
    }

    @Test
    public void fromExpense() throws Exception {
        // Given an expense object
        Expense expense = generateExpense(LocalDate.now(), new BigDecimal("10.00"), new BigDecimal("2.00"), "Test Expense");
        // When converted to an Expense DTO object
        ExpenseDto expenseDto = expenseDtoService.fromExpense(expense);
        // Then the values are correctly set.
        assertExpenseDtoValues(expenseDto, expense.getDate(), "10.00", "2.00", "Test Expense");
    }

    @Test
    public void fromExpense_nullValues() throws Exception {
        // Given an expense object with null values
        Expense expense = generateExpense(null, null, null, null);
        // When converted to an Expense DTO object
        ExpenseDto expenseDto = expenseDtoService.fromExpense(expense);
        // Then the values are correctly set.
        assertExpenseDtoValues(expenseDto, null, null, null, null);
    }

    @Test
    public void fromExpense_null() throws Exception {
        // When null is converted to an Expense DTO object
        ExpenseDto expenseDto = expenseDtoService.fromExpense(null);
        // Then null is returned.
        assertNull(expenseDto);
    }

    private Expense generateExpense(LocalDate localDate, BigDecimal amount, BigDecimal vat, String reason) {
        Expense expense = new Expense();
        expense.setDate(localDate);
        expense.setAmount(amount);
        expense.setVat(vat);
        expense.setReason(reason);
        return expense;
    }

    private ExpenseDto generateExpenseDto(LocalDate localDate, String amount, String reason) {
        ExpenseDto expenseDto = new ExpenseDto();
        expenseDto.setDate(localDate);
        expenseDto.setAmount(amount);
        expenseDto.setReason(reason);
        return expenseDto;
    }

    private void assertExpenseValues(Expense expense, LocalDate expectedLocalDate, BigDecimal expectedAmount, String expectedReason) {
        assertNotNull(expense);
        assertEquals(expectedLocalDate, expense.getDate());
        assertEquals(expectedAmount, expense.getAmount());
        assertEquals(expectedReason, expense.getReason());
    }

    private void assertExpenseDtoValues(ExpenseDto expenseDto, LocalDate expectedLocalDate, String expectedAmount, String expectedVat, String expectedReason) {
        assertNotNull(expenseDto);
        assertEquals(expectedLocalDate, expenseDto.getDate());
        assertEquals(expectedAmount, expenseDto.getAmount());
        assertEquals(expectedVat, expenseDto.getVat());
        assertEquals(expectedReason, expenseDto.getReason());
    }
}