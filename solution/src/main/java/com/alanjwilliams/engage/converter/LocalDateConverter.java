package com.alanjwilliams.engage.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Date;
import java.time.LocalDate;

/**
 *
 * LocalDate is great to use within Java as it allows simple use of dates (unlike java.util.Date or java.sql.Date).
 * However, LocalDate is stored in a database as a TINYBLOB type by default. This attribute converter allows it to be stored as a DATE type instead.
 *
 */
@Converter(autoApply = true)
public class LocalDateConverter implements AttributeConverter<LocalDate, Date> {
    @Override
    public Date convertToDatabaseColumn(LocalDate localDate) {
        return localDate == null ? null : Date.valueOf(localDate);
    }

    @Override
    public LocalDate convertToEntityAttribute(Date date) {
        return date == null ? null : date.toLocalDate();
    }
}
