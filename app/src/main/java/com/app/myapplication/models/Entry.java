package com.app.myapplication.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Entry {

    @SerializedName("allowResponse")
    @Expose
    private Boolean allowResponse;
    @SerializedName("currentUser_id")
    @Expose
    private Integer currentUserId;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("isPublic")
    @Expose
    private Boolean isPublic;
    @SerializedName("logger")
    @Expose
    private String logger;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("wasRequested")
    @Expose
    private Boolean wasRequested;

    public Boolean getAllowResponse() {
        return allowResponse;
    }

    public void setAllowResponse(Boolean allowResponse) {
        this.allowResponse = allowResponse;
    }

    public Integer getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(Integer currentUserId) {
        this.currentUserId = currentUserId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public String getLogger() {
        return logger;
    }

    public void setLogger(String logger) {
        this.logger = logger;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getWasRequested() {
        return wasRequested;
    }

    public void setWasRequested(Boolean wasRequested) {
        this.wasRequested = wasRequested;
    }

}