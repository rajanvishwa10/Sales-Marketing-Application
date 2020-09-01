package com.example.projectapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
public String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText editText = findViewById(R.id.name);
        name = editText.getText().toString().trim();
        SharedPreferences preferences = getSharedPreferences("pre", MODE_PRIVATE);
        String first = preferences.getString("first", "");
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        if (first.equals("yes")) {
            Intent intent = new Intent(this, OnlineActivity.class);
            intent.putExtra("name", name);
            startActivity(intent);
            finish();
        } else {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("first", "yes");
            editor.apply();
        }
    }

    public void submit(View view) {
        EditText editText = findViewById(R.id.name);
        final String name = editText.getText().toString().trim();
        if (name.isEmpty()) {
            editText.setError("Enter Name");
            editText.findFocus();
        } else {
            SharedPreferences sharedPreferences = getSharedPreferences("name", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("name", name);
            editor.apply();

            Intent intent = new Intent(this, OnlineActivity.class);
            intent.putExtra("name", name);
            startActivity(intent);
            finish();
        }
    }

}