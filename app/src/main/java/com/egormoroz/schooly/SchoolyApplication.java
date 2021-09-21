package com.egormoroz.schooly;

import android.app.Application;
import android.content.Intent;

public class SchoolyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        startService(new Intent(this, SchoolyService.class));
    }
}

