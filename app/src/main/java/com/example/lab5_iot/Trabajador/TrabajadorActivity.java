package com.example.lab5_iot.Trabajador;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.lab5_iot.R;
import com.example.lab5_iot.Tutor.DownListTrabajadorActivity;
import com.example.lab5_iot.Tutor.TutorActivity;
import com.example.lab5_iot.databinding.ActivityCommentBinding;
import com.example.lab5_iot.databinding.ActivityTrabajadorBinding;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TrabajadorActivity extends AppCompatActivity {
    private Button iniciarComentario;
    private Button feedbackButton;

    private ActivityTrabajadorBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTrabajadorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String meetingDateStr = null;
        if (getIntent().hasExtra("meetingDate")) {
            meetingDateStr = getIntent().getStringExtra("meetingDate");
        }

        iniciarComentario = findViewById(R.id.btnFeedback);

        if (meetingDateStr != null) {
            iniciarComentario.setVisibility(View.VISIBLE);
        } else {
            iniciarComentario.setVisibility(View.GONE);
        }

        iniciarComentario.setOnClickListener(view -> {
            // Iniciar la actividad de comentarios
            Intent commentIntent = new Intent(TrabajadorActivity.this, CommentActivity.class);
            startActivity(commentIntent);
        });
    }

}