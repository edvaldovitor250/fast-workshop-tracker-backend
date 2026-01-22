package br.com.fast.workshoptracker.presentation.rest.mapper;

import br.com.fast.workshoptracker.application.dto.command.AutenticarUsuarioCommand;
import br.com.fast.workshoptracker.application.dto.command.RegistrarUsuarioCommand;
import br.com.fast.workshoptracker.application.dto.query.AuthTokenDTO;
import br.com.fast.workshoptracker.application.dto.query.UsuarioDTO;
import br.com.fast.workshoptracker.presentation.rest.dto.request.AuthLoginRequest;
import br.com.fast.workshoptracker.presentation.rest.dto.request.AuthRegisterRequest;
import br.com.fast.workshoptracker.presentation.rest.dto.response.AuthTokenResponse;
import br.com.fast.workshoptracker.presentation.rest.dto.response.UsuarioResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {

	RegistrarUsuarioCommand toCommand(AuthRegisterRequest request);

	AutenticarUsuarioCommand toCommand(AuthLoginRequest request);

	UsuarioResponse toResponse(UsuarioDTO dto);

	AuthTokenResponse toResponse(AuthTokenDTO dto);
}
