package com.alanjwilliams.engage.rest.controller;

import com.alanjwilliams.engage.rest.dto.ExpenseDto;
import com.alanjwilliams.engage.rest.service.ExpenseDtoService;
import com.alanjwilliams.engage.service.ExpenseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Expense read controller providing read-only access to the Expense service.
 *
 */
@Slf4j
@RestController // RestController simplifies by including the Controller and RequestBody annotations
@RequestMapping(value = "/app/expenses")
public class ExpenseReadController {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private ExpenseDtoService expenseDtoService;

    /**
     * Get all the Expense objects from the repository.
     *
     * @return List of ExpenseDto.
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ExpenseDto> getAll() {
        log.debug("Received getAll() request.");
        // Get all the expense objects from the repository and convert to a list of DTO.
        List<ExpenseDto> expenseDtoList = expenseService.getAll()
                .stream()
                .map(expenseDtoService::fromExpense)
                .collect(Collectors.toList());
        log.debug("Returning {} expense objects.", expenseDtoList.size());
        return expenseDtoList;
    }
}
