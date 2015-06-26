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
package com.unknownpkg;

import java.util.List;

import org.assertj.core.api.Assertions;

import org.junit.Test;

import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;

import org.springframework.core.ParameterizedTypeReference;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.springframework.web.client.RestTemplate;

import org.zalando.stups.swagger.codegen.SwaggerResource;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ExampleApplication.class)
@WebIntegrationTest(randomPort = true)
public class StupsSwaggerCodegenIT {

    @Value("${local.server.port}")
    int port;

    private final ParameterizedTypeReference<List<SwaggerResource>> swaggerResourceParameterizedType =
        new ParameterizedTypeReference<List<SwaggerResource>>() { };

    @Test
    public void getResources() {
        RestTemplate rest = new RestTemplate();
        ResponseEntity<List<SwaggerResource>> responseEntity = rest.exchange("http://localhost:" + port
                    + "/swagger-resources", HttpMethod.GET, null, swaggerResourceParameterizedType);

        List<SwaggerResource> swaggerResourceList = responseEntity.getBody();
        Assertions.assertThat(swaggerResourceList).isNotEmpty();

        SwaggerResource resource = swaggerResourceList.get(0);
        Assertions.assertThat(resource).isNotNull();
        Assertions.assertThat(resource.getSwaggerVersion()).isEqualTo("2.0");
        Assertions.assertThat(resource.getName()).isEqualTo("default");
        Assertions.assertThat(resource.getLocation()).isEqualTo("/v2/api-docs");
    }
}
