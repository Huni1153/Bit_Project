package com.example.backgroundstt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private ArrayList<DataItem> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        
        // 아래의 함수 대신
        // 사용자가 음성 입력을 하면 RIGHT_CONTENT에 메시지를 보내주고,
        // 시스템이 응답할때 TTS와 함께 LEFT_CONTENT에 메시지를 보내준다.
        this.initializeData();

        RecyclerView recyclerView = findViewById(R.id.chat_rView_recyclerView);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);

        recyclerView.setLayoutManager(manager); // LayoutManager 등록
        recyclerView.setAdapter(new MyAdapter(dataList));  // Adapter 등록

    }


    public void initializeData()
    {
        dataList = new ArrayList<>();

        dataList.add(new DataItem("안녕하세요",  Code.ViewType.LEFT_CONTENT));
        dataList.add(new DataItem("안녕하세요",   Code.ViewType.RIGHT_CONTENT));

    }
}