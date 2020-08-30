package com.example.projectapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class OnlineActivity extends AppCompatActivity {
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch switc;
    TextView textView,textView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        SharedPreferences sharedPreferences = getSharedPreferences("name", MODE_PRIVATE);
        String name = sharedPreferences.getString("name","");
        textView = findViewById(R.id.text_name);
        textView.setText("Hi, "+name);
        switc = findViewById(R.id.switch1);
        switc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                textView2 = findViewById(R.id.status);
                if(switc.isChecked()){
                    textView2.setText(R.string.online);
                    Intent intent = new Intent(OnlineActivity.this, DataActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    textView2.setText(R.string.offline);
                }
            }
        });
    }
}