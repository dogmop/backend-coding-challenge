package com.alanjwilliams.engage.rest.service;

import com.alanjwilliams.engage.entity.Expense;
import com.alanjwilliams.engage.rest.dto.ExpenseDto;

/**
 * The ExpenseDto service provides service methods for {@link ExpenseDto}.
 */
public interface ExpenseDtoService {

    /**
     * Convert the ExpenseDto object into an Expense entity object.
     *
     * @param expenseDto ExpenseDto to convert.
     * @return Expense created from the ExpenseDto values.
     */
    Expense toExpense(ExpenseDto expenseDto);

    /**
     * Conver the Expense entity object into an ExpenseDto object.
     * @param expense Expense entity object.
     * @return ExpenseDto created from the Expense values.
     */
    ExpenseDto fromExpense(Expense expense);
}
