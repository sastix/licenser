package com.sastix.licenser.commons.exception;

import com.sastix.toolkit.restclient.exception.ToolkitBusinessException;

public class InvalidAccessCodeProvidedException extends ToolkitBusinessException {

    public InvalidAccessCodeProvidedException(String message) {
        super(message);
    }
}
