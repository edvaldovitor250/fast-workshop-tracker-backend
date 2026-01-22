package br.com.fast.workshoptracker.testconfig;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import br.com.fast.workshoptracker.application.dto.command.AutenticarUsuarioCommand;
import br.com.fast.workshoptracker.application.dto.command.RegistrarUsuarioCommand;
import br.com.fast.workshoptracker.application.dto.query.AuthTokenDTO;
import br.com.fast.workshoptracker.application.dto.query.UsuarioDTO;
import br.com.fast.workshoptracker.presentation.rest.dto.request.AuthLoginRequest;
import br.com.fast.workshoptracker.presentation.rest.dto.request.AuthRegisterRequest;
import br.com.fast.workshoptracker.presentation.rest.dto.response.AuthTokenResponse;
import br.com.fast.workshoptracker.presentation.rest.dto.response.UsuarioResponse;
import br.com.fast.workshoptracker.presentation.rest.mapper.AuthMapper;

@Component
@Primary
@Profile("test")
public class AuthMapperTestImpl implements AuthMapper {

	@Override
	public RegistrarUsuarioCommand toCommand(AuthRegisterRequest request) {
		if (request == null) {
			return null;
		}
		return new RegistrarUsuarioCommand(request.nome(), request.email(), request.senha());
	}

	@Override
	public AutenticarUsuarioCommand toCommand(AuthLoginRequest request) {
		if (request == null) {
			return null;
		}
		return new AutenticarUsuarioCommand(request.email(), request.senha());
	}

	@Override
	public UsuarioResponse toResponse(UsuarioDTO dto) {
		if (dto == null) {
			return null;
		}
		return new UsuarioResponse(dto.id(), dto.nome(), dto.email(), dto.roles());
	}

	@Override
	public AuthTokenResponse toResponse(AuthTokenDTO dto) {
		if (dto == null) {
			return null;
		}
		return new AuthTokenResponse(dto.tokenType(), dto.accessToken(), dto.expiresAt(), dto.roles());
	}
}
