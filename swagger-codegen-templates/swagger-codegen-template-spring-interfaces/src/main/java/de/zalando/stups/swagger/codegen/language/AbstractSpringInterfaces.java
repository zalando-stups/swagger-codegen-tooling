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

import java.io.File;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.zalando.stups.swagger.codegen.ConfigurableCodegenConfig;

import io.swagger.codegen.CodegenConfig;
import io.swagger.codegen.CodegenOperation;
import io.swagger.codegen.CodegenType;
import io.swagger.codegen.SupportingFile;
import io.swagger.codegen.languages.JavaClientCodegen;

import io.swagger.models.Operation;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.MapProperty;
import io.swagger.models.properties.Property;

/**
 * https://github.com/swagger-api/swagger-codegen/blob/master/modules/swagger-codegen/src/main/java/com/wordnik/swagger/codegen/languages/JaxRSServerCodegen.java.
 *
 * @author  jbellmann
 */
public class AbstractSpringInterfaces extends JavaClientCodegen implements CodegenConfig, ConfigurableCodegenConfig {

    protected String sourceFolder = "";

    public CodegenType getTag() {
        return CodegenType.SERVER;
    }

    public String getName() {
        return "springinterfaces";
    }

    public String getHelp() {
        return "Generates Spring-Interfaces.";
    }

    public AbstractSpringInterfaces() {
        super();
        templateDir = "SpringInterfaces";
        modelTemplateFiles.put("model.mustache", ".java");
        apiTemplateFiles.put("api.mustache", ".java");
    }

    @Override
    public List<SupportingFile> supportingFiles() {
        supportingFiles.clear();
// supportingFiles.add(new SupportingFile("ApiException.mustache", (apiPackage()).replace(".", File.separator),
// "ApiException.java"));
// supportingFiles.add(new SupportingFile("ApiOriginFilter.mustache", (apiPackage()).replace(".", File.separator),
// "ApiOriginFilter.java"));
// supportingFiles.add(new SupportingFile("ApiResponseMessage.mustache",
// (apiPackage()).replace(".", File.separator), "ApiResponseMessage.java"));
// supportingFiles.add(new SupportingFile("NotFoundException.mustache",
// (apiPackage()).replace(".", File.separator), "NotFoundException.java"));

        languageSpecificPrimitives = new HashSet<String>(Arrays.asList("String", "boolean", "Boolean", "Double",
                    "Integer", "Long", "Float"));

        return supportingFiles;
    }

    @Override
    public String apiFileFolder() {
        return outputFolder + "/" + apiPackage().replace('.', File.separatorChar);
    }

    public String modelFileFolder() {
        return outputFolder + "/" + modelPackage().replace('.', File.separatorChar);
    }

    @Override
    public String modelPackage() {
        if (this.modelPackage == null || this.modelPackage.trim().isEmpty()) {
            throw new RuntimeException("'modelPackage' should not be null or empty");
        }

        return this.modelPackage;
    }

    @Override
    public String apiPackage() {

        if (this.apiPackage == null || this.apiPackage.trim().isEmpty()) {
            throw new RuntimeException("'apiPackage' should not be null or empty");
        }

        return this.apiPackage;
    }

    @Override
    public String getTypeDeclaration(final Property p) {
        if (p instanceof ArrayProperty) {
            ArrayProperty ap = (ArrayProperty) p;
            Property inner = ap.getItems();
            return getSwaggerType(p) + "<" + getTypeDeclaration(inner) + ">";
        } else if (p instanceof MapProperty) {
            MapProperty mp = (MapProperty) p;
            Property inner = mp.getAdditionalProperties();

            return getTypeDeclaration(inner);
        }

        return super.getTypeDeclaration(p);
    }

    @Override
    public void addOperationToGroup(final String tag, final String resourcePath, final Operation operation,
            final CodegenOperation co, final Map<String, List<CodegenOperation>> operations) {
        String basePath = resourcePath;
        if (basePath.startsWith("/")) {
            basePath = basePath.substring(1);
        }

        int pos = basePath.indexOf("/");
        if (pos > 0) {
            basePath = basePath.substring(0, pos);
        }

        if (basePath == "") {
            basePath = "default";
        } else {
            if (co.path.startsWith("/" + basePath)) {
                co.path = co.path.substring(("/" + basePath).length());
            }

            co.subresourceOperation = !co.path.isEmpty();
        }

        List<CodegenOperation> opList = operations.get(basePath);
        if (opList == null) {
            opList = new ArrayList<CodegenOperation>();
            operations.put(basePath, opList);
        }

        opList.add(co);
        co.baseName = basePath;
    }

    public Map<String, Object> postProcessOperations(final Map<String, Object> objs) {
        Map<String, Object> operations = (Map<String, Object>) objs.get("operations");
        if (operations != null) {
            List<CodegenOperation> ops = (List<CodegenOperation>) operations.get("operation");
            for (CodegenOperation operation : ops) {
                if (operation.returnType == null) {
                    // operation.returnType = "Void";
                } else if (operation.returnType.startsWith("List")) {
                    String rt = operation.returnType;
                    int end = rt.lastIndexOf(">");
                    if (end > 0) {

                        // operation.returnType = rt.substring("List<".length(), end);
                        operation.returnContainer = "List";
                    }
                } else if (operation.returnType.startsWith("Map")) {
                    String rt = operation.returnType;
                    int end = rt.lastIndexOf(">");
                    if (end > 0) {

                        // operation.returnType = rt.substring("Map<".length(), end);
                        operation.returnContainer = "Map";
                    }
                } else if (operation.returnType.startsWith("Set")) {
                    String rt = operation.returnType;
                    int end = rt.lastIndexOf(">");
                    if (end > 0) {

                        // operation.returnType = rt.substring("Set<".length(), end);
                        operation.returnContainer = "Set";
                    }
                }
            }
        }

        return objs;
    }

    public void setApiPackage(final String apiPackage) {
        this.apiPackage = apiPackage;
    }

    public void setModelPackage(final String modelPackage) {
        this.modelPackage = modelPackage;
    }
}
