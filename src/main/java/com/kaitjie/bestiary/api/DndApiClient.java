package com.kaitjie.bestiary.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaitjie.bestiary.model.Monster;
import com.kaitjie.bestiary.model.MonsterListResponse;
import com.kaitjie.bestiary.model.MonsterSummary;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class DndApiClient {

    private static final String API_URL = "https://www.dnd5eapi.co/api/2014";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public DndApiClient() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public List<MonsterSummary> fetchMonsterList() throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "/monsters"))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode()!=200){
            throw new RuntimeException("Response error occurred");
        }

        MonsterListResponse monsters = objectMapper.readValue(response.body(), MonsterListResponse.class);

        return monsters.getResults();
    }

    public Monster fetchMonsterDetail(String index) throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL+"/monsters/"+index))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode()!=200){
            throw new RuntimeException("Response error occurred");
        }

        return objectMapper.readValue(response.body(), Monster.class);
    }
}
