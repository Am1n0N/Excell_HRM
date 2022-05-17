package com.app.myapplication.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EntryHistory {

    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("logger")
    @Expose
    private String logger;
    @SerializedName("title")
    @Expose
    private String title;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogger() {
        return logger;
    }

    public void setLogger(String logger) {
        this.logger = logger;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}