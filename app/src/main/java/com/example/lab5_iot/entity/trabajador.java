package com.example.lab5_iot.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

public class trabajador extends commentRpt implements Serializable {
    private String name;
    private String email;
    private String phone_number;
    private String meeting_date;
    private String employee_feedback;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getMeeting_date() {
        return meeting_date;
    }

    public void setMeeting_date(String meeting_date) {
        this.meeting_date = meeting_date;
    }

    public String getEmployee_feedback() {
        return employee_feedback;
    }

    public void setEmployee_feedback(String employee_feedback) {
        this.employee_feedback = employee_feedback;
    }
}
