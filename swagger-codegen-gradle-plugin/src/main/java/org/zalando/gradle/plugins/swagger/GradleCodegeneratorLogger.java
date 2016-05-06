package org.zalando.gradle.plugins.swagger;

import org.gradle.api.logging.Logger;
import org.zalando.stups.swagger.codegen.CodegeneratorLogger;

public class GradleCodegeneratorLogger implements CodegeneratorLogger {

    private final Logger delegate;

    GradleCodegeneratorLogger(Logger logger) {
        this.delegate = logger;
    }

    @Override
    public void info(String message) {
        delegate.info(message);
    }

}
