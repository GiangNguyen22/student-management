package com.app.desktopapp.model;

public class Course {
    private String code;
    private String name;
    private int credits;
    private String semester;

    public Course() {}
    public Course(String code, String name, int credits, String semester) {
        this.code = code;
        this.name = name;
        this.credits = credits;
        this.semester = semester;
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public int getCredits() { return credits; }
    public String getSemester() { return semester; }

    public void setCode(String code) { this.code = code; }
    public void setName(String name) { this.name = name; }
    public void setCredits(int credits) { this.credits = credits; }
    public void setSemester(String semester) { this.semester = semester; }
}
