package br.com.fast.workshoptracker.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.onionArchitecture;

@AnalyzeClasses(
		packages = "br.com.fast.workshoptracker",
		importOptions = ImportOption.DoNotIncludeTests.class
)
public class HexagonalArchitectureArchTest {

	@ArchTest
	static final ArchRule hexagonal_architecture_should_be_respected =
			onionArchitecture()
					.domainModels("..domain.entity..", "..domain.enums..")
					.domainServices("..domain.exception..")
					.applicationServices("..application..")
					.adapter("presentation", "..presentation..")
					.adapter("infrastructure", "..infrastructure..")
					.because("A arquitetura em camadas (onion/hexagonal) mant\u00e9m o dom\u00ednio isolado");

	@ArchTest
	static final ArchRule ports_should_be_public_interfaces =
			classes()
					.that().resideInAPackage("..application.port..")
					.should().beInterfaces()
					.andShould().bePublic()
					.because("Ports s\u00e3o contratos entre a aplica\u00e7\u00e3o e o mundo externo");

	@ArchTest
	static final ArchRule domain_should_not_depend_on_outer_layers =
			noClasses()
					.that().resideInAPackage("..domain..")
					.should().dependOnClassesThat().resideInAnyPackage(
							"..application..",
							"..presentation..",
							"..infrastructure.."
					)
					.because("Domain \u00e9 o n\u00facleo da aplica\u00e7\u00e3o e deve ser independente");

	@ArchTest
	static final ArchRule application_should_not_depend_on_adapters =
			noClasses()
					.that().resideInAPackage("..application..")
					.should().dependOnClassesThat().resideInAnyPackage(
							"..presentation..",
							"..infrastructure.."
					)
					.because("Application deve depender apenas de abstra\u00e7\u00f5es (ports), n\u00e3o de adapters");

	@ArchTest
	static final ArchRule presentation_should_not_depend_on_infrastructure =
			noClasses()
					.that().resideInAPackage("..presentation..")
					.should().dependOnClassesThat().resideInAPackage("..infrastructure..")
					.because("Adapters devem ser independentes entre si");

	@ArchTest
	static final ArchRule usecases_should_follow_naming_convention =
			classes()
					.that().resideInAPackage("..application.usecase..")
					.and().haveSimpleNameEndingWith("UseCaseImpl")
					.should().bePublic()
					.because("Conven\u00e7\u00e3o: implementa\u00e7\u00f5es de UseCase terminam com 'UseCaseImpl'");

	@ArchTest
	static final ArchRule controllers_should_follow_naming_convention =
			classes()
					.that().resideInAPackage("..presentation.rest.controller..")
					.and().areAnnotatedWith(org.springframework.web.bind.annotation.RestController.class)
					.should().haveSimpleNameEndingWith("Controller")
					.andShould().bePublic()
					.because("Conven\u00e7\u00e3o REST: Controllers devem terminar com 'Controller'");

	@ArchTest
	static final ArchRule repositories_should_be_in_infrastructure =
			classes()
					.that().areAssignableTo(org.springframework.data.jpa.repository.JpaRepository.class)
					.should().resideInAPackage("..infrastructure.persistence.repository..")
					.because("Repositories s\u00e3o detalhes de infraestrutura");

	@ArchTest
	static final ArchRule no_cycles_in_packages =
			SlicesRuleDefinition.slices()
					.matching("br.com.fast.workshoptracker.(*)..")
					.should().beFreeOfCycles()
					.because("Depend\u00eancias c\u00edclicas tornam o c\u00f3digo dif\u00edcil de manter");
}

