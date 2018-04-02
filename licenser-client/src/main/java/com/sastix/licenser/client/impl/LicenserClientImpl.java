package com.sastix.licenser.client.impl;

import com.sastix.licenser.client.LicenserClient;
import com.sastix.licenser.commons.domain.LicenserContextUrl;
import com.sastix.toolkit.restclient.service.RetryRestTemplate;
import com.sastix.toolkit.versioning.client.ApiVersionClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class LicenserClientImpl implements LicenserClient, LicenserContextUrl {
    private Logger LOG = (Logger) LoggerFactory.getLogger(LicenserClientImpl.class);

    @Autowired
    @Qualifier("licenserApiVersionClient")
    ApiVersionClient apiVersionClient;

    @Autowired
    @Qualifier("licenserRestTemplate")
    RetryRestTemplate retryRestTemplate;

    @Override
    public String helloEcho(String echo) {
        final String url = apiVersionClient.getApiUrl() + "/"+HELLO;
        LOG.debug("/"+HELLO+" call [get]: " + url);
        String response = retryRestTemplate.getForObject(url, String.class);
        return response;
    }
}
