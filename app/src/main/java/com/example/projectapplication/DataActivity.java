package com.example.projectapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DataActivity extends AppCompatActivity {
    //private Chronometer chronometer;
    // private boolean timer;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    //    public String CHANNEL_ID = "my_channel_01";
//    public CharSequence name = "my_channel";
//    public String Description = "This is my channel";
    private TextView textView, textView3;
    private EditText editText;
    private String number;
//    SwipeRefreshLayout swipeRefreshLayout;
    //Snackbar snackbar;
    //ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        this.setFinishOnTouchOutside(false);


        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        //constraintLayout = findViewById(R.id.constraint);
        //swipeRefreshLayout = findViewById(R.id.swipe);
//        chronometer = findViewById(R.id.time);
//        chronometer.start();
//        chronometer.setBase(SystemClock.elapsedRealtime());
//        timer = true;
        textView = findViewById(R.id.onlineDate);
        editText = findViewById(R.id.phonenumber);
        textView3 = findViewById(R.id.newUsername);

        SharedPreferences sharedPreferences = getSharedPreferences("name", MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "");
        number = editText.getText().toString();

        textView3.setText("Hi, " + name);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMMM-yyyy HH:mm:ss");
        Date date = new Date();
        String dateStr = formatter.format(date);
        textView.setText(dateStr);


//        Intent intent = new Intent(this, OnlineActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.putExtra("notification id", NOTIFICATION_ID);
//        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            int importance = NotificationManager.IMPORTANCE_HIGH;
//            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
//            mChannel.setDescription(Description);
//            mChannel.enableLights(true);
//            mChannel.setLightColor(Color.RED);
//            mChannel.setShowBadge(false);
//            notificationManager.createNotificationChannel(mChannel);
//        }
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
//                .setSmallIcon(R.drawable.ic_baseline_timer_24)
//                .setContentTitle("Online")
//                .setAutoCancel(true)
//                .setContentText("Click to go Offline")
//                .setContentIntent(resultPendingIntent)
//                .setUsesChronometer(true);
//
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
//        stackBuilder.addParentStack(DataActivity.class);
//        stackBuilder.addNextIntent(intent);
//        notificationManager.notify(NOTIFICATION_ID, builder.build());
        //recycler();

//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @RequiresApi(api = Build.VERSION_CODES.M)
//            @Override
//            public void onRefresh() {
//                recycler();
//                swipeRefreshLayout.setRefreshing(false);
//            }
//        });
    }

    private void recycler() {
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        SharedPreferences sharedPreferences = getSharedPreferences("name", MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "");
        adapter = new RecyclerAdapter(getApplicationContext(), getCalllogs(), name);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onNewIntent(Intent intent1) {
        super.onNewIntent(intent1);
        if (intent1 != null) {
            String action = intent1.getAction();
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Intent.ACTION_DIAL.equals(action)) {
                Uri uri = intent1.getData();
                String uriString = uri.toString();
                String[] num = uriString.split(":");
                EditText editText = findViewById(R.id.phonenumber);
                editText.setText(num[1]);
            }
//        else if (Intent.ACTION_SEND.equals(action)) {
//            Uri uri = intent1.getData();
//            String uriString = uri.toString();
//            EditText editText = findViewById(R.id.phonenumber);
//            editText.setText(uriString);
//        }
            else if (Intent.ACTION_VIEW.equals(action)) {
                Uri uri = intent1.getData();
                String uriString = uri.toString();
                String[] num = uriString.split(":");
                EditText editText = findViewById(R.id.phonenumber);
                editText.setText(num[1]);
            }

        }
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
        int sec = Integer.parseInt(str2[2]);
        calendar.set(year, month, day, hr, min, sec);
        String fromDate = String.valueOf(calendar.getTimeInMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy HH mm ss");
        Date date2 = new Date();
        String dateStr2 = formatter.format(date2);
        String[] splitStr2 = dateStr2.split("\\s+");
        int year2 = Integer.parseInt(splitStr2[2]);
        int day2 = Integer.parseInt(splitStr2[0]);
        int hr2 = Integer.parseInt(splitStr2[3]);
        int min2 = Integer.parseInt(splitStr2[4]);
        int sec2 = Integer.parseInt(splitStr2[5]);
        int month2 = calendar.get(Calendar.MONTH);
        calendar.set(year2, month2, day2, hr2, min2, sec2);
        String toDate = String.valueOf(calendar.getTimeInMillis());
        String[] where = {fromDate, toDate};
        List<Calllogs> list = new ArrayList<>();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG)
                == PackageManager.PERMISSION_GRANTED) {
            Cursor cursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, null,
                    CallLog.Calls.DATE + " BETWEEN ? AND ?", where, CallLog.Calls.DATE + " ASC");

            cursor.move(0);
            while (cursor.moveToNext()) {
                final int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
                final int dura = cursor.getColumnIndex(CallLog.Calls.DURATION);
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
                SharedPreferences sharedPreferences = getSharedPreferences("name", MODE_PRIVATE);
                final String name = sharedPreferences.getString("name", "");
                String callDate = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE));
                long seconds = Long.parseLong(callDate);
                final String num = cursor.getString(number);
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                String dateString = format.format(new Date(seconds));

                list.add(new Calllogs(num, app, dir, dateString));
                final String[] times = dateString.split(" ");

                final String finalDir = dir;
                StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbyXWSvct4NraanheJad2nrGp752R3GV8Rqk3QQHgKsTsVfG59rV/exec",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("action", "addItem");
                        params.put("name", name);
                        params.put("date", times[0]);
                        params.put("time", times[1]);
                        params.put("phone", num);
                        params.put("status", finalDir);
                        params.put("duration", "" + app);
                        return params;
                    }
                };
                int socketTimeOut = 1000;
                RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest.setRetryPolicy(retryPolicy);
                RequestQueue queue = Volley.newRequestQueue(this);
                queue.add(stringRequest);
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALL_LOG}, 1);
        }
        return list;
    }

