package com.example.adressbookmodule;



import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button pusgBtn;
    private final int PERMISSION = 1;
    private List<String> data = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 23) { // 퍼미션 체크
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS}, PERMISSION);
        }


        this.pusgBtn = (Button) findViewById(R.id.main_btn_push);

        this.pusgBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("HardwareIds")
            @Override
            public void onClick(View v) {
                int cnt = 0;
                contacts();
                Iterator<String> itr = data.iterator();
                while (itr.hasNext()) {
                    Log.d(cnt + " : ", itr.next());
                    cnt++;
                }
                String androidId = Settings.Secure.getString(MainActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);
                //String deviceId = md5Encode(androidId);

                TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                Log.d("단말기 ID : ", tm.getDeviceId());
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
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
                    Log.d("전화 번호 : ", tm.getLine1Number());
                }
                catch (NullPointerException e)
                {
                    Log.d("에러","핸드폰 번호가 없다....");
                }


            }
        });

        // 기기 ID, 전화번호부 목록에서 이름만
        // 핸드폰 번호가 없을때 번호를 조회하려고하면 나오는 값이 뭘까?
    }
    /**
     * 주소록 정보 가져오기.
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