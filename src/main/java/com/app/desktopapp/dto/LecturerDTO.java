package com.app.desktopapp.dto;

public class LecturerDTO {
    private String staffCode;
    private String name;

    @Override
    public String toString() {
        return name + " (" + staffCode + ")";
    }

    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
