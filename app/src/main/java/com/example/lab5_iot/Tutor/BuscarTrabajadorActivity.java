package com.example.lab5_iot.Tutor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.lab5_iot.databinding.ActivityAsignTutoriaBinding;
import com.example.lab5_iot.databinding.ActivityBuscarTrabajadorBinding;

public class BuscarTrabajadorActivity extends AppCompatActivity {

    private ActivityBuscarTrabajadorBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBuscarTrabajadorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}