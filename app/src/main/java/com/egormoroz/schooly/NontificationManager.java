package com.egormoroz.schooly;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class NontificationManager extends Worker {

    FirebaseModel firebaseModel=new FirebaseModel();
    private static final int NOTIFY_ID = 102;
    private static final String CHANNEL_ID = "channel";
    Nontification otherUserNickNonts;
    String nickOther;

    public NontificationManager(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }


    @NonNull
    @Override
    public Result doWork() {
        firebaseModel.initAll();
        getChangesInSubscribers();
        createNotificationChannel();
        Log.d("######", "great");
        return Result.success();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getApplicationContext().getString(R.string.chanel_name);
            String description = getApplicationContext().getString(R.string.app_name);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getApplicationContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            Log.d("####", "dd  ");
        }
    }

    public void getChangesInSubscribers(){
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                Query query=firebaseModel.getUsersReference().child(nick).child("nontifications");
                query.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        RecentMethods.getNontificationsList(nick, firebaseModel, new Callbacks.getNontificationsList() {
                            @Override
                            public void getNontificationsList(ArrayList<Nontification> nontifications) {
                                Log.d("#####", "f "+nontifications);
                                if(nontifications.size()!=0) {
                                    for(int i=0;i<nontifications.size();i++){
                                        otherUserNickNonts = nontifications.get(i);
                                        nickOther = otherUserNickNonts.getNick();
                                        Log.d("####", "shiiiiiiiitttttttttttt");
                                        if(otherUserNickNonts.getTypeDispatch().equals("не отправлено")) {
                                            nontification(nickOther);
                                            firebaseModel.getUsersReference().child(nick).child("nontifications").child(otherUserNickNonts.getNick())
                                                    .child("typeDispatch").setValue("отправлено");
                                        }
                                    }
                                }
                            }
                        });

                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    public void nontification(String nickOther){
        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(),
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_nontification_image)
                .setContentTitle(nickOther)
                .setContentText("хочет добавить вас в друзья")
                .setContentIntent(contentIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(getApplicationContext());
        notificationManager.notify(NOTIFY_ID, builder.build());
        Log.d("######", "good");
        Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(400);
    }

}
