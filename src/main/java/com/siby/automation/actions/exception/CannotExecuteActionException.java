package com.siby.automation.actions.exception;

public class CannotExecuteActionException extends BaseAutomationException {

    public CannotExecuteActionException(String msg) {
        super(msg);
    }

    public CannotExecuteActionException(String msg, Exception e) {
        super(msg, e);
    }
}
