package com.example.adressbookmodule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button adressBookReadBtn;
    private Button deviceInfoReadBtn;
    private final int PERMISSION = 1;
    private List<String> data = new ArrayList<String>();
    private Button callBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 기기 ID, 전화번호부 목록에서 이름만
        // 핸드폰 번호가 없을때 번호를 조회하려고하면 나오는 값이 뭘까?

        if (Build.VERSION.SDK_INT >= 23) { // 퍼미션 체크
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS}, PERMISSION);
        }


        this.adressBookReadBtn = (Button) findViewById(R.id.main_btn_adressBookReadBtn);
        this.deviceInfoReadBtn = (Button) findViewById(R.id.main_btn_deviceInfoReadBtn);
        this.callBtn = (Button)findViewById(R.id.main_btn_callBtn);


        // 주소록 불러오기 리스너
        this.adressBookReadBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("HardwareIds")
            @Override
            public void onClick(View v) {
                // 전화번호부 받아오는 부분
                int cnt = 0;
                contacts();
                Iterator<String> itr = data.iterator();
                while (itr.hasNext()) {
                    Log.d(cnt + " : ", itr.next());
                    cnt++;
                }


            }
        });
        // 단말기 정보 불러오기 리스너
        this.deviceInfoReadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String androidId = Settings.Secure.getString(MainActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);
                //String deviceId = md5Encode(androidId);


                TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

                // Log.d("단말기 ID : ", tm.getDeviceId()); // 안드로이드 10 버전이후 부터는 사용불가 ㅠ
                // 안드로이드 10버전에 단말기정보 변경사항 참고 자료 https://brunch.co.kr/@huewu/9

                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                try {
                    Log.d("전화 번호 : ", tm.getLine1Number()); // 단말기의 번호가 있다면 번호가 출력되고 공기계같이 번호가 없는 단말기라면 null을 리턴한다.
                }
                catch (NullPointerException e)
                {
                    Log.d("에러","핸드폰 번호가 없다...."); // 그래서 null일 경우에 대한 Exception 처리를 해줘야한다.
                }
                
            }
        });

        this.callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tel = "tel:01055161153";
                startActivity(new Intent("android.intent.action.CALL",Uri.parse(tel)));
            }
        });

    }
    /*
        주소록 정보 가져오는 함수
    */
    public void contacts(){
        ContentResolver resolver = getApplication().getContentResolver();
        Uri phoneUri = ContactsContract.Contacts.CONTENT_URI;
        String[] projection = {ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME};
        Cursor cursor = resolver.query(phoneUri,projection, null,null, null);

        while (cursor.moveToNext()){
            try {
                String v_id = cursor.getString(0);
                //String v_display_name = cursor.getString(1);
                data.add(cursor.getString(1));
            }catch(Exception e) {
                System.out.println(e.toString());
            }
        }
        cursor.close();
    }
}