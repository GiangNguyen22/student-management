package com.app.desktopapp.service;

import com.app.desktopapp.dto.ApiWrapper;
import com.app.desktopapp.model.Student;
import com.app.desktopapp.utils.AuthContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class StudentService {

    private static final String BASE =
            "http://localhost:8080/api/users";

    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.registerModule(new JavaTimeModule());
    }

    public static ApiResponse getStudents(int page) {
        return callApi(
                BASE + "/students/paged?page=" + page + "&size=20"
        );
    }

    public static ApiResponse searchStudents(
            String keyword, int page) {

        String q = "";
        if (keyword != null) {
            q = URLEncoder.encode(keyword, StandardCharsets.UTF_8);
        }

        return callApi(
                BASE + "/search?keyword=" + q
                        + "&page=" + page + "&size=20"
        );
    }

    private static ApiResponse callApi(String urlStr) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            String token = AuthContext.getToken();
            if (token != null && !token.isBlank()) {
                conn.setRequestProperty("Authorization",
                        "Bearer " + token);
            }

            conn.setRequestProperty("Content-Type", "application/json");
            conn.connect();

            int status = conn.getResponseCode();

            InputStream is = (status >= 400) ? conn.getErrorStream() : conn.getInputStream();
            if (is == null) {
                // No stream available, return empty result
                System.err.println("No response stream, HTTP status: " + status);
                return new ApiResponse(List.of(), 1);
            }

            // Read response (success or error). Error body will be logged.
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                String body = sb.toString();

                if (status >= 400) {
                    System.err.println("API error " + status + " for URL: " + urlStr + " -> " + body);
                    return new ApiResponse(List.of(), 1);
                }

                // Parse JSON response
                ApiWrapper<Student> responseWrapper =
                        mapper.readValue(body,
                                mapper.getTypeFactory()
                                        .constructParametricType(ApiWrapper.class, Student.class));

                List<Student> students = responseWrapper.getData().getContent();
                int totalPages = responseWrapper.getData().getTotalPages();


                return new ApiResponse(students, totalPages);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse(List.of(), 1);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}
