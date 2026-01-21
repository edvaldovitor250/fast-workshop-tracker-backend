package br.com.fast.workshoptracker.api.validation.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
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
@NotBlank(message = "email é obrigatório")
@Email(message = "email inválido")
@Size(max = 180, message = "email deve ter no máximo 180 caracteres")
@Schema(
		description = "E-mail do usuário",
		example = "ana@fast.com",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 180
)
public @interface EmailAddress {
}
