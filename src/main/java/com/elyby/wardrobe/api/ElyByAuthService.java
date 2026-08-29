package com.elyby.wardrobe.api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class ElyByAuthService {
    private static final String ELY_BY_API = "https://account.ely.by/api/users/";
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

    public static CompletableFuture<String> fetchUserTextures(String username) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ELY_BY_API + username + "/skins"))
                .header("Accept", "application/json")
                .GET()
                .build();

        return HTTP_CLIENT.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body);
    }
}
