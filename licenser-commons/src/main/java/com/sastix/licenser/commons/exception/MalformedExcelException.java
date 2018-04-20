package com.sastix.licenser.commons.exception;

import com.sastix.toolkit.restclient.exception.ToolkitBusinessException;

public class MalformedExcelException extends ToolkitBusinessException {

    public MalformedExcelException(String message) {
        super(message);
    }

}
