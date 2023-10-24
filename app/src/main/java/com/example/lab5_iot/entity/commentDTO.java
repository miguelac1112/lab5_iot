package com.example.lab5_iot.entity;

import java.util.List;

public class commentDTO {
    private List<commentRpt> message;

    public List<commentRpt> getMessage() {
        return message;
    }

    public void setMessage(List<commentRpt> message) {
        this.message = message;
    }
}
