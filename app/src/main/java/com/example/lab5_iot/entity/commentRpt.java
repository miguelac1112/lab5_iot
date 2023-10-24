package com.example.lab5_iot.entity;

import java.io.Serializable;

public class commentRpt implements Serializable {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
