package br.com.fast.workshoptracker.domain.entity;

import java.util.LinkedHashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Table(
		name = "ata",
		uniqueConstraints = {
				@UniqueConstraint(name = "uk_ata_workshop", columnNames = "workshop_id")
		}
)
@NamedEntityGraph(
		name = "Ata.withWorkshopAndColaboradores",
		attributeNodes = {
				@NamedAttributeNode("workshop"),
				@NamedAttributeNode("colaboradores")
		}
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
@EqualsAndHashCode(of = "id")
public class Ata {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "workshop_id", nullable = false, unique = true)
	@NonNull
	private Workshop workshop;

	@ManyToMany
	@JoinTable(
			name = "ata_colaborador",
			joinColumns = @JoinColumn(name = "ata_id"),
			inverseJoinColumns = @JoinColumn(name = "colaborador_id"),
			uniqueConstraints = @UniqueConstraint(name = "uk_ata_colaborador", columnNames = {"ata_id", "colaborador_id"})
	)
	private Set<Colaborador> colaboradores = new LinkedHashSet<>();

}
