package com.example.lab5_iot.Tutor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.lab5_iot.R;
import com.example.lab5_iot.databinding.ActivityDownListTrabajadorBinding;
import com.example.lab5_iot.entity.trabajadores;
import com.example.lab5_iot.entity.trabajadoresDTO;
import com.example.lab5_iot.ip;
import com.example.lab5_iot.services.TutorRepository;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DownListTrabajadorActivity extends AppCompatActivity {

    String serverIp = ip.SERVER_IP;
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
                    .baseUrl("http://"+serverIp+":3000")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(TutorRepository.class);

            tutorRepository.getTrabajadores(tutorIdText).enqueue(new Callback<trabajadoresDTO>() {
                @Override
                public void onResponse(Call<trabajadoresDTO> call, Response<trabajadoresDTO> response) {
                    if (response.isSuccessful()) {
                        trabajadoresDTO body = response.body();
                        List<trabajadores> trabajadoresList = body.getTrabajadores();
                        Log.d("msg-test", "Solicitando trabajadores con tutorId: " + tutorIdText);
                        Log.d("msg-test", "Lista de trabajadores:");
                        for (trabajadores trabajador : trabajadoresList) {
                            Log.d("msg-test", "Nombre: " + trabajador.getName());
                            Log.d("msg-test", "Email: " + trabajador.getEmail());
                            Log.d("msg-test", "Número de teléfono: " + trabajador.getPhone_number());
                        }
                        guardarArchivoComoTxt(trabajadoresList);
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
    public void guardarArchivoComoTxt(List<trabajadores> trabajadoresList) {

        StringBuilder content = new StringBuilder();

        for (trabajadores trabajador : trabajadoresList) {
            content.append("Nombre: ").append(trabajador.getName()).append("\n");
            content.append("Email: ").append(trabajador.getEmail()).append("\n");
            content.append("Número de teléfono: ").append(trabajador.getPhone_number()).append("\n\n");
        }

        String fileNameTxt = "listaTrabajadores.txt";

        try (FileOutputStream fileOutputStream = this.openFileOutput(fileNameTxt, Context.MODE_PRIVATE);
             OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream)) {

            writer.write(content.toString());

            Log.d("msg-test", "Archivo de texto guardado como " + fileNameTxt);

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("msg-test", "Error al guardar el archivo de texto: " + e.getMessage());
        }
    }

}