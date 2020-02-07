package com.github.houssemba.dependencytrack.mvn.publisher;

/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2020 Houssem BELHADJ AHMED
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;


/**
 * DependencyTrack Bom Publisher
 *
 * It allow to publish bom file to the DependencyTrack instance
 * <strong>Require</strong> online connection
 */
@Mojo(
        name = "publish",
        defaultPhase = LifecyclePhase.VERIFY,
        requiresOnline = true
)
public class DependencyTrackPublisher extends AbstractMojo {

    /**
     * The URI of the DependencyTrack instance
     */
    @Parameter(property = "uri", required = true)
    private String uri;

    /**
     * Project uuid
     */
    @Parameter(property = "projectUuid", required = true)
    private String projectUuid;

    /**
     * DependencyTrack API KEY
     */
    @Parameter(property = "apiKey", required = true)
    private String apiKey;


    /**
     * Path to the generated bom file
     * Support CycloneDX and SPDX BOMs
     */
    @Parameter(property = "bomPath", required = true, defaultValue = "target/bom.xml")
    private File bomPath;

    /**
     * Custom User-Agent
     */
    @Parameter(property = "userAgent", required = false)
    private String userAgent;

    public void execute() throws MojoExecutionException, MojoFailureException {
        Log log = getLog();

        log.debug("URI : " + uri);
        log.debug("projectUuid : " + projectUuid);
        log.debug("bomPath : " + bomPath.getAbsolutePath());
        log.debug("bomFile exists : " + bomPath.exists());


        DependencyTrackClient dependencyTrackClient = new DependencyTrackClient.ClientBuilder()
                .uri(uri)
                .projectUuid(projectUuid)
                .bomPath(bomPath.getAbsolutePath())
                .userAgent(userAgent)
                .apiKey(apiKey)
                .build();

        try {
            dependencyTrackClient.send();
        } catch (IOException e) {
            log.error("Unexpected error", e);
        };

        log.info("Dependency list has been successfully sent.");
    }
}
