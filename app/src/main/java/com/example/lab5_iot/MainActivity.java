package com.example.lab5_iot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.view.View;
import android.widget.Button;

import com.example.lab5_iot.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private Button iniciarBtnTutor;
    private Button iniciarBtnTrabajador;

    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        iniciarBtnTutor=findViewById(R.id.btnTutor);
        iniciarBtnTrabajador=findViewById(R.id.btnTrabajador);
        iniciarBtnTutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TutorActivity.class);
                startActivity(intent);
            }
        });
        iniciarBtnTrabajador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Iniciar la actividad de Trabajador aquí
                Intent intent = new Intent(MainActivity.this, TrabajadorActivity.class);
                startActivity(intent);
            }
        });
    }
}