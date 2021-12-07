package com.securityteste.securityspringteste.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String index(){
        return "home";
    }
    
    @GetMapping("/admin")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String admin(){
        return "admin";
    }
}
