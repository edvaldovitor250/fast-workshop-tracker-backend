package br.com.fast.workshoptracker.presentation.rest.validation.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.RECORD_COMPONENT})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@NotBlank(message = "senha é obrigatória")
@Size(min = 6, max = 72, message = "senha deve ter entre 6 e 72 caracteres")
@Schema(
		description = "Senha do usuário",
		example = "Senha@123",
		requiredMode = Schema.RequiredMode.REQUIRED,
		minLength = 6,
		maxLength = 72
)
public @interface Senha {
}
