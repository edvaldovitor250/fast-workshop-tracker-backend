package br.com.fast.workshoptracker.service;

import br.com.fast.workshoptracker.domain.entity.Ata;
import br.com.fast.workshoptracker.domain.entity.Colaborador;
import br.com.fast.workshoptracker.domain.entity.Workshop;
import br.com.fast.workshoptracker.dto.request.AtaAddColaboradorRequest;
import br.com.fast.workshoptracker.dto.request.AtaCreateRequest;
import br.com.fast.workshoptracker.dto.response.AtaResponse;
import br.com.fast.workshoptracker.dto.response.ColaboradorParticipacoesResponse;
import br.com.fast.workshoptracker.dto.response.WorkshopResponse;
import br.com.fast.workshoptracker.exception.BadRequestException;
import br.com.fast.workshoptracker.exception.ConflictException;
import br.com.fast.workshoptracker.exception.NotFoundException;
import br.com.fast.workshoptracker.mapper.AtaMapper;
import br.com.fast.workshoptracker.repository.AtaRepository;
import br.com.fast.workshoptracker.repository.ColaboradorRepository;
import br.com.fast.workshoptracker.repository.WorkshopRepository;
import br.com.fast.workshoptracker.repository.projection.ColaboradorWorkshopRow;
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
public class AtaService {

	private final AtaRepository ataRepository;
	private final WorkshopRepository workshopRepository;
	private final ColaboradorRepository colaboradorRepository;
	private final AtaMapper ataMapper;

	public AtaService(
			AtaRepository ataRepository,
			WorkshopRepository workshopRepository,
			ColaboradorRepository colaboradorRepository,
			AtaMapper ataMapper
	) {
		this.ataRepository = ataRepository;
		this.workshopRepository = workshopRepository;
		this.colaboradorRepository = colaboradorRepository;
		this.ataMapper = ataMapper;
	}

	@Transactional
	public AtaResponse create(AtaCreateRequest request) {
		validateUniqueIds(request.colaboradoresIds());

		Workshop workshop = workshopRepository.findById(request.workshopId())
				.orElseThrow(() -> new NotFoundException("Workshop não encontrado: id=" + request.workshopId()));

		if (ataRepository.existsByWorkshop_Id(workshop.getId())) {
			throw new ConflictException("Já existe ata para o workshop: id=" + workshop.getId());
		}

		Ata ata = new Ata(workshop);

		List<Long> colaboradoresIds = request.colaboradoresIds();
		if (colaboradoresIds != null && !colaboradoresIds.isEmpty()) {
			List<Colaborador> colaboradores = colaboradorRepository.findAllById(colaboradoresIds);
			if (colaboradores.size() != new HashSet<>(colaboradoresIds).size()) {
				Set<Long> found = new HashSet<>(colaboradores.size());
				for (Colaborador c : colaboradores) {
					found.add(c.getId());
				}
				List<Long> missing = colaboradoresIds.stream().distinct().filter(id -> !found.contains(id)).toList();
				throw new NotFoundException("Colaboradores não encontrados: ids=" + missing);
			}
			ata.getColaboradores().addAll(colaboradores);
		}

		ata = ataRepository.save(ata);
		return ataMapper.toResponse(ata);
	}

	@Transactional
	public AtaResponse addColaborador(Long workshopId, Long ataId, AtaAddColaboradorRequest request) {
		if (!workshopRepository.existsById(workshopId)) {
			throw new NotFoundException("Workshop não encontrado: id=" + workshopId);
		}

		Ata ata = ataRepository.findWithWorkshopAndColaboradoresByIdAndWorkshop_Id(ataId, workshopId)
				.orElseThrow(() -> new NotFoundException("Ata não encontrada para o workshop: ataId=" + ataId + ", workshopId=" + workshopId));

		Colaborador colaborador = colaboradorRepository.findById(request.colaboradorId())
				.orElseThrow(() -> new NotFoundException("Colaborador não encontrado: id=" + request.colaboradorId()));

		boolean added = ata.getColaboradores().add(colaborador);
		if (!added) {
			throw new ConflictException("Colaborador já está presente na ata: ataId=" + ataId + ", colaboradorId=" + colaborador.getId());
		}

		ata = ataRepository.save(ata);
		return ataMapper.toResponse(ata);
	}

	@Transactional
	public void removeColaborador(Long ataId, Long colaboradorId) {
		Ata ata = ataRepository.findWithWorkshopAndColaboradoresById(ataId)
				.orElseThrow(() -> new NotFoundException("Ata não encontrada: id=" + ataId));

		Colaborador colaborador = colaboradorRepository.findById(colaboradorId)
				.orElseThrow(() -> new NotFoundException("Colaborador não encontrado: id=" + colaboradorId));

		boolean removed = ata.getColaboradores().removeIf(c -> Objects.equals(c.getId(), colaborador.getId()));
		if (!removed) {
			throw new NotFoundException("Colaborador não está presente na ata: ataId=" + ataId + ", colaboradorId=" + colaboradorId);
		}

		ataRepository.save(ata);
	}

	@Transactional(readOnly = true)
	public List<ColaboradorParticipacoesResponse> listarParticipacoes(String workshopNome, LocalDate dataRealizacao) {
		String nome = normalizeBlankToNull(workshopNome);

		List<ColaboradorWorkshopRow> rows = ataRepository.findParticipacoes(nome, dataRealizacao);

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
				throw new BadRequestException("colaboradoresIds não pode conter null");
			}
			if (!seen.add(id)) {
				throw new BadRequestException("colaboradoresIds contém ids duplicados");
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

