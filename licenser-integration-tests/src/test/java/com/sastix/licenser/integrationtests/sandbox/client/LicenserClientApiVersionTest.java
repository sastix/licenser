package com.sastix.licenser.integrationtests.sandbox.client;


import com.sastix.licenser.client.config.LicenserClientConfig;
import com.sastix.licenser.server.LicenserApp;
import com.sastix.toolkit.restclient.config.ToolkitRestTemplateConfiguration;
import com.sastix.toolkit.versioning.client.ApiVersionClient;
import com.sastix.toolkit.versioning.model.VersionDTO;
import org.flywaydb.test.FlywayTestExecutionListener;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import static com.sastix.licenser.commons.domain.LicenserContextUrl.BASE_URL;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@Profile("integration")
@SpringBootTest(classes = {LicenserApp.class, LicenserClientConfig.class, ToolkitRestTemplateConfiguration.class},
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = {
                "spring.datasource.url:jdbc:h2:mem:licenser_db;DB_CLOSE_ON_EXIT=FALSE",
                "server.port: 8585",
                "licenser.server.protocol: http",
                "licenser.server.host: localhost",
                "licenser.server.port: 8585",
                "api.version: 1",
                "licenser.retry.backOffPeriod:10",
                "licenser.retry.maxAttempts:1",
                "logging.level.com.sastix:DEBUG"
        })
public class LicenserClientApiVersionTest {
    private static final Logger LOG = LoggerFactory.getLogger(LicenserClientApiVersionTest.class);

    @Autowired
    @Qualifier("licenserApiVersionClient")
    ApiVersionClient apiVersionClient;

    @Test
    public void licenserApiVersionTest(){
        VersionDTO versionDTO = apiVersionClient.getApiVersion();
        String apiUrl = apiVersionClient.getApiUrl();
        String licenserContext = apiVersionClient.getContext();
        assertThat(versionDTO.getMaxVersion(),is(1.0));
        assertThat(apiUrl,is("http://localhost:8585/"+BASE_URL+"/v1.0"));
        assertThat(licenserContext,is("/"+BASE_URL+"/v1.0"));
    }
}
