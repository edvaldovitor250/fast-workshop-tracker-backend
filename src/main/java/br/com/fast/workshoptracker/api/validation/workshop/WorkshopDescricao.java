package br.com.fast.workshoptracker.api.validation.workshop;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.RECORD_COMPONENT})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Size(max = 500, message = "descricao deve ter no máximo 500 caracteres")
@Schema(
		description = "Descrição do workshop",
		example = "Conteúdo do workshop...",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 500
)
public @interface WorkshopDescricao {
}
