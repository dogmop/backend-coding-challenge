package com.alanjwilliams.engage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * An exception thrown when internal server errors occur. Includes a 500 response status.
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerErrorException extends BaseException {

    public InternalServerErrorException(String msg) {
        super(msg);
    }
}
