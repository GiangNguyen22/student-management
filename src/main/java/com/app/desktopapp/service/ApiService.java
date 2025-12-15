package com.app.desktopapp.service;

import com.app.desktopapp.model.Student;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ApiService {

    private static ApiService instance;
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private final String baseUrl = "http://localhost:8080/api";

    private ApiService() { }

    public static ApiService getInstance() {
        if (instance == null) instance = new ApiService();
        return instance;
    }

    public boolean login(String username, String password) {
        try {
            URL url = new URL(baseUrl + "/auth/login");
            HttpURLConnection conn =
                    (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String json = """
                {
                  "username":"%s",
                  "password":"%s"
                }
            """.formatted(username, password);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes());
            }

            return conn.getResponseCode() == 200;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Student> getAllStudents() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(baseUrl + "/students"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Student[] students = mapper.readValue(response.body(), Student[].class);
            return Arrays.asList(students);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}

