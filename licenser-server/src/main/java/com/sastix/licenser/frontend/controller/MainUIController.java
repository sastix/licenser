package com.sastix.licenser.frontend.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static com.sastix.licenser.commons.domain.LicenserContextUrl.BASE_URL;
import static com.sastix.licenser.commons.domain.LicenserContextUrl.FRONTEND;

@Controller
@RequestMapping(value = BASE_URL + "/" + FRONTEND )
public class MainUIController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home() {
        return "home";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginForm() {
        return "login";
    }
}
