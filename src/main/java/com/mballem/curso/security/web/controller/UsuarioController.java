package com.mballem.curso.security.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mballem.curso.security.domain.Usuario;
import com.mballem.curso.security.service.UsuarioService;

@Controller
@RequestMapping("u")
public class UsuarioController {
	
	@Autowired
	UsuarioService usuarioService;

	// abrir pagina de dados pessoais de medicos 
	@GetMapping({"/novo/cadastro/usuario"})
	public String cadastroPorAdminParaAdminMedicoPaciente(Usuario usuario) {	
		
		return "usuario/cadastro";
	}
	
	//abrir lista de usuários
	@GetMapping("/lista")
	public String listarUsuarios() {	
		
		return "usuario/lista";
	}
	
	//abrir lista de usuários
	@GetMapping("/datatables/server/usuarios")
	public ResponseEntity<?> listarUsuariosDatatables(HttpServletRequest request) {	
		
		return ResponseEntity.ok(usuarioService.buscarTodos(request));
	}
}
