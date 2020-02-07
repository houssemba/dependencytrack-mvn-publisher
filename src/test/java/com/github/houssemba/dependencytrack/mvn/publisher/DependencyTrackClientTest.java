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

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class DependencyTrackClientTest {
    private DependencyTrackClient client;
    private MockWebServer webServer;

    @BeforeEach
    void setUp() throws IOException {
        webServer = new MockWebServer();
        webServer.start();
    }

    @AfterEach
    void tearDown() throws IOException {
        webServer.shutdown();
    }

    @Test
    void it_should_send_publish_bom_file() throws IOException, InterruptedException, NoSuchAlgorithmException {
        webServer.enqueue(new MockResponse().setResponseCode(200));
        String apiKey = UUID.randomUUID().toString();

        client = new DependencyTrackClient.ClientBuilder()
                .uri(webServer.url("/api/v1/bom").toString())
                .apiKey(apiKey)
                .projectUuid("projectUuid")
                .bomPath(getClass().getClassLoader().getResource("bom.xml").getPath())
                .build();

        client.send();

        RecordedRequest recordedRequest = webServer.takeRequest();
        assertThat(recordedRequest.getPath()).isEqualTo("/api/v1/bom");
        assertThat(recordedRequest.getHeader("X-API-Key")).isEqualTo(apiKey);
        assertThat(recordedRequest.getBody().readUtf8()).isNotEmpty();

    }
}
