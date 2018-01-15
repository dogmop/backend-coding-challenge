package com.alanjwilliams.engage;

import com.alanjwilliams.engage.entity.Expense;
import com.alanjwilliams.engage.repository.ExpenseRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests using the '/app/expenses' endpoint.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("integrationtest")
public class ExpensesIntegrationTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ExpenseRepository repository;

    @After
    public void tearDown() {
        // Clear the repository after each test.
        repository.deleteAll();
    }

    @Test
    public void getExpenses_noValues()
            throws Exception {
        // Given no existing expenses in the datastore, return an empty json array when performing GET.
        mvc.perform(get("/app/expenses")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string("[]"));
    }

    @Test
    public void getExpenses_values()
            throws Exception {
        // Given an expense object in the datastore
        Expense expense = new Expense();
        expense.setDate(LocalDate.of(2018, 2, 1));
        expense.setAmount(new BigDecimal("10.00"));
        expense.setVat(new BigDecimal("2.00"));
        expense.setReason("Test 1");
        repository.save(expense);
        // When we get expenses, the expense object is returned as JSON.
        mvc.perform(get("/app/expenses")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$[0].date", is("01/02/2018")))
                .andExpect(jsonPath("$[0].amount", is(expense.getAmount().toString())))
                .andExpect(jsonPath("$[0].vat", is(expense.getVat().toString())))
                .andExpect(jsonPath("$[0].reason", is(expense.getReason())));
    }

    @Test
    public void postExpenses()
            throws Exception {
        // Given a request JSON string with a date, amount and reason of an expense.
        String requestJson = "{ \"date\": \"01/02/2018\", \"amount\": \"10.00\", \"reason\": \"Test\"}";

        // When the request JSON is posted to the expenses endpoint.
        mvc.perform(post("/app/expenses")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        // Then the single expense is saved in the datastore with the correct values.
        List<Expense> expenseList = repository.findAll();
        assertEquals(1, expenseList.size());
        Expense savedExpense = expenseList.get(0);
        assertEquals(LocalDate.of(2018, 2, 1), savedExpense.getDate());
        assertEquals(new BigDecimal("10.00"), savedExpense.getAmount());
        assertEquals(new BigDecimal("2.00"), savedExpense.getVat()); // Ensure VAT is calculated at 20%
        assertEquals("Test", savedExpense.getReason());
    }

    @Test
    public void postExpenses_withCurrencyConversion()
            throws Exception {
        // Given a request JSON string with the amount in EUR with a comma delimiter.
        String requestJson = "{ \"date\": \"25/12/2017\", \"amount\": \"12,00 EUR\", \"reason\": \"Test EUR\"}";

        // When the request JSON is posted to the expenses endpoint.
        mvc.perform(post("/app/expenses")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        // Then the single expense is saved in the datastore with the correct values.
        List<Expense> expenseList = repository.findAll();
        assertEquals(1, expenseList.size());
        Expense savedExpense = expenseList.get(0);
        assertEquals(LocalDate.of(2017, 12, 25), savedExpense.getDate());
        // Since the currency rate can fluctuate we just check to ensure it is not zero - assuming no currency exchange rate would ever be zero!
        assertTrue(savedExpense.getAmount().compareTo(BigDecimal.ZERO) > 0);
        assertTrue(savedExpense.getVat().compareTo(BigDecimal.ZERO) > 0);
        assertEquals("Test EUR", savedExpense.getReason());
    }

    @Test
    public void postExpenses_invalidJson()
            throws Exception {
        // Given a request JSON string which is invalid.
        String requestJson = "Not valid JSON";

        // When the request JSON is posted to the expenses endpoint, a bad request code is expected.
        mvc.perform(post("/app/expenses")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        // Then nothing is saved to the repository.
        List<Expense> expenseList = repository.findAll();
        assertEquals(0, expenseList.size());
    }

    @Test
    public void postExpenses_invalidDate()
            throws Exception {
        // Given a request JSON string which has an invalid date (for the datastore).
        String requestJson = "{ \"date\": \"01/02/10000\", \"amount\": \"10.00\", \"reason\": \"Test\"}";

        // When the request JSON is posted to the expenses endpoint, a bad request code is expected.
        mvc.perform(post("/app/expenses")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        // Then nothing is saved to the repository.
        List<Expense> expenseList = repository.findAll();
        assertEquals(0, expenseList.size());
    }

    @Test
    public void postExpenses_invalidAmount()
            throws Exception {
        // Given a request JSON string which has an invalid amount.
        String requestJson = "{ \"date\": \"01/02/2018\", \"amount\": \"blah\", \"reason\": \"Test\"}";

        // When the request JSON is posted to the expenses endpoint, a bad request code is expected.
        mvc.perform(post("/app/expenses")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        // Then nothing is saved to the repository.
        List<Expense> expenseList = repository.findAll();
        assertEquals(0, expenseList.size());
    }

    @Test
    public void postExpenses_invalidCurrency()
            throws Exception {
        // Given a request JSON string which has an invalid amount currency code.
        String requestJson = "{ \"date\": \"01/02/2018\", \"amount\": \"10.00 XXX\", \"reason\": \"Test\"}";

        // When the request JSON is posted to the expenses endpoint, an internal server error code is expected.
        mvc.perform(post("/app/expenses")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

        // Then nothing is saved to the repository.
        List<Expense> expenseList = repository.findAll();
        assertEquals(0, expenseList.size());
    }
}
