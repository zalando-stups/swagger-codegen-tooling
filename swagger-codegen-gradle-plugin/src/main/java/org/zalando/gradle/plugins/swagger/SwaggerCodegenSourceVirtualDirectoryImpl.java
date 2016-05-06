package org.zalando.gradle.plugins.swagger;

import org.gradle.api.file.SourceDirectorySet;
import org.gradle.api.internal.file.DefaultSourceDirectorySet;
import org.gradle.api.internal.file.FileResolver;
import org.gradle.util.ConfigureUtil;

import groovy.lang.Closure;

public class SwaggerCodegenSourceVirtualDirectoryImpl implements SwaggerCodegenSourceVirtualDirectory {

    private static final String[] filters = { "**/*.yaml" };

    private final SourceDirectorySet swaggerCodegen;

    public SwaggerCodegenSourceVirtualDirectoryImpl(String parentDisplayName, FileResolver fileResolver) {
        final String displayName = String.format("%s Swagger-Codegen source", parentDisplayName);
        this.swaggerCodegen = new DefaultSourceDirectorySet(displayName, fileResolver);
        this.swaggerCodegen.getFilter().include(filters);
    }

    @Override
    public SourceDirectorySet getSwaggerCodegen() {
        return swaggerCodegen;
    }

    @Override
    public SwaggerCodegenSourceVirtualDirectory swaggerCodegen(Closure closure) {
        ConfigureUtil.configure(closure, getSwaggerCodegen());
        return this;
    }

}
