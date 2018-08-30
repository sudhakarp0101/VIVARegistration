package com.example.sudhakar.vivaregistration;

/**
 * Created by sudhakar on 12/21/2017.
 */

public class Venue {
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return  "\nDate='" + date + '\'' +
                ", \nVenue='" + place + '\'' +
                ", \nTime='" + time + '\'' +
                '\n';
    }

    private String date;
    private String place;
    private String time;

    public Venue(String date, String place, String time) {
        this.date = date;
        this.place = place;
        this.time = time;
    }
    public Venue() {
    }
}
