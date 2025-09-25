package com.example.jobschedulerex;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JobNotification extends JobService {
    private ExecutorService executorService;

    @Override
    public boolean onStartJob(JobParameters params) {
//        Thread backgroundThread = new Thread(new Runnable() {
//           @Override
//           public void run () {
//               try {
//                   Thread.sleep(3000);
//               } catch (InterruptedException e) {
//                   Log.e("JobNotificationStatus", "Hilo interrumpido");
//               }
//
//               Handler handler = new Handler(Looper.getMainLooper());
//               handler.post(new Runnable () {
//                   @Override
//                   public void run () {
//                       Toast.makeText(JobNotification.this, "JobNotification ejecutado", Toast.LENGTH_SHORT).show();
//                   }
//               });
//               jobFinished(params, false);
//           }
//        });
//        backgroundThread.start();

        // Executor con un solo hilo
        executorService = Executors.newSingleThreadExecutor();

        executorService.execute(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Log.e("JobNotificationStatus", "Hilo interrumpido", e);
            }

            // Volviendo al hilo principal con Handler
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() ->
                    Toast.makeText(JobNotification.this, "JobNotification ejecutado", Toast.LENGTH_SHORT).show()
            );

            // Avisando que el job terminó
            jobFinished(params, false);
        });

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
