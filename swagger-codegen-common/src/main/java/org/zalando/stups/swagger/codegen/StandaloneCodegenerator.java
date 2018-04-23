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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ServiceLoader;

import com.google.common.collect.ImmutableMap;

import io.swagger.codegen.ClientOptInput;
import io.swagger.codegen.ClientOpts;
import io.swagger.codegen.CodegenConfig;
import io.swagger.codegen.DefaultGenerator;
import io.swagger.models.Model;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;

/**
 * Extracted from Mojo to reuse for easier template-testing.
 *
 * @author jbellmann
 */
public class StandaloneCodegenerator {

    private Map<String, CodegenConfig> configs = new HashMap<String, CodegenConfig>();

    private CodegeneratorLogger codeGeneratorLogger = new SystemOutCodegeneratorLogger();

    private String apiPrefix;

    private String apiFile;

    private String language;

    private File outputDirectory;

    private String apiPackage;

    private String modelPackage;

    private boolean skipApigeneration;

    private boolean skipModelgeneration;

    private boolean enable303;

    private boolean enableBuilderSupport;

    private Map<String, Object> additionalProperties;

    private List<String> excludedModels;
    
    //
    private boolean skipModelTests = true;
    
    private boolean skipModelDocs = true;
    
    private boolean skipApiTests = true;
    
    private boolean skipApiDocs = true;

    public static CodegeneratorBuilder builder() {
        return new CodegeneratorBuilder();
    }

    public void generate() throws CodegenerationException {

        if (!getOutputDirectory().exists()) {
            getOutputDirectory().mkdirs();
        }

        checkModelPackage();

        checkApiFileExist();

        prepare();

        //
        final ClientOptInput clientOptInput = new ClientOptInput();
        final ClientOpts clientOpts = new ClientOpts();
        Swagger swagger = null;

        getLog().info("Generate for language : " + language);

        final CodegenConfig codegenConfig = getConfig(language);
        if (codegenConfig == null) {
            throw new CodegenerationException("No CodegenConfig-Implementation found for " + language);
        }

        codegenConfig.additionalProperties().putAll(additionalProperties);

        if (codegenConfig instanceof ConfigurableCodegenConfig) {

            // DefaultGenerator-line 72
            if (skipApigeneration) {
                ((ConfigurableCodegenConfig) codegenConfig).skipApiGeneration();
            }

            if (skipModelgeneration) {
                ((ConfigurableCodegenConfig) codegenConfig).skipModelGeneration();
            }

            if (skipApigeneration && apiPrefix == null) {
                ((ConfigurableCodegenConfig) codegenConfig).setApiPrefix("");
            } else {
                // config
                ((ConfigurableCodegenConfig) codegenConfig).setApiPrefix(apiPrefix);
            }
            if (skipApigeneration && apiPackage == null) {
                ((ConfigurableCodegenConfig) codegenConfig).setApiPackage("");
            } else {
                // config
                ((ConfigurableCodegenConfig) codegenConfig).setApiPackage(apiPackage);
            }
            if (skipModelgeneration && modelPackage == null) {
                ((ConfigurableCodegenConfig) codegenConfig).setModelPackage("");
            } else {
                ((ConfigurableCodegenConfig) codegenConfig).setModelPackage(modelPackage);
            }
            if (enable303) {
                getLog().info("JSR 303 enabled ...");
                if (((ConfigurableCodegenConfig) codegenConfig).is303Supported()) {
                    getLog().info("and supported by " + language);
                    ((ConfigurableCodegenConfig) codegenConfig).enable303();
                } else {
                    getLog().info("but not supported by " + language);
                }
            }

            if (enableBuilderSupport) {
                getLog().info("BuilderSupport enabled ...");
                if (((ConfigurableCodegenConfig) codegenConfig).isBuilderSupported()) {
                    getLog().info("and supported by : " + language);
                    ((ConfigurableCodegenConfig) codegenConfig).enableBuilderSupport();
                } else {
                    getLog().info("but not supported by : " + language);
                }
            }

        }

        clientOptInput.setConfig(codegenConfig);
        clientOptInput.getConfig().setOutputDir(outputDirectory.getAbsolutePath());

        swagger = new SwaggerParser().read(this.apiFile, clientOptInput.getAuthorizationValues(), true);

        if (skipApigeneration) {
            getLog().info("API-GENERATION DISABLED ...");
            System.setProperty("models", "");
            swagger.setPaths(new HashMap<String, Path>(0));
        }

        if (skipModelgeneration) {
            getLog().info("MODEL-GENERATION DISABLED ...");
            System.setProperty("apis", "");
            swagger.setDefinitions(new HashMap<String, Model>(0));
        } else if (!excludedModels.isEmpty()) {
            final Iterator<Entry<String, Model>> it = swagger.getDefinitions().entrySet().iterator();
            while (it.hasNext()) {
                final Map.Entry<String, Model> entry = it.next();
                if (excludedModels.contains(entry.getKey())) {
                    getLog().info("REMOVE MODEL '" + entry.getKey() + "' FROM GENERATION ...");
                    it.remove();
                }
            }
        }
        
        if(skipModelTests){
            System.setProperty("modelTests", Boolean.FALSE.toString());
        }
        
        if(skipModelDocs){
            System.setProperty("modelDocs", Boolean.FALSE.toString());
        }
        if(skipApiTests){
            System.setProperty("apiTests", Boolean.FALSE.toString());
        }
        if(skipApiDocs){
            System.setProperty("apiDocs", Boolean.FALSE.toString());
        }

        try {
            clientOptInput.opts(clientOpts).swagger(swagger);

            final DefaultGenerator generator = (DefaultGenerator) new DefaultGenerator().opts(clientOptInput);
            final List<File> generatedFiles = generator.generate();
            getLog().info(generatedFiles.size() + " generated Files");
            // updated to 2.1.4
            // if (CodeGenStatus.FAILED.equals(generator.status)) {
            // throw new CodegenerationException("Codegen failed by 'status'");
            // }
        } catch (final Exception e) {
            throw new CodegenerationException(e.getMessage(), e);
        }
    }

