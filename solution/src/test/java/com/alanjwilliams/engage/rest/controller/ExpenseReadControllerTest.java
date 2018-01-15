package com.alanjwilliams.engage.rest.controller;

import com.alanjwilliams.engage.entity.Expense;
import com.alanjwilliams.engage.rest.dto.ExpenseDto;
import com.alanjwilliams.engage.rest.service.ExpenseDtoService;
import com.alanjwilliams.engage.service.ExpenseService;
import com.alanjwilliams.engage.test.TestUtility;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link ExpenseReadController}.
 */
@RunWith(PowerMockRunner.class)
public class ExpenseReadControllerTest {

    @Mock
    ExpenseService expenseService;

    @Mock
    ExpenseDtoService expenseDtoService;

    @InjectMocks
    ExpenseReadController expenseReadController;

    @Test
    public void getAll() throws Exception {
        // Given 5 expense objects in the repository
        when(expenseService.getAll()).thenReturn(TestUtility.generateExpenseList(5));
        // And any expense object is converted into expense DTO
        when(expenseDtoService.fromExpense(any(Expense.class))).thenReturn(new ExpenseDto());
        // When get all is called
        List<ExpenseDto> expenseDtoList = expenseReadController.getAll();
        // Then a list of 5 expense DTO should be returned
        assertNotNull(expenseDtoList);
        assertEquals(5, expenseDtoList.size());
    }
}