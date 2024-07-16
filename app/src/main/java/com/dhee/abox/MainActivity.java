package com.dhee.abox;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.dhc.absdk.ABRet;
import com.dhc.absdk.ABSDK;

public class MainActivity extends AppCompatActivity {
        Button sw,tw,hm;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sw = findViewById(R.id.Switch_button);
        tw = findViewById(R.id.Temperature);
        hm = findViewById(R.id.HumanActivity);

    }
    public void SWITCH_onClick(View view) {
        Intent j;
        j = new Intent(MainActivity.this, SwitchActivity.class);
        startActivity(j);
    }

    public void Temperature_button(View view) {
        Intent i = new Intent(MainActivity.this, Get_tem_Activity2.class);
        startActivity(i);
    }



    public void more(View view) {
        Intent k = new Intent(MainActivity.this,NetActivity.class);
        startActivity(k);
    }

    public void HA_Click(View view) {
        Intent l = new Intent(MainActivity.this,HumanActivity.class);
        startActivity(l);
    }


}