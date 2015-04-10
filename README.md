##WORK IN PROGRESS -- USE AT YOUR OWN RISK

[![Build Status](https://travis-ci.org/zalando-stups/swagger-codegen-tooling.svg?branch=master)](https://travis-ci.org/zalando-stups/swagger-codegen-tooling)

###Build

    mvn clean install

###Run with integration-tests enabled

    mvn clean install -Pintegration

###Swagger-Codegen-Maven-Plugin

Plugin for Maven that generates pieces of code specified in the 'swagger.yaml' (maybe created with Swagger-Editor).

```xml
    <plugin>
        <groupId>org.zalando.maven.plugins</groupId>
        <artifactId>swagger-codegen-maven-plugin</artifactId>
        <version>${version}</version>
        <configuration>
            <apiFile>${project.basedir}/src/main/resources/petstore.json</apiFile>
            <language>jaxrsinterfaces</language>
            <apiPackage>org.zalando.project.api</apiPackage>
            <modelPackage>org.zalando.project.model</modelPackage>
        </configuration>
        <!-- Bundle custom templates into jars and add as dependency -->
        <dependencies>
            <dependency>
                <groupId>org.zalando.stups</groupId>
                <artifactId>swagger-codegen-template-jaxrs-interfaces</artifactId>
                <version>${version}</version>
            </dependency>
        </dependencies>
    </plugin>
```

Examples ([spring-boot-jaxrs](https://github.com/zalando-stups/swagger-codegen-tooling/tree/master/swagger-codegen-maven-plugin/src/it/spring-boot-jersey)) can be found at the integration-test section.


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

