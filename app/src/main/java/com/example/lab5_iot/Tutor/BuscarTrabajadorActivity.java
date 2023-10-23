package com.example.lab5_iot.Tutor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.lab5_iot.R;
import com.example.lab5_iot.databinding.ActivityAsignTutoriaBinding;
import com.example.lab5_iot.databinding.ActivityBuscarTrabajadorBinding;
import com.example.lab5_iot.entity.trabajador;
import com.example.lab5_iot.entity.trabajadorDTO;
import com.example.lab5_iot.entity.trabajadores;
import com.example.lab5_iot.entity.trabajadoresDTO;
import com.example.lab5_iot.services.TutorRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BuscarTrabajadorActivity extends AppCompatActivity {

    private Button buscarTrabajador;
    private ActivityBuscarTrabajadorBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBuscarTrabajadorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        buscarTrabajador=findViewById(R.id.btnDownloadTrabajador);
        buscarTrabajador.setOnClickListener(view ->  {
            EditText editTextTutorId = findViewById(R.id.codigoTrabajador);
            String trabajadorIdText = editTextTutorId.getText().toString();


            TutorRepository tutorRepository = new Retrofit.Builder()
                    .baseUrl("http://192.168.1.40:3000")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(TutorRepository.class);

            tutorRepository.getTrabajador(trabajadorIdText).enqueue(new Callback<trabajadorDTO>() {
                @Override
                public void onResponse(Call<trabajadorDTO> call, Response<trabajadorDTO> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            trabajadorDTO body = response.body();
                            List<trabajador> trabajadorList = body.getTrabajador();
                            Log.d("msg-test", "Solicitando trabajador con Id: " + trabajadorIdText);
                            if (trabajadorList != null) {
                                Log.d("msg-test", "Trabajador");
                                for (trabajador t : trabajadorList) {
                                    Log.d("msg-test", "Nombre: " + t.getName());
                                    Log.d("msg-test", "Email: " + t.getEmail());
                                    Log.d("msg-test", "Número de teléfono: " + t.getPhone_number());
                                }
                            } else {
                                Log.d("msg-test", "La lista de trabajadores es nula");
                            }
                        } else {
                            Log.d("msg-test", "La respuesta del servidor no tiene datos de trabajadores");
                        }
                    } else {
                        Log.d("msg-test", "La respuesta del servidor no es exitosa");
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