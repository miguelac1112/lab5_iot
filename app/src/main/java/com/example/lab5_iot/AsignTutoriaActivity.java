package com.example.lab5_iot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.lab5_iot.databinding.ActivityAsignTutoriaBinding;
import com.example.lab5_iot.databinding.ActivityCommentBinding;

public class AsignTutoriaActivity extends AppCompatActivity {

    private ActivityAsignTutoriaBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAsignTutoriaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}