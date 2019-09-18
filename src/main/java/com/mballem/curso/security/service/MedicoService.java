package com.mballem.curso.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mballem.curso.security.domain.Medico;
import com.mballem.curso.security.repository.MedicoRepository;

@Service
public class MedicoService {
	
	@Autowired
	private MedicoRepository medicoRepository;
	
	
	@Transactional(readOnly = true)
	public Medico buscarPorUsuarioId(Long id) {
		
		//faz a busca por um médico . se não encontrar retorna um novo Objeto Medico
		return medicoRepository.findByUsuarioId(id).orElse(new Medico());
	}

	@Transactional(readOnly = false)
	public void salvar(Medico medico) {
		medicoRepository.save(medico);
	}

	@Transactional(readOnly = false)
	public void editar(Medico medico) {
		
		Medico m2 = medicoRepository.findById(medico.getId()).get(); 
		
		m2.setCrm(medico.getCrm());
		m2.setDtInscricao(medico.getDtInscricao());
		m2.setNome(medico.getNome());
		
		if(!medico.getEspecialidades().isEmpty()) {
			m2.getEspecialidades().addAll(medico.getEspecialidades());
		}
		
		/*
		 * trecho opcional pois o m2 foi construido a partir de uma busca pelo Hibernate, logo ele é um objeto persistente e que já está sendo monitorado
		 * pelo Hibernate e qualquer modificação nos seus atributos será salva no banco de dados sem a necessidade do metodo save() do repository
		 */
		medicoRepository.save(m2);
	}

}
