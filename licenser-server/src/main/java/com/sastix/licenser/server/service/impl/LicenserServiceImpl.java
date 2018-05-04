package com.sastix.licenser.server.service.impl;

import com.sastix.licenser.commons.content.AccessCodeInfoDTO;
import com.sastix.licenser.commons.content.ActivateAccessCodeDTO;
import com.sastix.licenser.commons.content.ImportExcelDTO;
import com.sastix.licenser.commons.content.TenantDTO;
import com.sastix.licenser.commons.exception.AccessCodeNotFoundException;
import com.sastix.licenser.commons.exception.InvalidAccessCodeProvidedException;
import com.sastix.licenser.server.model.AccessCode;
import com.sastix.licenser.server.model.Tenant;
import com.sastix.licenser.server.model.UserAccessCode;
import com.sastix.licenser.server.repository.AccessCodeRepository;
import com.sastix.licenser.server.repository.TenantRepository;
import com.sastix.licenser.server.repository.UserAccessCodeRepository;
import com.sastix.licenser.server.service.ExcelParserService;
import com.sastix.licenser.server.service.LicenserService;
import com.sastix.licenser.server.utils.Conversions;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class LicenserServiceImpl implements LicenserService, Conversions {

    private static final Logger LOG = LoggerFactory.getLogger(LicenserServiceImpl.class);

    @Autowired
    AccessCodeRepository accessCodeRepository;

    @Autowired
    UserAccessCodeRepository userAccessCodeRepository;

    @Autowired
    TenantRepository tenantRepository;

    @Autowired
    ExcelParserService excelParserService;

    @Override
    public AccessCodeInfoDTO getAccessCodeInfo(String accessCode) throws AccessCodeNotFoundException {
        AccessCode mAccessCode = accessCodeRepository.getByAccessCode(accessCode);
        if (mAccessCode != null) {
            return convertAccessCodeToDTO.apply(mAccessCode);
        } else
            throw new AccessCodeNotFoundException(String.format("The access code: %s cannot be found.", accessCode));
    }

    @Override
    @Transactional
    public AccessCodeInfoDTO activateAccessCode(ActivateAccessCodeDTO activateAccessCodeDTO) {
        AccessCode mAccessCode = accessCodeRepository.getByAccessCode(activateAccessCodeDTO.getAccessCode());
        if (mAccessCode == null)
            throw new AccessCodeNotFoundException(String.format("The access code: %s cannot be found.", activateAccessCodeDTO.getAccessCode()));
        if (mAccessCode.getIsActivated() != null && mAccessCode.getIsActivated())
            throw new InvalidAccessCodeProvidedException(String.format("The access code: %s is already activated.", activateAccessCodeDTO.getAccessCode()));
        mAccessCode.setIsActivated(true);
        // Add UserAccessCode m2m
        UserAccessCode userAccessCode = new UserAccessCode(mAccessCode,
                activateAccessCodeDTO.getUserId(),
                new DateTime(System.currentTimeMillis()));
        mAccessCode.setUserAccessCode(userAccessCode);
        accessCodeRepository.save(mAccessCode);
        return convertAccessCodeToDTO.apply(mAccessCode);
    }

    @Override
    public List<AccessCodeInfoDTO> getAllAccessCodes() {
        List<AccessCodeInfoDTO> accessCodeInfoDTOS = new ArrayList<>();
        for (AccessCode accessCode : accessCodeRepository.findAll()) {
            accessCodeInfoDTOS.add(convertAccessCodeToDTO.apply(accessCode));
        }
        return accessCodeInfoDTOS;
    }

    @Override
    public List<AccessCodeInfoDTO> importFromExcel(ImportExcelDTO importExcelDTO) {
        List<AccessCode> accessCodeList = excelParserService.parseFile(importExcelDTO.getExcelFile());
        List<AccessCodeInfoDTO> accessCodeInfoDTOS = new ArrayList<>();

        Tenant tenant = tenantRepository.findOne(importExcelDTO.getTenantId());
        for (AccessCode accessCode : accessCodeList) {
            accessCode.setTenant(tenant);
            accessCodeInfoDTOS.add(convertAccessCodeToDTO.apply(accessCode));
            // Check if already exists
            if (accessCodeRepository.getAccessCodeByAccessCodeAndTenant(accessCode.getAccessCode(), tenant) != null) {
                throw new InvalidAccessCodeProvidedException("Access code " + accessCode.getAccessCode() + " already exists");
            }
        }

        accessCodeRepository.save(accessCodeList);
        return accessCodeInfoDTOS;
    }

    @Override
    public List<TenantDTO> getAllTenants() {
        List<TenantDTO> tenantDTOS = new ArrayList<>();
        for (Tenant tenant : tenantRepository.findAll()) {
            tenantDTOS.add(convertTenantToDTO.apply(tenant));
        }
        return tenantDTOS;
    }
}
