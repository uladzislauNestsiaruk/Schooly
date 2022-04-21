package com.egormoroz.schooly;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import androidx.work.Constraints;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.egormoroz.schooly.ui.main.NontificationService;

import java.util.concurrent.TimeUnit;

public class SchoolyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        OneTimeWorkRequest miningWorkRequest = new
                OneTimeWorkRequest.Builder(MiningManager.class)
                .setConstraints(constraints)
                .build();

        PeriodicWorkRequest notificationWorkRequest = new
                PeriodicWorkRequest.Builder(NontificationManager.class, 15, TimeUnit.MINUTES)
                .setConstraints(constraints
                )
                .build();

        WorkManager.getInstance(getApplicationContext()).enqueue(notificationWorkRequest);

        WorkManager.getInstance(getApplicationContext()).enqueueUniqueWork("mining", ExistingWorkPolicy.KEEP,miningWorkRequest);
    }



}

