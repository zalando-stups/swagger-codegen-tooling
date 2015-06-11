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
package org.zalando.maven.plugins.swagger.codegen;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.nio.file.Files;

import org.apache.commons.io.IOUtils;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import org.zalando.stups.swagger.codegen.CodegenerationException;
import org.zalando.stups.swagger.codegen.StandaloneCodegenerator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.util.Yaml;

/**
 * @author  jbellmann
 */
@Mojo(
    name = "codegen", defaultPhase = LifecyclePhase.GENERATE_SOURCES, requiresProject = true, threadSafe = false,
    requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME,
    requiresDependencyCollection = ResolutionScope.COMPILE_PLUS_RUNTIME
)
public class CodegenMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    @Parameter(required = true, defaultValue = "${project.build.directory}/generated-sources/java")
    private File outputDirectory;

    @Parameter(required = true, defaultValue = "${project.basedir}/src/main/resources/swagger.yaml")
    private String apiFile;

    @Parameter(required = true)
    private String language;

    @Parameter
    private String apiPackage;

    @Parameter
    private String modelPackage;

    @Parameter
    private boolean yamlToJson = false;

    @Parameter(required = true, defaultValue = "${project.build.directory}/classes")
    private File yamlToJsonOutputDirectory;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        StandaloneCodegenerator generator = StandaloneCodegenerator.builder().withApiFilePath(apiFile)
                                                                   .forLanguage(language)
                                                                   .writeResultsTo(outputDirectory)
                                                                   .withApiPackage(apiPackage)
                                                                   .withModelPackage(modelPackage)
                                                                   .withLogger(new MojoCodegeneratorLogger(getLog()))
                                                                   .build();

        try {
            generator.generate();

            //
            project.addCompileSourceRoot(generator.getOutputDirectoryPath());

            if (yamlToJson) {
                getLog().info("Generate .json file from .yaml");
                if (!yamlToJsonOutputDirectory.exists()) {
                    yamlToJsonOutputDirectory.mkdirs();
                    getLog().info("OutputDirectory created");
                }

                File jsonFile = new File(yamlToJsonOutputDirectory, getYamlFilename() + ".json");
                FileWriter fileWriter = null;
                try {

                    fileWriter = new FileWriter(jsonFile);
                    fileWriter.write(getYamlFileContentAsJson());
                    fileWriter.flush();
                    getLog().info("File written to " + jsonFile.getAbsolutePath());
                } catch (Exception e) {
                    throw new MojoExecutionException(e.getMessage(), e);
                } finally {
                    IOUtils.closeQuietly(fileWriter);
                }
            }

        } catch (CodegenerationException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }

    protected String getYamlFileContentAsJson() throws IOException {
        String data = new String(Files.readAllBytes(java.nio.file.Paths.get(new File(apiFile).toURI())));

        ObjectMapper yamlMapper = Yaml.mapper();
        JsonNode rootNode = yamlMapper.readTree(data);

        // must have swagger node set
        JsonNode swaggerNode = rootNode.get("swagger");

        return rootNode.toString();
    }

    protected String getYamlFilename() throws MojoExecutionException {

        File file = new File(apiFile);
        if (!file.exists()) {
            throw new MojoExecutionException("Api-File not found: " + apiFile);
        } else {
            String filename = file.getName();
            return filename.substring(0, filename.indexOf("."));
        }
    }
}
