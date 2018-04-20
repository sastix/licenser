package com.sastix.licenser.server.config;


import com.sastix.licenser.commons.domain.LicenserContextUrl;
import com.sastix.toolkit.versioning.model.VersionDTO;
import com.sastix.toolkit.versioning.service.ApiVersionService;
import com.sastix.toolkit.versioning.service.ApiVersionServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Version Configuration.
 */
@Configuration
@ComponentScan({"com.sastix.toolkit","com.sastix.licenser"})
public class VersionConfiguration implements LicenserContextUrl {

    public static VersionDTO LICENSER_SERVER_VERSION = new VersionDTO()
            .withMinVersion(Double.valueOf(REST_API_V1_0))
            .withMaxVersion(Double.valueOf(REST_API_V1_0))
            .withVersionContext(Double.valueOf(REST_API_V1_0),  "/"+BASE_URL+"/v" + REST_API_V1_0);

    @Bean
    public ApiVersionService apiVersionService() {
            /*
             * you need to configure the api version service with the
			 * constructor argument of the api ranges you support
			 */
        return new ApiVersionServiceImpl(LICENSER_SERVER_VERSION);
    }
}
