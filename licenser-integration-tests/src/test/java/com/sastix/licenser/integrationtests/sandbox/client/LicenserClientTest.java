package com.sastix.licenser.integrationtests.sandbox.client;

import com.sastix.licenser.client.LicenserClient;
import com.sastix.licenser.client.config.LicenserClientConfig;
import com.sastix.licenser.commons.domain.LicenserContextUrl;
import com.sastix.licenser.server.LicenserApp;
import com.sastix.toolkit.restclient.service.RetryRestTemplate;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {LicenserApp.class, LicenserClientConfig.class},
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
        })
public class LicenserClientTest implements LicenserContextUrl {
    private static final Logger LOG = LoggerFactory.getLogger(LicenserClientTest.class);

    @Autowired
    @Qualifier("licenserRestTemplate")
    RetryRestTemplate retryRestTemplate;

    @Autowired
    @Qualifier("licenserClient")
    LicenserClient licenserClient;

    @Test
    public void helloTest() {
        String testData = "some dummy data";
        String echoed = licenserClient.helloEcho(testData);
        assertThat(echoed,is(testData));
    }
}

