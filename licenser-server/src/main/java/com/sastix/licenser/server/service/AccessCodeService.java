package com.sastix.licenser.server.service;

import com.sastix.licenser.commons.content.AccessCodeInfoDTO;
import com.sastix.licenser.server.model.AccessCode;
import com.sastix.licenser.server.repository.AccessCodeRepository;
import com.sastix.licenser.server.utils.Conversions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccessCodeService implements Conversions {

    @Autowired
    private AccessCodeRepository accessCodeRepository;

    public List<AccessCodeInfoDTO> getAllByTenantId(Integer tenantId){
        List<AccessCode> accessCodes = accessCodeRepository.getAllByTenantId(tenantId);

        return accessCodes.stream()
                .map(convertAccessCodeToDTO).collect(Collectors.toList());
    }
}
