package com.example.projectapplication;

public class Calllogs {

    private String number, type, time;
    StringBuilder duration;

    public Calllogs(String number, StringBuilder duration, String type, String time) {
        this.number = number;
        this.duration = duration;
        this.time = time;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public StringBuilder getDuration() {
        return duration;
    }

    public void setDuration(StringBuilder duration) {
        this.duration = duration;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
