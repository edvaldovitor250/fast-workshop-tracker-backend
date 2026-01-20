package br.com.fast.workshoptracker.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(
		name = "ata",
		uniqueConstraints = {
				@UniqueConstraint(name = "uk_ata_workshop", columnNames = "workshop_id")
		}
)
public class Ata {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "workshop_id", nullable = false, unique = true)
	private Workshop workshop;

	@ManyToMany
	@JoinTable(
			name = "ata_colaborador",
			joinColumns = @JoinColumn(name = "ata_id"),
			inverseJoinColumns = @JoinColumn(name = "colaborador_id"),
			uniqueConstraints = @UniqueConstraint(name = "uk_ata_colaborador", columnNames = {"ata_id", "colaborador_id"})
	)
	private Set<Colaborador> colaboradores = new LinkedHashSet<>();

	protected Ata() {
	}

	public Ata(Workshop workshop) {
		this.workshop = workshop;
	}

	public Long getId() {
		return id;
	}

	public Workshop getWorkshop() {
		return workshop;
	}

	public Set<Colaborador> getColaboradores() {
		return colaboradores;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Ata that = (Ata) o;
		return id != null && id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}

