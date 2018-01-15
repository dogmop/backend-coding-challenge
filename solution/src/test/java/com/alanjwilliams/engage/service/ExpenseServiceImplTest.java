package com.alanjwilliams.engage.service;

import com.alanjwilliams.engage.entity.Expense;
import com.alanjwilliams.engage.repository.ExpenseRepository;
import com.alanjwilliams.engage.test.TestUtility;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ExpenseServiceImpl}
 */
@RunWith(PowerMockRunner.class)
public class ExpenseServiceImplTest {

    @Mock
    ExpenseRepository expenseRepository;

    @InjectMocks
    ExpenseServiceImpl expenseService;

    @Test
    public void getAll() throws Exception {
        // Given a list of expenses returned from the repository
        List<Expense> expenseList = TestUtility.generateExpenseList(5);
        when(expenseRepository.findAll()).thenReturn(expenseList);
        // When get all is called on the service
        List<Expense> returnedList = expenseService.getAll();
        // Then the list returned is the list from the repository.
        assertNotNull(returnedList);
        assertEquals(expenseList, returnedList);
        // and the expense repository was accessed
        verify(expenseRepository, times(1)).findAll();
    }

    @Test
    public void save() throws Exception {
        // Given an expense object
        Expense expense = new Expense();
        // When the object is saved
        expenseService.save(expense);
        // Then the object was saved in the repository
        verify(expenseRepository, times(1)).save(eq(expense));
    }
}