//    public void offline(View view) {
//        if (timer) {
//            chronometer.stop();
//            timer = false;
//        }
//        Intent intent = new Intent(this, OnlineActivity.class);
//        startActivity(intent);
//        finish();
//        final String startTime = textView.getText().toString();
////        final String[] splitStr = startTime.split(" ");
//        SimpleDateFormat formatter = new SimpleDateFormat("dd:MMMM:yyyy HH:mm:ss");
//        Date date2 = new Date();
//        String dateStr2 = formatter.format(date2);
//        final String[] splitStr2 = dateStr2.split("\\s+");
//        SharedPreferences sharedPreferences = getSharedPreferences("name", MODE_PRIVATE);
//        final String name = sharedPreferences.getString("name", "");
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbxRmh3ssfU07SRuXTxlv5lZG-dbHglv-MhyxhkNPr_OYfWnOt8h/exec",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<>();
//                params.put("action", "addTime");
//                params.put("name", name);
//                params.put("date", startTime);
//                params.put("startTime", textView2.getText().toString());
//                params.put("endTime", splitStr2[1]);
//                return params;
//            }
//        };
//        int socketTimeOut = 1000;
//        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        stringRequest.setRetryPolicy(retryPolicy);
//        RequestQueue queue = Volley.newRequestQueue(this);
//        queue.add(stringRequest);
//        //snackbar = Snackbar.make(constraintLayout, "You stayed online for: " + chronometer.getText(), Snackbar.LENGTH_INDEFINITE);
//        //snackbar.setDuration(5000);
//        //snackbar.show();
//        Toast.makeText(this, "You stayed online for: " + chronometer.getText(), Toast.LENGTH_LONG).show();
////        int NOTIFICATION_ID = 234;
////        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
////
////        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
////            int importance = NotificationManager.IMPORTANCE_HIGH;
////            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
////            mChannel.setDescription(Description);
////            mChannel.enableLights(true);
////            mChannel.setLightColor(Color.RED);
////            mChannel.setShowBadge(false);
////            notificationManager.createNotificationChannel(mChannel);
////        }
////        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
////                .setSmallIcon(R.drawable.ic_baseline_timer_24)
////                .setAutoCancel(true)
////                .setContentTitle("You were online for : " + chronometer.getText().toString());
////
////        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
////        stackBuilder.addParentStack(DataActivity.class);
////        stackBuilder.addNextIntent(intent);
////        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
////        builder.setContentIntent(resultPendingIntent);
////        notificationManager.notify(NOTIFICATION_ID, builder.build());
//    }

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

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onStart() {
        super.onStart();
        recycler();
    }

    public void call(View view) {

        //EditText editText1 = findViewById(R.id.edit);
        String number = editText.getText().toString();
        //String num = editText1.getText().toString();

        if (number.isEmpty()) {
            editText.setError("Enter Phone number");
            editText.requestFocus();
        }

//        } else if (num.isEmpty() || !num.contains("+")) {
//            editText1.setError("Enter Proper Country Code");
//            editText1.requestFocus();
//        }
        else {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {
                RequestPermissionsResult();
            } else {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + number));
                startActivity(intent);
                editText.setText(null);
                //editText1.setText(null);
            }
        }
    }

    private void RequestPermissionsResult() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
    }

    public void whatsapp(View view) {
        EditText editText = findViewById(R.id.phonenumber);
        String number = editText.getText().toString();
        boolean installed = installedOrNot();

        if (number.isEmpty()) {
            editText.setError("Enter Phone number");
            editText.requestFocus();
        } else {
            if (installed) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + "+91" + number));
                startActivity(intent);

            } else {
                Toast.makeText(this, "WhatsApp Not Installed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean installedOrNot() {
        PackageManager packageManager = getPackageManager();
        boolean installed;
        try {
            packageManager.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }

    public void paste(View view) {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        assert clipboardManager != null;
        ClipData clipData = clipboardManager.getPrimaryClip();
        assert clipData != null;
        ClipData.Item item = clipData.getItemAt(0);

        EditText editText = findViewById(R.id.phonenumber);
        editText.setText(item.getText().toString());

        Toast.makeText(this, "Pasted", Toast.LENGTH_SHORT).show();
    }

//    public void edit(View view) {
//        EditText editText = findViewById(R.id.newUsername);
//        String username = editText.getText().toString().trim();
//        if (username.isEmpty()) {
//            editText.setError("Enter New Username");
//            editText.requestFocus();
//        } else {
//
//        }
//    }

}