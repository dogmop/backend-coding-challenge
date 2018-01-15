package com.alanjwilliams.engage.service;

import com.alanjwilliams.engage.exception.InternalServerErrorException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Test class for {@link FixerApiCurrencyServiceImpl}.
 */
@RunWith(PowerMockRunner.class)
public class FixerApiCurrencyServiceImplTest {

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    FixerApiCurrencyServiceImpl currencyService;

    @Before
    public void setUp() throws Exception {
        ReflectionTestUtils.setField(currencyService, "currencyApiUrl", "http://test");
    }

    @Test
    public void convert() throws Exception {
        // Successfully convert values
        testConvert(new BigDecimal("10.00"), "EUR", "GBP", "0.8", new BigDecimal("8.00"));
        testConvert(new BigDecimal("10.00"), "GBP", "EUR", "1.2", new BigDecimal("12.00"));
        testConvert(new BigDecimal("25.25"), "EUR", "GBP", "0.8", new BigDecimal("20.20"));
        // And check the calls to the endpoint were made
        verify(restTemplate, times(3)).getForObject(eq("http://test"), eq(LinkedHashMap.class));
    }

    @Test(expected = InternalServerErrorException.class)
    public void convert_differenceSource() throws Exception {
        // Given a value and 2 currencies.
        BigDecimal value = new BigDecimal("10.00");
        String sourceCurrency = "EUR";
        String destCurrency = "GBP";
        // and return a response from the mock with a different base currency
        when(restTemplate.getForObject(eq("http://test"), eq(LinkedHashMap.class))).thenReturn(createResponse("USD", destCurrency, "0.8"));
        // When the value is converted
        currencyService.convert(value, sourceCurrency, destCurrency);
        // Then an internal server error exception is thrown.
    }

    @Test(expected = InternalServerErrorException.class)
    public void convert_nullRates() throws Exception {
        // Given a value and 2 currencies.
        BigDecimal value = new BigDecimal("10.00");
        String sourceCurrency = "EUR";
        String destCurrency = "GBP";
        // and return a response from the mock with no rates
        when(restTemplate.getForObject(eq("http://test"), eq(LinkedHashMap.class))).thenReturn(createResponse(sourceCurrency, null, null));
        // When the value is converted
        currencyService.convert(value, sourceCurrency, destCurrency);
        // Then an internal server error exception is thrown.
    }

    @Test(expected = InternalServerErrorException.class)
    public void convert_restClientError() throws Exception {
        // Given a value and 2 currencies.
        BigDecimal value = new BigDecimal("10.00");
        String sourceCurrency = "EUR";
        String destCurrency = "GBP";
        // and return a response from the mock which throws a RestClientException
        when(restTemplate.getForObject(eq("http://test"), eq(LinkedHashMap.class))).thenThrow(new RestClientException("Error"));
        // When the value is converted
        currencyService.convert(value, sourceCurrency, destCurrency);
        // Then an internal server error exception is thrown.
    }

    /**
     * Test that the value is converted to the destination rate.
     *
     * @param value          Value to convert.
     * @param sourceCurrency Source currency code to use.
     * @param destCurrency   Destination currency code to use.
     * @param destRate       Destination currency rate.
     * @param expectedValue  Expected value after conversion.
     */
    private void testConvert(BigDecimal value, String sourceCurrency, String destCurrency, String destRate, BigDecimal expectedValue) {
        // Given a response returned from the mock endpoint
        when(restTemplate.getForObject(eq("http://test"), eq(LinkedHashMap.class))).thenReturn(createResponse(sourceCurrency, destCurrency, destRate));
        // When the value is converted
        BigDecimal convertedValue = currencyService.convert(value, sourceCurrency, destCurrency);
        // Then the value is as expected
        assertNotNull(convertedValue);
        assertEquals(expectedValue, convertedValue);
    }

    /**
     * Create a response which is returned from the mock endpoint.
     *
     * @param baseCurrency Base currency code.
     * @param rateCurrency Rate currency code.
     * @param rate         Currency rate.
     * @return Response as hash map.
     */
    private LinkedHashMap createResponse(String baseCurrency, String rateCurrency, String rate) {
        LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("base", baseCurrency);
        if (rateCurrency != null) {
            LinkedHashMap<String, Double> rates = new LinkedHashMap<>();
            rates.put(rateCurrency, Double.valueOf(rate));
            linkedHashMap.put("rates", rates);
        }
        return linkedHashMap;
    }

}