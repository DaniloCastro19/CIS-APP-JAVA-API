package com.jala.university.api.infrastructure.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

public class ArchitectureTests {
    private static JavaClasses importedClasses;
    private static final String BASE_PACKAGE = "com.jala.university";

    @BeforeAll
    public static void setup() {
        importedClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(BASE_PACKAGE);
    }

    @Test
    void testPackageCyclicDependencies() {
        ArchRule noCyclicDependencies = SlicesRuleDefinition.slices()
                .matching(BASE_PACKAGE + ".(*)..")
                .should().beFreeOfCycles();

        noCyclicDependencies.check(importedClasses);
    }

    @Test
    void testControllerNamingConvention() {
        ArchRule controllerNaming = classes()
                .that().resideInAPackage(BASE_PACKAGE + ".api.controllers..")
                .should().haveSimpleNameEndingWith("Controller");

        controllerNaming.check(importedClasses);
    }

    @Test
    void testRepositoryNamingConvention() {
        ArchRule repositoryNaming = classes()
                .that().resideInAPackage(BASE_PACKAGE + ".data.repositories..")
                .should().haveSimpleNameEndingWith("Repository")
                .orShould().haveSimpleNameEndingWith("RepositoryImpl");

        repositoryNaming.check(importedClasses);
    }

    @Test
    void testRepositoryClassRules() {
        ArchRule repositoryRules = classes()
                .that().resideInAPackage(BASE_PACKAGE + ".data.repositories..")
                .and().haveSimpleNameEndingWith("RepositoryImpl")
                .should().beAnnotatedWith("org.springframework.stereotype.Repository")
                .andShould().bePublic();

        repositoryRules.check(importedClasses);
    }

    @Test
    void testExceptionNaming() {
        ArchRule exceptionNaming = classes()
                .that().resideInAPackage(BASE_PACKAGE + ".core.exception..")
                .should().haveSimpleNameEndingWith("Exception");

        exceptionNaming.allowEmptyShould(true).check(importedClasses);
    }

    @Test
    void testExceptionHandling() {
        ArchRule exceptionRule = classes()
                .that().areAssignableTo(Exception.class)
                .and().resideInAPackage(BASE_PACKAGE + ".core.exception..")
                .should().haveSimpleNameEndingWith("Exception")
                .andShould().bePublic();

        exceptionRule.check(importedClasses);
    }
}