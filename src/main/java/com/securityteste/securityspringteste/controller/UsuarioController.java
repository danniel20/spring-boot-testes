package com.securityteste.securityspringteste.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

	@GetMapping
	@PreAuthorize("hasAnyRole('ADMIN')")
	public String index(){
		return "usuarios/index";
	}

}
