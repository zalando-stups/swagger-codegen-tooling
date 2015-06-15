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

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Properties to be configured when using STUPS-Swagger-Codegen.
 *
 * @author  jbellmann
 */
@ConfigurationProperties(prefix = "swagger-codegen")
public class SwaggerCodegenProperties implements Validator {

    private String location = "/v2/api-docs";

    private String name = "default";

    private String swaggerVersion = "2.0";

    @NotNull(message = "'apiClasspathLocation' should never be null")
    private String apiClasspathLocation;

    public String getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public String getSwaggerVersion() {
        return swaggerVersion;
    }

    public String getApiClasspathLocation() {
        return apiClasspathLocation;
    }

    public void setLocation(final String location) {
        this.location = location;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setSwaggerVersion(final String swaggerVersion) {
        this.swaggerVersion = swaggerVersion;
    }

    public void setApiClasspathLocation(final String apiClasspathLocation) {
        this.apiClasspathLocation = apiClasspathLocation;
    }

    @Override
    public boolean supports(final Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        System.out.println("VALIDATION");
    }

}
