package br.com.fast.workshoptracker.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_FIELD_INJECTION;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_JODATIME;

@AnalyzeClasses(
		packages = "br.com.fast.workshoptracker",
		importOptions = ImportOption.DoNotIncludeTests.class
)
public class CodingRulesArchTest {

	@ArchTest
	static final ArchRule no_access_to_standard_streams = NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;

	@ArchTest
	static final ArchRule no_generic_exceptions = NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS;

	@ArchTest
	static final ArchRule no_jodatime = NO_CLASSES_SHOULD_USE_JODATIME;

	@ArchTest
	static final ArchRule no_java_util_logging = NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;

	@ArchTest
	static final ArchRule no_field_injection = NO_CLASSES_SHOULD_USE_FIELD_INJECTION;

	@ArchTest
	static final ArchRule usecases_should_be_annotated_with_service =
			classes()
					.that().resideInAPackage("..application.usecase..")
					.and().haveSimpleNameEndingWith("UseCaseImpl")
					.should().beAnnotatedWith(org.springframework.stereotype.Service.class)
					.because("UseCases s\u00e3o services do Spring e devem ser gerenciados pelo container");

	@ArchTest
	static final ArchRule controllers_should_be_annotated_with_rest_controller =
			classes()
					.that().resideInAPackage("..presentation.rest.controller..")
					.and().haveSimpleNameEndingWith("Controller")
					.should().beAnnotatedWith(org.springframework.web.bind.annotation.RestController.class)
					.because("Controllers REST devem ser anotados com @RestController");

	@ArchTest
	static final ArchRule jpa_repositories_should_extend_jpa_repository =
			classes()
					.that().resideInAPackage("..infrastructure.persistence.repository..")
					.and().haveSimpleNameEndingWith("Repository")
					.and().areInterfaces()
					.should().beAssignableTo(org.springframework.data.jpa.repository.JpaRepository.class)
					.because("Repositories JPA devem estender JpaRepository");

	@ArchTest
	static final ArchRule entities_should_be_in_domain_entity_package =
			classes()
					.that().areAnnotatedWith(jakarta.persistence.Entity.class)
					.should().resideInAPackage("..domain.entity..")
					.because("Entities JPA s\u00e3o modelos de dom\u00ednio");

	@ArchTest
	static final ArchRule dtos_should_not_be_in_domain =
			noClasses()
					.that().haveSimpleNameContaining("DTO")
					.or().haveSimpleNameContaining("Request")
					.or().haveSimpleNameContaining("Response")
					.should().resideInAPackage("..domain..")
					.because("Domain deve conter apenas entidades e value objects, n\u00e3o DTOs");

	@ArchTest
	static final ArchRule domain_should_not_depend_on_spring =
			noClasses()
					.that().resideInAPackage("..domain..")
					.and().resideOutsideOfPackage("..domain.exception..")
					.should().dependOnClassesThat().resideInAnyPackage("org.springframework..")
					.because("Domain deve ser independente de frameworks (exceto exceptions)");

	@ArchTest
	static final ArchRule no_reflection_in_production_code =
			noClasses()
					.that().resideInAPackage("br.com.fast.workshoptracker..")
					.and().resideOutsideOfPackage("..test..")
					.should().callMethod(Class.class, "forName", String.class)
					.orShould().callMethod(Class.class, "getDeclaredField", String.class)
					.orShould().callMethod(Class.class, "getDeclaredMethod", String.class, Class[].class)
					.because("Reflex\u00e3o deve ser evitada em c\u00f3digo de produ\u00e7\u00e3o");
}

