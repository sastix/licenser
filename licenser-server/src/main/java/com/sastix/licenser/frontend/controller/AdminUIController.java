package com.sastix.licenser.frontend.controller;

import com.sastix.licenser.commons.content.AccessCodeInfoDTO;
import com.sastix.licenser.commons.content.ActivateAccessCodeDTO;
import com.sastix.licenser.commons.content.ImportExcelDTO;
import com.sastix.licenser.commons.content.TenantDTO;
import com.sastix.licenser.commons.exception.InvalidAccessCodeProvidedException;
import com.sastix.licenser.commons.exception.MalformedExcelException;
import com.sastix.licenser.server.model.AccessCode;
import com.sastix.licenser.server.model.Tenant;
import com.sastix.licenser.server.repository.AccessCodeRepository;
import com.sastix.licenser.server.service.AccessCodeService;
import com.sastix.licenser.server.service.LicenserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.awt.print.Pageable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.sastix.licenser.commons.domain.LicenserContextUrl.BASE_URL;
import static com.sastix.licenser.commons.domain.LicenserContextUrl.FRONTEND;

@Controller
@RequestMapping(value = BASE_URL + "/" + FRONTEND )
public class AdminUIController {

    @Autowired
    private LicenserService licenserService;

    @Autowired
    private AccessCodeService accessCodeService;

    @RequestMapping(value = "/admin/import-codes", method = RequestMethod.GET)
    public String importExcel(Model model) {
        model.addAttribute("tenants", licenserService.getAllTenants());
        return "import-codes";
    }

    @RequestMapping(value = "/admin/import-codes", method = RequestMethod.POST)
    public String importExcelResult(@Valid @RequestParam("file") MultipartFile file, @RequestParam("tenant") String tenantId, Model model) {
        if (tenantId.equals(("0")) && (file.isEmpty())) {
            model.addAttribute("tenants", licenserService.getAllTenants());
            model.addAttribute("errorFile", "Please select a file to upload.");
            model.addAttribute("errorTenant", "Please select tenant.");
            return "import-codes";
        }
        if (tenantId.equals("0")) {
            model.addAttribute("tenants", licenserService.getAllTenants());
            model.addAttribute("errorTenant", "Please select tenant.");
            return "import-codes";
        }
        if (file.isEmpty()) {
            model.addAttribute("tenants", licenserService.getAllTenants());
            model.addAttribute("errorFile", "Please select a file to upload.");
            return "import-codes";
        }
        try {
            byte[] bytes = file.getBytes();
            licenserService.importFromExcel(new ImportExcelDTO(bytes, Integer.parseInt(tenantId)));

            model.addAttribute("message", "You successfully uploaded " + file.getOriginalFilename());
            model.addAttribute("codes", licenserService.getAllAccessCodes());
            return "import-codes-result";
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidAccessCodeProvidedException ex) {
            model.addAttribute("message", ex.getMessage());
            return "/error/invalid-access-code";
        } catch (MalformedExcelException ex) {
            model.addAttribute("error", ex.getMessage());
            return "/error/400";
        }
        return "import-codes";

    }


    @RequestMapping(value = "/admin/codes", method = RequestMethod.GET)
    public String allAccessCodes(Model model, @RequestParam(value = "tenantId", required = false) Integer tenantId) {
        List<AccessCodeInfoDTO> accessCodeInfoDTOS;
        if (tenantId != null) {
            accessCodeInfoDTOS = accessCodeService.getAllByTenantId(tenantId);
        } else {
            accessCodeInfoDTOS = licenserService.getAllAccessCodes();
        }
        model.addAttribute("tenants", licenserService.getAllTenants());
        model.addAttribute("codes", accessCodeInfoDTOS);
        return "access-codes";
    }

    @RequestMapping(value = "/admin/{code}", method = RequestMethod.GET)
    public String accessCodeInfo(@PathVariable("code") String code, Model model) {
        model.addAttribute("code", licenserService.getAccessCodeInfo(code));
        return "access-code-info";
    }

    @RequestMapping(value = "/admin/{code}", method = RequestMethod.POST)
    public String activateCode(@PathVariable("code") String code, @RequestParam("UserId") Long userId, Model model) {
        licenserService.activateAccessCode(new ActivateAccessCodeDTO(code, userId));
        model.addAttribute("code", licenserService.getAccessCodeInfo(code));
        return "access-code-info";
    }

}
