package com.app.desktopapp.model;

import java.time.LocalDate;

public class Student {

    private String studentCode;
    private String name;
    private String email;
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
}

