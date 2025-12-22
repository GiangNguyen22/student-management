package com.app.desktopapp.service;

import com.app.desktopapp.dto.ApiWrapper;
import com.app.desktopapp.model.Result;
import com.app.desktopapp.utils.AuthContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ResultService {

    private static final String BASE = "http://localhost:8080/api/results";
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.registerModule(new JavaTimeModule());
    }

    public static List<Result> getAllResults() {
        ApiWrapper<Result> wrapper = callApi(BASE, "GET", null);
        return wrapper != null && wrapper.getData() != null ? wrapper.getData().getContent() : List.of();
    }

    public static Result createResult(Result result) {
        ApiWrapper<Result> wrapper = callApi(BASE + "/create", "POST", result);
        return wrapper != null && wrapper.getData() != null ? wrapper.getData().getContent().get(0) : null;
    }

    public static Result updateResult(Result result) {
        ApiWrapper<Result> wrapper = callApi(BASE + "/update", "PUT", result);
        return wrapper != null && wrapper.getData() != null ? wrapper.getData().getContent().get(0) : null;
    }

    public static boolean deleteResult(String studentCode, String course) {
        try {
            String urlStr = BASE + "/delete?studentCode=" + studentCode + "&course=" + course;
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            String token = AuthContext.getToken();
            if (token != null && !token.isBlank()) {
                conn.setRequestProperty("Authorization", "Bearer " + token);
            }

            int status = conn.getResponseCode();
            return status >= 200 && status < 300;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static ApiWrapper<Result> callApi(String urlStr, String method, Result body) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestProperty("Content-Type", "application/json");

            String token = AuthContext.getToken();
            if (token != null && !token.isBlank()) {
                conn.setRequestProperty("Authorization", "Bearer " + token);
            }

            if (body != null) {
                conn.setDoOutput(true);
                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = mapper.writeValueAsBytes(body);
                    os.write(input, 0, input.length);
                }
            }

            InputStream is = conn.getResponseCode() >= 400 ? conn.getErrorStream() : conn.getInputStream();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) sb.append(line);

                return mapper.readValue(sb.toString(),
                        mapper.getTypeFactory().constructParametricType(ApiWrapper.class, Result.class));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
