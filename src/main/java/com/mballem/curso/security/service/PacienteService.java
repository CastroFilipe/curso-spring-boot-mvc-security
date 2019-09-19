package com.mballem.curso.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mballem.curso.security.domain.Paciente;
import com.mballem.curso.security.repository.PacienteRepository;

@Service
public class PacienteService {
	
	@Autowired
	private PacienteRepository pacienteRepository;

	@Transactional(readOnly = true)
	public Paciente buscarPorUsuarioEmail(String email) {
		return pacienteRepository.findByUsuarioEmail(email).orElse(new Paciente());
	}

	@Transactional(readOnly = false)
	public void salvar(Paciente paciente) {
		pacienteRepository.save(paciente);
	}

	@Transactional(readOnly = false)
	public void editar(Paciente paciente) {
		
		Paciente p2 = pacienteRepository.findById(paciente.getId()).get();
		
		p2.setNome(paciente.getNome());
		p2.setDtNascimento(paciente.getDtNascimento());
		
		//a linha abaixo é opcional pois p2 é um objeto persistente e que está sendo monitorado pelo Hibernate. Qualquer modificação em seus atributos é salva no banco automaticamente.
		pacienteRepository.save(p2);
	}
}
