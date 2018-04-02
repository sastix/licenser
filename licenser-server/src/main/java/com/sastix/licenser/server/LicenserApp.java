package com.sastix.licenser.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LicenserApp {
    private static final Logger LOG = (Logger) LoggerFactory.getLogger(LicenserApp.class);
	public static void main(String[] args) {
        LOG.info("**** Starting LICENSER app ****");
		SpringApplication.run(LicenserApp.class, args);
	}
}
