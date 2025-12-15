package com.app.desktopapp.service;

import com.app.desktopapp.model.Student;

import java.util.List;

public class ApiResponse {
    public List<Student> students;
    public int totalPages;

    public ApiResponse(List<Student> students, int totalPages) {
        this.students = students;
        this.totalPages = totalPages;
    }
}
