package br.com.fast.workshoptracker.application.usecase.ata;

import br.com.fast.workshoptracker.application.dto.command.CriarAtaCommand;
import br.com.fast.workshoptracker.application.dto.query.AtaDTO;
import br.com.fast.workshoptracker.application.dto.query.ColaboradorDTO;
import br.com.fast.workshoptracker.application.dto.query.WorkshopDTO;
import br.com.fast.workshoptracker.application.mapper.AtaApplicationMapper;
import br.com.fast.workshoptracker.application.port.output.AtaRepositoryPort;
import br.com.fast.workshoptracker.application.port.output.ColaboradorRepositoryPort;
import br.com.fast.workshoptracker.application.port.output.WorkshopRepositoryPort;
import br.com.fast.workshoptracker.domain.entity.Ata;
import br.com.fast.workshoptracker.domain.entity.Colaborador;
import br.com.fast.workshoptracker.domain.entity.Workshop;
import br.com.fast.workshoptracker.domain.exception.codes.BusinessErrorCode;
import br.com.fast.workshoptracker.domain.exception.codes.ValidationErrorCode;
import br.com.fast.workshoptracker.domain.exception.domain.BusinessException;
import br.com.fast.workshoptracker.domain.exception.validation.ValidationException;
import br.com.fast.workshoptracker.infrastructure.observability.CustomMetrics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CriarAtaUseCaseImplTest {

	@Mock
	private AtaRepositoryPort ataRepository;
	@Mock
	private WorkshopRepositoryPort workshopRepository;
	@Mock
	private ColaboradorRepositoryPort colaboradorRepository;
	@Mock
	private CustomMetrics customMetrics;

	private CriarAtaUseCaseImpl useCase;

	@BeforeEach
	void setUp() {
		when(customMetrics.timeBusinessOperation(any(), any())).thenAnswer(inv -> {
			@SuppressWarnings("unchecked")
			Supplier<Object> supplier = (Supplier<Object>) inv.getArgument(1);
			return supplier.get();
		});

		AtaApplicationMapper ataMapper = createTestMapper();
		useCase = new CriarAtaUseCaseImpl(ataRepository, workshopRepository, colaboradorRepository, ataMapper, customMetrics);
	}

	@Test
	void execute_quandoColaboradoresIdsDuplicados_deveRetornar400() {
		var cmd = new CriarAtaCommand(1L, List.of(1L, 1L));

		ValidationException ex = assertThrows(ValidationException.class, () -> useCase.execute(cmd));
		assertEquals(ValidationErrorCode.VAL_002_BAD_REQUEST, ex.getErrorCode());

		verify(workshopRepository, never()).findById(anyLong());
		verify(ataRepository, never()).save(any(Ata.class));
	}

	@Test
	void execute_quandoWorkshopInexistente_deveRetornar404() {
		when(workshopRepository.findById(1L)).thenReturn(Optional.empty());

		var cmd = new CriarAtaCommand(1L, List.of());

		BusinessException ex = assertThrows(BusinessException.class, () -> useCase.execute(cmd));
		assertEquals(BusinessErrorCode.BUS_001_NOT_FOUND, ex.getErrorCode());
	}

	@Test
	void execute_quandoAtaJaExisteParaWorkshop_deveRetornar409() {
		Workshop workshop = new Workshop("WS", LocalDate.of(2026, 1, 20));
		setId(workshop, 1L);

		when(workshopRepository.findById(1L)).thenReturn(Optional.of(workshop));
		when(ataRepository.existsByWorkshopId(1L)).thenReturn(true);

		var cmd = new CriarAtaCommand(1L, List.of());

		BusinessException ex = assertThrows(BusinessException.class, () -> useCase.execute(cmd));
		assertEquals(BusinessErrorCode.BUS_002_CONFLICT, ex.getErrorCode());
		verify(ataRepository, never()).save(any(Ata.class));
	}

	@Test
	void execute_quandoColaboradoresInexistentes_deveRetornar404() {
		Workshop workshop = new Workshop("WS", LocalDate.of(2026, 1, 20));
		setId(workshop, 1L);

		when(workshopRepository.findById(1L)).thenReturn(Optional.of(workshop));
		when(ataRepository.existsByWorkshopId(1L)).thenReturn(false);
		when(colaboradorRepository.findAllById(List.of(10L, 11L))).thenReturn(List.of(new Colaborador("Ana")));

		var cmd = new CriarAtaCommand(1L, List.of(10L, 11L));

		BusinessException ex = assertThrows(BusinessException.class, () -> useCase.execute(cmd));
		assertEquals(BusinessErrorCode.BUS_001_NOT_FOUND, ex.getErrorCode());
		verify(ataRepository, never()).save(any(Ata.class));
	}

	@Test
	void execute_quandoValido_deveCriarAta() {
		Workshop workshop = new Workshop("WS", LocalDate.of(2026, 1, 20));
		setId(workshop, 1L);

		Colaborador colaborador = new Colaborador("Ana");
		setId(colaborador, 10L);

		when(workshopRepository.findById(1L)).thenReturn(Optional.of(workshop));
		when(ataRepository.existsByWorkshopId(1L)).thenReturn(false);
		when(colaboradorRepository.findAllById(List.of(10L))).thenReturn(List.of(colaborador));
		when(ataRepository.save(any(Ata.class))).thenAnswer(inv -> {
			Ata ata = inv.getArgument(0, Ata.class);
			setId(ata, 2L);
			return ata;
		});

		var dto = useCase.execute(new CriarAtaCommand(1L, List.of(10L)));
		assertNotNull(dto);
		assertEquals(2L, dto.id());
		assertEquals(1L, dto.workshop().id());
		assertEquals(1, dto.colaboradores().size());
		assertEquals(10L, dto.colaboradores().getFirst().id());
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

	private static AtaApplicationMapper createTestMapper() {
		return new AtaApplicationMapper() {
			@Override
			public AtaDTO toDto(Ata entity) {
				if (entity == null) {
					return null;
				}
				return new AtaDTO(
						entity.getId(),
						toDto(entity.getWorkshop()),
						toColaboradorDtoList(entity.getColaboradores())
				);
			}

			@Override
			public WorkshopDTO toDto(Workshop entity) {
				if (entity == null) {
					return null;
				}
				return new WorkshopDTO(
						entity.getId(),
						entity.getNome(),
						entity.getDataRealizacao(),
						entity.getDescricao()
				);
			}

			@Override
			public ColaboradorDTO toDto(Colaborador entity) {
				if (entity == null) {
					return null;
				}
				return new ColaboradorDTO(entity.getId(), entity.getNome());
			}
		};
	}
}
