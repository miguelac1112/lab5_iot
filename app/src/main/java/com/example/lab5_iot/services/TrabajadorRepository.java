package com.example.lab5_iot.services;

import com.example.lab5_iot.entity.meetingDateDTO;
import com.example.lab5_iot.entity.trabajadorDTO;
import com.example.lab5_iot.entity.trabajadoresDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TrabajadorRepository {
    @GET("/tutoriaAgendada/{trabajadorId}")
    Call<trabajadorDTO> getFecha(@Path("trabajadorId") String trabajadorId);

    @GET("/tutoriaAgendada/{idTrabajador}")
    Call<meetingDateDTO> getMeetingDate(@Path("idTrabajador") String idTrabajador);
}
