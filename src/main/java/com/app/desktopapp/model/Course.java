package com.app.desktopapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Course {

    private Integer id;

    @JsonProperty("courseCode")
    private String courseCode;

    @JsonProperty("courseName")
    private String courseName;

    private String description;
    private String staffCode;
    private LocalDate startDate;
    private LocalDate endDate;

    public Course() {}

    // ===== getters =====
    public Integer getId() { return id; }
    public String getCourseCode() { return courseCode; }
    public String getCourseName() { return courseName; }
    public String getDescription() { return description; }
    public String getStaffCode() { return staffCode; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }

    // ===== setters =====
    public void setId(Integer id) { this.id = id; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public void setDescription(String description) { this.description = description; }
    public void setStaffCode(String staffCode) { this.staffCode = staffCode; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
}
