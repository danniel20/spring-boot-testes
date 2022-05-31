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
	public ModelAndView form(Usuario usuario){
		ModelAndView mv = new ModelAndView("usuarios/form");
		mv.addObject("usuario", usuario == null ? new Usuario() : usuario);
        return mv;
	}

	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ModelAndView create(@ModelAttribute @Valid Usuario usuario, BindingResult result, RedirectAttributes ra){

		if(result.hasErrors()){
			return form(usuario);
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

		if(usuario.getFotoFile() != null && !usuario.getFotoFile().isEmpty()){
			String fileName = storageService.store(usuario.getFotoFile());
			Foto foto = Foto.builder().fileName(fileName).usuario(usuarioNovo).build();
			usuarioNovo.setFoto(foto);
		}

		System.out.println("TESTE >>> " + usuarioNovo);
		this.usuarioService.salvar(usuarioNovo);

		ra.addFlashAttribute("notice", "Usuário cadastrado com sucesso!");
		return new ModelAndView("redirect:/usuarios");
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
	public ModelAndView delete(@PathVariable("id") Long id){
		usuarioService.deletarPorId(id);

		ModelAndView mv = new ModelAndView("usuarios/index :: datatable-users");
		mv.getModel().put("usuarios", this.usuarioService.buscarTodos());
		return mv;
	}

}
