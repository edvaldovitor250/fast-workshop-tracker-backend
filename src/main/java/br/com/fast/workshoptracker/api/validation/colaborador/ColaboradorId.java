package br.com.fast.workshoptracker.api.validation.colaborador;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.RECORD_COMPONENT, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@NotNull(message = "colaboradorId é obrigatório")
@Positive(message = "colaboradorId deve ser um número positivo")
@Schema(
		description = "ID do colaborador",
		example = "10",
		requiredMode = Schema.RequiredMode.REQUIRED,
		minimum = "1"
)
public @interface ColaboradorId {
}
