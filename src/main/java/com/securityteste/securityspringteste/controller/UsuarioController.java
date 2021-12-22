package com.securityteste.securityspringteste.controller;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;

import com.securityteste.securityspringteste.model.Usuario;
import com.securityteste.securityspringteste.service.usuarios.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

	@GetMapping("/new")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ModelAndView form(){
		ModelAndView mv = new ModelAndView("usuarios/form");
		mv.addObject("usuario", new Usuario());
        return mv;
	}

	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN')")
	public String create(@ModelAttribute @Valid Usuario usuario, BindingResult result, RedirectAttributes ra){

		if(result.hasErrors()){
			return "usuarios/form";
		}

		Usuario usuarioSalvo = this.usuarioService.salvar(usuario);

		ra.addFlashAttribute("flash", usuarioSalvo);
		return "redirect:/usuarios";
	}

	@ModelAttribute("dataAtual")
	public LocalDate dataAtual(){
		return LocalDate.now();
	}

}
