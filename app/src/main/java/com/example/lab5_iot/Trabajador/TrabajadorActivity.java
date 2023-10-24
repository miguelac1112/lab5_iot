package com.example.lab5_iot.Trabajador;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DownloadManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;

import com.example.lab5_iot.R;
import com.example.lab5_iot.Tutor.DownListTrabajadorActivity;
import com.example.lab5_iot.Tutor.TutorActivity;
import com.example.lab5_iot.databinding.ActivityCommentBinding;
import com.example.lab5_iot.databinding.ActivityTrabajadorBinding;
import com.example.lab5_iot.entity.meetingDate;
import com.example.lab5_iot.entity.meetingDateDTO;
import com.example.lab5_iot.ip;
import com.example.lab5_iot.services.TrabajadorRepository;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TrabajadorActivity extends AppCompatActivity {
    private Button iniciarComentario;
    private Button btnDownloadHorarios;

    String channelID = "channelDefaultPri";
    String serverIp = ip.SERVER_IP;


    private ActivityTrabajadorBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTrabajadorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent1 = getIntent();

        String codigo = intent1.getStringExtra("codigo");
        Log.d("msg-test", "Llegó el codigo: "+codigo);

        btnDownloadHorarios=findViewById(R.id.btnDownloadHorarios);
        final Context contexto = this;
        btnDownloadHorarios.setOnClickListener(view ->{

            TrabajadorRepository trabajadorRepository = new Retrofit.Builder()
                    .baseUrl("http://"+serverIp+":3000")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(TrabajadorRepository.class);

            trabajadorRepository.getMeetingDate(codigo).enqueue(new Callback<meetingDateDTO>(){
                @Override
                public void onResponse(Call<meetingDateDTO> call, Response<meetingDateDTO> response) {
                    if (response.isSuccessful()) {
                        Log.d("msg-test", String.valueOf(response));
                        meetingDateDTO body = response.body();
                        List<meetingDate> meetingDateList = body.getMeetingDate();

                        Log.d("msg-test", "hola");
                        for (meetingDate meet : meetingDateList) {
                            Log.d("msg-test", "entre");
                            Log.d("msg-test", "La fecha es : " + meet.getMeeting_date());
                        }
                        String fecha = String.valueOf(meetingDateList.get(0).getMeeting_date());
                        Log.d("msg-test", "La fecha es : " + fecha);
                        if(fecha.equals("null")){
                            Log.d("msg-test","La fecha es nula");
                            notificarImportanceHigh2();
                        }else{
                            Log.d("msg-test","La fecha no es nula");

                            String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;

                            // >=29
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ||
                                    ContextCompat.checkSelfPermission(contexto, permission) == PackageManager.PERMISSION_GRANTED) {
                                Log.d("msg-test","llegue");
                                descargarConDownloadManager();
                                Log.d("msg-test","sali");
                            }else{
                                launcher.launch(permission);
                            }

                        }
                    } else {
                        Log.d("msg-test", "La respuesta del servidor no es exitosa");
                    }
                }

                @Override
                public void onFailure(Call<meetingDateDTO> call, Throwable t) {
                    Log.d("msg-test", "algo pasó!!!");
                    Log.d("msg-test", t.getMessage());
                    t.printStackTrace();
                }
            });

        });


        iniciarComentario=findViewById(R.id.btnFeedback);
        iniciarComentario.setOnClickListener(view ->  {
            Intent intent = new Intent(TrabajadorActivity.this, CommentActivity.class);
            intent.putExtra("employeeId", codigo);  // Pasa el ID del empleado
            startActivity(intent);
        });
    }


    public void descargarConDownloadManager(){
        String fileName = "horario.jpg";
        String url = "https://i.pinimg.com/564x/4e/8e/a5/4e8ea537c896aa277e6449bdca6c45da.jpg";

        //URL URi
        Uri downloadUri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(downloadUri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(false);
        request.setTitle(fileName);
        request.setMimeType("image/jpeg");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, File.separator + fileName);

        DownloadManager dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Log.d("msg-test","descargando?");
        try {
            dm.enqueue(request);
        }catch (RuntimeException e){
            e.printStackTrace();
        }
    }
    ActivityResultLauncher<String> launcher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {

                if (isGranted) { // permiso concedido
                    descargarConDownloadManager();
                } else {
                    Log.e("msg-test", "Permiso denegado");
                }
            });


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
            ActivityCompat.requestPermissions(TrabajadorActivity.this, new String[]{POST_NOTIFICATIONS}, 101);
        }
    }

    public void notificarImportanceHigh2() {
        Intent intent = new Intent(this, TrabajadorActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Tutorías") // Mensaje requerido
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        builder.setContentText("“No cuenta con tutorías pendientes");

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        if (ActivityCompat.checkSelfPermission(this, POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify(1, builder.build());
        }
    }
}