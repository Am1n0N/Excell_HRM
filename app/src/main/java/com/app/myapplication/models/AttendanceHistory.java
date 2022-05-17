package com.app.myapplication.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AttendanceHistory {

    @SerializedName("arrival")
    @Expose
    private String arrival;
    @SerializedName("day")
    @Expose
    private String day;
    @SerializedName("departure")
    @Expose
    private String departure;
    @SerializedName("note_end")
    @Expose
    private String noteEnd;
    @SerializedName("note_start")
    @Expose
    private String noteStart;

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getNoteEnd() {
        return noteEnd;
    }

    public void setNoteEnd(String noteEnd) {
        this.noteEnd = noteEnd;
    }

    public String getNoteStart() {
        return noteStart;
    }

    public void setNoteStart(String noteStart) {
        this.noteStart = noteStart;
    }

}