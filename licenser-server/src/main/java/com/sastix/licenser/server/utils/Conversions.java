package com.sastix.licenser.server.utils;

import com.sastix.licenser.commons.content.AccessCodeInfoDTO;
import com.sastix.licenser.commons.content.TenantDTO;
import com.sastix.licenser.server.model.AccessCode;
import com.sastix.licenser.server.model.Tenant;

import java.util.function.Function;

public interface Conversions {

    Function<AccessCode, AccessCodeInfoDTO> convertAccessCodeToDTO = (accessCode) -> {
        final AccessCodeInfoDTO accessCodeDTO = new AccessCodeInfoDTO();
        if (accessCode != null) {
            accessCodeDTO.setAccessCode(accessCode.getAccessCode());
            accessCodeDTO.setLevel(accessCode.getLevel());
            accessCodeDTO.setDuration(accessCode.getDuration());
            if (accessCode.getIsActivated() != null) {
                accessCodeDTO.setActivated(accessCode.getIsActivated());
            }
            if (accessCode.getUserAccessCode() != null) {
                accessCodeDTO.setActivationDate(accessCode.getUserAccessCode().getActivationDate());
                accessCodeDTO.setUserId(accessCode.getUserAccessCode().getUserId());
            }
        }
        return accessCodeDTO;
    };

    Function<Tenant, TenantDTO> convertTenantToDTO = (tenant) -> {
        final TenantDTO tenantDTO = new TenantDTO();
        if (tenant != null) {
            tenantDTO.setId(tenant.getId());
            tenantDTO.setName(tenant.getName());
            tenantDTO.setDescription(tenant.getDescription());
            tenantDTO.setLevel(tenant.getLevel());
            if(tenant.getParentId() != null) {
                tenantDTO.setParentId(tenant.getParentId().getId());
            }
        }
        return tenantDTO;
    };

}
