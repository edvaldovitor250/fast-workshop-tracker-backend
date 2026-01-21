package br.com.fast.workshoptracker.api.validation.usuario;

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
@NotBlank(message = "nome é obrigatório")
@Size(min = 2, max = 120, message = "nome deve ter entre 2 e 120 caracteres")
@Schema(
		description = "Nome do usuário",
		example = "Ana Silva",
		requiredMode = Schema.RequiredMode.REQUIRED,
		minLength = 2,
		maxLength = 120
)
public @interface UsuarioNome {
}
