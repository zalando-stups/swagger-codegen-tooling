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

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.util.StreamUtils;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Simple {@link RestController} to support swagger-ui.
 *
 * @author  jbellmann
 */
@RestController
public class SwaggerCodegenController {

    private final SwaggerCodegenProperties swaggerCodegenProperties;

    @Autowired
    public SwaggerCodegenController(final SwaggerCodegenProperties swaggerCodegenProperties) {
        this.swaggerCodegenProperties = swaggerCodegenProperties;
    }

    @RequestMapping(value = "/swagger-resources")
    public List<SwaggerResource> getResources() {
        final SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setLocation(swaggerCodegenProperties.getLocation());
        swaggerResource.setName(swaggerCodegenProperties.getName());
        swaggerResource.setSwaggerVersion(swaggerCodegenProperties.getSwaggerVersion());
        return Collections.singletonList(swaggerResource);
    }

    @RequestMapping(value = "/v2/api-docs")
    public String getApi() throws IOException {
        return new String(StreamUtils.copyToByteArray(
                    getClass().getResourceAsStream(swaggerCodegenProperties.getApiClasspathLocation())));
    }
}
