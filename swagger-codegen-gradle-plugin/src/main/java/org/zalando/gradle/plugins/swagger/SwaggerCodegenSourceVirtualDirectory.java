package org.zalando.gradle.plugins.swagger;

import org.gradle.api.file.SourceDirectorySet;

import groovy.lang.Closure;

public interface SwaggerCodegenSourceVirtualDirectory {

    SourceDirectorySet getSwaggerCodegen();

    SwaggerCodegenSourceVirtualDirectory swaggerCodegen(Closure closure);

}
