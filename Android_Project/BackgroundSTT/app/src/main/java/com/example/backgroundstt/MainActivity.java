package com.example.backgroundstt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
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
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    public static final String[] EVENT_PROJECTION = new String[]{
            CalendarContract.Calendars._ID,                             // 0
            CalendarContract.Calendars.ACCOUNT_NAME,                    // 1
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,           // 2
            CalendarContract.Calendars.OWNER_ACCOUNT,                   // 3
            CalendarContract.Events.CALENDAR_ID,                        // 4
            CalendarContract.Events._ID,                                // 5
            CalendarContract.Events.TITLE,                              // 6
            CalendarContract.Events.DESCRIPTION,                        // 7
            CalendarContract.Events.DTSTART,                            // 8
            CalendarContract.Events.DTEND,                              // 9
            CalendarContract.Events.EVENT_TIMEZONE                      // 10
    };

    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;
    private static final int PROJECTION_CALENDAR_ID = 4;
    private static final int PROJECTION_EVENT_ID = 5;
    private static final int PROJECTION_EVENT_TITLE = 6;
    private static final int PROJECTION_EVENT_DESCRIPTION = 7;
    private static final int PROJECTION_EVENT_DTSTART = 8;
    private static final int PROJECTION_EVENT_DTEND = 9;
    private static final int PROJECTION_EVENT_TIMEZONE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission2();
        findViewById(R.id.main_btn_sttStart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(MainActivity.this, Recognition.class));
                System.out.println("여기 오는가?");
            }
        });

        findViewById(R.id.main_btn_sttEnd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(new Intent(MainActivity.this, Recognition.class));
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
                    //eventRead();
                    //eventRead2();
                    test();
                }
                else
                {
                    // 접근 권한이 없다면 종료
                    finish();
                }
            }
        });
    }
    private void checkPermission2()
    {
        //if(ContextCompat.checkSelfPermission("컨텍스트 정보의 자리","요청할 권한") != PackageManager.PERMISSION_GRANTED)
        // checkSelfPermission()의 리턴값은 요청한 권한이 수락되었을때 PERMISSION_GRANTED를 리턴한다.
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED // Manifest.permission.WRITE_CONTACTS 승인되지 않았을 떄
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED // Manifest.permission.READ_CONTACTS 승인되지 않았을 떄
                || ContextCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED // Manifest.permission.READ_PHONE_STATE 승인되지 않았을 떄
                || ContextCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED // Manifest.permission.CALL_PHONE 승인되지 않았을 떄
                || ContextCompat.checkSelfPermission(this,Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED // Manifest.permission.GET_ACCOUNTS 승인되지 않았을 떄
                || ContextCompat.checkSelfPermission(this,Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED // Manifest.permission.INTERNET 승인되지 않았을 떄
                || ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED // Manifest.permission.RECORD_AUDIO 승인되지 않았을 떄
                || ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED // Manifest.permission.WRITE_CALENDAR 승인되지 않았을 떄
                || ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {// Manifest.permission.READ_CALENDAR 승인되지 않았을 떄
            ActivityCompat.requestPermissions(this, Code.PERMISSION_PROJECTION, 0);
        }
        else{
            //이 영역은 권한이 거부되었을 때
            //권한이 거부되었을 때에 사용자에게 권한 설정을 요구하는 다이얼로그를 띄우기 위해서
            //requestPermission을 쓴다. requestPermission(액티비티의 정보,String[]의 권한요청명령 ,request 코드)
            //여기서 리퀘스트 코드는 후에 onRequestPermissionResult() 메서드에 결과물이 전달될 시, 결과물들을 구분 짓는 인덱스
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_CONTACTS)){ // 이곳에 퍼미션을 더 추가해야 한다.
                //사용자가 다시 보지 않기에 체크를 하지 않고, 권한 설정을 거절한 이력이 있는 경우
                // 이 경우 계속해서 권한은 묻는건 사용자의 입장이 곤란할 수 있지만 그렇다고 사용에 꼭 필요한 권한일 경우 묻지 않을 수 없다.
                // 그렇기 때문에 한번 취소했던 이력이 있는 경우 이것에 대해 설명해주는 Notice를 주는 등, 여러가지 작업을 생각해서 작성해보자.

            }else{
                //사용자가 다시 보지 않기에 체크하고, 권한 설정을 거절한 이력이 있는 경우

            }
            //사용자에게 권한 요청 다이얼로그를 띄우는데 만약 사용자가 다시 보지 않기에 체크를  했을 경우
            //곧바로 onRequestPermissionResult가 실행된다.
            ActivityCompat.requestPermissions(this, Code.PERMISSION_PROJECTION,0);
        }
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
         long calID = 1;
         long startMillis = 0;
         long endMillis = 0;

        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2021, 2, 25, 7, 30);
        startMillis = beginTime.getTimeInMillis();
        //Log.d("이거 몇시? : ",startMillis);
        Calendar endTime = Calendar.getInstance();
        endTime.set(2021, 2, 25, 8, 45);
        endMillis = endTime.getTimeInMillis();

        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.TITLE, "대체 어디에 추가 되는거니??");
        values.put(CalendarContract.Events.DESCRIPTION, "이건 어디에 나오는거고??");
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
        if(cur == null || !cur.moveToFirst())
        {
            // 디바이스에 캘린더가 존재하지 않음(계정이 등록되어 있지 않음)
            Toast myToast = Toast.makeText(this.getApplicationContext(),"디바이스에 캘린더가 존재하지 않음", Toast.LENGTH_LONG);
        }



        while (cur.moveToNext()) {
            // Get the field values
            String calID = null;
            String displayName = null;
            String accountName = null;
            String ownerName = null;
            String calendarId = null;
            String eventId = null;
            String eventTitle = null;
            String eventDescription = null;
            String eventDTStart = null;
            String eventDTEnd = null;
            String eventTimeZone = null;

            calID = cur.getString(PROJECTION_ID_INDEX);
            displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
            accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
            ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);
            calendarId = cur.getString(PROJECTION_CALENDAR_ID);
            eventId = cur.getString(PROJECTION_EVENT_ID);
            eventTitle = cur.getString(PROJECTION_EVENT_TITLE);
            eventDescription = cur.getString(PROJECTION_EVENT_DESCRIPTION);
            eventDTStart = cur.getString(PROJECTION_EVENT_DTSTART);
            eventDTEnd = cur.getString(PROJECTION_EVENT_DTEND);
            eventTimeZone = cur.getString(PROJECTION_EVENT_TIMEZONE);

            // Do something with the values...
            Log.d("데이터 확인", "calId : " + calID);
            Log.d("데이터 확인","displayName : " + displayName);
            Log.d("데이터 확인", "accountName : " + accountName);
            Log.d("데이터 확인","ownerName : " + ownerName);
            Log.d("데이터 확인","calendarId : " + calendarId);
            Log.d("데이터 확인","eventId : " + eventId);
            Log.d("데이터 확인","eventTitle : " + eventTitle);
            Log.d("데이터 확인","eventDescription : " + eventDescription);
            Log.d("데이터 확인","eventDTStart : " + eventDTStart);
            Log.d("데이터 확인","eventDTEnd : " + eventDTEnd);
            Log.d("데이터 확인","eventTimeZone : " + eventTimeZone);
        }
    }
    public void eventRead2()
    {
        AccountManager acctMgr = AccountManager.get(this); // 디바이스에 등록된 사용자계정 전부 불러온다.
        Account[] accts = acctMgr.getAccounts(); // 사용자 계정들을 저장할 배열.
        int acctCnt = accts.length; // 저장된 계정의 갯수.
        Account acct; // 필요한 사용자 계정을 입력할 때 사용할 변수.

        String acctName = "";
        String acctType = "";

        int row = 0;

        while(row < acctCnt)
        {
            acct = accts[row];
            if(acct.type.equals("com.google"))
            {
                acctName += acct.name;
                acctType += acct.type;
                break;
            }
            row ++;
        }

        final String[] EVENT_PROJECTION1 = new String[]{
                CalendarContract.Calendars._ID, CalendarContract.Calendars.ACCOUNT_NAME, CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, CalendarContract.Calendars.OWNER_ACCOUNT};
        Cursor cur = null;
        ContentResolver cr = getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND ("
                + CalendarContract.Calendars.ACCOUNT_TYPE + " = ?) AND ("
                + CalendarContract.Calendars.OWNER_ACCOUNT + " = ?))";
        //Log.d("확인용","이름 : " + acctName +"계정 타입 : " + acctType + "계정 이름 : " + acctName); // 여기서 못불러온다....
        //String[] selectionArgs = new String[]{acctName,acctType,acctName};
        String[] selectionArgs = new String[] {"aelleek@gmail.com", "com.google","aelleek@gmail.com"};

        cur = cr.query(uri,EVENT_PROJECTION1,selection,selectionArgs,null);

        int test = 0;
        while(!cur.isLast())
        {
            cur.moveToNext();
            //test = cur.getInt(0);
            Log.d("확인용",cur.getString(0) + " " + cur.getString(2));
        }
    }
    public void test()
    {
        String projection[] = {"_id", "calendar_displayName"};
        Uri calendars;
        calendars = Uri.parse("content://com.android.calendar/calendars");

        ContentResolver contentResolver = getContentResolver();
        Cursor managedCursor = contentResolver.query(calendars, projection, null, null, null);

        if (managedCursor.moveToFirst()){
            //m_calendars = new MyCalendar[managedCursor.getCount()];
            String calName;
            String calID;
            int cont= 0;
            int nameCol = managedCursor.getColumnIndex(projection[1]);
            int idCol = managedCursor.getColumnIndex(projection[0]);
            do {
                calName = managedCursor.getString(nameCol);
                calID = managedCursor.getString(idCol);
                //Log.d("테스트 용 ", "캘린더 이름 : " + calName + "캘린더 id : " + calID);
               // m_calendars[cont] = new MyCalendar(calName, calID);
                cont++;
            } while(managedCursor.moveToNext());
            managedCursor.close();
        }
    }
}
