package br.com.fast.workshoptracker.infrastructure.persistence.repository;

import br.com.fast.workshoptracker.domain.entity.Workshop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkshopRepository extends JpaRepository<Workshop, Long> {
}

