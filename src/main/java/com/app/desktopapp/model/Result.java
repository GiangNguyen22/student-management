package com.app.desktopapp.model;

public class Result {
    private String studentCode;
    private String studentName;
    private String course;
    private double score;

    public Result() {}
    public Result(String studentCode, String studentName, String course, double score) {
        this.studentCode = studentCode;
        this.studentName = studentName;
        this.course = course;
        this.score = score;
    }

    public String getStudentCode() { return studentCode; }
    public String getStudentName() { return studentName; }
    public String getCourse() { return course; }
    public double getScore() { return score; }

    public void setStudentCode(String studentCode) { this.studentCode = studentCode; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public void setCourse(String course) { this.course = course; }
    public void setScore(double score) { this.score = score; }
}
