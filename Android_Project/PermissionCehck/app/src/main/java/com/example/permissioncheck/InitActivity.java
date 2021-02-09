package com.example.permissioncheck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class InitActivity extends AppCompatActivity {

    private String[] permissionArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        permissionArrayInit();
        if(checkPermission1() == true) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else
        {
            finish();
        }
    }

    private void permissionArrayInit()
    {
        this.permissionArray = new String[]{
                Manifest.permission.WRITE_CONTACTS,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.GET_ACCOUNTS,
                Manifest.permission.INTERNET,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_CALENDAR,
                Manifest.permission.READ_CALENDAR
        };
    }

    private void samplePermission(){
        //if(ContextCompat.checkSelfPermission("컨텍스트 정보의 자리","요청할 권한")
        // == PackageManager.PERMISSION_GRANTED)
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            //이 영역은 Manifest.permission.ACCESS_FINE_LOCATION이 접근 승낙 상태일 때
        }else{
            //이 영역은 권한 접근이 거부되었을 때
            //접근이 거부되었을 때에 사용자에게 접근 권한 설정을 요구하는 다이얼로그를 띄우기 위해서
            //requestPermission을 쓴다. requestPermission(액티비티의 정보,String[]의 권한요청명령 ,request 코드)
            //여기서 리퀘스트 코드는 후에 onRequestPermissionResult() 메서드에 결과물이 전달될 시, 결과물들을 구분 짓는 인덱스
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
                //사용자가 다시 보지 않기에 체크를 하지 않고, 권한 설정을 거절한 이력이 있는 경우
            }else{
                //사용자가 다시 보지 않기에 체크하고, 권한 설정을 거절한 이력이 있는 경우
            }
            //사용자에게 권한 요청 다이얼로그를 띄우는데 만약 사용자가 다시 보지 않기에 체크를 했을 경우
            //곧바로 onRequestPermissionResult가 실행된다.
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},0);
        }
    }

    private boolean checkPermission(){
        //if(ContextCompat.checkSelfPermission("컨텍스트 정보의 자리","요청할 권한") != PackageManager.PERMISSION_GRANTED)
        // checkSelfPermission()의 리턴값은 요청한 권한이 수락되었을때 PERMISSION_GRANTED를 리턴한다.
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED // Manifest.permission.WRITE_CONTACTS가 거부되었을 때
        && ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED // Manifest.permission.READ_CONTACTS 거부되었을 때
        && ContextCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED // Manifest.permission.READ_PHONE_STATE 거부되었을 때
        && ContextCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED // Manifest.permission.CALL_PHONE 거부되었을 때
        && ContextCompat.checkSelfPermission(this,Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED // Manifest.permission.GET_ACCOUNTS 거부되었을 때
        && ContextCompat.checkSelfPermission(this,Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED // Manifest.permission.INTERNET 거부되었을 때
        && ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED // Manifest.permission.RECORD_AUDIO 거부되었을 때
        && ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED // Manifest.permission.WRITE_CALENDAR 거부되었을 때
        && ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED) {// Manifest.permission.READ_CALENDAR 거부되었을 때
            // 모든 권한이 수락되었을 때 MainActivity로 넘어간다.
            return false;
        }
        else{
            //이 영역은 권한이 거부되었을 때
            //권한이 거부되었을 때에 사용자에게 권한 설정을 요구하는 다이얼로그를 띄우기 위해서
            //requestPermission을 쓴다. requestPermission(액티비티의 정보,String[]의 권한요청명령 ,request 코드)
            //여기서 리퀘스트 코드는 후에 onRequestPermissionResult() 메서드에 결과물이 전달될 시, 결과물들을 구분 짓는 인덱스
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_CONTACTS)){
                //사용자가 다시 보지 않기에 체크를 하지 않고, 권한 설정을 거절한 이력이 있는 경우
                // 이 경우 계속해서 권한은 묻는건 사용자의 입장이 곤란할 수 있지만 그렇다고 사용에 꼭 필요한 권한일 경우 묻지 않을 수 없다.
                // 그렇기 때문에 한번 취소했던 이력이 있는 경우 이것에 대해 설명해주는 Notice를 주는 등, 여러가지 작업을 생각해서 작성해보자.

            }else{
                //사용자가 다시 보지 않기에 체크하고, 권한 설정을 거절한 이력이 있는 경우

            }
            //사용자에게 권한 요청 다이얼로그를 띄우는데 만약 사용자가 다시 보지 않기에 체크를 했을 경우
            //곧바로 onRequestPermissionResult가 실행된다.
            ActivityCompat.requestPermissions(this,permissionArray,0);
            return true;
        }
    }
    private boolean checkPermission1(){
        //if(ContextCompat.checkSelfPermission("컨텍스트 정보의 자리","요청할 권한") != PackageManager.PERMISSION_GRANTED)
        // checkSelfPermission()의 리턴값은 요청한 권한이 수락되었을때 PERMISSION_GRANTED를 리턴한다.
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)
        {
            // 모든 권한이 수락되었을 때 MainActivity로 넘어간다.
            return false;
        }
        else{
            //이 영역은 권한이 거부되었을 때
            //권한이 거부되었을 때에 사용자에게 권한 설정을 요구하는 다이얼로그를 띄우기 위해서
            //requestPermission을 쓴다. requestPermission(액티비티의 정보,String[]의 권한요청명령 ,request 코드)
            //여기서 리퀘스트 코드는 후에 onRequestPermissionResult() 메서드에 결과물이 전달될 시, 결과물들을 구분 짓는 인덱스
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},0);

            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.RECORD_AUDIO)){
                //사용자가 다시 보지 않기에 체크를 하지 않고, 권한 설정을 거절한 이력이 있는 경우
                // 이 경우 계속해서 권한은 묻는건 사용자의 입장이 곤란할 수 있지만 그렇다고 사용에 꼭 필요한 권한일 경우 묻지 않을 수 없다.
                // 그렇기 때문에 한번 취소했던 이력이 있는 경우 이것에 대해 설명해주는 Notice를 주는 등, 여러가지 작업을 생각해서 작성해보자.
                Toast.makeText(this.getApplicationContext(),"해당 권한을 승인하지 않으면 프로그램을 사용할 수 없습니다.", Toast.LENGTH_SHORT);
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},0);
            }else{
                //사용자가 다시 보지 않기에 체크하고, 권한 설정을 거절한 이력이 있는 경우
                Log.d("여기","사용자가 다시 보지 않기에 체크하고, 권한 설정을 거절한 이력이 있는 경우");
            }
            //사용자에게 권한 요청 다이얼로그를 띄우는데 만약 사용자가 다시 보지 않기에 체크를 했을 경우
            //곧바로 onRequestPermissionResult가 실행된다.
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //requestPermission 메서드에서 리퀘스트 코드로 지정한, 마지막 매개변수에 0을 넣어 줬으므로

        if(requestCode == 0){
            // requestPermission의 두번째 매개변수는 배열이므로 아이템이 여러개 있을 수 있기 때문에 결과를 배열로 받는다.
            // 요청 퍼미션이 9개 이므로 index는 0~8까지 사용한다.
            if(grantResults.length > 0){
                // 해당 권한이 승낙된 경우.

            }else{
                // 해당 권한이 거절된 경우.
                // 우리 프로그램의 경우 주소록을 제외한 음성,계정,전화,캘린더,인터넷,기기정보를 거절한 경우 프로그램 종료하게 만든다.
                // 왜? 그럼 우리 프로그램을 쓰는 이유가 없기 때문.
                Log.d("여기","종료");
            }
        }
    }
}