package com.example.lab5_iot.Tutor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.lab5_iot.databinding.ActivityAsignTutoriaBinding;

public class AsignTutoriaActivity extends AppCompatActivity {

    private ActivityAsignTutoriaBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAsignTutoriaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}