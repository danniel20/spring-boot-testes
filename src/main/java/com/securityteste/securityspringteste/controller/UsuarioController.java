package com.securityteste.securityspringteste.controller;

import java.time.LocalDate;
import java.util.List;

import com.securityteste.securityspringteste.model.Usuario;
import com.securityteste.securityspringteste.service.usuarios.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@GetMapping
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ModelAndView index(){
		List<Usuario> usuariosList = this.usuarioService.buscarTodos();

		ModelAndView mv = new ModelAndView("usuarios/index");
        mv.addObject("usuarios", usuariosList);
        return mv;
	}

	@ModelAttribute("dataAtual")
	public LocalDate dataAtual(){
		return LocalDate.now();
	}

}
