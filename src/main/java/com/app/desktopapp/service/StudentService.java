package com.app.desktopapp.service;

import com.app.desktopapp.dto.ApiListWrapper;
import com.app.desktopapp.dto.ApiWrapper;
import com.app.desktopapp.dto.CreateStudentRequest;
import com.app.desktopapp.dto.LecturerDTO;
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
import java.util.Map;

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

        String q = keyword == null
                ? ""
                : URLEncoder.encode(keyword, StandardCharsets.UTF_8);

        return callApi(
                BASE + "/search"
                        + "?studentCode=" + q
                        + "&name=" + q
                        + "&grade=" + q
                        + "&page=" + page
                        + "&size=20"
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

    public static boolean createStudent(CreateStudentRequest req) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(BASE + "/create");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setDoOutput(true);

            String token = AuthContext.getToken();
            if (token != null && !token.isBlank()) {
                conn.setRequestProperty("Authorization",
                        "Bearer " + token);
            }

            conn.setRequestProperty("Content-Type", "application/json");

            // Convert object -> JSON
            String json = mapper.writeValueAsString(req);
            conn.getOutputStream()
                    .write(json.getBytes(StandardCharsets.UTF_8));

            int status = conn.getResponseCode();

            if (status == 200 || status == 201) {
                return true;
            }

            InputStream err = conn.getErrorStream();
            if (err != null) {
                System.err.println(
                        new String(err.readAllBytes(), StandardCharsets.UTF_8)
                );
            }
            return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) conn.disconnect();
        }
    }

    public static boolean updateStudent(
            String studentCode,
            Map<String, Object> updateData) {

        HttpURLConnection conn = null;
        try {
            URL url = new URL(BASE + "/student/" + studentCode);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            String token = AuthContext.getToken();
            if (token != null && !token.isBlank()) {
                conn.setRequestProperty("Authorization",
                        "Bearer " + token);
            }

            conn.setRequestProperty("Content-Type", "application/json");

            String json = mapper.writeValueAsString(updateData);
            conn.getOutputStream()
                    .write(json.getBytes(StandardCharsets.UTF_8));

            int status = conn.getResponseCode();

            return status == 200;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) conn.disconnect();
        }
    }


    public static boolean deleteStudent(String studentCode) {

        HttpURLConnection conn = null;
        try {
            URL url = new URL(BASE + "/student/" + studentCode);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            String token = AuthContext.getToken();
            if (token != null && !token.isBlank()) {
                conn.setRequestProperty(
                        "Authorization", "Bearer " + token
                );
            }

            conn.connect();

            int status = conn.getResponseCode();
            return status == 200 || status == 204;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) conn.disconnect();
        }
    }

    public static List<LecturerDTO> getAllLecturer(){
        HttpURLConnection conn = null;
        try {
            URL url = new URL(BASE + "/staffs");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            String token = AuthContext.getToken();
            if (token != null && !token.isBlank()) {
                conn.setRequestProperty(
                        "Authorization", "Bearer " + token
                );
            }

            conn.setRequestProperty(
                    "Content-Type", "application/json"
            );

            InputStream is = conn.getInputStream();
            String json = new BufferedReader(
                    new InputStreamReader(is)
            ).lines().reduce("", (a, b) -> a + b);

            ApiListWrapper<LecturerDTO> wrapper =
                    mapper.readValue(
                            json,
                            mapper.getTypeFactory()
                                    .constructParametricType(
                                            ApiListWrapper.class,
                                            LecturerDTO.class
                                    )
                    );

            return wrapper.getData();

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        } finally {
            if (conn != null) conn.disconnect();
        }
    }

}
