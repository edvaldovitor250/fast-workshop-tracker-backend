package br.com.fast.workshoptracker.service;

import br.com.fast.workshoptracker.config.JwtProperties;
import br.com.fast.workshoptracker.domain.entity.UserRole;
import br.com.fast.workshoptracker.domain.entity.Usuario;
import br.com.fast.workshoptracker.dto.request.AuthLoginRequest;
import br.com.fast.workshoptracker.dto.request.AuthRegisterRequest;
import br.com.fast.workshoptracker.dto.response.AuthTokenResponse;
import br.com.fast.workshoptracker.dto.response.UsuarioResponse;
import br.com.fast.workshoptracker.exception.UnauthorizedException;
import br.com.fast.workshoptracker.exception.ConflictException;
import br.com.fast.workshoptracker.mapper.UsuarioMapper;
import br.com.fast.workshoptracker.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final PasswordEncoder passwordEncoder;
	private final JwtEncoder jwtEncoder;
	private final JwtProperties jwtProperties;
	private final UsuarioRepository usuarioRepository;
	private final UsuarioMapper usuarioMapper;

	public UsuarioResponse register(AuthRegisterRequest request) {
		String email = normalizeEmail(request.email());
		if (usuarioRepository.existsByEmailIgnoreCase(email)) {
			throw new ConflictException("E-mail já cadastrado");
		}

		String senhaHash = passwordEncoder.encode(request.senha());
		Usuario usuario = new Usuario(request.nome(), email, senhaHash);
		usuario.getRoles().addAll(Set.of(UserRole.CREATOR, UserRole.READER));
		usuario = usuarioRepository.save(usuario);
		return usuarioMapper.toResponse(usuario);
	}

	public AuthTokenResponse login(AuthLoginRequest request) {
		String email = normalizeEmail(request.email());
		Usuario usuario = usuarioRepository.findByEmailIgnoreCase(email)
				.orElseThrow(() -> new UnauthorizedException("Credenciais inválidas"));

		if (!passwordEncoder.matches(request.senha(), usuario.getSenhaHash())) {
			throw new UnauthorizedException("Credenciais inválidas");
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
		return email.trim().toLowerCase();
	}
}
