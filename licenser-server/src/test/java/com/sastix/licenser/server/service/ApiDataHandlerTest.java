package com.sastix.licenser.server.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sastix.licenser.server.LicenserApp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {LicenserApp.class},
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = {
                "spring.datasource.url:jdbc:h2:mem:licenser_db",
                "server.port: 8585",
                "licenser.server.protocol: http",
                "licenser.server.host: localhost",
                "licenser.server.port: 8585",
                "api.version: 1",
                "licenser.retry.backOffPeriod:10",
                "licenser.retry.maxAttempts:1",
                "logging.level.com.sastix.licenser=DEBUG"
        })
public class ApiDataHandlerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    LicenserService licenserService;

    @DirtiesContext
    @Test
    public void helloTest(){
        String testData = "just testing";
        String echoed = licenserService.helloEcho(testData);
        assertThat(echoed,is(testData));
    }
}
