package br.com.fast.workshoptracker.presentation.rest.validation.workshop;

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
@Size(min = 2, max = 150, message = "nome deve ter entre 2 e 150 caracteres")
@Schema(
		description = "Nome do workshop",
		example = "Workshop Spring",
		requiredMode = Schema.RequiredMode.REQUIRED,
		minLength = 2,
		maxLength = 150
)
public @interface WorkshopNome {
}
