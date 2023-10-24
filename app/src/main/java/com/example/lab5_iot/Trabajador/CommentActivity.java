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

import com.example.lab5_iot.R;
import com.example.lab5_iot.Tutor.AsignTutoriaActivity;
import com.example.lab5_iot.databinding.ActivityCommentBinding;
import com.example.lab5_iot.databinding.ActivityMainBinding;
import com.example.lab5_iot.entity.commentDTO;
import com.example.lab5_iot.entity.commentRpt;
import com.example.lab5_iot.entity.trabajador;
import com.example.lab5_iot.entity.trabajadorDTO;
import com.example.lab5_iot.ip;
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
    String serverIp = ip.SERVER_IP;
    String channelIDcom = "channelDefaultPri";

    private ActivityCommentBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        createNotificationChannel();

        Intent intent1 = getIntent();
        String codigo = intent1.getStringExtra("employeeId");
        Log.d("msg-test", "Llegó el codigo: "+codigo);

        String commentStr = intent1.getStringExtra("comment2");
        Log.d("msg-test", "Llegó el comentario "+commentStr);

        edtFeedback = findViewById(R.id.edtFeedback);
        btnEnviarFeedback = findViewById(R.id.btnEnviarFeedback);
        btnEnviarFeedback.setOnClickListener(view ->  {

            edtFeedback = findViewById(R.id.edtFeedback);
            String commentIdText = edtFeedback.getText().toString();


            TrabajadorRepository trabajadorRepository = new Retrofit.Builder()
                    .baseUrl("http://"+serverIp+":3000")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(TrabajadorRepository.class);

            trabajadorRepository.ingresarFeedback(codigo, commentIdText).enqueue(new Callback<commentRpt>() {
                @Override
                public void onResponse(Call<commentRpt> call, Response<commentRpt> response) {
                    if (response.isSuccessful()) {
                        commentRpt body = response.body();
                        Log.d("msg-test", String.valueOf(body));
                        String comentario = body.getMessage();
                        Log.d("msg-test", comentario);
                        if(comentario.equals("Actualización exitosa")){
                            // Comentario almacenado en la base de datos, recibir notificación
                            notificarImportanceHigh3(true);
                        } else if (comentario.equals("Aun no tiene la tutoria")) {
                            notificarImportanceHigh2();
                        }

                    } else {
                        Log.d("msg-test", "no se envio nada ");
                        // Manejar la respuesta del servidor si no fue exitosa
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
        NotificationChannel channel = new NotificationChannel(channelIDcom,
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
            ActivityCompat.requestPermissions(CommentActivity.this, new String[]{POST_NOTIFICATIONS}, 101);
        }
    }

    public void notificarImportanceHigh3(boolean tienecomentario) {
        Intent intent = new Intent(this, CommentActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelIDcom)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("FEEDBACK") // Mensaje requerido
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        builder.setContentText("Feedback enviado de manera exitosa");

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        if (ActivityCompat.checkSelfPermission(this, POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify(1, builder.build());
        }
    }

    public void notificarImportanceHigh2() {
        Intent intent = new Intent(this, AsignTutoriaActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelIDcom)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("FEEDBACK") // Mensaje requerido
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        builder.setContentText("Aun no tiene la tutoria");

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        if (ActivityCompat.checkSelfPermission(this, POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify(1, builder.build());
        }
    }
}