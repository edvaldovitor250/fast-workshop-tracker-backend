package br.com.fast.workshoptracker.presentation.rest.validation.colaborador;

import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.RECORD_COMPONENT})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Schema(
		description = "IDs de colaboradores presentes (pode ser vazio ou ausente)",
		example = "[1,2,3]",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
)
public @interface ColaboradoresIds {
}
