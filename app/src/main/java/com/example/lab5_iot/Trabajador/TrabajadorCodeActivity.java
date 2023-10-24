package com.example.lab5_iot.Trabajador;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.lab5_iot.MainActivity;
import com.example.lab5_iot.R;
import com.example.lab5_iot.databinding.ActivityMainBinding;
import com.example.lab5_iot.databinding.ActivityTrabajadorCodeBinding;
import com.example.lab5_iot.entity.trabajador;
import com.example.lab5_iot.entity.trabajadorDTO;
import com.example.lab5_iot.entity.trabajadores;
import com.example.lab5_iot.entity.trabajadoresDTO;
import com.example.lab5_iot.services.TrabajadorRepository;
import com.example.lab5_iot.services.TutorRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TrabajadorCodeActivity extends AppCompatActivity {

    private Button iniciarFlujoTrabajador;
    String channelID = "channelDefaultPri";

    private ActivityTrabajadorCodeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTrabajadorCodeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        createNotificationChannel();

        iniciarFlujoTrabajador = findViewById(R.id.btnIngresarTrabajador);

        iniciarFlujoTrabajador.setOnClickListener(view ->  {

            EditText editTextTutorId = findViewById(R.id.codigoTrabajador);
            String trabajadorIdText = editTextTutorId.getText().toString();


            TrabajadorRepository trabajadorRepository = new Retrofit.Builder()
                    .baseUrl("http://192.168.1.40:3000")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(TrabajadorRepository.class);

            trabajadorRepository.getFecha(trabajadorIdText).enqueue(new Callback<trabajadorDTO>() {
                @Override
                public void onResponse(Call<trabajadorDTO> call, Response<trabajadorDTO> response) {
                    if (response.isSuccessful()) {
                        trabajadorDTO body = response.body();
                        List<trabajador> trabajadorList = body.getTrabajador();
                        Log.d("msg-test", "Solicitando trabajador con Id: " + trabajadorIdText);

                        Log.d("msg-test", "Trabajador");
                        for (trabajador t : trabajadorList) {
                            Log.d("msg-test", "hora: " + t.getMeeting_date());

                            LocalDateTime meetingDate = null;
                            if (t.getMeeting_date() != null) {
                                meetingDate = LocalDateTime.parse(t.getMeeting_date(), DateTimeFormatter.ISO_DATE_TIME);
                            }

                            boolean tieneTutoria = (meetingDate != null);
                            notificarImportanceHigh2(tieneTutoria, String.valueOf(meetingDate));

                            if (tieneTutoria) {
                                Intent intent = new Intent(TrabajadorCodeActivity.this, TrabajadorActivity.class);
                                intent.putExtra("meetingDate", String.valueOf(meetingDate));
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(TrabajadorCodeActivity.this, TrabajadorActivity.class);
                                startActivity(intent);
                            }
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
    public void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(channelID,
                "Canal notificaciones default",
                NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription("Canal para notificaciones con prioridad default");

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

        askPermission();
    }

    public void askPermission() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ActivityCompat.checkSelfPermission(this, POST_NOTIFICATIONS) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(TrabajadorCodeActivity.this, new String[]{POST_NOTIFICATIONS}, 101);
        }
    }
    public void notificarImportanceHigh2(boolean tieneTutoria, String meetingDateStr) {
        Intent intent = new Intent(this, TrabajadorActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Está entrando en modo Empleado") // Mensaje requerido
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        if (tieneTutoria) {
            // Convertir la cadena de fecha y hora en un objeto LocalDateTime
            LocalDateTime meetingDateTime = LocalDateTime.parse(meetingDateStr, DateTimeFormatter.ISO_DATE_TIME);

            // Formatear la fecha y hora de la tutoría como una cadena de texto
            String fechaHoraTutoria = meetingDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            builder.setContentText("Tiene una tutoría agendada el " + fechaHoraTutoria);
        }

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        if (ActivityCompat.checkSelfPermission(this, POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify(1, builder.build());
        }
    }

}