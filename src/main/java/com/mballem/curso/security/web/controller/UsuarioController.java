package com.mballem.curso.security.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mballem.curso.security.domain.Perfil;
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
	
	/**
	 * Método que recebe a requisição do formulário para cadastro de novos usuários no sistema.
	 * O método será utilizado quando um perfil do tipo ADMIN estiver logado. Com isso, novos usuários com perfis ADMIN e MEDICO poderão
	 * ser salvos.
	 * O único usuário que poderá fazer seu próprio cadastro será o PACIENTE.
	 * 
	 * @param usuario objeto do tipo Usuario que foi enviado pelo formulário
	 * @param attr Objeto para fazer o redirecionamento
	 * */
	@PostMapping("/cadastro/salvar")
	public String salvarUsuarios(Usuario usuario, RedirectAttributes attr) {
		List<Perfil> perfis = usuario.getPerfis();//pega a lista de perfis do novo usuário p-ara facilitar os testes a seguir
		
		if(
			perfis.size() > 2 //se a lista de perfis contiver mais de dois perfis, significa que um usuário está sendo cadastrado como sendo ADMIN, MEDICO e PACIENTE ao mesmo tempo. E as regras de negócio não permitem.
			|| perfis.contains(new Perfil(1L)) && perfis.contains(new Perfil(3L))//impede o cadastro de um usuário com perfil de ADMIN e PACIENTE
			|| perfis.contains(new Perfil(2L)) && perfis.contains(new Perfil(3L))//impede o cadastro de um usuário com perfil de MEDICO e PACIENTE
			) {
			
			attr.addFlashAttribute("falha", "paciente não pode ser ADMIN e/ou MEDICO");
			attr.addFlashAttribute("usuario", usuario );
		} 
		
		else { //Se todos os testes acima forem falsos, o cadastro poderá ser feito
			usuarioService.salvarUsuario(usuario);
			attr.addFlashAttribute("sucesso", "Usuário salvo com sucesso");
		}
		
		return "redirect:/u/novo/cadastro/usuario";//redireciona para a próprio formulário de cadastro
	}
}
