package com.example.backgroundstt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private ArrayList<DataItem> dataList;
    private EditText inputData;
    private Button inputBtn;
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        
        // 아래의 함수 대신
        // 사용자가 음성 입력을 하면 RIGHT_CONTENT에 메시지를 보내주고,
        // 시스템이 응답할때 TTS와 함께 LEFT_CONTENT에 메시지를 보내준다.
        this.initializeData();

        recyclerView = findViewById(R.id.chat_rView_recyclerView);

        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);

        recyclerView.setLayoutManager(manager); // LayoutManager 등록
//        adapter = new MyAdapter(dataList);
//        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(new MyAdapter(dataList));  // Adapter 등록

        inputData = (EditText)findViewById(R.id.chat_et_inputText);
        inputBtn = (Button)findViewById(R.id.chat_btn_inputBtn);

        inputBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userText(inputData.getText().toString());
                inputData.setText("");
            }
        });
    }

    public void initializeData()
    {
        //dataList.add(new DataItem("여긴 비서가 음성으로 말하면서 출력될 말풍선 입니다.",  Code.ViewType.LEFT_CONTENT));
        //dataList.add(new DataItem("여긴 우리가 음성으로 말하면 출력될 말풍선 입니다.",   Code.ViewType.RIGHT_CONTENT));
    }
    public void userText(String voiceData)
    {
        dataList.add(new DataItem(voiceData,Code.ViewType.RIGHT_CONTENT));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        int action = MotionEventCompat.getActionMasked(event);

        switch(action) {
            case (MotionEvent.ACTION_DOWN) :
                Log.d("이벤트 확인","Action was DOWN");
                return true;
            case (MotionEvent.ACTION_MOVE) :
                Log.d("이벤트 확인","Action was MOVE");
                return true;
            case (MotionEvent.ACTION_UP) :
                Log.d("이벤트 확인","Action was UP");
                finish();
                return true;
            case (MotionEvent.ACTION_CANCEL) :
                Log.d("이벤트 확인","Action was CANCEL");
                return true;
            case (MotionEvent.ACTION_OUTSIDE) :
                Log.d("이벤트 확인","Movement occurred outside bounds " +
                        "of current screen element");
                return true;
            default :
                return super.onTouchEvent(event);
        }
    }
}