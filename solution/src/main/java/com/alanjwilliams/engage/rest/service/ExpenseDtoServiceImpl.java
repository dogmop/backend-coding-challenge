package com.alanjwilliams.engage.rest.service;

import com.alanjwilliams.engage.entity.Expense;
import com.alanjwilliams.engage.exception.BadRequestException;
import com.alanjwilliams.engage.rest.dto.ExpenseDto;
import com.alanjwilliams.engage.service.CurrencyService;
import com.alanjwilliams.engage.util.VatCalculationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The ExpenseDto service provides service methods for {@link ExpenseDto}.
 */
@Slf4j
@Service
public class ExpenseDtoServiceImpl implements ExpenseDtoService {

    @Autowired
    CurrencyService currencyService;

    /**
     * The default currency to use within the application.
     */
    private static final String defaultCurrency = "GBP";

    @Override
    public Expense toExpense(ExpenseDto expenseDto) {
        // Handle null value.
        if (expenseDto == null) {
            return null;
        }

        Expense expense = new Expense();
        expense.setDate(expenseDto.getDate());
        expense.setAmount(processAmount(expenseDto.getAmount()));
        expense.setVat(VatCalculationUtil.calculateVat(expense.getAmount()));
        expense.setReason(expenseDto.getReason());

        return expense;
    }

    @Override
    public ExpenseDto fromExpense(Expense expense) {
        // Handle null value.
        if (expense == null) {
            return null;
        }

        ExpenseDto expenseDto = new ExpenseDto();
        expenseDto.setDate(expense.getDate());
        expenseDto.setAmount(expense.getAmount() != null ? expense.getAmount().toString() : null);
        expenseDto.setVat(expense.getVat() != null ? expense.getVat().toString() : null);
        expenseDto.setReason(expense.getReason());

        return expenseDto;
    }

    /**
     * Process the amount string to return a BigDecimal.
     *
     * If the string ends with a ISO-4217 code, the value is converted to GBP.
     *
     * @param amount Amount string, eg '10.00' or '12,00 EUR'.
     * @return BigDecimal value of string.
     */
    private BigDecimal processAmount(String amount) {
        if (amount != null) {
            String currency = defaultCurrency;
            String trimmedAmount = amount.trim();

            Pattern pattern = Pattern.compile("(^\\d*[\\.\\,]?\\d*)|([A-Za-z]{3})");
            Matcher matcher = pattern.matcher(trimmedAmount);

            String valueString = "";
            // Any first match should be the value.
            if (matcher.find()) {
                valueString = matcher.group();
            }
            // If there is a second match, it is the currency code.
            if (matcher.find()) {
                currency = matcher.group();
                // Replace any commas (EU number format) with period for BigDecimal.
                valueString = valueString.replaceAll(",", ".");
            }

            try {
                BigDecimal value = new BigDecimal(valueString);
                // Perform value currency conversion to the default currency if the currency differs from the default.
                if (!currency.equals(defaultCurrency)) {
                    // Depending how you feel, you could check to see if this is an 'allowed' currency to prevent lookups
                    // for invalid currencies, ie. 'XXX'. However, I've left this open to interpretation for now as an
                    // invalid currency code will just return an internal server exception to the API anyway.
                    value = currencyService.convert(value, currency, defaultCurrency);
                }
                return value;
            } catch (NumberFormatException e) {
                throw new BadRequestException(MessageFormat.format("Invalid amount specified: {0}", valueString));
            }
        } else {
            throw new BadRequestException("No amount specified.");
        }
    }
}
