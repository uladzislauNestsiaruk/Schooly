package com.egormoroz.schooly;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.egormoroz.schooly.ui.main.NontificationService;

public class SchoolyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        startService(new Intent(this, NontificationService.class));
        startService(new Intent(this, SchoolyService.class));
    }
}

