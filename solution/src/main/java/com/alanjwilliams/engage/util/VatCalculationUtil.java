package com.alanjwilliams.engage.util;

import java.math.BigDecimal;

/**
 * VAT Calculation Utility.
 */
public class VatCalculationUtil {
    /**
     * Fixed 20% VAT rate.
     */
    private static final BigDecimal vatRate = new BigDecimal("0.2");

    private VatCalculationUtil() {
        // Private constructor.
    }

    /**
     * Calculate the amount of VAT in the BigDecimal supplied.
     *
     * @param bigDecimal BigDecimal to calculate VAT from.
     * @return Amount of VAT.
     */
    public static BigDecimal calculateVat(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            return null;
        }
        return bigDecimal.multiply(vatRate).setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
