package com.alanjwilliams.engage.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Test class for {@link JsonLocalDateSerializer}
 */
public class JsonLocalDateSerializerTest {

    JsonLocalDateSerializer jsonLocalDateSerializer;

    JsonGenerator jsonGenerator;
    SerializerProvider serializerProvider;

    @Before
    public void setUp() throws Exception {
        jsonLocalDateSerializer = new JsonLocalDateSerializer();

        jsonGenerator = mock(JsonGenerator.class);
        serializerProvider = mock(SerializerProvider.class);
    }

    @Test
    public void serialize() throws Exception {
        // Given a local date.
        LocalDate localDate = LocalDate.of(2018, 2, 1);
        // When serialized for JSON.
        jsonLocalDateSerializer.serialize(localDate, jsonGenerator, serializerProvider);
        // Then the date is serialized correctly.
        verify(jsonGenerator).writeString(eq("01/02/2018"));
    }

    /**
     * Test a null LocalDate object to verify a NullPointerException thrown.
     *
     * @throws Exception
     */
    @Test(expected = NullPointerException.class)
    public void serialize_null() throws Exception {
        // When serializing a null date.
        jsonLocalDateSerializer.serialize(null, jsonGenerator, serializerProvider);
        // Then a NullPointerException is thrown.
    }

}