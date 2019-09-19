package com.mballem.curso.security.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mballem.curso.security.domain.Medico;
import com.mballem.curso.security.domain.Usuario;
import com.mballem.curso.security.service.MedicoService;
import com.mballem.curso.security.service.UsuarioService;

@Controller
@RequestMapping("medicos")
public class MedicoController {

	@Autowired
	MedicoService medicoService;
	
	@Autowired
	UsuarioService usuarioService;
	
	// abrir pagina de dados pessoais de medicos 
	@GetMapping({"/dados"})
	public String abrirPorMedico(Medico medico, ModelMap model, @AuthenticationPrincipal User user) {	
		
		if(medico.hasNotId()) {
			medico = medicoService.buscarPorEmail(user.getUsername());
			model.addAttribute("medico", medico);
		}
		
		return "medico/cadastro";
	}
	
	/**
	 * Método para salvar um Médico.
	 * 
	 * a anotação @AuthenticationPrincipal fornece uma forma de pegar as informações do usuário logado na aplicação. Assim poderemos pegar o username(email)
	 * do usuário logado e com isso, fazer uma busca do id desse usuário.
	 * 
	 * @param user representa o usuário que está logado.
	 * */
	@PostMapping({"/salvar"})
	public String salvar(Medico medico, RedirectAttributes attr, @AuthenticationPrincipal User user) {
		
		
		if(medico.hasNotId() && medico.getUsuario().hasNotId()) {
			System.out.println("----------------------------------"+user.getUsername());
			Usuario usuario = usuarioService.buscarPorEmail(user.getUsername());
			medico.setUsuario(usuario);
		}
		
		medicoService.salvar(medico);
		
		attr.addFlashAttribute("sucesso", "Operação realizada com sucesso");
		attr.addFlashAttribute("medico", medico);
		
		return "redirect:/medicos/dados";
	}
	
	@PostMapping({"/editar"})
	public String editar(Medico medico, RedirectAttributes attr) {	
		medicoService.editar(medico);
		attr.addFlashAttribute("sucesso", "Operação realizada com sucesso");
		attr.addFlashAttribute("medico", medico);	
		return "redirect:/medicos/dados";
	}
}
