package org.zalando.stups.swagger.codegen;

import org.assertj.core.api.Assertions;

import org.junit.Test;

import com.wordnik.swagger.codegen.DefaultCodegen;

public class ConfigurableCodegenConfigTest {

    @Test
    public void testConfigurableCodegenConfig() {
        TestConfigurableCodegenConfig codegenConfig = new TestConfigurableCodegenConfig();
        codegenConfig.setApiPackage("org.zalando.stups.api");
        codegenConfig.setModelPackage("org.zalando.stups.model");

        codegenConfig.setOutputDir("/a/path/to/generatedOutput");

        //
        Assertions.assertThat(codegenConfig.apiPackage()).isEqualTo("org.zalando.stups.api");
        Assertions.assertThat(codegenConfig.modelPackage()).isEqualTo("org.zalando.stups.model");

        Assertions.assertThat(codegenConfig.getOutputDir()).isEqualTo("/a/path/to/generatedOutput");

        Assertions.assertThat(codegenConfig.apiFileFolder()).isEqualTo(
            "/a/path/to/generatedOutput/org/zalando/stups/api");
        Assertions.assertThat(codegenConfig.modelFileFolder()).isEqualTo(
            "/a/path/to/generatedOutput/org/zalando/stups/model");

    }

    /**
     * Simple {@link TestConfigurableCodegenConfig} for testing.
     *
     * @author  jbellmann
     */
    static class TestConfigurableCodegenConfig extends DefaultCodegen implements ConfigurableCodegenConfig {

        @Override
        public void setApiPackage(final String apiPackage) {
            this.apiPackage = apiPackage;
        }

        @Override
        public void setModelPackage(final String modelPackage) {
            this.modelPackage = modelPackage;
        }
    }
}
