package com.alanjwilliams.engage.converter;

import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Test class for {@link LocalDateConverter}
 */
public class LocalDateConverterTest {

    LocalDateConverter localDateConverter;
    LocalDate localDate;

    @Before
    public void setUp() {
        localDateConverter = new LocalDateConverter();
        localDate = LocalDate.now();
    }

    @Test
    public void convertToDatabaseColumn() throws Exception {
        Date date = localDateConverter.convertToDatabaseColumn(localDate);
        assertNotNull(date);
        assertEquals(localDate, date.toLocalDate());
    }

    @Test
    public void convertToDatabaseColumn_null() throws Exception {
        Date date = localDateConverter.convertToDatabaseColumn(null);
        assertNull(date);
    }

    @Test
    public void convertToEntityAttribute() throws Exception {
        LocalDate result = localDateConverter.convertToEntityAttribute(Date.valueOf(localDate));
        assertNotNull(result);
        assertEquals(localDate, result);
    }

    @Test
    public void convertToEntityAttribute_null() throws Exception {
        LocalDate result = localDateConverter.convertToEntityAttribute(null);
        assertNull(result);
    }
}