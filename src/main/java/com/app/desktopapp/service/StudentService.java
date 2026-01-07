package com.app.desktopapp.service;

import com.app.desktopapp.dto.*;
import com.app.desktopapp.model.Student;
import com.app.desktopapp.utils.AuthContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.DeserializationFeature;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StudentService {

    private static final String BASE = "http://localhost:8080/api/users";
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.registerModule(new JavaTimeModule()); // hỗ trợ LocalDate
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
    }

    public static ApiResponse getStudents(int page) {
        return callApi(BASE + "/students/paged?page=" + page + "&size=20");
    }

    public static ApiResponse searchStudents(String keyword, int page) {
        String q = keyword == null ? "" : URLEncoder.encode(keyword, StandardCharsets.UTF_8);
        return callApi(BASE + "/search?studentCode=" + q + "&name=" + q + "&grade=" + q + "&page=" + page + "&size=20");
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
                conn.setRequestProperty("Authorization", "Bearer " + token);
            }

            conn.setRequestProperty("Content-Type", "application/json");
            conn.connect();

            int status = conn.getResponseCode();
            InputStream is = (status >= 400) ? conn.getErrorStream() : conn.getInputStream();
            if (is == null) return new ApiResponse(List.of(), 1);

            String body = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))
                    .lines().reduce("", (a, b) -> a + b);

            if (status >= 400) {
                System.err.println("API error " + status + " -> " + body);
                return new ApiResponse(List.of(), 1);
            }

            ApiWrapper<Student> wrapper = mapper.readValue(body,
                    mapper.getTypeFactory().constructParametricType(ApiWrapper.class, Student.class));

            List<Student> students = wrapper.getData().getContent();
            int totalPages = wrapper.getData().getTotalPages();
            return new ApiResponse(students, totalPages);

        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse(List.of(), 1);
        } finally {
            if (conn != null) conn.disconnect();
        }
    }

    public static ActionResponse createStudent(CreateStudentRequest req) {
        try {
            URL url = new URL(BASE + "/create");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            String json = mapper.writeValueAsString(req);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }

            int status = conn.getResponseCode();

            if (status == 200 || status == 201) {
                return new ActionResponse(true, "Thêm sinh viên thành công");
            } else {
                String errorBody = readStream(conn.getErrorStream());
                String message = extractMessage(errorBody);
                return new ActionResponse(false, message);
            }

        } catch (Exception e) {
            return new ActionResponse(false, e.getMessage());
        }
    }


    private static String readStream(InputStream is) throws IOException {
        if (is == null) return "Unknown error";
        return new BufferedReader(new InputStreamReader(is))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    private static String extractMessage(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(json);
            return node.get("message").asText();
        } catch (Exception e) {
            return json;
        }
    }



    public static boolean updateStudent(String studentCode, Map<String, Object> updateData) {
        return sendJsonRequest(BASE + "/student/" + studentCode, "PUT", updateData);
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
                conn.setRequestProperty("Authorization", "Bearer " + token);
            }

            int status = conn.getResponseCode();
            return status == 200 || status == 204;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) conn.disconnect();
        }
    }

    private static boolean sendJsonRequest(String urlStr, String method, Object data) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setDoOutput(true);

            String token = AuthContext.getToken();
            if (token != null && !token.isBlank()) {
                conn.setRequestProperty("Authorization", "Bearer " + token);
            }

            conn.setRequestProperty("Content-Type", "application/json");
            if (data != null) {
                String json = mapper.writeValueAsString(data);
                conn.getOutputStream().write(json.getBytes(StandardCharsets.UTF_8));
            }

            int status = conn.getResponseCode();
            return status >= 200 && status < 300;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) conn.disconnect();
        }
    }

    public static List<LecturerDTO> getAllLecturer() {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(BASE + "/staffs");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            String token = AuthContext.getToken();
            if (token != null && !token.isBlank()) {
                conn.setRequestProperty("Authorization", "Bearer " + token);
            }

            conn.setRequestProperty("Content-Type", "application/json");

            InputStream is = conn.getInputStream();
            String json = new BufferedReader(new InputStreamReader(is)).lines().reduce("", (a, b) -> a + b);

            ApiListWrapper<LecturerDTO> wrapper = mapper.readValue(json,
                    mapper.getTypeFactory().constructParametricType(ApiListWrapper.class, LecturerDTO.class));

            return wrapper.getData();

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        } finally {
            if (conn != null) conn.disconnect();
        }
    }
}
