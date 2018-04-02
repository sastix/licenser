package com.sastix.licenser.client.config;

import com.sastix.licenser.client.LicenserClient;
import com.sastix.licenser.client.impl.LicenserClientImpl;
import com.sastix.licenser.commons.exception.LicenserException;
import com.sastix.licenser.commons.exception.InvalidDataTypeException;
import com.sastix.licenser.commons.domain.LicenserContextUrl;
import com.sastix.toolkit.restclient.config.RestTemplateConfiguration;
import com.sastix.toolkit.restclient.exception.ToolkitBusinessException;
import com.sastix.toolkit.restclient.handler.ExceptionHandler;
import com.sastix.toolkit.restclient.service.RetryRestTemplate;
import com.sastix.toolkit.versioning.client.ApiVersionClient;
import com.sastix.toolkit.versioning.client.ApiVersionClientImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class LicenserClientConfig implements LicenserContextUrl {
    @Value("${licenser.server.protocol:http}")
    private String protocol;

    @Value("${licenser.server.host:localhost}")
    private String host;

    @Value("${licenser.server.port:8085}")
    private String port;

    @Value("${licenser.retry.backOffPeriod:5000}")
    private String backOffPeriod;

    @Value("${licenser.retry.maxAttempts:3}")
    private String maxAttempts;

    @Value("${licenser.client.ssl.enabled:false}")
    Boolean licenserClientSslEnabled;

    @Value("${licenser.client.ssl.jks.keystore:path}")
    String licenserClientSslJksKeystore;

    @Value("${licenser.client.ssl.jks.keystore.password:securedPass}")
    String licenserClientSslJksKeystorePassword;

    @Autowired
    ResourcePatternResolver resourcePatternResolver;

    private static final ConcurrentHashMap<String, ExceptionHandler> SUPPORTED_EXCEPTIONS = new ConcurrentHashMap<>();

    static {
        SUPPORTED_EXCEPTIONS.put(ToolkitBusinessException.class.getName(), ToolkitBusinessException::new);
        SUPPORTED_EXCEPTIONS.put(InvalidDataTypeException.class.getName(), InvalidDataTypeException::new);
        SUPPORTED_EXCEPTIONS.put(LicenserException.class.getName(), LicenserException::new);
    }

    @Bean(name = "licenserClient")
    public LicenserClient licenserClient(){
        return new LicenserClientImpl();
    }

    @Autowired
    @Qualifier("licenserRestTemplate")
    RetryRestTemplate retryRestTemplate;

    @Bean(name="licenserRestTemplate")
    public RetryRestTemplate getRetryRestTemplate() throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        RestTemplateConfiguration restTemplateConfiguration = new RestTemplateConfiguration(backOffPeriod, maxAttempts, licenserClientSslEnabled, licenserClientSslJksKeystore, licenserClientSslJksKeystorePassword, resourcePatternResolver);
        return restTemplateConfiguration.getRestTemplateWithSupportedExceptions(SUPPORTED_EXCEPTIONS);
    }

    @Bean(name = "licenserApiVersionClient")
    public ApiVersionClient getApiVersionClient() throws Exception {
        return new ApiVersionClientImpl(protocol, host, port, REST_API_V1_0, retryRestTemplate);
    }

}
