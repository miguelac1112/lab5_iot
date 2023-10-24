package com.example.lab5_iot.Trabajador;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.lab5_iot.R;
import com.example.lab5_iot.databinding.ActivityCommentBinding;
import com.example.lab5_iot.databinding.ActivityMainBinding;
import com.example.lab5_iot.entity.trabajador;
import com.example.lab5_iot.entity.trabajadorDTO;
import com.example.lab5_iot.services.TrabajadorRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CommentActivity extends AppCompatActivity {
    private EditText edtFeedback;
    private Button btnEnviarFeedback;

    private ActivityCommentBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent1 = getIntent();
        String codigo = intent1.getStringExtra("codigo");
        Log.d("msg-test", "Llegó el codigo: "+codigo);

        edtFeedback = findViewById(R.id.edtFeedback);
        btnEnviarFeedback = findViewById(R.id.btnEnviarFeedback);

    }
}