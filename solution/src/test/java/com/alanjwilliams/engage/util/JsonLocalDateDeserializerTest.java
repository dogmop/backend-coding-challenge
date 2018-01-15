package com.alanjwilliams.engage.util;

import com.alanjwilliams.engage.exception.BadRequestException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link JsonLocalDateDeserializer}.
 */
public class JsonLocalDateDeserializerTest {

    JsonLocalDateDeserializer jsonLocalDateDeserializer;

    JsonParser jsonParser;
    DeserializationContext deserializationContext;

    @Before
    public void setUp() throws Exception {
        jsonLocalDateDeserializer = new JsonLocalDateDeserializer();

        jsonParser = mock(JsonParser.class);
        deserializationContext = mock(DeserializationContext.class);
    }

    @Test
    public void deserialize() throws Exception {
        // Given a date string
        when(jsonParser.getText()).thenReturn("01/02/2018");
        // When the date string is deserialized into a LocalDate object
        LocalDate localDate = jsonLocalDateDeserializer.deserialize(jsonParser, deserializationContext);
        // Then the LocalDate values are correctly set.
        assertNotNull(localDate);
        assertEquals(1, localDate.getDayOfMonth());
        assertEquals(2, localDate.getMonthValue());
        assertEquals(2018, localDate.getYear());
    }

    /**
     * Test an invalid date String to verify a BadRequestException thrown.
     *
     * @throws Exception
     */
    @Test(expected = BadRequestException.class)
    public void deserialize_invalid() throws Exception {
        // Given an invalid date string
        when(jsonParser.getText()).thenReturn("not a date");
        // When the date string is deserialized into a LocalDate object
        jsonLocalDateDeserializer.deserialize(jsonParser, deserializationContext);
        // Then a BadRequestException is thrown.
    }

    /**
     * Test a null date String to verify a BadRequestException thrown.
     * @throws Exception
     */
    @Test(expected = BadRequestException.class)
    public void deserialize_null() throws Exception {
        // Given an null string
        when(jsonParser.getText()).thenReturn(null);
        // When the string is deserialized into a LocalDate object
        jsonLocalDateDeserializer.deserialize(jsonParser, deserializationContext);
        // Then a BadRequestException is thrown.
    }
}