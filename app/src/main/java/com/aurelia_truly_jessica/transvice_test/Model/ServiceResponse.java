package com.aurelia_truly_jessica.transvice_test.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServiceResponse {
    private String message;

    @SerializedName("data")
    private List<Service> serviceList;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Service> getServiceList() {
        return serviceList;
    }

    public void setServiceList(List<Service> serviceList) {
        this.serviceList = serviceList;
    }
}
