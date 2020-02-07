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

import okhttp3.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

public class DependencyTrackClient {
    private String uri;
    private String projectUuid;
    private String apiKey;
    private String bomPath;
    private String userAgent;

    private DependencyTrackClient() {
    }

    public static class ClientBuilder {
        private String uri;
        private String projectUuid;
        private String apiKey;
        private String bomPath;
        private String userAgent;

        public ClientBuilder uri(String uri) {
            this.uri = uri;

            return this;
        }

        public ClientBuilder projectUuid(String projectUuid) {
            this.projectUuid = projectUuid;

            return this;
        }

        public ClientBuilder apiKey(String apikey) {
            this.apiKey = apikey;

            return this;
        }

        public ClientBuilder bomPath(String bomPath) {
            this.bomPath = bomPath;

            return this;
        }

        public ClientBuilder userAgent(String userAgent) {
            this.userAgent = userAgent;

            return this;
        }

        public DependencyTrackClient build() {
            DependencyTrackClient client = new DependencyTrackClient();

            client.uri = this.uri;
            client.projectUuid = this.projectUuid;
            client.apiKey = this.apiKey;
            client.bomPath = this.bomPath;
            client.userAgent = this.userAgent;

            return client;
        }
    }

    public void send() throws IOException {
        String json = "{" +
                "\"project\":\"" + projectUuid + "\"," +
                "\"bom\":\"" + Base64.getEncoder().encodeToString(FileUtils.readFileToByteArray(new File(bomPath))) + "\"" +
                "}";

        OkHttpClient client = new OkHttpClient();
        Request.Builder builder = new Request.Builder()
                .url(uri)
                .method("PUT", RequestBody.create(MediaType.parse("application/json"), json))
                .addHeader("X-API-Key", apiKey);

        if (userAgent != null) {
            builder.addHeader("User-Agent", userAgent);
        }

        Request request = builder.build();

        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) {
            throw new PublishFailedException("Report publish failed");
        }
    }
}
