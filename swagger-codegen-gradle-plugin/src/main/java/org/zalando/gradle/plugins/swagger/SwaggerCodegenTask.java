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
package org.zalando.gradle.plugins.swagger;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.gradle.api.Action;
import org.gradle.api.GradleException;
import org.gradle.api.file.SourceDirectorySet;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.OutputDirectories;
import org.gradle.api.tasks.SourceTask;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.incremental.IncrementalTaskInputs;
import org.gradle.api.tasks.incremental.InputFileDetails;
import org.zalando.stups.swagger.codegen.CodegenerationException;
import org.zalando.stups.swagger.codegen.StandaloneCodegenerator;
import org.zalando.stups.swagger.codegen.YamlToJson;

public class SwaggerCodegenTask extends SourceTask {

    private final ArrayList<File> include = new ArrayList<>();

    private SourceDirectorySet source;

    private File out = getProject().file("build/generated-src/swagger-codegen");

    private boolean recurse = true;

    private boolean verbose = false;

    private boolean strict = false;

    private boolean debug = false;

    // @Parameter(required = true, defaultValue =
    // "${project.basedir}/src/main/resources/swagger.yaml")
    private String apiFile;

    // @Parameter(required = true)
    private String language;

    // @Parameter
    private String apiPackage;

    // @Parameter
    private String modelPackage;

    // @Parameter(defaultValue = "false")
    private boolean skipModelgeneration = false;

    // @Parameter(defaultValue = "false")
    private boolean skipApigeneration = false;

    // @Parameter(defaultValue = "false")
    private boolean enable303 = false;

    // @Parameter(defaultValue = "false")
    private boolean enableBuilderSupport = false;

    // @Parameter
    private Map<String, Object> additionalProperties = new HashMap<>();

    // @Parameter
    private ArrayList<String> excludedModels = new ArrayList<>();

    // @Parameter
    private boolean yamlToJson = false;

    // @Parameter(required = true, defaultValue =
    // "${project.build.directory}/classes")
    private File yamlToJsonOutputDirectory = getProject().file("build/generated-src/swagger-codegen");

    //@formatter:off
    @TaskAction
    public void invokeSwaggerCodegen(final IncrementalTaskInputs inputs) throws Exception {
        final ArrayList<File> inputFiles = new ArrayList<>();
        if (inputs.isIncremental()) {
            inputs.outOfDate(new Action<InputFileDetails>() {
                @Override
                public void execute(InputFileDetails inputFileDetails) {
                    if (inputFileDetails.isAdded() || inputFileDetails.isModified()) {
                        inputFiles.add(inputFileDetails.getFile());
                    }
                }
            });
        } else {
            inputFiles.addAll(getSource().getFiles());
        }

        
        try {

            final StandaloneCodegenerator swaggerGenerator = StandaloneCodegenerator.builder()
                                                                                     .withApiFilePath(apiFile)
                                                                                     .forLanguage(language)
                                                                                     .writeResultsTo(out)
                                                                                     .withApiPackage(apiPackage)
                                                                                     .withModelPackage(modelPackage)
                                                                                     .withLogger(new GradleCodegeneratorLogger(getProject().getLogger()))
                                                                                     .skipModelgeneration(skipModelgeneration)
                                                                                     .skipApigeneration(skipApigeneration)
                                                                                     .withModelsExcluded(excludedModels)
                                                                                     .additionalProperties(additionalProperties)
                                                                                     .enable303(enable303)
                                                                                     .enableBuilderSupport(enableBuilderSupport)
                                                                                     .build();

            try {
                swaggerGenerator.generate();

                if (yamlToJson) {

                    final YamlToJson converter = YamlToJson.builder()
                                                            .withYamlInputPath(apiFile)
                                                            .withCodegeneratorLogger(new GradleCodegeneratorLogger(getProject().getLogger()))
                                                            .withOutputDirectoryPath(yamlToJsonOutputDirectory.getAbsolutePath())
                                                            .build();
                    converter.convert();
                }
            } catch (CodegenerationException e) {
                throw new GradleException(e.getMessage(), e);
            }
        } catch (GradleException e) {
            throw e;
        } catch (Exception e) {
            throw new GradleException("Unexpected error while executing swagger-codegen: " + e.getMessage(), e);

        }
    }
    //@formatter:on

    @OutputDirectories
    public Set<File> getOutputDirectories() {
        final HashSet<File> files = new HashSet<>();
        boolean useSharedOutputDir = false;
        // for (final Generator generator : generators) {
        // useSharedOutputDir |= generator.getOut() == null;
        // files.add(generatorOutputDirectory(generator));
        // }
        if (useSharedOutputDir) {
            files.add(this.out);
        }
        return files;
    }

    public void setSource(final SourceDirectorySet sourceDirectorySet) {
        this.source = sourceDirectorySet;
    }

    @Override
    @InputFiles
    public SourceDirectorySet getSource() {
        return source;
    }

    @Input
    public List<File> getInclude() {
        return include;
    }

    @Input
    public boolean isRecurse() {
        return recurse;
    }

    @Input
    public boolean isVerbose() {
        return verbose;
    }

    @Input
    public boolean isStrict() {
        return strict;
    }

    @Input
    public boolean isDebug() {
        return debug;
    }

    public void out(Object dir) {
        this.out = getProject().file(dir);
    }

    public void recurse(boolean recurse) {
        this.recurse = recurse;
    }

    public void verbose(boolean verbose) {
        this.verbose = verbose;
    }

    public void strict(boolean strict) {
        this.strict = strict;
    }

    public void debug(boolean debug) {
        this.debug = debug;
    }

    public void path(Object file) {
        include.add(getProject().file(file));
    }

    @Input
    public String getApiFile() {
        return apiFile;
    }

    public void setApiFile(String apiFile) {
        this.apiFile = apiFile;
    }

    @Input
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Input
    public String getApiPackage() {
        return apiPackage;
    }

    public void setApiPackage(String apiPackage) {
        this.apiPackage = apiPackage;
    }

    @Input
    public String getModelPackage() {
        return modelPackage;
    }

    public void setModelPackage(String modelPackage) {
        this.modelPackage = modelPackage;
    }

    @Input
    public boolean isSkipModelgeneration() {
        return skipModelgeneration;
    }

    public void setSkipModelgeneration(boolean skipModelgeneration) {
        this.skipModelgeneration = skipModelgeneration;
    }

    @Input
    public boolean isSkipApigeneration() {
        return skipApigeneration;
    }

    public void setSkipApigeneration(boolean skipApigeneration) {
        this.skipApigeneration = skipApigeneration;
    }

    @Input
    public boolean isYamlToJson() {
        return yamlToJson;
    }

    public void setYamlToJson(boolean yamlToJson) {
        this.yamlToJson = yamlToJson;
    }
}
