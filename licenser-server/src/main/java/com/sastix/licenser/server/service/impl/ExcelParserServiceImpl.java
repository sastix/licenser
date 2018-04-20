package com.sastix.licenser.server.service.impl;

import com.sastix.licenser.commons.exception.MalformedExcelException;
import com.sastix.licenser.server.model.AccessCode;
import com.sastix.licenser.server.service.ExcelParserService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service()
public class ExcelParserServiceImpl implements ExcelParserService {

    enum Column {
        ACCESS_CODE("access_code"),
        LEVEL("level"),
        PRICE("price"),
        DURATION("duration"),
        ACTIVATED("is_activated");

        private final String title;

        Column(String title) {
            this.title = title;
        }

        public final String toString() {
            return title;
        }

    }

    public List<AccessCode> parseFile(InputStream stream) throws MalformedExcelException {
        Workbook workbook;
        try {
            workbook = WorkbookFactory.create(stream);
        } catch (IOException|InvalidFormatException e) {
            throw new MalformedExcelException("Cannot read excel file");
        }

        Sheet sheet = workbook.getSheetAt(0);

        Row firstRow = sheet.getRow(0);
        Map<Integer, Column> columnPositions = getColumnPositions(firstRow);

        List<AccessCode> accessCodeList = new ArrayList<>();

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            AccessCode accessCode = parseRow(columnPositions, row);
            accessCodeList.add(accessCode);
        }

        return accessCodeList;
    }

    @Override
    public List<AccessCode> parseFile(byte[] file) throws MalformedExcelException {
        return parseFile(new ByteArrayInputStream(file));
    }

    /**
     * Parses the header and return the data type of each column along with its index
     *
     * @param header The row of the header
     * @return A map of indices with column types
     * @throws MalformedExcelException
     */
    private Map<Integer, Column> getColumnPositions(Row header) throws MalformedExcelException {
        Map<Integer, Column> positions = new HashMap<>();

        for (Cell cell : header) {
            if (cell.getStringCellValue().equals(Column.ACCESS_CODE.toString())) {
                // Access code column
                positions.put(cell.getColumnIndex(), Column.ACCESS_CODE);
            } else if (cell.getStringCellValue().equals(Column.LEVEL.toString())) {
                // Level column
                positions.put(cell.getColumnIndex(), Column.LEVEL);
            } else if (cell.getStringCellValue().equals(Column.PRICE.toString())) {
                // Price column
                positions.put(cell.getColumnIndex(), Column.PRICE);
            } else if (cell.getStringCellValue().equals(Column.DURATION.toString())) {
                // Duration column
                positions.put(cell.getColumnIndex(), Column.DURATION);
            } else if (cell.getStringCellValue().equals(Column.ACTIVATED.toString())) {
                // Activated column
                positions.put(cell.getColumnIndex(), Column.ACTIVATED);
            } else {
                throw new MalformedExcelException(String.format(
                        "Malformed header at column %d. %s is unknown.",
                        cell.getColumnIndex(),
                        cell.getStringCellValue()
                ));
            }
        }
        return positions;
    }

    /**
     * Parses a data row of the excel file and returns an AccessCode object
     *
     * @param columnPositions The data types of each column
     * @param row             The row to be parsed
     * @return A new AccessCode object
     * @throws MalformedExcelException
     */
    private AccessCode parseRow(Map<Integer, Column> columnPositions, Row row) throws MalformedExcelException {

        AccessCode accessCode = new AccessCode();

        for (Map.Entry<Integer, Column> entry : columnPositions.entrySet()) {
            Cell cell = row.getCell(entry.getKey());

            try {
                switch (entry.getValue()) {
                    case ACCESS_CODE:
                        accessCode.setAccessCode(getCellValue(cell));
                        break;
                    case LEVEL:
                        Integer level = Integer.parseInt(getCellValue(cell));
                        accessCode.setLevel(level);
                        break;
                    case PRICE:
                        BigDecimal price = new BigDecimal(getCellValue(cell));
                        accessCode.setPrice(price);
                        break;
                    case DURATION:
                        Integer duration = Integer.parseInt(getCellValue(cell));
                        accessCode.setDuration(duration);
                        break;
                    case ACTIVATED:
                        if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
                            accessCode.setIsActivated(false);
                        } else {
                            accessCode.setIsActivated(true);
                        }
                        break;
                }
            } catch (IllegalStateException | NumberFormatException e) {
                throw new MalformedExcelException(String.format(
                        "Value error in line %d and column %d. %s not valid for column %s",
                        cell.getRowIndex(),
                        cell.getColumnIndex(),
                        cell.getStringCellValue(),
                        entry.getValue().toString()
                ));
            }
        }
        return accessCode;
    }

    /**
     * Returns the content of the cell as string. Cleanups can be defined here
     *
     * @param cell the cell we want the contents
     * @return contents of the cell as string
     */
    private String getCellValue(Cell cell) {
        // Set string type so we can handle the conversion ourselves
        cell.setCellType(Cell.CELL_TYPE_STRING);

        return cell.getStringCellValue().trim();
    }

}
