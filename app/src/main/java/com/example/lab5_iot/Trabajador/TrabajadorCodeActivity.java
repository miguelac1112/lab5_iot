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
import android.widget.Button;

import com.example.lab5_iot.MainActivity;
import com.example.lab5_iot.R;
import com.example.lab5_iot.databinding.ActivityMainBinding;
import com.example.lab5_iot.databinding.ActivityTrabajadorCodeBinding;

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

            Intent intent = new Intent(TrabajadorCodeActivity.this, TrabajadorActivity.class);
            startActivity(intent);
            // Simula si el trabajador tiene una tutoría agendada o no
            boolean tieneTutoriaAgendada = true; // Cambia a true si tiene tutoría

            notificarImportanceHigh2(tieneTutoriaAgendada);
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
    public void notificarImportanceHigh2(boolean tieneTutoria) {
        Intent intent = new Intent(this, TrabajadorActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Está entrando en modo Empleado") // Mensaje requerido
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        if (tieneTutoria) {
            builder.setContentText("Tiene una tutoría agendada el [fecha] a las [hora]"); // Reemplaza [fecha] y [hora] con los datos reales
        }

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        if (ActivityCompat.checkSelfPermission(this, POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify(2, builder.build());
        }
    }
}