package com.sastix.licenser.server.service;

import com.sastix.licenser.commons.exception.MalformedExcelException;
import com.sastix.licenser.server.model.AccessCode;
import com.sastix.licenser.server.service.impl.ExcelParserServiceImpl;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles({"production", "test"})
@ContextConfiguration(classes = {ExcelParserServiceImpl.class})
public class ExcelParserServiceTest {

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Autowired
    ExcelParserService parserService;

    @Test
    public void parsingValidXls() throws MalformedExcelException {
        InputStream file = getClass().getClassLoader().getResourceAsStream("fixtures/valid.xls");
        List<AccessCode> accessCodes = parserService.parseFile(file);

        // Assert that values matches with the first row of the sample file
        AccessCode firstRow = accessCodes.get(0);
        assertThat(firstRow.getAccessCode(), is("test1"));
        assertThat(firstRow.getLevel(), is(0));
        assertThat(firstRow.getPrice(), is(BigDecimal.valueOf(123.4)));
        assertThat(firstRow.getDuration(), is(1500));
        assertThat(firstRow.getIsActivated(), is(false));

        // Assert that values matches with the first row of the sample file
        AccessCode secondRow = accessCodes.get(1);
        assertThat(secondRow.getAccessCode(), is("test2"));
        assertThat(secondRow.getLevel(), is(1));
        assertThat(secondRow.getPrice(), is(BigDecimal.valueOf(12312)));
        assertThat(secondRow.getDuration(), is(30));
        assertThat(secondRow.getIsActivated(), is(true));
    }

    @Test
    public void parsingValidXlsx() throws MalformedExcelException {
        InputStream file = getClass().getClassLoader().getResourceAsStream("fixtures/valid.xlsx");
        List<AccessCode> accessCodes = parserService.parseFile(file);

        // Assert that values matches with the first row of the sample file
        AccessCode firstRow = accessCodes.get(0);
        assertThat(firstRow.getAccessCode(), is("test1"));
        assertThat(firstRow.getLevel(), is(0));
        assertThat(firstRow.getPrice(), is(BigDecimal.valueOf(123.4)));
        assertThat(firstRow.getDuration(), is(1500));
        assertThat(firstRow.getIsActivated(), is(false));

        // Assert that values matches with the first row of the sample file
        AccessCode secondRow = accessCodes.get(1);
        assertThat(secondRow.getAccessCode(), is("test2"));
        assertThat(secondRow.getLevel(), is(1));
        assertThat(secondRow.getPrice(), is(BigDecimal.valueOf(12312)));
        assertThat(secondRow.getDuration(), is(30));
        assertThat(secondRow.getIsActivated(), is(true));
    }

    @Test
    public void malformedHeaderTest() throws MalformedExcelException {
        InputStream file = getClass().getClassLoader().getResourceAsStream("fixtures/bad_header.xls");
        expectedEx.expect(MalformedExcelException.class);
        expectedEx.expectMessage(containsString("Malformed header"));
        parserService.parseFile(file);
    }

    @Test
    public void notValidLevelTest() throws MalformedExcelException {
        InputStream file = getClass().getClassLoader().getResourceAsStream("fixtures/invalid_level.xls");
        expectedEx.expect(MalformedExcelException.class);
        expectedEx.expectMessage(containsString("not valid for column level"));
        parserService.parseFile(file);
    }

    @Test
    public void notValidPriceTest() throws MalformedExcelException {
        InputStream file = getClass().getClassLoader().getResourceAsStream("fixtures/invalid_price.xls");
        expectedEx.expect(MalformedExcelException.class);
        expectedEx.expectMessage(containsString("not valid for column price"));
        parserService.parseFile(file);
    }

    @Test
    public void notValidDurationTest() throws MalformedExcelException {
        InputStream file = getClass().getClassLoader().getResourceAsStream("fixtures/invalid_duration.xls");
        expectedEx.expect(MalformedExcelException.class);
        expectedEx.expectMessage(containsString("not valid for column duration"));
        parserService.parseFile(file);
    }

}