package com.example.modulith;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

class ModulithStructureTest {

    ApplicationModules modules = ApplicationModules.of(ModulithApplication.class);

    @Test
    void verifyArchitecture() {
        // Automatically checks for cyclic dependencies and illegal package imports
        modules.verify();
    }

    @Test
    void writeDocumentation() {
        // Automatically creates Asciidoc/PlantUML diagrams of your system inside target/modulith-docs
        new Documenter(modules).writeModulesAsPlantUml();
    }
}