    public String getOutputDirectoryPath() {
        return getOutputDirectory().getAbsolutePath();
    }

    private CodegeneratorLogger getLog() {
        return codeGeneratorLogger;
    }

    protected void checkModelPackage() {
        if (modelPackage == null || modelPackage.trim().isEmpty()) {
            getLog().info("No 'modelPackage' was specified, use configured 'apiPackage' : " + apiPackage);
            modelPackage = apiPackage;
        }
    }

    protected void checkApiFileExist() throws CodegenerationException {
        try {
            URL url = new URL(apiFile);
            String prot = url.getProtocol();
            if ((!"https".equals(prot)) || (!"http".equals(prot))) {
                getLog().info("'apiFile' should use 'http' or 'https'");
            }
            return;
        } catch (MalformedURLException e) {
            getLog().info("'apiFile' seems not be an valid URL, check file exist");
            final File file = new File(apiFile);
            if (!file.exists()) {
                getLog().info("The 'apiFile' does not exists at : " + apiFile);
                throw new CodegenerationException("The 'apiFile' does not exists at : " + apiFile);
            }
        }
    }

    public File getOutputDirectory() {
        return this.outputDirectory;
    }

    protected void prepare() {
        final List<CodegenConfig> extensions = getExtensions();
        final StringBuilder sb = new StringBuilder();

        for (final CodegenConfig config : extensions) {
            if (sb.toString().length() != 0) {
                sb.append(", ");
            }

            sb.append(config.getName());
            getLog().info("register config : '" + config.getName() + "' with class : " + config.getClass().getName());
            configs.put(config.getName(), config);
            // do not know
            // configString = sb.toString();
        }
    }

    private CodegenConfig getConfig(final String name) {
        if (configs.containsKey(name)) {
            return configs.get(name);
        } else {
            try {
                getLog().info("loading class " + name);

                final Class<?> customClass = Class.forName(name);
                getLog().info("loaded " + name);
                return (CodegenConfig) customClass.newInstance();
            } catch (final Exception e) {
                throw new RuntimeException("can't load config-class for '" + name + "'", e);
            }
        }
    }

