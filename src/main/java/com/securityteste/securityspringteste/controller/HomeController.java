package com.securityteste.securityspringteste.controller;

import javax.annotation.security.RolesAllowed;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {
    
    @GetMapping
    @RolesAllowed("ADMIN, USER")
    public String index(){
        return "Olá";
    }
}
