package com.securityteste.securityspringteste.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

	@GetMapping("/")
	public String root(){
		return "redirect:/home";
	}

    @GetMapping("/home")
    public ModelAndView index(){
        ModelAndView mv = new ModelAndView("home");
        mv.addObject("message", "Seja bem-vindo!");
        return mv;
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String admin(){
        return "admin";
    }
}
