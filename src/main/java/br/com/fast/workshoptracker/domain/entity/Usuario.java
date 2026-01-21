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
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(
		name = "usuario",
		uniqueConstraints = @UniqueConstraint(name = "uk_usuario_email", columnNames = "email"),
		indexes = @Index(name = "idx_usuario_email", columnList = "email")
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 120)
	@NonNull
	private String nome;

	@Column(nullable = false, length = 180)
	@NonNull
	private String email;

	@Column(name = "senha_hash", nullable = false, length = 255)
	@NonNull
	private String senhaHash;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "usuario_role", joinColumns = @JoinColumn(name = "usuario_id"))
	@Column(name = "role", nullable = false, length = 30)
	@Enumerated(EnumType.STRING)
	private Set<UserRole> roles = new LinkedHashSet<>();

	@EqualsAndHashCode.Include
	private Long idForEquality() {
		return Objects.requireNonNull(id, "id must not be null for equality");
	}

}
