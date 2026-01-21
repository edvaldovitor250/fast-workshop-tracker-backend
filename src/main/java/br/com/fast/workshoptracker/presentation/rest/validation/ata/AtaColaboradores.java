package br.com.fast.workshoptracker.presentation.rest.validation.ata;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.RECORD_COMPONENT})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@NotNull(message = "colaboradores é obrigatório")
@Schema(
		description = "Lista de colaboradores presentes na ata",
		requiredMode = Schema.RequiredMode.REQUIRED
)
public @interface AtaColaboradores {
}
