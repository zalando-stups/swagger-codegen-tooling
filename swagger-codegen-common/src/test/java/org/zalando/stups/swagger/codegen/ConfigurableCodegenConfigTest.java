/**
 * Copyright (C) 2015 Zalando SE (http://tech.zalando.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.zalando.stups.swagger.codegen;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import io.swagger.codegen.DefaultCodegen;

/**
 * @author jbellmann
 */
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

        Assertions.assertThat(codegenConfig.apiFileFolder())
                .isEqualTo("/a/path/to/generatedOutput/org/zalando/stups/api");
        Assertions.assertThat(codegenConfig.modelFileFolder())
                .isEqualTo("/a/path/to/generatedOutput/org/zalando/stups/model");

    }

    /**
     * Simple {@link TestConfigurableCodegenConfig} for testing.
     *
     * @author jbellmann
     */
    static class TestConfigurableCodegenConfig extends DefaultCodegen implements ConfigurableCodegenConfig {

        private boolean skipApiGeneration = false;

        private boolean skipModelGeneration = false;

        @Override
        public void skipApiGeneration() {
            skipApiGeneration = true;
        }

        @Override
        public void skipModelGeneration() {
            skipModelGeneration = true;
        }

        @Override
        public void setApiPackage(final String apiPackage) {
            this.apiPackage = apiPackage;
        }

        @Override
        public void setModelPackage(final String modelPackage) {
            this.modelPackage = modelPackage;
        }

        @Override
        public boolean is303Supported() {
            return false;
        }

        @Override
        public void enable303() {
        }

        @Override
        public boolean isBuilderSupported() {

            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public void enableBuilderSupport() {
            // TODO Auto-generated method stub

        }

    }
}
