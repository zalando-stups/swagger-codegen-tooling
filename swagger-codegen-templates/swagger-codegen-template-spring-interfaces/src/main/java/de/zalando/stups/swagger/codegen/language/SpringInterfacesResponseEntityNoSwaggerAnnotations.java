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

/**
 * https://github.com/swagger-api/swagger-codegen/blob/master/modules/swagger-codegen/src/main/java/com/wordnik/swagger/codegen/languages/JaxRSServerCodegen.java.
 *
 * @author  jbellmann
 */
public class SpringInterfacesResponseEntityNoSwaggerAnnotations extends AbstractSpringInterfaces {

    protected String sourceFolder = "";

    public String getName() {
        return "springinterfacesResponseEntityNoSwaggerAnnotations";
    }

    public String getHelp() {
        return "Generates Spring-Interfaces with ResponseEntity without Swagger-Annotations.";
    }

    public SpringInterfacesResponseEntityNoSwaggerAnnotations() {
        super();
        templateDir = "SpringInterfacesResponseEntityNoSwaggerAnnotations";
        modelTemplateFiles.put("model.mustache", ".java");
        apiTemplateFiles.put("api.mustache", ".java");
    }
}
