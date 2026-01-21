package br.com.fast.workshoptracker.domain.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(
		name = "workshop",
		indexes = {
				@Index(name = "idx_workshop_nome", columnList = "nome"),
				@Index(name = "idx_workshop_data_realizacao", columnList = "data_realizacao")
		}
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
@EqualsAndHashCode(of = "id")
public class Workshop {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 150)
	@Setter
	@NonNull
	private String nome;

	@Column(name = "data_realizacao", nullable = false)
	@Setter
	@NonNull
	private LocalDate dataRealizacao;

	@Column(length = 500)
	@Setter
	private String descricao;

}
