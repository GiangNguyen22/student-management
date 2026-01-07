package com.app.desktopapp.dto;

public class ActionResponse {
    private boolean success;
    private String message;

    public ActionResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
