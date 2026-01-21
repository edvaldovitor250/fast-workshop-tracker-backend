package br.com.fast.workshoptracker.infrastructure.security.adapter;

import br.com.fast.workshoptracker.application.dto.query.AuthTokenDTO;
import br.com.fast.workshoptracker.application.port.output.TokenGeneratorPort;
import br.com.fast.workshoptracker.infrastructure.config.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenGeneratorAdapter implements TokenGeneratorPort {

	private final JwtEncoder jwtEncoder;
	private final JwtProperties jwtProperties;

	@Override
	public AuthTokenDTO generate(String subject, List<String> roles) {
		Instant now = Instant.now();
		Instant expiresAt = now.plus(jwtProperties.getTtl());

		JwtClaimsSet claims = JwtClaimsSet.builder()
				.issuer(jwtProperties.getIssuer())
				.subject(subject)
				.issuedAt(now)
				.expiresAt(expiresAt)
				.claim("roles", roles)
				.build();

		JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
		String token = jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();

		return new AuthTokenDTO("Bearer", token, expiresAt, roles);
	}
}

