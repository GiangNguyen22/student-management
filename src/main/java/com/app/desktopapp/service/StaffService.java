package com.app.desktopapp.service;

import com.app.desktopapp.model.Staff;
import com.app.desktopapp.utils.AuthContext;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class StaffService {

    private static final String BASE = "http://localhost:8080/api/users/staff";
    private static final ObjectMapper mapper = new ObjectMapper();

    /* ================= GET ALL STAFF ================= */
    public static ApiResponse getStaffs() {
        try {
            HttpURLConnection conn = openConn(BASE, "GET");

            String body = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))
                    .lines().reduce("", String::concat);

            List<Staff> staffs = mapper.readValue(
                    body,
                    mapper.getTypeFactory().constructCollectionType(List.class, Staff.class));

            return new ApiResponse(staffs, 1);

        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse(List.of(), 1);
        }
    }

    /* ================= CREATE STAFF ================= */
    public static boolean createStaff(Staff staff) {
        try {
            HttpURLConnection conn = openConn(BASE, "POST");
            conn.setDoOutput(true);
            String json = mapper.writeValueAsString(staff);
            conn.getOutputStream().write(json.getBytes(StandardCharsets.UTF_8));
            int status = conn.getResponseCode();
            return status >= 200 && status < 300;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /* ================= UPDATE STAFF ================= */
    public static boolean updateStaff(Staff staff) {
        try {
            HttpURLConnection conn = openConn(BASE + "/" + staff.getUsername(), "PUT");
            conn.setDoOutput(true);
            String json = mapper.writeValueAsString(staff);
            conn.getOutputStream().write(json.getBytes(StandardCharsets.UTF_8));
            int status = conn.getResponseCode();
            return status >= 200 && status < 300;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /* ================= HANDLE EDIT ================= */
    public static boolean handleEdit(String username, Staff staff) {
        staff.setUsername(username);
        return updateStaff(staff);
    }

    /* ================= DELETE STAFF ================= */
   public static boolean deleteStaff(String username) {
    try {
        String urlStr = BASE + "/" + username;
        System.out.println("Deleting staff at URL: " + urlStr);

        HttpURLConnection conn = openConn(urlStr, "DELETE");
        int status = conn.getResponseCode();
        System.out.println("HTTP status: " + status);

        InputStream is = status >= 400 ? conn.getErrorStream() : conn.getInputStream();
        if (is != null) {
            String body = new BufferedReader(new InputStreamReader(is))
                    .lines().reduce("", (a,b) -> a+b);
            System.out.println("Response body: " + body);
        }

        return status == 200 || status == 204;

    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}

    /* ================= COMMON ================= */
    private static HttpURLConnection openConn(String urlStr, String method) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
        conn.setRequestMethod(method);
        conn.setRequestProperty("Content-Type", "application/json");

        String token = AuthContext.getToken();
        if (token != null && !token.isBlank()) {
            conn.setRequestProperty("Authorization", "Bearer " + token);
        }
        return conn;
    }
}
