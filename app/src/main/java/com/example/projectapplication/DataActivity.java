package com.example.projectapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.CallLog;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class DataActivity extends AppCompatActivity {
    private Chronometer chronometer;
    private boolean timer;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    public String CHANNEL_ID = "my_channel_01";
    public CharSequence name = "my_channel";
    public String Description = "This is my channel";
    private TextView textView;
    SwipeRefreshLayout swipeRefreshLayout;
    int NOTIFICATION_ID = 234;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        final ProgressDialog progressDialog = ProgressDialog.show(this, "Going Online", "Wait...", true);
        progressDialog.setCancelable(false);
        progressDialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        }, 10000);
        swipeRefreshLayout = findViewById(R.id.swipe);
        chronometer = findViewById(R.id.time);
        chronometer.start();
        chronometer.setBase(SystemClock.elapsedRealtime());
        timer = true;
        textView = findViewById(R.id.dayandtime);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMMM-yyyy HH:mm");
        Date date = new Date();
        String dateStr = formatter.format(date);
        textView.setText(dateStr);
        Intent intent = new Intent(this, OnlineActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("notification id", NOTIFICATION_ID);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_timer_24)
                .setContentTitle("Online")
                .setAutoCancel(true)
                .setContentText("Click to go Offline")
                .setContentIntent(resultPendingIntent)
                .setUsesChronometer(true);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addParentStack(DataActivity.class);
        stackBuilder.addNextIntent(intent);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
        recycler();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onRefresh() {
                recycler();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void recycler() {
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        String name = getIntent().getStringExtra("name");
        adapter = new RecyclerAdapter(getApplicationContext(), getCalllogs(), name);
        recyclerView.setAdapter(adapter);
    }

    private List<Calllogs> getCalllogs() {
        String date = textView.getText().toString();
        final String[] splitStr = date.split("\\s+");
        String[] str = splitStr[0].split("-");
        String[] str2 = splitStr[1].split(":");
        Calendar calendar = Calendar.getInstance();
        int year = Integer.parseInt(str[2]);
        int day = Integer.parseInt(str[0]);
        int hr = Integer.parseInt(str2[0]);
        int month = calendar.get(Calendar.MONTH);
        int min = Integer.parseInt(str2[1]);
        calendar.set(year, month, day, hr, min - 1);
        String fromDate = String.valueOf(calendar.getTimeInMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy HH mm");
        Date date2 = new Date();
        String dateStr2 = formatter.format(date2);
        String[] splitStr2 = dateStr2.split("\\s+");
        int year2 = Integer.parseInt(splitStr2[2]);
        int day2 = Integer.parseInt(splitStr2[0]);
        int hr2 = Integer.parseInt(splitStr2[3]);
        int min2 = Integer.parseInt(splitStr2[4]);
        //int sec2 = Integer.parseInt(splitStr2[5]);
        int month2 = calendar.get(Calendar.MONTH);
        calendar.set(year2, month2, day2, hr2, min2);
        String toDate = String.valueOf(calendar.getTimeInMillis());
        String[] where = {fromDate, toDate};
        List<Calllogs> list = new ArrayList<>();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG)
                == PackageManager.PERMISSION_GRANTED) {
            final Cursor cursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, null,
                    CallLog.Calls.DATE + " BETWEEN ? AND ?", where, CallLog.Calls.DATE + " ASC");

            final int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
            final int dura = cursor.getColumnIndex(CallLog.Calls.DURATION);
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                final String duration = cursor.getString(dura);
                final StringBuilder app = new StringBuilder(duration);
                app.append(" secs");
                String callType = cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE));
                String dir = null;
                int dircode = Integer.parseInt(callType);
                switch (dircode) {
                    case CallLog.Calls.OUTGOING_TYPE:
                        dir = "OUTGOING";
                        break;
                    case CallLog.Calls.INCOMING_TYPE:
                        dir = "INCOMING";
                        break;
                    case CallLog.Calls.MISSED_TYPE:
                        dir = "MISSED";
                        break;
                    case CallLog.Calls.REJECTED_TYPE:
                        dir = "REJECTED";
                        break;
                    case CallLog.Calls.BLOCKED_TYPE:
                        dir = "BLOCKED";
                        break;
                }
                String callDate = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE));
                long seconds = Long.parseLong(callDate);
                final String num = cursor.getString(number);
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                String dateString = format.format(new Date(seconds));

                list.add(new Calllogs(num, app, dir, dateString));
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALL_LOG}, 1);
        }
        return list;
    }

    public void offline(View view) {
        if (timer) {
            chronometer.stop();
            timer = false;
        }
        Intent intent = new Intent(this, OnlineActivity.class);
        startActivity(intent);
        finish();
        Toast.makeText(this, "You stayed online for: " + chronometer.getText(), Toast.LENGTH_LONG).show();
        int NOTIFICATION_ID = 234;
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_timer_24)
                .setContentTitle("You were online for : " + chronometer.getText().toString());

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addParentStack(DataActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

}