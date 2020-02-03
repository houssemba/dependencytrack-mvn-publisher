package com.github.houssemba;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.commons.io.FileUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;
import java.util.Base64;


@Mojo(
        name = "publish",
        defaultPhase = LifecyclePhase.VERIFY,
        requiresOnline = true
)
public class DependencyTrackPublisher extends AbstractMojo {
    @Parameter(property = "uri", required = true)
    private String uri;

    @Parameter(property = "projectUuid", required = true)
    private String projectUuid;

    @Parameter(property = "apiKey", required = true)
    private String apiKey;

    @Parameter(property = "bomPath", required = true, defaultValue = "target/bom.xml")
    private File bomPath;

    @Parameter(property = "userAgent", required = false)
    private String userAgent;

    public void execute() throws MojoExecutionException, MojoFailureException {
        Log log = getLog();

        log.debug("uri : " + uri);
        log.debug("projectUuid : " + projectUuid);
        log.debug("bomPath : " + bomPath.getAbsolutePath());

        CloseableHttpClient httpClient = HttpClients.createDefault();

        try {
            HttpPut request = new HttpPut(uri);
            request.addHeader("X-API-Key", apiKey);
            request.addHeader("Content-Type", "application/json");

            if (userAgent != null) {
                request.addHeader("User-Agent", userAgent);
            }

            String json = "{" +
                    "\"project\":\"" + projectUuid + "\"," +
                    "\"bom\":\"" + Base64.getEncoder().encodeToString(FileUtils.readFileToByteArray(bomPath)) + "\"" +
                    "}";
            request.setEntity(new StringEntity(json));

            CloseableHttpResponse response = httpClient.execute(request);

            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode != 200) {
                log.error("Unexpected error while sending dependency list to DependencyTrack");
                return;
            }

            log.info("Dependency list has been successfully sent.");
        } catch (IOException e) {
            log.error("Unexpected error", e);
        }
    }
}
