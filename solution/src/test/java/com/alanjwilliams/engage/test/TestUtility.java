package com.alanjwilliams.engage.test;

import com.alanjwilliams.engage.entity.Expense;

import java.util.ArrayList;
import java.util.List;

public class TestUtility {
    private TestUtility() {

    }

    /**
     * Generate a list of empty expense objects to the provided size.
     * @param size Size of list to generate.
     * @return List of expense objects.
     */
    public static List<Expense> generateExpenseList(int size) {
        List<Expense> expenseList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            expenseList.add(new Expense());
        }
        return expenseList;
    }
}
