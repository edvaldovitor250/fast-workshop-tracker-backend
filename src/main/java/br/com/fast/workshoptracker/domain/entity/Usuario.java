package br.com.fast.workshoptracker.domain.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Entity;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(
		name = "usuario",
		uniqueConstraints = @UniqueConstraint(name = "uk_usuario_email", columnNames = "email"),
		indexes = @Index(name = "idx_usuario_email", columnList = "email")
)
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 120)
	private String nome;

	@Column(nullable = false, length = 180)
	private String email;

	@Column(name = "senha_hash", nullable = false, length = 255)
	private String senhaHash;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "usuario_role", joinColumns = @JoinColumn(name = "usuario_id"))
	@Column(name = "role", nullable = false, length = 30)
	@Enumerated(EnumType.STRING)
	private Set<UserRole> roles = new LinkedHashSet<>();

	protected Usuario() {
	}

	public Usuario(String nome, String email, String senhaHash, Set<UserRole> roles) {
		this.nome = nome;
		this.email = email;
		this.senhaHash = senhaHash;
		this.roles = roles == null ? new LinkedHashSet<>() : new LinkedHashSet<>(roles);
	}

	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public String getEmail() {
		return email;
	}

	public String getSenhaHash() {
		return senhaHash;
	}

	public Set<UserRole> getRoles() {
		return roles;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Usuario that = (Usuario) o;
		return id != null && id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}

