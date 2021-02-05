package com.example.unittestmodule;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener
{
    // stt 모듈 변수
    Intent intent;
    SpeechRecognizer mRecognizer;
    Button sttBtn;
    TextView textView;
    final int PERMISSION = 1;

    // tts 모듈 변수
    private TextToSpeech tts;
    private Button btn_Speak;

    // 일정관리 변수1
    private Button accountRead_btn;
    private Button eventAdd_btn;
    private EditText eventName_et;
    private EditText eventContent_et;
    private TextView eventList_tv;
    private int REQUEST_CODE;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if ( Build.VERSION.SDK_INT >= 23 )
        { // 퍼미션 체크
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET, Manifest.permission.RECORD_AUDIO},PERMISSION);
        }
        // xml의 버튼과 텍스트 뷰 연결
        textView = (TextView)findViewById(R.id.main_tv_sttResult);
        this.sttBtn = (Button) findViewById(R.id.main_btn_sttStart);

        // RecognizerIntent 객체 생성
        intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"ko-KR");

        // 버튼을 클릭 이벤트 - 객체에 Context와 listener를 할당한 후 실행
        this.sttBtn.setOnClickListener(v ->
        {
            mRecognizer=SpeechRecognizer.createSpeechRecognizer(this);
            mRecognizer.setRecognitionListener(listener);
            mRecognizer.startListening(intent);
        });

        // tts 모듈
        this.tts = new TextToSpeech(this, this);
        this.btn_Speak = findViewById(R.id.main_btn_ttsStart);
        //txtText = findViewById(R.id.sttResult);

        this.btn_Speak.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v)
            {
                speakOut();
            }
        });

        // 일정관리
        this.eventName_et = (EditText) findViewById(R.id.main_et_eventName);
        this.eventContent_et = (EditText) findViewById(R.id.main_et_eventContent);
        this.eventAdd_btn = (Button)findViewById(R.id.main_btn_eventAdd);
        this.accountRead_btn = (Button)findViewById(R.id.main_btn_accountRead);
        this.eventList_tv = (TextView)findViewById(R.id.main_tv_eventList);

         /* 사용자 계정정보 얻어오기
                 AccountManager를 통해 핸드폰 안의 사용자 계정을 가지고 온다. */
        /* ※ android O(오레오)시상 부터는 manifests의 GET_ACCOUNTS 권한만으로는 앱에서 계정정보를 불러올 수 없다...?
         아니 그럼 api를 수정했어야 되는거 아닙니까?
         그래서 AccountManager.newChooseAccountIntent() 또는 인증자의 특정한 메소드를 사용해야 한다. 그 후에 AccountManager.getAccounts()를 호출하여 계정에 접근할 수 있다.*/
        // 결과 : 이부분도 꼭 필요한 부분이다 밑에 intent에서 한번 계정 정보를 불러왔다면 이 코드만으로도 계정 목록을 불러올 수 있다.
        AccountManager acctMgr = AccountManager.get(this); // 사용자계정 전부를 불러온다.
        Account[] acctArray = acctMgr.getAccounts(); // 사용자 계정들을 배열에 입력
        int acctCnt = acctArray.length;
        String acctName = ""; // 사용자 이름
        String acctType = ""; // 계정 유형
        int row = 0; // 반복문에 사용할 변수
        Account acct; // 필요한 사용자 계정을 입력할 때 쓸 String 변수

                /* AccountManager를 이용해 사용자가 핸드폰에 쓰고 있는 모든 계정, 앱과 연동 되어있는
                계정들을 전부 불러오고 그걸 Account의 배열에 입력한다. 이를 통해서 필요한 계정정보를 찾을 준비가 됨. */

        // 핸드폰에 어떤 계정이 어떠한 유형으로 있는지 알아보는 코드
        while( row < acctCnt)
        {
            acct = acctArray[row];
            if(acct.type.equals("com.google"))
            {
                acctName = acct.name;
                acctType = acct.type;
                break;
            }
            row ++;
        }
        // Log.i("test","이름 : " + acctName + "계정 유형 : " + acctType);
        eventList_tv.setText(acctName + " " + acctType);



        this.accountRead_btn.setOnClickListener(new  View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onClick(View v){
                chooseAccountIntent();
            }
            //  이 부분이 계정 정보를 불러오고나서 계정 목록을 intent로 창을 띄어서 선택 받게 하는 부분 (아마 어플리케이션을 제일 처음 켰을때 해야되는 부분이라고 생각된다.)
            //  계정정보 불러오는 함수
            @RequiresApi(api = Build.VERSION_CODES.M)
            private void chooseAccountIntent() {
                Intent intent = AccountManager.newChooseAccountIntent(
                        null, null, new String[]{"com.google"}, null,
                        null, null, null);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }
    // intent에서 선택한 결과를 가져오는 함수
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (data != null) {
                Bundle extras = data.getExtras();
                final String accountName = extras.getString(AccountManager.KEY_ACCOUNT_NAME);
                final String accountType = extras.getString(AccountManager.KEY_ACCOUNT_TYPE);
                // Log.d("", "Account Name: " + accountName);
                // Log.d("", "Account Type: " + accountType);
                eventList_tv.setText("계정 : " + accountName + "\n유형 : " + accountType);
            }
        }
    }

    // RecognizerIntent 객체에 할당할 listener 생성
    private RecognitionListener listener = new RecognitionListener()
    {
        @Override
        public void onReadyForSpeech(Bundle params)
        {
            Toast.makeText(getApplicationContext(),"음성인식을 시작합니다.",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onBeginningOfSpeech() {}

        @Override
        public void onRmsChanged(float rmsdB) {}

        @Override
        public void onBufferReceived(byte[] buffer) {}

        @Override
        public void onEndOfSpeech() {}

        @Override
        public void onError(int error)
        {
            String message;
            switch (error)
            {
                case SpeechRecognizer.ERROR_AUDIO:
                    message = "오디오 에러";
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    message = "클라이언트 에러";
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    message = "퍼미션 없음";
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    message = "네트워크 에러";
                    break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    message = "네트웍 타임아웃";
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    message = "찾을 수 없음";
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    message = "RECOGNIZER가 바쁨";
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    message = "서버가 이상함";
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    message = "말하는 시간초과";
                    break;
                default:
                    message = "알 수 없는 오류임";
                    break;
            }
            Toast.makeText(getApplicationContext(), "에러가 발생하였습니다. : " + message,Toast.LENGTH_SHORT).show();
        }

        @Override public void onResults(Bundle results)
        {
            // 말을 하면 ArrayList에 단어를 넣고 textView에 단어를 이어줍니다.
            ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            for(int i = 0; i < matches.size() ; i++)
            {
                textView.setText(matches.get(i));
            }
        }

        @Override
        public void onPartialResults(Bundle partialResults) {}

        @Override
        public void onEvent(int eventType, Bundle params) {}
    };


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void speakOut()
    {
        CharSequence text = textView.getText();
        this.tts.setPitch((float) 0.6);
        this.tts.setSpeechRate((float) 1.0);
        this.tts.speak(text,TextToSpeech.QUEUE_FLUSH,null,"id1");
    }
    @Override
    public void onDestroy()
    {
        if (this.tts != null)
        {
            this.tts.stop();
            this.tts.shutdown();
        }
        super.onDestroy();
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    @Override
    public void onInit(int status)
    {

        if (status == TextToSpeech.SUCCESS)
        {
            int result = this.tts.setLanguage(Locale.KOREA);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
            {
                Log.e("TTS", "This Language is not supported");
            }
            else
            {
                this.btn_Speak.setEnabled(true);
                speakOut();
            }
        }
        else
        {
            Log.e("TTS", "Initilization Failed!");
        }
    }
}