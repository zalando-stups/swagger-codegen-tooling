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
package de.zalando.stups.swagger.codegen.language;

import java.util.Map;

import io.swagger.codegen.CodegenModel;
import io.swagger.models.Model;

/**
 * https://github.com/swagger-api/swagger-codegen/blob/master/modules/swagger-
 * codegen/src/main/java/com/wordnik/swagger/codegen/languages/
 * JaxRSServerCodegen.java.
 *
 * @author jbellmann
 */
public class SpringInterfacesSplitResponseEntityNoSwaggerAnnotations extends SpringInterfacesResponseEntityNoSwaggerAnnotations {
    @Override
    public String getName() {
        return "springinterfacesSplitResponseEntityNoSwaggerAnnotations";
    }

    @Override
    public String getHelp() {
        return "Generates Spring-Interfaces and concrete Rest Service Implementation with ResponseEntity without Swagger-Annotations.";
    }

    public SpringInterfacesSplitResponseEntityNoSwaggerAnnotations() {
        super();
        embeddedTemplateDir = templateDir = "SpringInterfacesSplitResponseEntityNoSwaggerAnnotations";
        apiTemplateFiles.put("api-impl.mustache", "Impl.java");
    }
}
