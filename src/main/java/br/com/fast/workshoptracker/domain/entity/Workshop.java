package br.com.fast.workshoptracker.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(
		name = "workshop",
		indexes = {
				@Index(name = "idx_workshop_nome", columnList = "nome"),
				@Index(name = "idx_workshop_data_realizacao", columnList = "data_realizacao")
		}
)
public class Workshop {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 150)
	private String nome;

	@Column(name = "data_realizacao", nullable = false)
	private LocalDate dataRealizacao;

	@Column(length = 500)
	private String descricao;

	protected Workshop() {
	}

	public Workshop(String nome, LocalDate dataRealizacao, String descricao) {
		this.nome = nome;
		this.dataRealizacao = dataRealizacao;
		this.descricao = descricao;
	}

	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public LocalDate getDataRealizacao() {
		return dataRealizacao;
	}

	public void setDataRealizacao(LocalDate dataRealizacao) {
		this.dataRealizacao = dataRealizacao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Workshop that = (Workshop) o;
		return id != null && id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}

