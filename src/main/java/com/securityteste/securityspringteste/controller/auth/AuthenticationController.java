package com.securityteste.securityspringteste.controller.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthenticationController {

    @GetMapping("/login")
    public String login(){
        return "auth/login";
    }

    @GetMapping("/accessDenied")
    public String accessDenied(){
        return "error/403";
    }

}
