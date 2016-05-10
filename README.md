[![Build Status](https://travis-ci.org/zalando-stups/swagger-codegen-tooling.svg?branch=master)](https://travis-ci.org/zalando-stups/swagger-codegen-tooling)

### Swagger-Codegen-Tooling

The project provides some tooling around Maven and Gradle to generate code from OpenAPI-Specs. It comes with custom-templates to support Spring-MVC/Spring-Boot projects. Instead of generating code only once when a project starts (design phase), code will be generated at every build to make sure your code is in sync with
your spec. So controllers/resources are generated as interfaces developers have to implement then. So changes
in the spec should be reflected immediately on build/compile-step.

#### Getting started with Maven

To get started in a Maven project just add the following plugin-definition to you pom.xml.

```xml
    <plugin>
        <groupId>org.zalando.maven.plugins</groupId>
        <artifactId>swagger-codegen-maven-plugin</artifactId>
        <version>${version}</version>
        <configuration>
            <apiFile>${project.basedir}/src/main/resources/api.yaml</apiFile>
            <language>springinterfaces</language>
            <apiPackage>com.example.project.api</apiPackage>
            <modelPackage>com.example.project.model</modelPackage>
        </configuration>
        <executions>
            <execution>
                <id>swagger-codegen</id>
                <goals>
                    <goal>codegen</goal>
                </goals>
            </execution>
        </executions>
    </plugin>
```

According to your OpenAPI-spec (api.yaml) code will be generated in `${basedir}/target/generated-sources/swagger-codegen`

More examples how to use the Maven-Plugin can be found in the [integration-test](https://github.com/zalando-stups/swagger-codegen-tooling/tree/master/swagger-codegen-maven-plugin/src/it) section.

#### Getting started with Gradle

To get started in a Gradle project, make sure the following configuration is present in your `build.gradle`

```
apply plugin: 'java'
apply plugin: 'swagger-codegen'

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath 'org.zalando.stups:swagger-codegen-gradle-plugin:${version}'
    }
}

swaggerCodegen {
    apiFile 'src/main/swagger-codegen/kio-api.yaml'
    language 'jaxrsinterfaces'
    apiPackage 'com.example.project.api'
    modelPackage 'com.example.project.model'
}

...
```

NOTE: The Swagger-Codegen-Gradle-Plugin is currently in development. So be prepared for changes.

### Development/Contribution


#### Build

The project itself uses Maven:

    mvn clean install

#### Run with integration-tests enabled

    mvn clean install -Pintegration

#### TODO's

* improve robustness for Gradle-Plugin
* improve Templates (what about [Controllers that delegate to an interface](https://github.com/zalando-stups/swagger-codegen-tooling/issues/32))
* improve documentation
* prepare an example that uses [spring-restdocs](https://projects.spring.io/spring-restdocs)


#### Contributions

Many thanks to [ePaul](https://github.com/ePaul) for reporting issues and code-contributions.

## License

Copyright 2015 Zalando SE

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   [http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

