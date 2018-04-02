package com.sastix.licenser.commons.exception;


import com.sastix.toolkit.restclient.exception.ToolkitBusinessException;

public class LicenserException extends ToolkitBusinessException {

    private static final long serialVersionUID = -5704967797520663048L;

    public LicenserException(String message) {
        super(message);
    }

    public LicenserException(String message, Throwable cause) {
        super(message, cause);
    }
}
