package com.example.lab5_iot.services;

import com.example.lab5_iot.entity.trabajadoresDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TutorRepository {
    @GET("/listaTrabajadoresTutor/{tutorId}")
    Call<trabajadoresDTO> getTrabajadores(@Path("tutorId") String tutorId);
}

