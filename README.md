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
                        <groupId>com.github.houssemba</groupId>
                        <artifactId>dependencytrack-mvn-publisher</artifactId>
                        <version>0.0.1-SNAPSHOT</version>
                        <configuration>
                            <uri>https://dependencytrack.build.malt.tech/api/v1/bom</uri>
                            <apiKey>${env.DEPENDENCYTRACK_API_KEY}</apiKey>
                            <bomPath>target/bom.xml</bomPath>
                            <projectUuid>098831c9-a901-4929-a786-522297c43f10</projectUuid>
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