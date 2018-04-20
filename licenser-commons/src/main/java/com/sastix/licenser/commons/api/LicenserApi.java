package com.sastix.licenser.commons.api;


import com.sastix.licenser.commons.content.AccessCodeInfoDTO;
import com.sastix.licenser.commons.content.ActivateAccessCodeDTO;
import com.sastix.licenser.commons.content.ImportExcelDTO;
import com.sastix.licenser.commons.exception.AccessCodeNotFoundException;
import com.sastix.licenser.commons.exception.InvalidAccessCodeProvidedException;

import java.util.List;

public interface LicenserApi {

    AccessCodeInfoDTO getAccessCodeInfo(String accessCode) throws AccessCodeNotFoundException;

    AccessCodeInfoDTO activateAccessCode(ActivateAccessCodeDTO activateAccessCodeDTO) throws AccessCodeNotFoundException, InvalidAccessCodeProvidedException;

    List<AccessCodeInfoDTO> getAllAccessCodes();

    List<AccessCodeInfoDTO> importFromExcel(ImportExcelDTO importExcelDTO);
}
