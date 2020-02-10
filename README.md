# DependencyTrack Maven Publisher

## Introduction
This plugin allows you to send BOM files directly to DependencyTrack. It saves you from using `curl` and handle everything using maven.

## Usage:

* Create a dedicated profile : `dependencytrack`
  * Use `CycloneDX or SPDX` to generate BOM
  * Use `dependencytrack-mvn-publisher` to publish generated BOM
* Run : `mvn clean install -Pdependencytrack`

```xml
        <profile>
            <id>dependencytrack</id>
            <build>
                <plugins>
                    <plugin>
                        <groupId>org.cyclonedx</groupId>
                        <artifactId>cyclonedx-maven-plugin</artifactId>
                        <version>1.6.3</version>
                        <executions>
                            <execution>
                                <goals>
                                    <goal>makeAggregateBom</goal>
                                </goals>
                            </execution>
                        </executions>
                    </plugin>

                    <plugin>
                        <groupId>io.github.houssemba</groupId>
                        <artifactId>dependencytrack-mvn-publisher</artifactId>
                        <version>0.0.3</version>
                        <configuration>
                            <uri>https://dependencytrack/api/v1/bom</uri>
                            <apiKey>${env.DEPENDENCYTRACK_API_KEY}</apiKey>
                            <bomPath>target/bom.xml</bomPath>
                            <projectUuid>CHANGEME</projectUuid>
                        </configuration>
                        <executions>
                            <execution>
                                <goals>
                                    <goal>publish</goal>
                                </goals>
                            </execution>
                        </executions>
                    </plugin>
                </plugins>
            </build>
        </profile>
```

## Options

| Option               | Required             |                                                                                                                                                        |
|----------------------|----------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------|
| uri                  | yes                  | The URI of the DependencyTrack instance. (https://dependencytrack/api/v1/bom                                                                           |
| apiKey               | yes                  | DependencyTrack API Key                                                                                                                                |
| bomPath              | yes                  | Path to the generated BOM                                                                                                                              |
| projectUuid          | yes                  | Project Uuid                                                                                                                                           |
| userAgent            | no                   | A custom userAgent                                                                                                                                     |