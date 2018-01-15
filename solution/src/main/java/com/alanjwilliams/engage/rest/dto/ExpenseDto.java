package com.alanjwilliams.engage.rest.dto;

import com.alanjwilliams.engage.util.JsonLocalDateDeserializer;
import com.alanjwilliams.engage.util.JsonLocalDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.time.LocalDate;

/**
 * Expense Data Transfer Object. Used to transfer Expense data between front end and back end.
 */
@Data
public class ExpenseDto {

    @JsonSerialize(using=JsonLocalDateSerializer.class)
    @JsonDeserialize(using=JsonLocalDateDeserializer.class)
    private LocalDate date;
    private String amount;
    private String vat;
    private String reason;
}
