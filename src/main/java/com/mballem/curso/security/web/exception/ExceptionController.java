package com.mballem.curso.security.web.exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * A classe ExceptionController possui regras que, quando satisfeitas, chamam o método equivalente.
 * Anotação @ControllerAdvice funciona como um "ouvinte" na aplicação. Logo que uma regra for satisfeita, o método será chamado.
 * 
 * */
@ControllerAdvice
public class ExceptionController {

	/**
	 * Método que captura uma exceção do tipo UsernameNotFoundException
	 * 
	 * @param ex exceção do tipo UsernameNotFoundException.
	 * */
	//anotação @ExceptionHandler informa que esse método será chamado quando uma exceção do tipo UsernameNotFoundException for lançada na aplicação.
	@ExceptionHandler(UsernameNotFoundException.class)
	public ModelAndView usuarioNaoEncontradoException(UsernameNotFoundException ex) {
		
		//página de destino a qual o model será enviado. o model conterá as informções a serem exibidas ao usuário na página error.html
		ModelAndView model = new ModelAndView("error");
		
		model.addObject("status", 404);//status, error e message são váriaveis definidas, via thymeleaf, na página templates/usuario/error.html
		model.addObject("error", "Operação não pode ser realizada");
		model.addObject("message", ex.getMessage());
		
		return model;
	}
}
