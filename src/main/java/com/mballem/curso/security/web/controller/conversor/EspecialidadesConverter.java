package com.mballem.curso.security.web.controller.conversor;

import java.util.HashSet;
import java.util.Set;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.mballem.curso.security.domain.Especialidade;
import com.mballem.curso.security.service.EspecialidadeService;

/**
 * Classe conversora, converte um array de String, contendo nomes das especialidades, em um conjunto Set<Especialidade>
 * */
@Component
public class EspecialidadesConverter implements Converter<String[], Set<Especialidade>> {
	
	private EspecialidadeService especialidadeService;
	
	/**
	 * @param titulos array de titulos em formato String vindas da view
	 * */
	@Override
	public Set<Especialidade> convert(String[] titulos) {
		
		Set<Especialidade> especialidades = new HashSet<>();
		
		if(titulos != null && titulos.length > 0) {
			especialidades.addAll(especialidadeService.buscarPorTitulos(titulos));
		}
		
		return especialidades;
	}
}
