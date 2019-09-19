package com.mballem.curso.security.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mballem.curso.security.domain.Paciente;
import com.mballem.curso.security.domain.Usuario;
import com.mballem.curso.security.service.PacienteService;
import com.mballem.curso.security.service.UsuarioService;

@Controller
@RequestMapping("pacientes")
public class PacienteController {
	
	@Autowired
	private PacienteService pacienteService;
	
	@Autowired
	private UsuarioService usuarioService;

	// abrir pagina de dados pessoais do paciente. caso o paciente já possua os dados cadastrados no sistema, a página será aberta com os compos preenchidos.
	@GetMapping("/dados")
	public String cadastrar(Paciente paciente, ModelMap model, @AuthenticationPrincipal User user) {
		paciente = pacienteService.buscarPorUsuarioEmail(user.getUsername());
		
		//caso não possua id o usuário está fazendo um insert
		if (paciente.hasNotId()) {
			paciente.setUsuario(new Usuario(user.getUsername()));
		}
		model.addAttribute("paciente", paciente);
		return "paciente/cadastro";
	}
}
