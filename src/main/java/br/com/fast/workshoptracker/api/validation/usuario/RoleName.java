package br.com.fast.workshoptracker.api.validation.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.RECORD_COMPONENT, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@NotBlank(message = "role não pode ser vazio")
@Schema(
		description = "Role do usuário",
		example = "READER",
		requiredMode = Schema.RequiredMode.REQUIRED
)
public @interface RoleName {
}
