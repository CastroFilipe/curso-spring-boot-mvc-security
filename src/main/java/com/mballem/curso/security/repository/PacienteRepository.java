package com.mballem.curso.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mballem.curso.security.domain.Paciente;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
	
	@Query("SELECT DISTINCT p FROM Paciente p WHERE p.usuario.email LIKE :email")
	Optional<Paciente> findByUsuarioEmail(String email);
}
