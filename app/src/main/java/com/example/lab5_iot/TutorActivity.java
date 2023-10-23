package com.example.lab5_iot;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.lab5_iot.databinding.ActivityCommentBinding;
import com.example.lab5_iot.databinding.ActivityTutorBinding;

public class TutorActivity extends AppCompatActivity {
    private ActivityTutorBinding binding;
    private Button iniciarBtnDownList;
    private Button iniciarAsignTutoria;
    private Button iniciarBuscarEmpleado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTutorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        iniciarBtnDownList=findViewById(R.id.btnDownloadTrabajadores);
        iniciarAsignTutoria=findViewById(R.id.btnAsignarTutoria);
        iniciarBuscarEmpleado=findViewById(R.id.btnBuscarTrabajador);

        iniciarBtnDownList.setOnClickListener(view ->  {
            Intent intent = new Intent(TutorActivity.this, DownListTrabajadorActivity.class);
            startActivity(intent);
        });
        iniciarAsignTutoria.setOnClickListener(view ->  {
            Intent intent = new Intent(TutorActivity.this, AsignTutoriaActivity.class);
            startActivity(intent);
        });
        iniciarBuscarEmpleado.setOnClickListener(view ->  {
            Intent intent = new Intent(TutorActivity.this, BuscarTrabajadorActivity.class);
            startActivity(intent);
        });

    }
}