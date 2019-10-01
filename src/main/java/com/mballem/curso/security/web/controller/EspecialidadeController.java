package com.mballem.curso.security.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	
	/**
	 * metodo que recebe o submite do formulario para salvar uma especialidade
	 * 
	 * @param especialidade objeto especialidade a ser salvo.
	 * @param attr objeto para o redirecionamento de dados 
	 * */
	@PostMapping("/salvar")
	public String salvar(Especialidade especialidade, RedirectAttributes attr) {
		especialidadeService.salvar(especialidade);//salva a especialidade
		attr.addFlashAttribute("sucesso", "Operação realizada com sucesso!");//envia uma mensagem de alerta em caso de sucesso
		return "redirect:/especialidades"; //redireciona para a rela de cadastro
	}
	
	/**
	 * Método para listar as especialidades na tabela presente na página especialidade/especialidade.html
	 * 
	 * @param request os parâmetros de solicitação do cliente.
	 * 
	 * */
	@GetMapping("/datatables/server")
	public ResponseEntity<?> getEspecialidades(HttpServletRequest request) {	
		
		return ResponseEntity.ok(especialidadeService.buscarEspecialidades(request));
	}
	
	/**
	 * metodo que recebe a requisição quando o botão de editar de uma especialidade receber um click. 
	 * Após o evento, Faz a busca na especialidade que se quer editar.
	 * 
	 * @param id o id da especialidade a ser editada
	 * 
	 */
	@GetMapping("/editar/{id}")
	public String preEditar(@PathVariable("id") Long id, ModelMap model) {	
		model.addAttribute("especialidade", especialidadeService.buscarPorId(id));//adiciona as informações da especialidade nos campos para edição
		
		return "especialidade/especialidade";
	}
	
	/**
	 * Faz a exclusão de uma especialidade
	 * 
	 * @param id o id da especialidade a ser excluída
	 * @param attr objeto para o redirecionamento de dados 
	 * */
	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") Long id, RedirectAttributes attr) {	
		
		especialidadeService.remover(id);
		
		attr.addFlashAttribute("sucesso", "excluído com sucesso");
		
		return "redirect:/especialidades";
	}
	
	@GetMapping("/titulo")
	public ResponseEntity<?> getEspecialidadesPorTermo(@RequestParam("termo") String termo) {	
		List<String> especialidades = especialidadeService.buscarEspecialidadeByTermo(termo);
		return ResponseEntity.ok(especialidades);
	}
}
