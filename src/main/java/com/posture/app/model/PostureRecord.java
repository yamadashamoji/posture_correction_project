package com.posture.app.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class PostureRecord {
    private LocalDate date;
    private LocalTime time;
    private String posture;
    private String notes;

    public PostureRecord() {
        this.date = LocalDate.now();
        this.time = LocalTime.now();
    }

    public PostureRecord(LocalDate date, LocalTime time, String posture, String notes) {
        this.date = date;
        this.time = time;
        this.posture = posture;
        this.notes = notes;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getPosture() {
        return posture;
    }

    public void setPosture(String posture) {
        this.posture = posture;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return String.format("%s %s - %s", date, time, posture);
    }
}
