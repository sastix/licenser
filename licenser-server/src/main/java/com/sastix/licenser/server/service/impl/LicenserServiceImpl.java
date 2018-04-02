package com.sastix.licenser.server.service.impl;

import com.sastix.licenser.server.repository.AccessCodeRepository;
import com.sastix.licenser.server.service.LicenserService;
import com.sastix.licenser.server.utils.Conversions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LicenserServiceImpl implements LicenserService,Conversions {
    private static final Logger LOG = LoggerFactory.getLogger(LicenserServiceImpl.class);

    @Autowired
    AccessCodeRepository accessCodeRepository;

    @Override
    public String helloEcho(String echo) {
        return echo;
    }
}
