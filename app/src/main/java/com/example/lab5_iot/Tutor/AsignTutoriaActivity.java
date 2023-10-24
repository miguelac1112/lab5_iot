package com.example.lab5_iot.Tutor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.lab5_iot.R;
import com.example.lab5_iot.databinding.ActivityAsignTutoriaBinding;
import com.example.lab5_iot.entity.trabajadorDTO;
import com.example.lab5_iot.ip;
import com.example.lab5_iot.services.TutorRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AsignTutoriaActivity extends AppCompatActivity {

    private ActivityAsignTutoriaBinding binding;
    private Button asignarTutoriaButton;
    private EditText editTextTutorCode;
    private EditText editTextEmployeeId;
    private EditText editTextFecha;
    String serverIp = ip.SERVER_IP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAsignTutoriaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        asignarTutoriaButton=findViewById(R.id.asignarTutoriaButton);
        asignarTutoriaButton.setOnClickListener(view ->{

            editTextTutorCode=findViewById(R.id.editTextTutorCode);
            editTextEmployeeId=findViewById(R.id.editTextEmployeeId);
            editTextFecha=findViewById(R.id.editTextFecha);

            TutorRepository tutorRepository = new Retrofit.Builder()
                    .baseUrl("http://"+serverIp+":3000")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(TutorRepository.class);


            tutorRepository.actualizarMeeting(String.valueOf(editTextEmployeeId), String.valueOf(editTextEmployeeId), String.valueOf(editTextFecha)).enqueue(new Callback<trabajadorDTO>() {
                @Override
                public void onResponse(Call<trabajadorDTO> call, Response<trabajadorDTO> response) {
                    if (response.isSuccessful()) {
                        // Manejar la respuesta exitosa
                    } else {
                        // Manejar una respuesta no exitosa
                    }
                }
                @Override
                public void onFailure(Call<trabajadorDTO> call, Throwable t) {
                    t.printStackTrace();
                }
            });

        });

    }
}