    private List<CodegenConfig> getExtensions() {
        final ServiceLoader<CodegenConfig> loader = ServiceLoader.load(CodegenConfig.class);
        final List<CodegenConfig> output = new ArrayList<CodegenConfig>();
        final Iterator<CodegenConfig> itr = loader.iterator();
        while (itr.hasNext()) {
            output.add(itr.next());
        }

        return output;
    }

    public static class CodegeneratorBuilder {

        private CodegeneratorLogger codeGeneratorLogger;

        private String apiPrefix;

        private String apiFile;

        private String language;

        private File outputDirectory;

        private String apiPackage;

        private String modelPackage;

        private boolean skipModelgeneration = false;

        private boolean skipApigeneration = false;

        private boolean enable303 = false;

        private boolean enableBuilderSupport = false;

        private List<String> excludedModels = new ArrayList<String>(0);

        private Map<String, Object> additionalProperties = ImmutableMap.of();

        public CodegeneratorBuilder withApiFilePath(final String pathToApiFile) {
            this.apiFile = pathToApiFile;
            return this;
        }

        public CodegeneratorBuilder withApiPrefix(final String apiPrefix) {
            this.apiPrefix = apiPrefix;
            return this;
        }

        public CodegeneratorBuilder withApiFile(final File apiFile) {
            this.apiFile = apiFile.getAbsolutePath();
            return this;
        }

        public CodegeneratorBuilder forLanguage(final String languageDelimiter) {
            this.language = languageDelimiter;
            return this;
        }

        public CodegeneratorBuilder writeResultsTo(final File outputDirectory) {
            this.outputDirectory = outputDirectory;
            return this;
        }

        public CodegeneratorBuilder withApiPackage(final String apiPackage) {
            this.apiPackage = apiPackage;
            return this;
        }

        public CodegeneratorBuilder withModelPackage(final String modelPackage) {
            this.modelPackage = modelPackage;
            return this;
        }

        public CodegeneratorBuilder withLogger(final CodegeneratorLogger codegeneratorLogger) {
            this.codeGeneratorLogger = codegeneratorLogger;
            return this;
        }

        public CodegeneratorBuilder skipModelgeneration(final boolean skip) {
            this.skipModelgeneration = skip;
            return this;
        }

        public CodegeneratorBuilder withModelsExcluded(final List<String> excludedModels) {
            this.excludedModels = excludedModels;
            return this;
        }

        public CodegeneratorBuilder skipApigeneration(final boolean skip) {
            this.skipApigeneration = skip;
            return this;
        }

        public CodegeneratorBuilder enable303(final boolean enable303) {
            this.enable303 = enable303;
            return this;
        }

        public CodegeneratorBuilder enableBuilderSupport(final boolean enableBuilderSupport) {
            this.enableBuilderSupport = enableBuilderSupport;
            return this;
        }

        public CodegeneratorBuilder additionalProperties(Map<String, Object> properties) {
            this.additionalProperties = properties;
            return this;
        }

        public StandaloneCodegenerator build() {
            final StandaloneCodegenerator generator = new StandaloneCodegenerator();

            generator.apiPrefix = this.apiPrefix;
            generator.apiFile = this.apiFile;
            generator.language = this.language;
            generator.outputDirectory = this.outputDirectory;
            generator.apiPackage = this.apiPackage;
            generator.modelPackage = this.modelPackage;
            generator.skipModelgeneration = this.skipModelgeneration;
            generator.excludedModels = this.excludedModels;
            generator.skipApigeneration = this.skipApigeneration;
            generator.enable303 = this.enable303;
            generator.enableBuilderSupport = this.enableBuilderSupport;
            generator.additionalProperties = this.additionalProperties;

            if (this.codeGeneratorLogger != null) {

                generator.codeGeneratorLogger = this.codeGeneratorLogger;
            }

            return generator;
        }
    }
}
