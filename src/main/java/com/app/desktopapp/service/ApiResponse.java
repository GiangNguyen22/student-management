package com.app.desktopapp.service;

import java.util.List;

public class ApiResponse<T>{
    public List<T> data;
    public int totalPages;

    public ApiResponse(List<T> data, int totalPages) {
        this.data = (data == null) ? List.of() : data;
        this.totalPages = totalPages;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
