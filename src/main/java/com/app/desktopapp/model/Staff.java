package com.app.desktopapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Staff {

    private Long userId;
    private String username;   // 🔥 PHẢI LÀ username
    private String name;
    private String majorName;
    private String position;
    private String staffCode;
    private String gender;

    public Staff() {}

    /* ================= GETTER ================= */

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getMajorName() {
        return majorName;
    }

    public String getPosition() {
        return position;
    }

    public String getStaffCode() {
        return staffCode;
    }

    public String getGender() {
        return gender;
    }

    /* ================= SETTER ================= */

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @JsonProperty("username") // 🔥 đảm bảo map đúng
    public void setUsername(String username) {
        this.username = username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMajorName(String majorName) {
        this.majorName = majorName;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return staffCode + " - " + name;
    }
}
