package com.app.myapplication.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Contract {

    @SerializedName("Fullname")
    @Expose
    private String fullname;
    @SerializedName("Position")
    @Expose
    private String position;
    @SerializedName("Signing_date")
    @Expose
    private String signingDate;
    @SerializedName("Status")
    @Expose
    private Boolean status;
    @SerializedName("id")
    @Expose
    private Integer id;

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getSigningDate() {
        return signingDate;
    }

    public void setSigningDate(String signingDate) {
        this.signingDate = signingDate;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}