package com.app.desktopapp.utils;

public class AuthContext {
    private static String token;

    public static void setToken(String t) {
        token = t;
    }

    public static String getToken() {
        return token;
    }
}
