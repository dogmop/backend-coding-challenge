package com.alanjwilliams.engage.service;

import java.math.BigDecimal;

/**
 * Currency Service methods.
 */
public interface CurrencyService {


    /**
     * Convert the value from the source currency into the destination currency.
     *
     * @param value Value to convert.
     * @param sourceCurrency Source currency ISO-4217 code.
     * @param destinationCurrency Destination currency ISO-4217 code.
     * @return Value converted to the destination currency equivilant value.
     */
    BigDecimal convert(BigDecimal value, String sourceCurrency, String destinationCurrency);
}
