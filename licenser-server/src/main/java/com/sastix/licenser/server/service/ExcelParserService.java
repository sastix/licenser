package com.sastix.licenser.server.service;

import com.sastix.licenser.commons.exception.MalformedExcelException;
import com.sastix.licenser.server.model.AccessCode;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;


public interface ExcelParserService {

    List<AccessCode> parseFile(InputStream stream) throws MalformedExcelException;

    List<AccessCode> parseFile(byte[] file) throws MalformedExcelException;
}

