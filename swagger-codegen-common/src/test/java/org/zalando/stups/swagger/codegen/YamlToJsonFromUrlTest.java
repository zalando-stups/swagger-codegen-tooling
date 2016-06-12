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

import java.io.IOException;

import org.assertj.core.api.Assertions;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class YamlToJsonFromUrlTest {

    @Test
    public void testWithUrl() {
        YamlToJson res = YamlToJson.builder().withYamlInputPath(
                "https://raw.githubusercontent.com/zalando/nakadi/nakadi-jvm/api/nakadi-event-bus-api.yaml").build();
        String yamlFilename = res.getYamlFilename();
        Assertions.assertThat(yamlFilename).isNotNull();
    }

    @Test
    public void testWithUrlAndReadContent() throws IOException {
        YamlToJson res = YamlToJson.builder()
                .withYamlInputPath(
                        "https://raw.githubusercontent.com/zalando/nakadi/nakadi-jvm/api/nakadi-event-bus-api.yaml")
                .build();
        String yamlFilename = res.getYamlFilename();
        Assertions.assertThat(yamlFilename).isNotNull();
        String json = res.getYamlFileContentAsJson();
        Assertions.assertThat(json).isNotNull();
        Assertions.assertThat(json).isNotEmpty();
        Assertions.assertThat(json).startsWith("{").endsWith("}");
    }
}
