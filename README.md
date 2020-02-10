### Usage:
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

`mvn clean install -Pdependencytrack`