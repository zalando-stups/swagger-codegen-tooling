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

/**
 * @author  jbellmann
 */
@Mojo(
    name = "codegen", defaultPhase = LifecyclePhase.GENERATE_SOURCES, requiresProject = true, threadSafe = false,
    requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME,
    requiresDependencyCollection = ResolutionScope.COMPILE_PLUS_RUNTIME
)
public class CodegenMojo extends AbstractMojo {

// protected Map<String, CodegenConfig> configs = new HashMap<String, CodegenConfig>();
//
// protected String configString;

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    @Parameter(required = true, defaultValue = "${project.build.directory}/generated-sources/java")
    private File outputDirectory;

    @Parameter(required = true, defaultValue = "${project.basedir}/src/main/resources/swagger.yaml")
    private String apiFile;

    @Parameter(required = true)
    private String language;

    @Parameter(required = true)
    private String apiPackage;

    @Parameter
    private String modelPackage;

// @Parameter
// protected Map<String, String> codegenConfig = new HashMap<String, String>();
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        StandaloneCodegenerator generator = StandaloneCodegenerator.builder().withApiFilePath(apiFile)
                                                                   .forLanguage(language)
                                                                   .writeResultsTo(outputDirectory)
                                                                   .withApiPackage(apiPackage)
                                                                   .withModelPackage(modelPackage).build();

        try {
            generator.generate();
            project.addCompileSourceRoot(generator.getOutputDirectoryPath());

        } catch (CodegenerationException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }

// if (!getOutputDirectory().exists()) {
// getOutputDirectory().mkdirs();
// }
//
// checkModelPackage();
//
// checkApiFileExist();
//
// prepare();
//
// //
// ClientOptInput clientOptInput = new ClientOptInput();
// ClientOpts clientOpts = new ClientOpts();
// Swagger swagger = null;
//
// getLog().info("Generate for language : " + language);
//
// CodegenConfig codegenConfig = getConfig(language);
// if (codegenConfig == null) {
// throw new MojoExecutionException("No CodegenConfig-Implementation found for " + language);
// }
//
// if (!(codegenConfig instanceof ConfigurableCodegenConfig)) {
// throw new MojoExecutionException(
// "Unable to configure CodegenConfig because not of type ConfigurableCodegenConfig");
// }
//
// // config
// ((ConfigurableCodegenConfig) codegenConfig).setApiPackage(apiPackage);
// ((ConfigurableCodegenConfig) codegenConfig).setModelPackage(modelPackage);
//
// clientOptInput.setConfig(codegenConfig);
// clientOptInput.getConfig().setOutputDir(outputDirectory.getAbsolutePath());
//
// swagger = new SwaggerParser().read(this.apiFile, clientOptInput.getAuthorizationValues(), true);
// try {
// clientOptInput.opts(clientOpts).swagger(swagger);
// new Codegen().opts(clientOptInput).generate();
// } catch (Exception e) {
// throw new MojoExecutionException(e.getMessage(), e);
// }

// project.addCompileSourceRoot(getOutputDirectory().getAbsolutePath());
        // maybe use this for static resources (static html)
        // FileSet fileSet = new FileSet();
        // fileSet.setDirectory("");
        // project.addResource(null);
    }

// protected void checkModelPackage() {
// if (modelPackage == null || modelPackage.trim().isEmpty()) {
// getLog().info("No 'modelPackage' was specified, use configured 'apiPackage' : " + apiPackage);
// modelPackage = apiPackage;
// }
// }
//
// protected void checkApiFileExist() throws MojoExecutionException {
// File file = new File(apiFile);
// if (!file.exists()) {
// throw new MojoExecutionException("The 'apiFile' does not exists at : " + apiFile);
// }
// }
//
// public File getOutputDirectory() {
// return this.outputDirectory;
// }
//
// protected void prepare() {
// List<CodegenConfig> extensions = getExtensions();
// StringBuilder sb = new StringBuilder();
//
// for (CodegenConfig config : extensions) {
// if (sb.toString().length() != 0) {
// sb.append(", ");
// }
//
// sb.append(config.getName());
// getLog().info("register config : '" + config.getName() + "' with class : " + config.getClass().getName());
// configs.put(config.getName(), config);
// configString = sb.toString();
// }
// }
//
// private CodegenConfig getConfig(final String name) {
// if (configs.containsKey(name)) {
// return configs.get(name);
// } else {
// try {
// getLog().info("loading class " + name);
//
// Class<?> customClass = Class.forName(name);
// getLog().info("loaded");
// return (CodegenConfig) customClass.newInstance();
// } catch (Exception e) {
// throw new RuntimeException("can't load config-class for '" + name + "'");
// }
// }
// }
//
// private List<CodegenConfig> getExtensions() {
// ServiceLoader<CodegenConfig> loader = ServiceLoader.load(CodegenConfig.class);
// List<CodegenConfig> output = new ArrayList<CodegenConfig>();
// Iterator<CodegenConfig> itr = loader.iterator();
// while (itr.hasNext()) {
// output.add(itr.next());
// }
//
// return output;
// }
}
