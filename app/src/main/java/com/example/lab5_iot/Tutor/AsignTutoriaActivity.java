package com.example.lab5_iot.Tutor;

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

import com.example.lab5_iot.R;
import com.example.lab5_iot.Trabajador.TrabajadorActivity;
import com.example.lab5_iot.databinding.ActivityAsignTutoriaBinding;
import com.example.lab5_iot.entity.commentRpt;
import com.example.lab5_iot.entity.meetingDate;
import com.example.lab5_iot.entity.meetingDateDTO;
import com.example.lab5_iot.entity.trabajadorDTO;
import com.example.lab5_iot.entity.trabajadores;
import com.example.lab5_iot.ip;
import com.example.lab5_iot.services.TutorRepository;

import java.util.List;

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
    String channelID = "channelDefaultPri";
    String serverIp = ip.SERVER_IP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAsignTutoriaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        createNotificationChannel();

        asignarTutoriaButton=findViewById(R.id.asignarTutoriaButton);
        asignarTutoriaButton.setOnClickListener(view ->{

            editTextTutorCode=findViewById(R.id.editTextTutorCode);
            editTextEmployeeId=findViewById(R.id.editTextEmployeeId);
            editTextFecha=findViewById(R.id.editTextFecha);

            String editTextTutorCodeTxt = editTextTutorCode.getText().toString();
            String editTextEmployeeIdTxt = editTextEmployeeId.getText().toString();
            String editTextFechaTxt = editTextFecha.getText().toString();

            Log.d("msg-test", editTextTutorCodeTxt);
            Log.d("msg-test", editTextEmployeeIdTxt);
            Log.d("msg-test", editTextFechaTxt);

            TutorRepository tutorRepository = new Retrofit.Builder()
                    .baseUrl("http://"+serverIp+":3000")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(TutorRepository.class);


            tutorRepository.actualizarMeeting(editTextEmployeeIdTxt, editTextTutorCodeTxt, editTextFechaTxt).enqueue(new Callback<commentRpt>() {
                @Override
                public void onResponse(Call<commentRpt> call, Response<commentRpt> response) {
                    if (response.isSuccessful()) {
                        // Manejar la respuesta exitosa
                        commentRpt body = response.body();
                        Log.d("msg-test", String.valueOf(body));
                        String comentario = body.getMessage();
                        Log.d("msg-test", comentario);
                        if(comentario.equals("Actualización exitosa")){
                            //notificar bien
                            notificarImportanceHigh2();
                        }else if(comentario.equals("El empleado y el gerente no están relacionados")){
                            //notificar mal relacion
                            notificarImportanceHigh3();
                        }else{
                            //notificar que ya tiene cita agendada
                            notificarImportanceHigh4();
                        }
                    } else {
                        Log.d("msg-test", "La respuesta del servidor no es exitosa");
                    }
                }
                @Override
                public void onFailure(Call<commentRpt> call, Throwable t) {
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
            ActivityCompat.requestPermissions(AsignTutoriaActivity.this, new String[]{POST_NOTIFICATIONS}, 101);
        }
    }

    public void notificarImportanceHigh2() {
        Intent intent = new Intent(this, AsignTutoriaActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Tutorías") // Mensaje requerido
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        builder.setContentText("Asignación del trabajador correcta");

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        if (ActivityCompat.checkSelfPermission(this, POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify(1, builder.build());
        }
    }

    public void notificarImportanceHigh3() {
        Intent intent = new Intent(this, AsignTutoriaActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Tutorías") // Mensaje requerido
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        builder.setContentText("No es manager del empleado");

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        if (ActivityCompat.checkSelfPermission(this, POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify(1, builder.build());
        }
    }

    public void notificarImportanceHigh4() {
        Intent intent = new Intent(this, AsignTutoriaActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Tutorías") // Mensaje requerido
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        builder.setContentText("El trabajador\n" +
                "ya tiene una cita asignada. Elija otro trabajador");

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        if (ActivityCompat.checkSelfPermission(this, POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify(1, builder.build());
        }
    }
}