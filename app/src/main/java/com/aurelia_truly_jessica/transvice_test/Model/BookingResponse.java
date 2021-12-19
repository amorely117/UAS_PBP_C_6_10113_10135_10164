package com.aurelia_truly_jessica.transvice_test.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BookingResponse {
    private String message;

    @SerializedName("data")
    private List<Booking> bookingList;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Booking> getBookingList() {
        return bookingList;
    }

    public void setBookingList(List<Booking> bookingList) {
        this.bookingList = bookingList;
    }
}
