package com.siby.automation.actions.exception;

public class BaseAutomationException extends RuntimeException {

    public BaseAutomationException(String msg) {
        super(msg);
    }

    public BaseAutomationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
