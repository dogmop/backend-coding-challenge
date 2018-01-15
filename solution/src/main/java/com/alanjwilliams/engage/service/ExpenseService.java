package com.alanjwilliams.engage.service;

import com.alanjwilliams.engage.entity.Expense;

import java.util.List;

/**
 * Expense Entity Service Methods.
 */
public interface ExpenseService {

    /**
     * Get all Expense entity objects.
     * @return List of expense entity objects.
     */
    List<Expense> getAll();

    /**
     * Save an expense entity object.
     * @param expense Expense entity object to save.
     * @return The saved Expense entity.
     */
    Expense save(Expense expense);
}
