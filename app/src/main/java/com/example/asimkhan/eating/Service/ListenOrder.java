package com.example.asimkhan.eating.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.asimkhan.eating.Model.Requests;
import com.example.asimkhan.eating.OrdersStatus;
import com.example.asimkhan.eating.R;
import com.example.asimkhan.eating.common.Common;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ListenOrder extends Service implements ChildEventListener {
    FirebaseDatabase db;
    DatabaseReference request;
    public ListenOrder(){

    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        db = FirebaseDatabase.getInstance();
        request = db.getReference("Requests");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        request.addChildEventListener(this);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        Requests requests = dataSnapshot.getValue(Requests.class);
        shownotification(dataSnapshot.getKey(),requests);
    }

    private void shownotification(String key, Requests requests) {
        Intent intent = new Intent(getBaseContext(), OrdersStatus.class);
        intent.putExtra("userphone",requests.getPhone());
        PendingIntent contentintent = PendingIntent.getActivity(getBaseContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel =
                    new NotificationChannel("asimfoods","asimfood", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "asimfoods")
                .setDefaults(Notification.DEFAULT_ALL)
                .setTicker("asimfoods")
                .setContentInfo("your order was updated")
                .setSmallIcon(R.drawable.access_time_clock)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentText("Order #"+key+"was update status to "+ Common.convertcodetostatus(requests.getStatus()))
                .setContentIntent(contentintent)
                .setContentInfo("info");
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(999, builder.build());
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
}
