package br.com.fast.workshoptracker.infrastructure.persistence.repository;

import br.com.fast.workshoptracker.domain.entity.Colaborador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColaboradorRepository extends JpaRepository<Colaborador, Long> {
}

