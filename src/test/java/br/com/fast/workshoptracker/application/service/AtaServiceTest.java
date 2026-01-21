package br.com.fast.workshoptracker.application.service;

import br.com.fast.workshoptracker.domain.entity.Ata;
import br.com.fast.workshoptracker.domain.entity.Colaborador;
import br.com.fast.workshoptracker.domain.entity.Workshop;
import br.com.fast.workshoptracker.domain.exception.codes.BusinessErrorCode;
import br.com.fast.workshoptracker.domain.exception.codes.ValidationErrorCode;
import br.com.fast.workshoptracker.domain.exception.domain.BusinessException;
import br.com.fast.workshoptracker.domain.exception.validation.ValidationException;
import br.com.fast.workshoptracker.api.dto.request.AtaAddColaboradorRequest;
import br.com.fast.workshoptracker.api.dto.request.AtaCreateRequest;
import br.com.fast.workshoptracker.api.mapper.AtaMapper;
import br.com.fast.workshoptracker.infrastructure.persistence.repository.AtaRepository;
import br.com.fast.workshoptracker.infrastructure.persistence.repository.ColaboradorRepository;
import br.com.fast.workshoptracker.infrastructure.persistence.repository.WorkshopRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AtaServiceTest {

	@Mock
	private AtaRepository ataRepository;
	@Mock
	private WorkshopRepository workshopRepository;
	@Mock
	private ColaboradorRepository colaboradorRepository;
	@Mock
	private AtaMapper ataMapper;

	@InjectMocks
	private AtaService ataService;

	@Test
	void create_quandoColaboradoresIdsDuplicados_deveRetornar400() {
		AtaCreateRequest req = new AtaCreateRequest(1L, List.of(1L, 1L));

		ValidationException ex = assertThrows(ValidationException.class, () -> ataService.create(req));
		assertEquals(ValidationErrorCode.VAL_002_BAD_REQUEST, ex.getErrorCode());

		verify(workshopRepository, never()).findById(anyLong());
		verify(ataRepository, never()).save(any(Ata.class));
	}

	@Test
	void create_quandoWorkshopInexistente_deveRetornar404() {
		when(workshopRepository.findById(1L)).thenReturn(Optional.empty());

		AtaCreateRequest req = new AtaCreateRequest(1L, List.of());

		BusinessException ex = assertThrows(BusinessException.class, () -> ataService.create(req));
		assertEquals(BusinessErrorCode.BUS_001_NOT_FOUND, ex.getErrorCode());
	}

	@Test
	void create_quandoAtaJaExisteParaWorkshop_deveRetornar409() {
		Workshop workshop = new Workshop("WS", LocalDate.of(2026, 1, 20));
		setId(workshop, 1L);

		when(workshopRepository.findById(1L)).thenReturn(Optional.of(workshop));
		when(ataRepository.existsByWorkshopId(1L)).thenReturn(true);

		AtaCreateRequest req = new AtaCreateRequest(1L, List.of());

		BusinessException ex = assertThrows(BusinessException.class, () -> ataService.create(req));
		assertEquals(BusinessErrorCode.BUS_002_CONFLICT, ex.getErrorCode());
	}

	@Test
	void addColaborador_quandoJaPresente_deveRetornar409() {
		Workshop workshop = new Workshop("WS", LocalDate.of(2026, 1, 20));
		setId(workshop, 1L);

		Colaborador colaborador = new Colaborador("Ana");
		setId(colaborador, 10L);

		Ata ata = new Ata(workshop);
		setId(ata, 2L);
		ata.getColaboradores().add(colaborador);

		when(workshopRepository.existsById(1L)).thenReturn(true);
		when(ataRepository.findByIdAndWorkshopId(2L, 1L)).thenReturn(Optional.of(ata));
		when(colaboradorRepository.findById(10L)).thenReturn(Optional.of(colaborador));

		BusinessException ex = assertThrows(BusinessException.class, () -> ataService.addColaborador(1L, 2L, new AtaAddColaboradorRequest(10L)));
		assertEquals(BusinessErrorCode.BUS_002_CONFLICT, ex.getErrorCode());
	}

	private static void setId(Object entity, Long id) {
		try {
			Field idField = entity.getClass().getDeclaredField("id");
			idField.setAccessible(true);
			idField.set(entity, id);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}

