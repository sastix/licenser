package com.sastix.licenser.server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sastix.licenser.commons.content.AccessCodeInfoDTO;
import com.sastix.licenser.commons.content.ActivateAccessCodeDTO;
import com.sastix.licenser.commons.content.ImportExcelDTO;
import com.sastix.licenser.commons.exception.MalformedExcelException;
import com.sastix.licenser.server.exception.LicenserExceptionHandlingController;
import com.sastix.licenser.server.repository.AccessCodeRepository;
import com.sastix.licenser.server.repository.TenantRepository;
import com.sastix.licenser.server.service.LicenserService;
import com.sastix.toolkit.versioning.service.ApiVersionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.StreamUtils;
import org.springframework.web.context.WebApplicationContext;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.sastix.licenser.commons.domain.LicenserContextUrl.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
        TestContext.class,
        WebApplicationContext.class,
        LicenserMainController.class,
        LicenserExceptionHandlingController.class
})
@WebMvcTest(controllers = LicenserMainController.class)
public class ApiControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    LicenserService licenserService;

    @MockBean
    ApiVersionService apiVersionService;

    @MockBean
    AccessCodeRepository accessCodeRepository;

    @MockBean
    TenantRepository tenantRepository;

    @Test
    public void getAccessCodeInfoTest() throws Exception {
        reset(licenserService);
        when(licenserService.getAccessCodeInfo(any(String.class)))
                .thenReturn(new AccessCodeInfoDTO("test", 0, 1500, 1));

        MvcResult result = mockMvc.perform(
                get("/" + BASE_URL + "/v" + REST_API_V1_0 + "/" + ACCESS_CODE + "/test")
                        .content(mapper.writeValueAsString("test")).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

        AccessCodeInfoDTO accessCodeInfoDTO = mapper.readValue(result.getResponse().getContentAsByteArray(), AccessCodeInfoDTO.class);

        assertThat(accessCodeInfoDTO.getAccessCode(), is("test"));
        assertThat(accessCodeInfoDTO.getLevel(), is(0));
        assertThat(accessCodeInfoDTO.getDuration(), is(1500));
    }

    @Test
    public void activateAccessCodeTest() throws Exception {
        reset(licenserService);
        when(licenserService.activateAccessCode(any(ActivateAccessCodeDTO.class)))
                .thenReturn(new AccessCodeInfoDTO("test", 0, 1500, 1));

        MvcResult result = mockMvc.perform(
                post("/" + BASE_URL + "/v" + REST_API_V1_0 + "/" + ACTIVATE)
                        .content(mapper.writeValueAsString(new ActivateAccessCodeDTO("test", 0L)))
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

        AccessCodeInfoDTO accessCodeInfoDTO = mapper.readValue(result.getResponse().getContentAsByteArray(), AccessCodeInfoDTO.class);

        assertThat(accessCodeInfoDTO.getAccessCode(), is("test"));
        assertThat(accessCodeInfoDTO.getLevel(), is(0));
        assertThat(accessCodeInfoDTO.getDuration(), is(1500));
    }

    @Test
    public void getAllAccessCodesTest() throws Exception {
        reset(licenserService);
        List<AccessCodeInfoDTO> accessCodeInfoDTOList = new ArrayList<>();
        accessCodeInfoDTOList.add(new AccessCodeInfoDTO("test", 0, 1500, 1));
        when(licenserService.getAllAccessCodes())
                .thenReturn(accessCodeInfoDTOList);

        MvcResult result = mockMvc.perform(
                get("/" + BASE_URL + "/v" + REST_API_V1_0 + "/" + ACCESS_CODE))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

        List<AccessCodeInfoDTO> accessCodeInfoDTOS = Arrays.asList(mapper.readValue(result.getResponse().getContentAsByteArray(), AccessCodeInfoDTO[].class));

        assertThat(accessCodeInfoDTOS.size(), is(1));
        assertThat(accessCodeInfoDTOS.get(0).getAccessCode(), is("test"));
        assertThat(accessCodeInfoDTOS.get(0).getLevel(), is(0));
        assertThat(accessCodeInfoDTOS.get(0).getDuration(), is(1500));
    }

    @Test
    public void importExcelTest() throws Exception {
        // Mock service return
        reset(licenserService);
        List<AccessCodeInfoDTO> accessCodeInfoDTOList = new ArrayList<>();
        accessCodeInfoDTOList.add(new AccessCodeInfoDTO("test", 0, 1500, 1));
        when(licenserService.importFromExcel(any(ImportExcelDTO.class))).thenReturn(accessCodeInfoDTOList);

        ImportExcelDTO importExcelDTO = new ImportExcelDTO("test".getBytes(), 0);

        MvcResult result = mockMvc.perform(
                post("/" + BASE_URL + "/v" + REST_API_V1_0 + "/" + IMPORT_EXCEL)
                        .content(mapper.writeValueAsString(importExcelDTO))
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

        AccessCodeInfoDTO[] accessCodeInfoDTOS = mapper.readValue(result.getResponse().getContentAsByteArray(), AccessCodeInfoDTO[].class);

        assertThat(accessCodeInfoDTOS[0].getAccessCode(), is("test"));
        assertThat(accessCodeInfoDTOS[0].getLevel(), is(0));
        assertThat(accessCodeInfoDTOS[0].getDuration(), is(1500));
    }


    @Test
    public void importExcelMalformedTest() throws Exception {
        reset(licenserService);
        when(licenserService.importFromExcel(any(ImportExcelDTO.class)))
                .thenThrow(new MalformedExcelException("Error"));

        ImportExcelDTO importExcelDTO = new ImportExcelDTO("test".getBytes(), 0);

        mockMvc.perform(
                post("/" + BASE_URL + "/v" + REST_API_V1_0 + "/" + IMPORT_EXCEL)
                        .content(mapper.writeValueAsString(importExcelDTO))
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message", is("Error")))
                .andExpect(jsonPath("$.exception", containsString("MalformedExcelException")));
    }

    @Test
    public void importInvalidExcelDTOTest() throws Exception {
        reset(licenserService);

        ImportExcelDTO importExcelDTO = new ImportExcelDTO();

        mockMvc.perform(
                post("/" + BASE_URL + "/v" + REST_API_V1_0 + "/" + IMPORT_EXCEL)
                .content(mapper.writeValueAsString(importExcelDTO))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    public void activateInvalidAccessCodeTest() throws Exception {
        reset(licenserService);

        mockMvc.perform(
                post("/" + BASE_URL + "/v" + REST_API_V1_0 + "/" + ACTIVATE)
                        .content(mapper.writeValueAsString(new ActivateAccessCodeDTO()))
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

}
