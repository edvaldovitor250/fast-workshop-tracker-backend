package br.com.fast.workshoptracker.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthLoginRequest(
		@Schema(example = "ana@fast.com")
		@NotBlank
		@Email
		@Size(max = 180)
		String email,

		@Schema(example = "Senha@123")
		@NotBlank
		@Size(min = 6, max = 72)
		String senha
) {
}

