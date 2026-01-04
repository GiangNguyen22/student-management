package com.app.desktopapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Student {

    private String studentCode;
    @JsonProperty("fullName")
    private String name;
    private String email;
    @JsonProperty("majorName")
    private String major;
    private LocalDate dob;
    private String startYear;
    private String gender;

    public Student() {
    }

    public Student(String studentCode, String name, String email, String major, LocalDate dob, String startYear, String gender) {
        this.studentCode = studentCode;
        this.name = name;
        this.email = email;
        this.major = major;
        this.dob = dob;
        this.startYear = startYear;
        this.gender = gender;
    }

    public String getStudentCode() { return studentCode; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getMajor() { return major; }
    public LocalDate getDob() { return dob; }
    public String getStartYear() { return startYear; }
    public String getGender() { return gender; }

    public void setStudentCode(String studentCode) { this.studentCode = studentCode; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setMajor(String major) { this.major = major; }
    public void setDob(LocalDate dob) { this.dob = dob; }
    public void setStartYear(String startYear) { this.startYear = startYear; }
    public void setGender(String gender) { this.gender = gender; }
}

