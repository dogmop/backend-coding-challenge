package com.alanjwilliams.engage.rest.controller;

import com.alanjwilliams.engage.entity.Expense;
import com.alanjwilliams.engage.rest.dto.ExpenseDto;
import com.alanjwilliams.engage.rest.service.ExpenseDtoService;
import com.alanjwilliams.engage.service.ExpenseService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link ExpenseWriteController}.
 */
@RunWith(PowerMockRunner.class)
public class ExpenseWriteControllerTest {

    @Mock
    ExpenseService expenseService;

    @Mock
    ExpenseDtoService expenseDtoService;

    @InjectMocks
    ExpenseWriteController expenseWriteController;

    @Test
    public void save() throws Exception {
        // Given expense DTO object saved as an expense object
        ExpenseDto expenseDto = new ExpenseDto();
        Expense expense = new Expense();
        when(expenseDtoService.toExpense(eq(expenseDto))).thenReturn(expense);
        when(expenseDtoService.fromExpense(eq(expense))).thenReturn(new ExpenseDto());
        // When the expense DTO is saved
        ResponseEntity response = expenseWriteController.save(expenseDto);
        // Then the response is valid and has status code 201 (CREATED)
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        // And the save method was called on the expense service.
        verify(expenseService, times(1)).save(eq(expense));
    }

}