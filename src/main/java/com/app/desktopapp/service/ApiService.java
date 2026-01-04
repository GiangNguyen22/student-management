package com.app.desktopapp.service;

import com.app.desktopapp.model.Student;
import com.app.desktopapp.utils.AuthContext;

import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
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

    public boolean register(String username, String password) {
        try {
            URL url = new URL(baseUrl + "/auth/register");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String json = """
                {
                  "username":"%s",
                  "password":"%s",
                  "role" : "STUDENT"
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

    public boolean registerAdmin(String username, String password, String email, String name) {
        try {
            URL url = new URL(baseUrl + "/auth/register");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String json = """
                {
                  "username":"%s",
                  "password":"%s",
                  "email":"%s",
                  "name":"%s",
                  "role":"ADMIN"
                }
            """.formatted(username, password, email, name);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes());
            }

            return conn.getResponseCode() == 200;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean registerStaff(String username, String password, String email, String name, String staffCode) {
        try {
            URL url = new URL(baseUrl + "/auth/register");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String json = """
                {
                  "username":"%s",
                  "password":"%s",
                  "email":"%s",
                  "name":"%s",
                  "staffCode":"%s",
                  "role":"STAFF"
                }
            """.formatted(username, password, email, name, staffCode);

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
                    .uri(new URI(baseUrl + "/users/students"))
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

    public List<Map<String, Object>> getAllStaff() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(baseUrl + "/users/staff"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Map<String, Object>[] staff = mapper.readValue(response.body(), Map[].class);
            return Arrays.asList(staff);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public boolean createResult(String studentCode, String courseCode, double score) {
        try {
            URL url = new URL(baseUrl + "/results/create");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String json = """
                {
                  "studentCode":"%s",
                  "courseCode":"%s",
                  "score":%f
                }
            """.formatted(studentCode, courseCode, score);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes());
            }

            return conn.getResponseCode() == 200;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateResult(String studentCode, String courseCode, double score) {
        try {
            URL url = new URL(baseUrl + "/results/update");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String json = """
                {
                  "studentCode":"%s",
                  "courseCode":"%s",
                  "score":%f
                }
            """.formatted(studentCode, courseCode, score);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes());
            }

            return conn.getResponseCode() == 200;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteResult(String studentCode, String courseCode) {
        try {
            URL url = new URL(baseUrl + "/results/delete?studentCode=" + studentCode + "&courseCode=" + courseCode);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Content-Type", "application/json");

            return conn.getResponseCode() == 200;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

