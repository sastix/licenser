package com.sastix.licenser.commons.exception;

import com.sastix.toolkit.restclient.exception.ToolkitBusinessException;

public class AccessCodeNotFoundException extends ToolkitBusinessException {

    public AccessCodeNotFoundException(String message) { super(message); }
}
