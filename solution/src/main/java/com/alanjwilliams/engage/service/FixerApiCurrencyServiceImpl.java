package com.alanjwilliams.engage.service;

import com.alanjwilliams.engage.exception.InternalServerErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.LinkedHashMap;

/**
 * Implementation of the Currency Service to work with the <a href="http://fixer.io/">Fixer.io</a> currency conversion
 * API.
 */
@Slf4j
@Service
public class FixerApiCurrencyServiceImpl implements CurrencyService {

    @Autowired
    RestTemplate restTemplate;

    /**
     * Fixer.io API URL.
     */
    @Value("${currency.api.url}")
    String currencyApiUrl;

    @Override
    public BigDecimal convert(BigDecimal value, String sourceCurrency, String destinationCurrency) {
        log.debug("Converting value '{}' from '{}' currency to '{}' currency.", value, sourceCurrency, destinationCurrency);
        BigDecimal exchangeRate = getGetExchangeRate(sourceCurrency, destinationCurrency);
        BigDecimal converted = value.multiply(exchangeRate).setScale(2, BigDecimal.ROUND_HALF_UP);
        log.debug("Value converted to '{} {}'.", value, destinationCurrency);
        return converted;
    }

    /**
     * Get the exchange rate from the Fixer API of the source to the destination currency.
     *
     * @param sourceCurrency Source Currency Code.
     * @param destinationCurrency Destination Currency Code.
     * @return Exchange Rate.
     */
    private BigDecimal getGetExchangeRate(String sourceCurrency, String destinationCurrency) {
        // Load the data from the currency API.
        String url = MessageFormat.format(currencyApiUrl, sourceCurrency, destinationCurrency);
        log.debug("Looking up currency exchange rate for '{}' to '{}'", sourceCurrency, destinationCurrency);
        try {
            LinkedHashMap<String, Object> response = restTemplate.getForObject(url, LinkedHashMap.class);
            // Ensure the 'base' of the response is indeed our intended source currency.
            String baseCurrency = (String) response.get("base");
            if (!sourceCurrency.equals(baseCurrency)) {
                throw new InternalServerErrorException(
                        MessageFormat.format(
                                "Base currency ({0}) in response from API does not match requested source currency ({1}).",
                                baseCurrency, sourceCurrency));
            }
            // Read the rate(s) from the response.
            LinkedHashMap<String, Double> rates = (LinkedHashMap<String, Double>) response.get("rates");
            if (rates == null || rates.isEmpty()) {
                throw new InternalServerErrorException(MessageFormat.format(
                        "Destination currency ({0}) not found in response from API.", destinationCurrency
                ));
            }
            Double exchangeRate = rates.get(destinationCurrency);
            log.debug("Currency exchange rate for '{}' to '{}' is: {}", sourceCurrency, destinationCurrency, exchangeRate);
            return new BigDecimal(exchangeRate);
        } catch (RestClientException e) {
            log.error("Error communicating with fixer currency exchange API.", e);
            throw new InternalServerErrorException("An error occurred when communicating with currency API.");
        }
    }
}
