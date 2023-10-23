package com.example.lab5_iot.Tutor;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.lab5_iot.R;
import com.example.lab5_iot.databinding.ActivityCommentBinding;
import com.example.lab5_iot.databinding.ActivityDownListTrabajadorBinding;
import com.example.lab5_iot.databinding.ActivityMainBinding;
import com.example.lab5_iot.entity.trabajadores;
import com.example.lab5_iot.entity.trabajadoresDTO;
import com.example.lab5_iot.services.TutorRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DownListTrabajadorActivity extends AppCompatActivity {
    private Button iniciarDescarga;
    private ActivityDownListTrabajadorBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDownListTrabajadorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        iniciarDescarga=findViewById(R.id.btnDescargarLista);

        iniciarDescarga.setOnClickListener(view ->  {
            EditText editTextTutorId = findViewById(R.id.codigoTutorEditText);
            String tutorIdText = editTextTutorId.getText().toString();


            TutorRepository tutorRepository = new Retrofit.Builder()
                    .baseUrl("http://192.168.1.40:3000")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(TutorRepository.class);

            tutorRepository.getTrabajadores(tutorIdText).enqueue(new Callback<trabajadoresDTO>() {
                @Override
                public void onResponse(Call<trabajadoresDTO> call, Response<trabajadoresDTO> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            trabajadoresDTO body = response.body();
                            List<trabajadores> trabajadoresList = body.getTrabajadores();
                            Log.d("msg-test", "Solicitando trabajadores con tutorId: " + tutorIdText);
                            if (trabajadoresList != null) {
                                Log.d("msg-test", "Lista de trabajadores:");
                                for (trabajadores trabajador : trabajadoresList) {
                                    Log.d("msg-test", "Nombre: " + trabajador.getName());
                                    Log.d("msg-test", "Email: " + trabajador.getEmail());
                                    Log.d("msg-test", "Número de teléfono: " + trabajador.getPhone_number());
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
                public void onFailure(Call<trabajadoresDTO> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        });
    }
}