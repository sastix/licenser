package com.sastix.licenser.server.controller;
import com.sastix.licenser.commons.domain.LicenserContextUrl;
import com.sastix.licenser.server.service.LicenserService;
import com.sastix.toolkit.versioning.client.ApiVersionClient;
import com.sastix.toolkit.versioning.service.ApiVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class LicenserMainController implements LicenserContextUrl {

    @Autowired
    LicenserService licenserService;

    @Autowired
    ApiVersionService apiVersionService;


    @RequestMapping("/")
    public String viewHome(final Model model) {
        return HOME_VIEW;
    }

    @ResponseBody
    @RequestMapping(value = "/v"+ REST_API_V1_0 +"/"+HELLO+"/{echo}", method = RequestMethod.GET)
    public String hello(@PathVariable String echo) {
        return "Hello! echo: "+echo;
    }

    @ResponseBody
    @RequestMapping(value = "/v"+ REST_API_V1_1 +"/"+HELLO+"/{echo}", method = RequestMethod.GET)
    public String helloV1_1(@PathVariable String echo) {
        return "Hello [versions supported min:"+apiVersionService.getApiVersion().getMinVersion()+", max:"+apiVersionService.getApiVersion().getMaxVersion()+"]! echo: "+echo;
    }
}
