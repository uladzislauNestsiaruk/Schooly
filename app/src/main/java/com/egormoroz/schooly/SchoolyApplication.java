package com.egormoroz.schooly;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import androidx.work.Constraints;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.egormoroz.schooly.ui.main.NontificationService;

import java.util.concurrent.TimeUnit;

public class SchoolyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        startService(new Intent(this, SchoolyService.class));

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        PeriodicWorkRequest sendLogsWorkRequest = new
                PeriodicWorkRequest.Builder(NontificationManager.class, 24, TimeUnit.HOURS)
                .setConstraints(constraints
                )
                .build();

        WorkManager.getInstance(getApplicationContext()).enqueue(sendLogsWorkRequest);
    }



}

