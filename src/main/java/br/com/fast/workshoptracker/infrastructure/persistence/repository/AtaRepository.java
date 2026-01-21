package br.com.fast.workshoptracker.infrastructure.persistence.repository;

import br.com.fast.workshoptracker.domain.entity.Ata;
import br.com.fast.workshoptracker.infrastructure.persistence.projection.ColaboradorWorkshopRow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface AtaRepository extends JpaRepository<Ata, Long> {

	boolean existsByWorkshopId(Long workshopId);

	@EntityGraph("Ata.withWorkshopAndColaboradores")
	Optional<Ata> findById(Long id);

	@EntityGraph("Ata.withWorkshopAndColaboradores")
	Optional<Ata> findByIdAndWorkshopId(Long id, Long workshopId);

	@Query("""
			select
				c.id as colaboradorId,
				c.nome as colaboradorNome,
				w.id as workshopId,
				w.nome as workshopNome,
				w.dataRealizacao as workshopDataRealizacao,
				w.descricao as workshopDescricao
			from Ata a
			join a.colaboradores c
			join a.workshop w
			where (:workshopNome is null or lower(w.nome) like lower(concat('%', :workshopNome, '%')))
			  and (:dataRealizacao is null or w.dataRealizacao = :dataRealizacao)
			order by c.nome asc, w.dataRealizacao asc, w.nome asc
			""")
	Page<ColaboradorWorkshopRow> findParticipacoes(
			@Param("workshopNome") String workshopNome,
			@Param("dataRealizacao") LocalDate dataRealizacao,
			Pageable pageable
	);
}

