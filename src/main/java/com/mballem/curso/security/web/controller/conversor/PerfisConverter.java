package com.mballem.curso.security.web.controller.conversor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.mballem.curso.security.domain.Perfil;

/**
 * Classe conversora, converte um array de String contendo Ids em uma Lista de perfis
 * */
@Component
public class PerfisConverter implements Converter<String[], List<Perfil>> {
	
	/**
	 * @param source array contendo os IDS dos perfis selecionados no cadastro de um novo usuário.
	 * */
	@Override
	public List<Perfil> convert(String[] source) {
		List<Perfil> perfis = new ArrayList<>();
		
		for(String id : source) {
			if(!id.equals("0")) {
				perfis.add(new Perfil(Long.parseLong(id)));
			}
		}
		
		return perfis;
	}
}
