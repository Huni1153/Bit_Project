package com.example.backgroundtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.main_btn_serviceStart).setOnClickListener(this);
        findViewById(R.id.main_btn_serviceStop).setOnClickListener(this);

    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();

        if(id == R.id.main_btn_serviceStart)
        {
            Toast.makeText(getApplicationContext(),"음성인식을 시작합니다.",Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getApplicationContext(),MyService.class);
            //intent.putExtra("",""); // MainActivity에서 특정한 값을 보내줄때 사용한다.
            startActivity(intent);
        }
        else if(id == R.id.main_btn_serviceStop)
        {
            Toast.makeText(getApplicationContext(),"음성인식을 종료합니다.",Toast.LENGTH_SHORT).show();

        }
    }
}