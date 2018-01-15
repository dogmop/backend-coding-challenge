package com.alanjwilliams.engage.util;

import com.alanjwilliams.engage.exception.BadRequestException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/**
 * JSON to LocalDate Deserializer.
 */
public class JsonLocalDateDeserializer extends JsonDeserializer<LocalDate> {

    /**
     * DateTimeFormatter. Uses Strict resolution to prevent unsupported datasource dates being deserialized, ie. 01/01/10000.
     */
    private static final DateTimeFormatter dateTimeFormatter =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    @Override
    public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String dateString = jsonParser.getText();
        if (dateString != null) {
            try {
                return LocalDate.parse(dateString, dateTimeFormatter);
            } catch (DateTimeParseException e) {
                throw new BadRequestException(dateString);
            }
        }
        throw new BadRequestException(MessageFormat.format("Invalid Date String: {0}", dateString));
    }
}
