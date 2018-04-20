package com.sastix.licenser.integrationtests.sandbox.client;

import com.sastix.licenser.client.LicenserClient;
import com.sastix.licenser.client.config.LicenserClientConfig;
import com.sastix.licenser.commons.content.AccessCodeInfoDTO;
import com.sastix.licenser.commons.content.ActivateAccessCodeDTO;
import com.sastix.licenser.commons.content.ImportExcelDTO;
import com.sastix.licenser.commons.domain.LicenserContextUrl;
import com.sastix.licenser.commons.exception.AccessCodeNotFoundException;
import com.sastix.licenser.commons.exception.InvalidAccessCodeProvidedException;
import com.sastix.licenser.commons.exception.MalformedExcelException;
import com.sastix.licenser.server.LicenserApp;
import com.sastix.licenser.server.model.AccessCode;
import com.sastix.licenser.server.service.ExcelParserService;
import com.sastix.toolkit.restclient.service.RetryRestTemplate;
import org.flywaydb.test.FlywayTestExecutionListener;
import org.flywaydb.test.annotation.FlywayTest;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;

@FlywayTest
@RunWith(SpringRunner.class)
@Profile("integration")
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        FlywayTestExecutionListener.class })
@SpringBootTest(classes = {LicenserApp.class, LicenserClientConfig.class},
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = {
                "spring.datasource.url:jdbc:h2:mem:licenser_db;DB_CLOSE_ON_EXIT=FALSE",
                "server.port: 8586",
                "licenser.server.protocol: http",
                "licenser.server.host: localhost",
                "licenser.server.port: 8586",
                "api.version: 1",
                "licenser.retry.backOffPeriod:10",
                "licenser.retry.maxAttempts:1",
        })
@FlywayTest(locationsForMigrate = {"db/data"})
public class LicenserClientTest {
    private static final Logger LOG = LoggerFactory.getLogger(LicenserClientTest.class);

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Autowired
    @Qualifier("licenserRestTemplate")
    RetryRestTemplate retryRestTemplate;

    @Autowired
    @Qualifier("licenserClient")
    LicenserClient licenserClient;

    @Before
    @FlywayTest(locationsForMigrate = {"db/data"})
    public void before(){

    }

    @Test
    public void getAllAccessCodesTest() {
        List<AccessCodeInfoDTO> accessCodes = licenserClient.getAllAccessCodes();
        assertThat(accessCodes.size(), is(2));
        assertThat(accessCodes.get(0).getAccessCode(), is("test"));
        assertThat(accessCodes.get(0).getLevel(), is(1));
        assertThat(accessCodes.get(0).getDuration(), is(1500));
    }

    @Test
    public void getAccessCodeInfoTest() {
        AccessCodeInfoDTO requestedAccessCode = licenserClient.getAccessCodeInfo("test");
        assertThat(requestedAccessCode.getAccessCode(), is("test"));
        assertThat(requestedAccessCode.getLevel(), is(1));
        assertThat(requestedAccessCode.getDuration(), is(1500));
    }

    @Test
    public void getAccessCodeInfoAccessCodeNotFoundExceptionTest() {
        expectedEx.expect(AccessCodeNotFoundException.class);
        expectedEx.expectMessage(CoreMatchers.containsString("cannot be found"));
        licenserClient.getAccessCodeInfo("null");
    }

    @Test
    public void activateAccessCodeTest() {
        // Check access code is currently not activated
        AccessCodeInfoDTO accessCodeInfoDTO = licenserClient.getAccessCodeInfo("test");
        assertThat(accessCodeInfoDTO.getActivated(), is(false));
        assertThat(accessCodeInfoDTO.getActivationDate(), is(nullValue()));
        assertThat(accessCodeInfoDTO.getUserId(), is(nullValue()));

        // Activate access code
        ActivateAccessCodeDTO activateAccessCodeDTO = new ActivateAccessCodeDTO("test", 1L);
        accessCodeInfoDTO = licenserClient.activateAccessCode(activateAccessCodeDTO);
        assertThat(accessCodeInfoDTO.getActivated(), is(true));

        // Verify that is activated
        accessCodeInfoDTO = licenserClient.getAccessCodeInfo("test");
        assertThat(accessCodeInfoDTO.getActivated(), is(true));
        assertThat(accessCodeInfoDTO.getActivationDate(), is(notNullValue()));
        assertThat(accessCodeInfoDTO.getUserId(), is(1L));

    }

    @Test
    public void activateAccessCodeNotFoundExceptionTest() {
        expectedEx.expect(AccessCodeNotFoundException.class);
        expectedEx.expectMessage(CoreMatchers.containsString("cannot be found"));
        licenserClient.activateAccessCode(new ActivateAccessCodeDTO("null", 1L));
    }

    @Test
    public void activateAccessCodeInvalidAccessCodeProvidedException() {
        expectedEx.expect(InvalidAccessCodeProvidedException.class);
        expectedEx.expectMessage(CoreMatchers.containsString("is already activated"));
        licenserClient.activateAccessCode(new ActivateAccessCodeDTO("activated", 1L));
    }

    @Test
    public void importExcelTest() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("fixtures/valid.xlsx");
        ImportExcelDTO importExcelDTO = new ImportExcelDTO(StreamUtils.copyToByteArray(inputStream), 1);

        List<AccessCodeInfoDTO> importedData = licenserClient.importFromExcel(importExcelDTO);

        assertThat(importedData.size(), is(2));

        // Assert that values matches with the first row of the sample file
        assertThat(importedData.get(0).getAccessCode(), is("test1"));
        assertThat(importedData.get(0).getLevel(), is(0));
        assertThat(importedData.get(0).getDuration(), is(1500));

        AccessCodeInfoDTO requestedAccessCode = licenserClient.getAccessCodeInfo("test1");
        assertThat(requestedAccessCode.getAccessCode(), is("test1"));
    }

    @Test
    public void importExcelTwice() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("fixtures/valid.xlsx");
        ImportExcelDTO importExcelDTO = new ImportExcelDTO(StreamUtils.copyToByteArray(inputStream), 1);
        licenserClient.importFromExcel(importExcelDTO);

        expectedEx.expect(InvalidAccessCodeProvidedException.class);
        licenserClient.importFromExcel(importExcelDTO);

    }

    @Test
    public void importExcelExceptionTest() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("fixtures/bad_header.xls");
        ImportExcelDTO importExcelDTO = new ImportExcelDTO(StreamUtils.copyToByteArray(inputStream), 1);

        expectedEx.expect(MalformedExcelException.class);
        expectedEx.expectMessage(CoreMatchers.containsString("Malformed header"));

        licenserClient.importFromExcel(importExcelDTO);

    }
}

