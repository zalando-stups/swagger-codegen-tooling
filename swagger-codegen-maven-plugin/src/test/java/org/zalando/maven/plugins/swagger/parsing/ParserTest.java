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
package org.zalando.maven.plugins.swagger.parsing;

import java.io.IOException;

import org.assertj.core.api.Assertions;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.models.Swagger;

import io.swagger.util.Yaml;

/**
 * @author  jbellmann
 */
public class ParserTest {

    private Swagger swagger;

    /**
     * Same code as in the parser itself to find out what is wrong with the swagger.yaml file.
     */
    @Test
    public void yamlParse() throws JsonProcessingException, IOException {
        JsonNode rootNode = null;
        ObjectMapper objectMapper = Yaml.mapper();
        rootNode = objectMapper.readTree(getClass().getResourceAsStream("/swagger.yaml"));

        JsonNode swaggerNode = rootNode.get("swagger");
        if (swaggerNode == null) {

            throw new NullPointerException("You failed!");
        }

        swagger = objectMapper.convertValue(rootNode, Swagger.class);
        Assertions.assertThat(swagger).isNotNull();
    }
}
