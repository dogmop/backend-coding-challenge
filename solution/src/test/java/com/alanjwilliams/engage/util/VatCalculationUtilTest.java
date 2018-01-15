package com.alanjwilliams.engage.util;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test class for {@link VatCalculationUtil}.
 */
public class VatCalculationUtilTest {
    @Test
    public void calculateVat() {
        assertEquals(new BigDecimal("2.00"), VatCalculationUtil.calculateVat(new BigDecimal("10.00")));
        assertEquals(new BigDecimal("48.22"), VatCalculationUtil.calculateVat(new BigDecimal("241.12")));
    }

    @Test
    public void calculateVat_zero() {
        assertEquals(new BigDecimal("0.00"), VatCalculationUtil.calculateVat(new BigDecimal("0.00")));
    }

    @Test
    public void calculateVat_decimalplaces() {
        assertEquals(new BigDecimal("24.69"), VatCalculationUtil.calculateVat(new BigDecimal("123.45678")));
    }

    @Test
    public void calculateVat_negative() {
        assertEquals(new BigDecimal("-2.00"), VatCalculationUtil.calculateVat(new BigDecimal("-10.00")));
    }

    @Test
    public void calculateVat_null() {
        assertNull(VatCalculationUtil.calculateVat(null));
    }

}