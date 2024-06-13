package org.tcloud.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.tcloud.config.ObjectMapperConfig;
import lombok.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public final class ApiConfig {
    private ApiConfig() {
        throw new IllegalStateException("This class can't be initialized");
    }

    private static final String DELIMITER = "/";
    private static final String BASE_URL = "baseUrl";
    private static final String API = "api";
    private static final String METHOD = "method";

    public static JsonNode getApiConfigNode() throws IOException {
        InputStream inputStream = ApiConfig.class.getResourceAsStream("/api-config.json");
        return ObjectMapperConfig.getObjectMapper().readTree(inputStream);
    }

    public static URI getURI(@NonNull final String access,
                             @NonNull final String version,
                             @NonNull final String endpoint) throws IOException {
        final JsonNode apiConfigNode = getApiConfigNode();
        final String baseUrl = apiConfigNode.get(BASE_URL).asText();
        final String url = String.join(DELIMITER, baseUrl, API, access, version, endpoint);
        System.out.println("URL: " + url);
        return URI.create(url);
    }

    public static String getMethod(@NonNull final String endpoint) throws IOException {
        final JsonNode apiConfigNode = getApiConfigNode();
        final JsonNode apiNode = apiConfigNode.get(API);
        final JsonNode endpointNode = apiNode.get(endpoint);
        return endpointNode.get(METHOD).asText();
    }
}
