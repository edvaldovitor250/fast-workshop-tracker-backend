package br.com.fast.workshoptracker.application.service;

import br.com.fast.workshoptracker.api.dto.request.AuthLoginRequest;
import br.com.fast.workshoptracker.api.dto.request.AuthRegisterRequest;
import br.com.fast.workshoptracker.api.dto.response.AuthTokenResponse;
import br.com.fast.workshoptracker.api.dto.response.UsuarioResponse;
import br.com.fast.workshoptracker.api.mapper.UsuarioMapper;
import br.com.fast.workshoptracker.domain.entity.Usuario;
import br.com.fast.workshoptracker.domain.enums.UserRole;
import br.com.fast.workshoptracker.domain.exception.Exceptions;
import br.com.fast.workshoptracker.domain.exception.util.ExceptionUtils;
import br.com.fast.workshoptracker.infrastructure.config.JwtProperties;
import br.com.fast.workshoptracker.infrastructure.persistence.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final PasswordEncoder passwordEncoder;
	private final JwtEncoder jwtEncoder;
	private final JwtProperties jwtProperties;
	private final UsuarioRepository usuarioRepository;
	private final UsuarioMapper usuarioMapper;

	@Transactional
	public UsuarioResponse register(AuthRegisterRequest request) {
		String email = normalizeEmail(request.email());
		if (usuarioRepository.existsByEmailIgnoreCase(email)) {
			throw Exceptions.conflict(
					"E-mail já cadastrado",
					ExceptionUtils.context("email", email)
			);
		}

		String senhaHash = passwordEncoder.encode(request.senha());
		Usuario usuario = new Usuario(request.nome(), email, senhaHash);
		usuario.getRoles().addAll(Set.of(UserRole.CREATOR, UserRole.READER));
		usuario = usuarioRepository.save(usuario);
		return usuarioMapper.toResponse(usuario);
	}

	@Transactional(readOnly = true)
	public AuthTokenResponse login(AuthLoginRequest request) {
		String email = normalizeEmail(request.email());
		Usuario usuario = usuarioRepository.findByEmailIgnoreCase(email)
				.orElseThrow(() -> Exceptions.invalidCredentials(email));

		if (!passwordEncoder.matches(request.senha(), usuario.getSenhaHash())) {
			throw Exceptions.invalidCredentials(email);
		}

		List<String> roles = usuario.getRoles().stream().map(Enum::name).sorted().toList();

		Instant now = Instant.now();
		Instant expiresAt = now.plus(jwtProperties.getTtl());

		JwtClaimsSet claims = JwtClaimsSet.builder()
				.issuer(jwtProperties.getIssuer())
				.subject(usuario.getEmail())
				.issuedAt(now)
				.expiresAt(expiresAt)
				.claim("roles", roles)
				.build();

		JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
		String token = jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();

		return new AuthTokenResponse("Bearer", token, expiresAt, roles);
	}

	private static String normalizeEmail(String email) {
		if (email == null) {
			return null;
		}
		return email.trim().toLowerCase(Locale.ROOT);
	}
}
