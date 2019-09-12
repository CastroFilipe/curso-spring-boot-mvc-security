package com.mballem.curso.security.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mballem.curso.security.domain.Usuario;

@Controller
@RequestMapping("u")
public class UsuarioController {

	// abrir pagina de dados pessoais de medicos 
	@GetMapping({"/novo/cadastro/usuario"})
	public String cadastroPorAdminParaAdminMedicoPaciente(Usuario usuario) {	
		
		return "usuario/cadastro";
	}
}
