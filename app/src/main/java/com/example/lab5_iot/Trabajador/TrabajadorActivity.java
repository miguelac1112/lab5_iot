package com.example.lab5_iot.Trabajador;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.lab5_iot.R;
import com.example.lab5_iot.Tutor.DownListTrabajadorActivity;
import com.example.lab5_iot.Tutor.TutorActivity;
import com.example.lab5_iot.databinding.ActivityCommentBinding;
import com.example.lab5_iot.databinding.ActivityTrabajadorBinding;

public class TrabajadorActivity extends AppCompatActivity {
    private Button iniciarComentario;


    private ActivityTrabajadorBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTrabajadorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        iniciarComentario=findViewById(R.id.btnFeedback);

        iniciarComentario.setOnClickListener(view ->  {
                Intent intent = new Intent(TrabajadorActivity.this, CommentActivity.class);
            startActivity(intent);
        });
    }
}