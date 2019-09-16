package com.mballem.curso.security.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mballem.curso.security.domain.Especialidade;
import com.mballem.curso.security.service.EspecialidadeService;

@Controller
@RequestMapping("especialidades")
public class EspecialidadeController {
	
	@Autowired
	EspecialidadeService especialidadeService;
 
	@GetMapping({"", "/"})
	public String abrir(Especialidade especialidade) {	
		
		return "especialidade/especialidade";
	}
	
	//metodo que recebe o submite do formulario para salvar uma especialidade
	@PostMapping("/salvar")
	public String salvar(Especialidade especialidade, RedirectAttributes attr) {
		especialidadeService.salvar(especialidade);//salva a especialidade
		attr.addFlashAttribute("sucesso", "Operação realizada com sucesso!");//envia uma mensagem de alerta em caso de sucesso
		return "redirect:/especialidades"; //redireciona para a rela de cadastro
	}
}
