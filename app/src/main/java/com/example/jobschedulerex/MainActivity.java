package com.example.jobschedulerex;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private static final int JOB_ID = 1;
    private static TextView txtMensaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtMensaje = findViewById(R.id.txtMensaje);
    }

    private BroadcastReceiver airplaneReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isAirplaneModeOn = intent.getBooleanExtra("state", false);
            String mensaje;

            if (isAirplaneModeOn) {
                mensaje = "Modo avión activado";
            } else {
                mensaje = "Modo avión desactivado";
            }

            txtMensaje.setText("Broadcast dinámico: " + mensaje);
            Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();

            JobInfo jobInfo = getJobInfo(MainActivity.this);
            JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

            if (scheduler != null) {
                scheduler.cancel(JOB_ID);

                int result = scheduler.schedule(jobInfo);
                if (result == JobScheduler.RESULT_SUCCESS) {
                    Toast.makeText(context, "Job programado al cambiar al modo avión", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Fallo al programar el job en el servicio", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    public static JobInfo getJobInfo(MainActivity mainActivity) {
        ComponentName componentName = new ComponentName(mainActivity, JobNotification.class);
        return new JobInfo.Builder(JOB_ID, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setMinimumLatency(2000)
                .setOverrideDeadline(5000)
                .setPersisted(false)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        registerReceiver(airplaneReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(airplaneReceiver);
    }
}