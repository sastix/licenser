package com.sastix.licenser.frontend.controller;

import com.sastix.licenser.commons.content.ActivateAccessCodeDTO;
import com.sastix.licenser.commons.content.ImportExcelDTO;
import com.sastix.licenser.commons.exception.InvalidAccessCodeProvidedException;
import com.sastix.licenser.server.service.LicenserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.sastix.licenser.commons.domain.LicenserContextUrl.BASE_URL;
import static com.sastix.licenser.commons.domain.LicenserContextUrl.FRONTEND;

@Controller
@RequestMapping(value = BASE_URL + "/" + FRONTEND )
public class AdminUIController {

    @Autowired
    private LicenserService licenserService;

    @RequestMapping(value = "/admin/import-codes", method = RequestMethod.GET)
    public String importExcel() {
        return "import-codes";
    }

    @RequestMapping(value = "/admin/import-codes", method = RequestMethod.POST)
    public String importExcelResult(@RequestParam("file") MultipartFile file, Model model) {
        if (file.isEmpty()) {
            model.addAttribute("message", "Please select a file to upload");
            return "import-codes-result";
        }
        try {
            byte[] bytes = file.getBytes();
            licenserService.importFromExcel(new ImportExcelDTO(bytes, 1));

            model.addAttribute("message", "You successfully uploaded " + file.getOriginalFilename());
            model.addAttribute("codes", licenserService.getAllAccessCodes());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidAccessCodeProvidedException ex) {
            model.addAttribute("message", ex.getMessage());
            return "/error/invalid-access-code";
        }
        return "import-codes-result";
    }


    @RequestMapping(value = "/admin/codes", method = RequestMethod.GET)
    public String allAccessCodes(Model model) {
        model.addAttribute("codes", licenserService.getAllAccessCodes());
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
