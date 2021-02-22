package com.example.backgroundstt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    public static final String[] EVENT_PROJECTION = new String[]{
            CalendarContract.Calendars._ID,
            CalendarContract.Events.DTSTART,
            CalendarContract.Events.DTEND,
            CalendarContract.Events.TITLE,
            CalendarContract.Events.EVENT_LOCATION,
            CalendarContract.Events.ORGANIZER,
            CalendarContract.Events._ID,
            CalendarContract.Events.DESCRIPTION,
            CalendarContract.Events.DURATION,
            CalendarContract.Events.SYNC_DATA1,
            CalendarContract.Events.DIRTY,
            CalendarContract.Events.UID_2445,
            CalendarContract.Events.DELETED,
            CalendarContract.Events.LAST_DATE,
            CalendarContract.Events.SYNC_DATA2,
            CalendarContract.Events.ALL_DAY,
            CalendarContract.Events.RRULE,
            CalendarContract.Events.STATUS,
            CalendarContract.Events.RDATE
    };

    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.main_btn_sttStart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(MainActivity.this, Recognition.class));
            }
        });

        findViewById(R.id.main_btn_sttEnd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(MainActivity.this, Recognition.class));
            }
        });

        findViewById(R.id.main_btn_scheduleAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_CALENDAR,Manifest.permission.READ_CALENDAR},0);
                if(checkPermission() == true)
                {
                    // 접근 권한이 있다면 기능 수행
                    eventAdd();
                }
                else
                {
                    // 접근 권한이 없다면 종료
                    finish();
                }
            }
        });

        findViewById(R.id.main_btn_scheduleSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_CALENDAR,Manifest.permission.READ_CALENDAR},0);
                if(checkPermission() == true)
                {
                    // 접근 권한이 있다면 기능 수행
                    eventRead();
                }
                else
                {
                    // 접근 권한이 없다면 종료
                    finish();
                }
            }
        });
    }


    public boolean checkPermission()
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        else
        {
            //이 영역은 권한 접근이 거부되었을 때
            //접근이 거부되었을 때에 사용자에게 접근 권한 설정을 요구하는 다이얼로그를 띄우기 위해서
            //requestPermission을 쓴다. requestPermission(액티비티의 정보,String[]의 권한요청명령 ,request 코드)
            //여기서 리퀘스트 코드는 후에 onRequestPermissionResult() 메서드에 결과물이 전달될 시, 결과물들을 구분 짓는 인덱스
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_CALENDAR)){
                //사용자가 다시 보지 않기에 체크를 하지 않고, 권한 설정을 거절한 이력이 있는 경우
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_CALENDAR,Manifest.permission.READ_CALENDAR},0);
            }else{
                //사용자가 다시 보지 않기에 체크하고, 권한 설정을 거절한 이력이 있는 경우
                return false;
            }
            //사용자에게 권한 요청 다이얼로그를 띄우는데 만약 사용자가 다시 보지 않기에 체크를 했을 경우
            //곧바로 onRequestPermissionResult가 실행된다.
            return false;
        }
    }


    public void eventAdd()
    {
         long calID = 3;
         long startMillis = 0;
         long endMillis = 0;

        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2021, 2, 23, 7, 30);
        startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(2021, 2, 23, 8, 45);
        endMillis = endTime.getTimeInMillis();

        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.TITLE, "일정 제목");
        values.put(CalendarContract.Events.DESCRIPTION, "일정 설명");
        values.put(CalendarContract.Events.CALENDAR_ID, calID);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, "Asia/Korea");

        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
        long eventID = Long.parseLong(uri.getLastPathSegment());

    }
    public void eventRead()
    {
        Cursor cur = null;
        ContentResolver cr = getContentResolver();
        Uri uri = CalendarContract.Events.CONTENT_URI;
        String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND ("
                + CalendarContract.Calendars.ACCOUNT_TYPE + " = ?) AND ("
                + CalendarContract.Calendars.OWNER_ACCOUNT + " = ?))";
        String[] selectionArgs = new String[] {"aelleek@gmail.com", "com.google","aelleek@gmail.com"};
        // Submit the query and get a Cursor object back.

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALENDAR,Manifest.permission.READ_CALENDAR},12);
        }
        cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);

        while (cur.moveToNext()) {
            long calID = 0;
            String displayName = null;
            String accountName = null;
            String ownerName = null;
            String eventTitle = null;
            String eventId = null;
            String startDate = null;
            String endDate = null;

            // Get the field values
            calID = cur.getLong(PROJECTION_ID_INDEX);
            displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
            accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
            ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);
            eventTitle = cur.getString(cur.getColumnIndex(CalendarContract.Events.TITLE));
            eventId = cur.getString(cur.getColumnIndex(CalendarContract.Events._ID));
            startDate = cur.getString(cur.getColumnIndex(CalendarContract.Events.DTSTART));
            endDate = cur.getString(cur.getColumnIndex(CalendarContract.Events.DTEND));

            // Do something with the values...
            Log.d("데이터 확인", "calId : " + calID);
            Log.d("데이터 확인","displayName : " + displayName);
            Log.d("데이터 확인", "accountName : " + accountName);
            Log.d("데이터 확인","ownerName : " + ownerName);
            Log.d("Events", "->" + eventTitle + "->" + eventId + "->" + startDate + "->" + endDate);
        }

    }
}