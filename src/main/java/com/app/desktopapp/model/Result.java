package com.app.desktopapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Result {

    // ===== Student =====
    private String studentCode;

    @JsonProperty("fullName")
    private String studentName;

    // ===== Course =====
    private String courseCode;

    private String courseName;

    // ===== Result =====
    private Double score;
    private String grade;
    private String semester;
    private String timeStudied;

    public Result() {}

    // ===== getters =====
    public String getStudentCode() { return studentCode; }
    public String getStudentName() { return studentName; }
    public String getCourseCode() { return courseCode; }
    public String getCourseName() { return courseName; }
    public Double getScore() { return score; }
    public String getGrade() { return grade; }
    public String getSemester() { return semester; }
    public String getTimeStudied() { return timeStudied; }

    // ===== setters =====
    public void setStudentCode(String studentCode) { this.studentCode = studentCode; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public void setScore(Double score) { this.score = score; }
    public void setGrade(String grade) { this.grade = grade; }
    public void setSemester(String semester) { this.semester = semester; }
    public void setTimeStudied(String timeStudied) { this.timeStudied = timeStudied; }
}
