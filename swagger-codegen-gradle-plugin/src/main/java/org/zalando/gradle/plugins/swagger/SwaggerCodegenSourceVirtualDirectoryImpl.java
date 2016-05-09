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

import org.gradle.api.file.SourceDirectorySet;
import org.gradle.api.internal.file.DefaultSourceDirectorySet;
import org.gradle.api.internal.file.FileResolver;
import org.gradle.api.internal.file.collections.DefaultDirectoryFileTreeFactory;
import org.gradle.util.ConfigureUtil;

import groovy.lang.Closure;

public class SwaggerCodegenSourceVirtualDirectoryImpl implements SwaggerCodegenSourceVirtualDirectory {

    private static final String[] filters = { "**/*.yaml" };

    private final SourceDirectorySet swaggerCodegen;

    public SwaggerCodegenSourceVirtualDirectoryImpl(String parentDisplayName, FileResolver fileResolver) {
        final String displayName = String.format("%s Swagger-Codegen source", parentDisplayName);
        this.swaggerCodegen = new DefaultSourceDirectorySet("swagger-codegen", displayName, fileResolver,
                new DefaultDirectoryFileTreeFactory());

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
