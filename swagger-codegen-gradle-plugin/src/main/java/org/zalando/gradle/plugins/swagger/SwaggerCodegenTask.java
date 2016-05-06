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

    // private File thrift;

    // private NamedDomainObjectContainer<Generator> generators =
    // getProject().container(Generator.class);

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

            final StandaloneCodegenerator swaggerGenerator = StandaloneCodegenerator.builder().withApiFilePath(apiFile)
                    .forLanguage(language).writeResultsTo(out).withApiPackage(apiPackage).withModelPackage(modelPackage)
                    .withLogger(new GradleCodegeneratorLogger(getProject().getLogger()))
                    .skipModelgeneration(skipModelgeneration).skipApigeneration(skipApigeneration)
                    .withModelsExcluded(excludedModels).additionalProperties(additionalProperties).enable303(enable303)
                    .enableBuilderSupport(enableBuilderSupport).build();

            try {
                swaggerGenerator.generate();

                if (yamlToJson) {

                    final YamlToJson converter = YamlToJson.builder().withYamlInputPath(apiFile)
                            .withCodegeneratorLogger(new GradleCodegeneratorLogger(getProject().getLogger()))
                            .withOutputDirectoryPath(yamlToJsonOutputDirectory.getAbsolutePath()).build();
                    converter.convert();
                }
            } catch (CodegenerationException e) {
                throw new GradleException(e.getMessage(), e);
            }
        } catch (GradleException e) {
            throw e;
        } catch (Exception e) {
            throw new GradleException("Unexpected error while executing thrift: " + e.getMessage(), e);

        }

        // for (final Generator generator : generators) {
        // for (final File file : inputFiles) {
        // final File out = generatorOutputDirectory(generator);
        //
        // final List<String> command = buildCommand(generator, out,
        // file.getAbsolutePath());
        // getProject().getLogger().info("Running swagger-codegen: " + command);
        // if (!out.isDirectory()) {
        // if (!out.mkdirs()) {
        // throw new GradleException("Could not create swagger-codegen output
        // directory: " + out);
        // }
        // }
        //
        //
        // }
        // }
    }

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

    // @Input
    // public Map<String, Generator> getGenerators() {
    // return generators.getAsMap();
    // }

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

    // @Input
    // public String executable() {
    // return this.thrift != null ? this.thrift.getAbsolutePath() : "thrift";
    // }

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

    // public void generators(Closure c) {
    // generators.configure(c);
    // }

    // public void executable(final Object executable) {
    // this.thrift = getProject().file(executable);
    // }

    // public List<String> buildCommand(final Generator generator, File out,
    // String fileName) {
    // final String thrift = executable();
    // final List<String> command = new ArrayList<>(Arrays.asList(thrift,
    // "-out", out.getAbsolutePath()));
    // command.add("--gen");
    // command.add(generator.getName() + ":" + join(",",
    // generator.getOptions()));
    // for (final File include : this.include) {
    // command.add("-I");
    // command.add(include.getAbsolutePath());
    // }
    // if (recurse)
    // command.add("-recurse");
    // if (verbose)
    // command.add("-verbose");
    // if (strict)
    // command.add("-strict");
    // if (debug)
    // command.add("-debug");
    // command.add(fileName);
    // return command;
    // }
    //
    // private static String join(final String sep, final List<String> arg) {
    // final StringBuilder sb = new StringBuilder();
    // for (int i = 0; i < arg.size(); i++) {
    // sb.append(arg.get(i));
    // if (i < arg.size() - 1) {
    // sb.append(sep);
    // }
    // }
    // return sb.toString();
    // }
    //
    // private File generatorOutputDirectory(final Generator generator) {
    // if (generator.getOut() != null) {
    // return getProject().file(generator.getOut());
    // } else {
    // return this.out;
    // }
    // }
    //
    // private final class SlurpThread extends Thread {
    // private final CountDownLatch latch;
    // private final InputStream in;
    // private final PrintStream out;
    //
    // public SlurpThread(final CountDownLatch latch, final InputStream in,
    // final PrintStream out) {
    // setDaemon(true);
    //
    // this.latch = latch;
    // this.in = in;
    // this.out = out;
    // }
    //
    // @Override
    // public void run() {
    // try {
    // final InputStreamReader reader = new InputStreamReader(in);
    // final char[] buf = new char[8 * 1024];
    // for (;;) {
    // try {
    // if (reader.read(buf) <= 0) {
    // break;
    // }
    // out.print(buf);
    // } catch (IOException e) {
    // getLogger().error("Failed to read from input stream", e);
    // break;
    // }
    // }
    // } finally {
    // latch.countDown();
    // }
    // }
    // }
}
