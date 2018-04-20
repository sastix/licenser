package com.sastix.licenser.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sastix.licenser.client.config.LicenserClientConfig;
import com.sastix.licenser.commons.content.AccessCodeInfoDTO;
import com.sastix.licenser.commons.content.ActivateAccessCodeDTO;
import com.sastix.licenser.commons.content.ImportExcelDTO;
import com.sastix.toolkit.restclient.service.RetryRestTemplate;
import com.sastix.toolkit.versioning.client.ApiVersionClient;
import com.sastix.toolkit.versioning.model.VersionDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;

import java.util.List;

import static com.sastix.licenser.commons.domain.LicenserContextUrl.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {LicenserClient.class, LicenserClientConfig.class})
@RestClientTest(LicenserClient.class)
public class LicenserClientTest {

    static VersionDTO TEST_VERSION = new VersionDTO().withMinVersion(1.0)
            .withMaxVersion(1.0)
            .withVersionContext(1.0, "/licenser/v1.0");

    static String BASE_URL = "http://localhost/licencer/v1.0/";

    MockRestServiceServer mockServer;

    @MockBean(name = "licenserApiVersionClient")
    ApiVersionClient apiVersionClient;

    @Autowired
    @Qualifier("licenserRestTemplate")
    RetryRestTemplate restTemplate;

    @Autowired
    LicenserClient licenserClient;

    private static final ObjectMapper mapper = new ObjectMapper();

    private AccessCodeInfoDTO responseData;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockServer = MockRestServiceServer.bindTo(restTemplate).build();
        when(apiVersionClient.getApiVersion()).thenReturn(TEST_VERSION);
        when(apiVersionClient.getApiUrl()).thenReturn(BASE_URL);

        responseData = new AccessCodeInfoDTO("test", 0, 1500);

    }

    @Test
    public void importExcelTest() throws JsonProcessingException {
        AccessCodeInfoDTO[] responseData = new AccessCodeInfoDTO[]{new AccessCodeInfoDTO("test", 0, 1500)};
        String response = mapper.writeValueAsString(responseData);

        mockServer.expect(requestTo(BASE_URL + IMPORT_EXCEL))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(response, MediaType.APPLICATION_JSON_UTF8));
        List<AccessCodeInfoDTO> importedData = licenserClient.importFromExcel(new ImportExcelDTO("test".getBytes(), 0));

        mockServer.verify();
        assertThat(importedData.size(), equalTo(1));
        assertThat(importedData.get(0).getAccessCode(), equalTo("test"));
        assertThat(importedData.get(0).getLevel(), equalTo(0));
        assertThat(importedData.get(0).getDuration(), equalTo(1500));
    }

    @Test
    public void getAccessCodeInfoTest() throws JsonProcessingException {
        String response = mapper.writeValueAsString(responseData);
        mockServer.expect(requestTo(BASE_URL + ACCESS_CODE + "/test"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(response, MediaType.APPLICATION_JSON));
        AccessCodeInfoDTO requestedAccessCode = licenserClient.getAccessCodeInfo("test");

        mockServer.verify();
        assertThat(requestedAccessCode.getAccessCode(), equalTo("test"));
        assertThat(requestedAccessCode.getLevel(), equalTo(0));
        assertThat(requestedAccessCode.getDuration(), equalTo(1500));
    }

    @Test
    public void activateAccessCodeTest() throws JsonProcessingException {
        String response = mapper.writeValueAsString(new AccessCodeInfoDTO("test",0, true, 1500));
        mockServer.expect(requestTo(BASE_URL + ACTIVATE))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(response, MediaType.APPLICATION_JSON));
        ActivateAccessCodeDTO activateAccessCodeDTO = new ActivateAccessCodeDTO("test", 1L);
        AccessCodeInfoDTO activatedAccessCode = licenserClient.activateAccessCode(activateAccessCodeDTO);

        mockServer.verify();
        assertThat(activatedAccessCode.getActivated(), equalTo(true));
    }

    @Test
    public void getAllAccessCodesTest() throws JsonProcessingException {
        AccessCodeInfoDTO[] responseData = new AccessCodeInfoDTO[]{new AccessCodeInfoDTO("test", 0, 1500)};
        String response = mapper.writeValueAsString(responseData);

        mockServer.expect(requestTo(BASE_URL + ACCESS_CODE))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(response, MediaType.APPLICATION_JSON_UTF8));

        List<AccessCodeInfoDTO> accessCodeInfoDTOS = licenserClient.getAllAccessCodes();

        mockServer.verify();
        assertThat(accessCodeInfoDTOS.size(), equalTo(1));
        assertThat(accessCodeInfoDTOS.get(0).getAccessCode(), equalTo("test"));
        assertThat(accessCodeInfoDTOS.get(0).getLevel(), equalTo(0));
        assertThat(accessCodeInfoDTOS.get(0).getDuration(), equalTo(1500));
    }
}
