package com.example.lab5_iot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class DownListTrabajadorActivity extends AppCompatActivity {
    private Button iniciarDescarga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_list_trabajador);

        iniciarDescarga=findViewById(R.id.btnDescargarLista);

        iniciarDescarga.setOnClickListener(view ->  {
            // Simula si el trabajador tiene una tutoría agendada o no
            boolean tieneTutoriaAgendada = true; // Cambia a true si tiene tutoría
        });
    }
}