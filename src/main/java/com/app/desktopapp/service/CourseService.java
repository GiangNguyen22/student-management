package com.app.desktopapp.service;

import com.app.desktopapp.dto.ApiWrapper;
import com.app.desktopapp.dto.CourseRequestDTO;
import com.app.desktopapp.model.Course;
import com.app.desktopapp.utils.AuthContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class CourseService {

    private static final String BASE =
            "http://localhost:8080/courses";

    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.registerModule(new JavaTimeModule());
    }

    /** Lấy danh sách khóa học (phân trang) */
    public static ApiResponse getCourses(int page) {
        return callApi(
                BASE + "?page=" + page + "&size=20"
        );
    }

    public static ApiResponse searchCourses(String keyword, int page) {

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

            InputStream is = (status >= 400)
                    ? conn.getErrorStream()
                    : conn.getInputStream();

            if (is == null) {
                System.err.println("No response stream, HTTP status: " + status);
                return new ApiResponse(List.of(), 1);
            }

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(is, StandardCharsets.UTF_8))) {

                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                String body = sb.toString();

                if (status >= 400) {
                    System.err.println("API error " + status + " -> " + body);
                    return new ApiResponse(List.of(), 1);
                }

                ApiWrapper<Course> responseWrapper =
                        mapper.readValue(
                                body,
                                mapper.getTypeFactory()
                                        .constructParametricType(ApiWrapper.class, Course.class)
                        );

                List<Course> courses =
                        responseWrapper.getData().getContent();
                int totalPages =
                        responseWrapper.getData().getTotalPages();

                return new ApiResponse(courses, totalPages);
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

    /** Thêm khóa học */
    public static boolean createCourse(CourseRequestDTO request) {

        HttpURLConnection conn = null;
        try {
            URL url = new URL(BASE + "/create");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            String token = AuthContext.getToken();
            if (token != null && !token.isBlank()) {
                conn.setRequestProperty(
                        "Authorization",
                        "Bearer " + token
                );
            }

            conn.setRequestProperty("Content-Type", "application/json");

            // convert request -> JSON
            String json = mapper.writeValueAsString(request);
            conn.getOutputStream()
                    .write(json.getBytes(StandardCharsets.UTF_8));

            int status = conn.getResponseCode();

            if (status == 200 || status == 201) {
                return true;
            }

            // log lỗi backend nếu có
            InputStream err = conn.getErrorStream();
            if (err != null) {
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(err, StandardCharsets.UTF_8))) {
                    System.err.println(br.readLine());
                }
            }

            return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public static boolean updateCourse(CourseRequestDTO course) {

        HttpURLConnection conn = null;
        try {
            // 1️⃣ Build URL
            URL url = new URL(
                    BASE + "/" + course.getCourseCode() + "/update"
            );

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            // 2️⃣ Header Authorization
            String token = AuthContext.getToken();
            if (token != null && !token.isBlank()) {
                conn.setRequestProperty(
                        "Authorization", "Bearer " + token
                );
            }

            conn.setRequestProperty(
                    "Content-Type", "application/json"
            );

            // 3️⃣ Build request body (map DTO → Request)
            CourseRequestDTO request = new CourseRequestDTO();
            request.setCourseName(course.getCourseName());
            request.setDescription(course.getDescription());
            request.setStaffCode(course.getStaffCode());
            request.setStartDate(course.getStartDate());
            request.setEndDate(course.getEndDate());

            String json = mapper.writeValueAsString(request);

            // 4️⃣ Send body
            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
                os.flush();
            }

            // 5️⃣ Handle response
            int status = conn.getResponseCode();

            if (status >= 200 && status < 300) {
                return true;
            } else {
                InputStream es = conn.getErrorStream();
                if (es != null) {
                    String err = new BufferedReader(
                            new InputStreamReader(es)
                    ).lines().reduce("", (a, b) -> a + b);
                    System.err.println("Update course error: " + err);
                }
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) conn.disconnect();
        }
    }

    public static boolean deleteCourse(String courseCode) {

        HttpURLConnection conn = null;
        try {
            URL url = new URL(
                    BASE + "/" + courseCode + "/delete"
            );

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

            conn.setRequestProperty(
                    "Content-Type", "application/json"
            );

            int status = conn.getResponseCode();

            if (status >= 200 && status < 300) {
                return true;
            } else {
                InputStream es = conn.getErrorStream();
                if (es != null) {
                    String err = new BufferedReader(
                            new InputStreamReader(es)
                    ).lines().reduce("", (a, b) -> a + b);
                    System.err.println("Delete course error: " + err);
                }
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) conn.disconnect();
        }
    }



}
