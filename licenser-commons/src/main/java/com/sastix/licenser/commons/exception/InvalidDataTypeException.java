package com.sastix.licenser.commons.exception;


import com.sastix.toolkit.restclient.exception.ToolkitBusinessException;

public class InvalidDataTypeException extends ToolkitBusinessException {
    private static final long serialVersionUID = -3033734643550517655L;

    public InvalidDataTypeException(String message) {
        super(message);
    }

    public InvalidDataTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}
