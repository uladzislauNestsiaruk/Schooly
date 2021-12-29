//package com.egormoroz.schooly;
//
//import android.util.Log;
//
//import androidx.annotation.NonNull;
//
//import java.util.concurrent.TimeUnit;
//
//public class NontificationManager extends Worker{
//
//    static final String TAG = "workmng";
//
//    @NonNull
//    @Override
//    public WorkerResult doWork() {
//        Log.d(TAG, "doWork: start");
//
//        try {
//            TimeUnit.SECONDS.sleep(10);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        Log.d(TAG, "doWork: end");
//
//        return WorkerResult.SUCCESS;
//    }
//}
