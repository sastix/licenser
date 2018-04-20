package com.sastix.licenser.server.controller;

import com.sastix.licenser.commons.content.AccessCodeInfoDTO;
import com.sastix.licenser.commons.content.ActivateAccessCodeDTO;
import com.sastix.licenser.commons.content.ImportExcelDTO;
import com.sastix.licenser.commons.domain.LicenserContextUrl;
import com.sastix.licenser.server.service.LicenserService;
import com.sastix.toolkit.versioning.service.ApiVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static com.sastix.licenser.commons.domain.LicenserContextUrl.BASE_URL;


@RestController
@RequestMapping(value = BASE_URL)
public class LicenserMainController implements LicenserContextUrl {

    @Autowired
    LicenserService licenserService;

    @Autowired
    ApiVersionService apiVersionService;


    @RequestMapping(value = "/v" + REST_API_V1_0 + "/" + IMPORT_EXCEL, method = RequestMethod.POST)
    public List<AccessCodeInfoDTO> importExcel(@Valid @RequestBody ImportExcelDTO importExcelDTO){
        return licenserService.importFromExcel(importExcelDTO);
    }

    @RequestMapping(value = "/v" + REST_API_V1_0 + "/" + ACCESS_CODE +"/{accessCode}", method = RequestMethod.GET)
    public AccessCodeInfoDTO getAccessCodeInfo(@PathVariable String accessCode) {
        return licenserService.getAccessCodeInfo(accessCode);
    }

    @RequestMapping(value = "/v" + REST_API_V1_0 + "/" +  ACTIVATE, method = RequestMethod.POST)
    public AccessCodeInfoDTO activateAccessCode(@Valid @RequestBody ActivateAccessCodeDTO activateAccessCodeDTO) {
        return licenserService.activateAccessCode(activateAccessCodeDTO);
    }

    @RequestMapping(value = "/v" + REST_API_V1_0 + "/" + ACCESS_CODE, method = RequestMethod.GET)
    public List<AccessCodeInfoDTO> getAllAccessCodes() {
        return licenserService.getAllAccessCodes();
    }
}
