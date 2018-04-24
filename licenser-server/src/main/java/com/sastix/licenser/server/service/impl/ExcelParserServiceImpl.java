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
import java.util.*;


@Service()
public class ExcelParserServiceImpl implements ExcelParserService {

    enum Column {
        ACCESS_CODE("access_code", true),
        LEVEL("level", true),
        PRICE("price", false),
        DURATION("duration", true);

        private final String title;
        private final Boolean required;

        Column(String title, Boolean required) {
            this.title = title;
            this.required = required;
        }

        public final String toString() {
            return title;
        }

        public final Boolean getRequired(){
            return required;
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
        // Linkedlist so we can remove
        List<Column> required = new LinkedList<>(Arrays.asList(Column.class.getEnumConstants()));

        // Find Column indexes
        for (Cell cell : header) {
            Column currentColumn;
            Integer currentPosition;
            if (cell.getStringCellValue().toLowerCase().contains(Column.ACCESS_CODE.toString())) {
                // Access code column
                currentPosition = cell.getColumnIndex();
                currentColumn = Column.ACCESS_CODE;
            } else if (cell.getStringCellValue().toLowerCase().contains(Column.LEVEL.toString())) {
                // Level column
                currentPosition = cell.getColumnIndex();
                currentColumn = Column.LEVEL;
            } else if (cell.getStringCellValue().toLowerCase().contains(Column.PRICE.toString())) {
                // Price column
                currentPosition = cell.getColumnIndex();
                currentColumn = Column.PRICE;
            } else if (cell.getStringCellValue().toLowerCase().contains(Column.DURATION.toString())) {
                // Duration column
                currentPosition = cell.getColumnIndex();
                currentColumn = Column.DURATION;
            } else {
                throw new MalformedExcelException(String.format(
                        "Malformed header at column %d. %s is unknown.",
                        cell.getColumnIndex(),
                        cell.getStringCellValue()
                ));
            }
            required.removeIf(x -> x.equals(currentColumn));
            positions.put(currentPosition, currentColumn);
        }

        if (required.size() != 0) {
            StringBuilder not_found = new StringBuilder();
            for ( Column column : required){
                not_found.append(column.toString()).append(" ");
            }
            throw new MalformedExcelException("Required header not found: " + not_found.toString());
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

            // Skip if empty
            if (cell == null){
                continue;
            }

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
