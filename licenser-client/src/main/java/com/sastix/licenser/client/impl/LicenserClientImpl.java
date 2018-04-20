package com.sastix.licenser.client.impl;

import com.sastix.licenser.client.LicenserClient;
import com.sastix.licenser.commons.content.AccessCodeInfoDTO;
import com.sastix.licenser.commons.content.ActivateAccessCodeDTO;
import com.sastix.licenser.commons.content.ImportExcelDTO;
import com.sastix.licenser.commons.domain.LicenserContextUrl;
import com.sastix.licenser.commons.exception.AccessCodeNotFoundException;
import com.sastix.licenser.commons.exception.InvalidAccessCodeProvidedException;
import com.sastix.toolkit.restclient.service.RetryRestTemplate;
import com.sastix.toolkit.versioning.client.ApiVersionClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.List;

@Service
public class LicenserClientImpl implements LicenserClient, LicenserContextUrl {
    private Logger LOG = (Logger) LoggerFactory.getLogger(LicenserClientImpl.class);

    @Autowired
    @Qualifier("licenserApiVersionClient")
    ApiVersionClient apiVersionClient;

    @Autowired
    @Qualifier("licenserRestTemplate")
    RetryRestTemplate retryRestTemplate;

    @Override
    public AccessCodeInfoDTO getAccessCodeInfo(String accessCode) throws AccessCodeNotFoundException {
        String url = apiVersionClient.getApiUrl() + "/" + ACCESS_CODE + "/" + accessCode;
        LOG.debug("/" + ACCESS_CODE + " call [get]: " + url);
        return retryRestTemplate.getForObject(url, AccessCodeInfoDTO.class);
    }

    @Override
    public AccessCodeInfoDTO activateAccessCode(ActivateAccessCodeDTO activateAccessCodeDTO) throws AccessCodeNotFoundException, InvalidAccessCodeProvidedException {
        String url = apiVersionClient.getApiUrl() + "/" + ACTIVATE;
        LOG.debug("/" +  ACTIVATE + " call [post]: " + url);
        return retryRestTemplate.postForObject(url, activateAccessCodeDTO, AccessCodeInfoDTO.class);
    }

    @Override
    public List<AccessCodeInfoDTO> getAllAccessCodes() {
        String url = apiVersionClient.getApiUrl() + "/" + ACCESS_CODE;
        LOG.debug("/" + ACCESS_CODE + " call [get]: " + url);
        return Arrays.asList(retryRestTemplate.getForObject(url, AccessCodeInfoDTO[].class));
    }

    @Override
    public List<AccessCodeInfoDTO> importFromExcel(ImportExcelDTO importExcelDTO) {
        // Fixme: Not tested
        String url = apiVersionClient.getApiUrl() + '/' + IMPORT_EXCEL;
        LOG.debug("/" + IMPORT_EXCEL + " call [post]: " + url);
        return Arrays.asList(retryRestTemplate.postForObject(url, importExcelDTO, AccessCodeInfoDTO[].class));
    }
}
