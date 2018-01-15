package com.alanjwilliams.engage.rest.controller;

import com.alanjwilliams.engage.entity.Expense;
import com.alanjwilliams.engage.rest.dto.ExpenseDto;
import com.alanjwilliams.engage.rest.service.ExpenseDtoService;
import com.alanjwilliams.engage.service.ExpenseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Expense write controller providing write access to the Expense service.
 */
@Slf4j
@RestController
@RequestMapping(value = "/app/expenses")
public class ExpenseWriteController {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private ExpenseDtoService expenseDtoService;

    /**
     * Save an Expense to the repository.
     * @param expenseDto ExpenseDto.
     * @return ResponseEntity.
     */
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ExpenseDto> save(@RequestBody @Valid ExpenseDto expenseDto) {
        log.debug("Received save(ExpenseDto) request.");
        Expense savedExpense = expenseService.save(expenseDtoService.toExpense(expenseDto));
        log.debug("Saved expense {}", savedExpense);
        ExpenseDto savedExpenseDto = expenseDtoService.fromExpense(savedExpense);
        return new ResponseEntity<>(savedExpenseDto, HttpStatus.CREATED);
    }
}
