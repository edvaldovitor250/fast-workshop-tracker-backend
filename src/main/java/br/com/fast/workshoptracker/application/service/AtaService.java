package br.com.fast.workshoptracker.application.service;

import br.com.fast.workshoptracker.api.dto.request.AtaAddColaboradorRequest;
import br.com.fast.workshoptracker.api.dto.request.AtaCreateRequest;
import br.com.fast.workshoptracker.api.dto.response.AtaResponse;
import br.com.fast.workshoptracker.api.dto.response.ColaboradorParticipacoesResponse;
import br.com.fast.workshoptracker.api.dto.response.WorkshopResponse;
import br.com.fast.workshoptracker.api.mapper.AtaMapper;
import br.com.fast.workshoptracker.domain.entity.Ata;
import br.com.fast.workshoptracker.domain.entity.Colaborador;
import br.com.fast.workshoptracker.domain.entity.Workshop;
import br.com.fast.workshoptracker.domain.exception.Exceptions;
import br.com.fast.workshoptracker.domain.exception.util.ExceptionUtils;
import br.com.fast.workshoptracker.infrastructure.persistence.projection.ColaboradorWorkshopRow;
import br.com.fast.workshoptracker.infrastructure.persistence.repository.AtaRepository;
import br.com.fast.workshoptracker.infrastructure.persistence.repository.ColaboradorRepository;
import br.com.fast.workshoptracker.infrastructure.persistence.repository.WorkshopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AtaService {

	private final AtaRepository ataRepository;
	private final WorkshopRepository workshopRepository;
	private final ColaboradorRepository colaboradorRepository;
	private final AtaMapper ataMapper;

	@Transactional
	public AtaResponse create(AtaCreateRequest request) {
		validateUniqueIds(request.colaboradoresIds());

		Workshop workshop = workshopRepository.findById(request.workshopId())
				.orElseThrow(() -> Exceptions.notFound(
						"Workshop não encontrado: id=" + request.workshopId(),
						ExceptionUtils.context("workshopId", request.workshopId())
				));

		if (ataRepository.existsByWorkshopId(workshop.getId())) {
			throw Exceptions.conflict(
					"Já existe ata para o workshop: id=" + workshop.getId(),
					ExceptionUtils.context("workshopId", workshop.getId())
			);
		}

		Ata ata = new Ata(workshop);

		List<Long> colaboradoresIds = request.colaboradoresIds();
		if (colaboradoresIds != null && !colaboradoresIds.isEmpty()) {
			List<Colaborador> colaboradores = colaboradorRepository.findAllById(colaboradoresIds);
			if (colaboradores.size() != colaboradoresIds.size()) {
				Set<Long> foundIds = new HashSet<>(colaboradores.size());
				for (Colaborador c : colaboradores) {
					foundIds.add(c.getId());
				}
				List<Long> missing = colaboradoresIds.stream().filter(id -> !foundIds.contains(id)).toList();
				throw Exceptions.notFound(
						"Colaboradores não encontrados: ids=" + missing,
						ExceptionUtils.context("colaboradoresIds", missing)
				);
			}
			ata.getColaboradores().addAll(colaboradores);
		}

		ata = ataRepository.save(ata);
		return ataMapper.toResponse(ata);
	}

	@Transactional
	public AtaResponse addColaborador(Long workshopId, Long ataId, AtaAddColaboradorRequest request) {
		Ata ata = ataRepository.findByIdAndWorkshopId(ataId, workshopId).orElse(null);
		if (ata == null) {
			if (!workshopRepository.existsById(workshopId)) {
				throw Exceptions.notFound(
						"Workshop não encontrado: id=" + workshopId,
						ExceptionUtils.context("workshopId", workshopId)
				);
			}
			throw Exceptions.notFound(
					"Ata não encontrada para o workshop: ataId=" + ataId + ", workshopId=" + workshopId,
					ExceptionUtils.context("ataId", ataId, "workshopId", workshopId)
			);
		}

		Colaborador colaborador = colaboradorRepository.findById(request.colaboradorId())
				.orElseThrow(() -> Exceptions.notFound(
						"Colaborador não encontrado: id=" + request.colaboradorId(),
						ExceptionUtils.context("colaboradorId", request.colaboradorId())
				));

		boolean added = ata.getColaboradores().add(colaborador);
		if (!added) {
			throw Exceptions.conflict(
					"Colaborador já está presente na ata: ataId=" + ataId + ", colaboradorId=" + colaborador.getId(),
					ExceptionUtils.context("ataId", ataId, "colaboradorId", colaborador.getId())
			);
		}

		return ataMapper.toResponse(ata);
	}

	@Transactional
	public void removeColaborador(Long ataId, Long colaboradorId) {
		Ata ata = ataRepository.findById(ataId)
				.orElseThrow(() -> Exceptions.notFound(
						"Ata não encontrada: id=" + ataId,
						ExceptionUtils.context("ataId", ataId)
				));

		if (!colaboradorRepository.existsById(colaboradorId)) {
			throw Exceptions.notFound(
					"Colaborador não encontrado: id=" + colaboradorId,
					ExceptionUtils.context("colaboradorId", colaboradorId)
			);
		}

		boolean removed = ata.getColaboradores().removeIf(c -> Objects.equals(c.getId(), colaboradorId));
		if (!removed) {
			throw Exceptions.notFound(
					"Colaborador não está presente na ata: ataId=" + ataId + ", colaboradorId=" + colaboradorId,
					ExceptionUtils.context("ataId", ataId, "colaboradorId", colaboradorId)
			);
		}
	}

	@Transactional(readOnly = true)
	public List<ColaboradorParticipacoesResponse> listarParticipacoes(String workshopNome, LocalDate dataRealizacao) {
		String nome = normalizeBlankToNull(workshopNome);

		List<ColaboradorWorkshopRow> rows = ataRepository.findParticipacoes(nome, dataRealizacao, Pageable.unpaged()).getContent();

		Map<Long, ColaboradorParticipacoesResponseAccumulator> map = new LinkedHashMap<>();
		for (ColaboradorWorkshopRow row : rows) {
			ColaboradorParticipacoesResponseAccumulator acc = map.computeIfAbsent(
					row.getColaboradorId(),
					id -> new ColaboradorParticipacoesResponseAccumulator(row.getColaboradorId(), row.getColaboradorNome())
			);
			acc.addWorkshop(new WorkshopResponse(
					row.getWorkshopId(),
					row.getWorkshopNome(),
					row.getWorkshopDataRealizacao(),
					row.getWorkshopDescricao()
			));
		}

		return map.values().stream().map(ColaboradorParticipacoesResponseAccumulator::toResponse).toList();
	}

	private static void validateUniqueIds(List<Long> ids) {
		if (ids == null || ids.isEmpty()) {
			return;
		}
		Set<Long> seen = new HashSet<>(ids.size());
		for (Long id : ids) {
			if (id == null) {
				throw Exceptions.badRequest(
						"colaboradoresIds não pode conter null",
						ExceptionUtils.context("colaboradoresIds", ids)
				);
			}
			if (!seen.add(id)) {
				throw Exceptions.badRequest(
						"colaboradoresIds contém ids duplicados",
						ExceptionUtils.context("colaboradoresIds", ids)
				);
			}
		}
	}

	private static String normalizeBlankToNull(String value) {
		if (value == null) return null;
		String trimmed = value.trim();
		return trimmed.isEmpty() ? null : trimmed;
	}

	private static final class ColaboradorParticipacoesResponseAccumulator {
		private final Long colaboradorId;
		private final String nome;
		private final List<WorkshopResponse> workshops = new ArrayList<>();
		private final Set<Long> workshopIds = new HashSet<>();

		private ColaboradorParticipacoesResponseAccumulator(Long colaboradorId, String nome) {
			this.colaboradorId = colaboradorId;
			this.nome = nome;
		}

		private void addWorkshop(WorkshopResponse workshop) {
			if (workshop == null || workshop.id() == null) {
				return;
			}
			if (workshopIds.add(workshop.id())) {
				workshops.add(workshop);
			}
		}

		private ColaboradorParticipacoesResponse toResponse() {
			return new ColaboradorParticipacoesResponse(colaboradorId, nome, workshops);
		}
	}
}

