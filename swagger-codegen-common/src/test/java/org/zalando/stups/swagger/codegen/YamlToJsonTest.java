/**
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.zalando.stups.swagger.codegen;

import java.io.IOException;

import java.net.URISyntaxException;

import java.nio.file.Files;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.util.Yaml;

public class YamlToJsonTest {

    @Test
    public void transform() throws JsonProcessingException, IOException {
        String data = getResourceContent("/kio-api.yaml");
        ObjectMapper yamlMapper = Yaml.mapper();
        JsonNode rootNode = yamlMapper.readTree(data);

        // must have swagger node set
        JsonNode swaggerNode = rootNode.get("swagger");

        String rootNodeString = rootNode.toString();
        System.out.println(rootNodeString);
    }

    @Test
    public void transformWithConverter() throws URISyntaxException {
        YamlToJson converter = YamlToJson.builder()
                                         .withYamlInputPath(java.nio.file.Paths.get(
                                                 YamlToJsonTest.class.getResource("/kio-api.yaml").toURI())
                                                 .toAbsolutePath().toString())
                                         .withOutputDirectoryPath(System.getProperty("user.dir") + "/target").build();
        converter.convert();
    }

    protected static String getResourceContent(final String classpathResource) {
        try {
            return new String(Files.readAllBytes(
                        java.nio.file.Paths.get(YamlToJsonTest.class.getResource(classpathResource).toURI())));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
