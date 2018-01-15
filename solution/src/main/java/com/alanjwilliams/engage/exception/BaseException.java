package com.alanjwilliams.engage.exception;

/**
 * BaseException for application runtime exceptions to extend.
 *
 * In a RESTful application, RuntimeExceptions can be useful to allow the exception to be thrown all the way up to controller without the need much additional code.
 */
public abstract class BaseException extends RuntimeException {

    public BaseException(String msg) {
        super(msg);
    }
}
