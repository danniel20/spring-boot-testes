package com.securityteste.securityspringteste.controller;

import java.util.List;

import javax.validation.Valid;

import com.securityteste.securityspringteste.model.Foto;
import com.securityteste.securityspringteste.model.Papel;
import com.securityteste.securityspringteste.model.Usuario;
import com.securityteste.securityspringteste.service.papeis.PapelServiceImpl;
import com.securityteste.securityspringteste.service.storage.StorageServiceImpl;
import com.securityteste.securityspringteste.service.usuarios.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
    private PapelServiceImpl papelService;

	@Autowired
    private PasswordEncoder passwordEncoder;

	@Autowired
	private StorageServiceImpl storageService;

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
	public ModelAndView newForm(){
		ModelAndView mv = new ModelAndView("usuarios/form");
		mv.addObject("usuario", new Usuario());
        return mv;
	}

	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN')")
	public String create(@ModelAttribute @Valid Usuario usuario, BindingResult result, @RequestParam(required = false) MultipartFile file, RedirectAttributes ra){

		if(result.hasErrors()){
			return "usuarios/form";
		}

		Usuario usuarioNovo = Usuario.builder()
                .login(usuario.getLogin())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .senha(passwordEncoder.encode(usuario.getSenha()))
                .dataNascimento(usuario.getDataNascimento())
                .build();

		Papel papel = this.papelService.bucarPorNome("USER").get();
		usuarioNovo.getPapeis().add(papel);

		if(file != null && !file.isEmpty()){
			String fileName = storageService.store(file);
			Foto foto = Foto.builder().fileName(fileName).build();
			usuarioNovo.setFoto(foto);
		}

		this.usuarioService.salvar(usuarioNovo);

		ra.addFlashAttribute("notice", "Usuário cadastrado com sucesso!");
		return "redirect:/usuarios";
	}

	@GetMapping("/{id}")
	public ModelAndView show(@PathVariable("id") Long id){
		ModelAndView mv = new ModelAndView("usuarios/fragments/usuario :: modal-user-show");
		Usuario usuario = usuarioService.bucarPorId(id).get();
		mv.addObject("usuario", usuario);
        return mv;
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public String delete(@PathVariable("id") Long id, RedirectAttributes ra){
		usuarioService.deletarPorId(id);
		ra.addFlashAttribute("notice", "Usuário removido com sucesso!");
		return "redirect:/usuarios/index";
	}

}
