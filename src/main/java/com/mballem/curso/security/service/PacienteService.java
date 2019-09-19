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
}
