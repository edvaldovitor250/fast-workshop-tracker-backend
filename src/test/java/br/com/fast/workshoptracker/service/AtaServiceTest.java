package br.com.fast.workshoptracker.service;

import br.com.fast.workshoptracker.domain.entity.Ata;
import br.com.fast.workshoptracker.domain.entity.Colaborador;
import br.com.fast.workshoptracker.domain.entity.Workshop;
import br.com.fast.workshoptracker.dto.request.AtaAddColaboradorRequest;
import br.com.fast.workshoptracker.dto.request.AtaCreateRequest;
import br.com.fast.workshoptracker.exception.BadRequestException;
import br.com.fast.workshoptracker.exception.ConflictException;
import br.com.fast.workshoptracker.exception.NotFoundException;
import br.com.fast.workshoptracker.mapper.AtaMapper;
import br.com.fast.workshoptracker.repository.AtaRepository;
import br.com.fast.workshoptracker.repository.ColaboradorRepository;
import br.com.fast.workshoptracker.repository.WorkshopRepository;
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

		assertThrows(BadRequestException.class, () -> ataService.create(req));

		verify(workshopRepository, never()).findById(anyLong());
		verify(ataRepository, never()).save(any(Ata.class));
	}

	@Test
	void create_quandoWorkshopInexistente_deveRetornar404() {
		when(workshopRepository.findById(1L)).thenReturn(Optional.empty());

		AtaCreateRequest req = new AtaCreateRequest(1L, List.of());

		assertThrows(NotFoundException.class, () -> ataService.create(req));
	}

	@Test
	void create_quandoAtaJaExisteParaWorkshop_deveRetornar409() {
		Workshop workshop = new Workshop("WS", LocalDate.of(2026, 1, 20), null);
		setId(workshop, 1L);

		when(workshopRepository.findById(1L)).thenReturn(Optional.of(workshop));
		when(ataRepository.existsByWorkshop_Id(1L)).thenReturn(true);

		AtaCreateRequest req = new AtaCreateRequest(1L, List.of());

		assertThrows(ConflictException.class, () -> ataService.create(req));
	}

	@Test
	void addColaborador_quandoJaPresente_deveRetornar409() {
		Workshop workshop = new Workshop("WS", LocalDate.of(2026, 1, 20), null);
		setId(workshop, 1L);

		Colaborador colaborador = new Colaborador("Ana");
		setId(colaborador, 10L);

		Ata ata = new Ata(workshop);
		setId(ata, 2L);
		ata.getColaboradores().add(colaborador);

		when(workshopRepository.existsById(1L)).thenReturn(true);
		when(ataRepository.findWithWorkshopAndColaboradoresByIdAndWorkshop_Id(2L, 1L)).thenReturn(Optional.of(ata));
		when(colaboradorRepository.findById(10L)).thenReturn(Optional.of(colaborador));

		assertThrows(ConflictException.class, () -> ataService.addColaborador(1L, 2L, new AtaAddColaboradorRequest(10L)));
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

