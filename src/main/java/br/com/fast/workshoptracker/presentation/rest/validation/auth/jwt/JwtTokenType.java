package br.com.fast.workshoptracker.presentation.rest.validation.auth.jwt;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.RECORD_COMPONENT})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@NotBlank(message = "tokenType é obrigatório")
@Schema(
		description = "Tipo do token",
		example = "Bearer",
		requiredMode = Schema.RequiredMode.REQUIRED
)
public @interface JwtTokenType {
}
