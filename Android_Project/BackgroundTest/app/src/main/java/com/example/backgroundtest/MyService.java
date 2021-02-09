package com.example.backgroundtest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {

    private static final String TAG = "MyService";

    public MyService() {
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        // 서비스는 한번 실행되면 계속 실행된 상태로 있는다.
        // 따라서 서비스 특성상 intent를 받아서 처리하기에 적합하지 않다.
        // intent에 대한 처리는 onStartcommand()에서 처리해준다.
        Log.d(TAG,"onCreate() called");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"onStartCommand() called");
        if(intent == null)
        {
            return Service.START_STICKY; // 서비스가 종료되어도 자동으로 다시 실행 시키기!
        }
        else // intent가 null이 아니면.
        {

        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.d(TAG,"onDestroy() called");
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

}


/* 서비스란??

- 어플리케이션을 구성하는 4대 컴포넌트 중에 하나
- 액티비티처럼 사용자와 상호인터렉션 하는 컴포넌트가 아니라, 사용자 몰래 화면뒷단에서 동작하는 컴포넌트
- 백그라운드에서 동작하는 컴포넌트
- 액티비티와 다르게 UI가 존재하지 않는다.
- 서비스의 시작과 종료는 다른서비스, Activity, BroadCast Receiver를 포함한 다른 Application에서 가능.
- 만약 서비스가 실행되고 있는 상태라면, 안드로이드 OS 에선 왠만하면 프로세스를 죽이지 않고 관리한다.


서비스가 필요한 이유??

 - 예를 들어 Activity가 Pause되거나, 화면에 없어지는 경우(Stop) 음악이 계속흘러나와야 하는 할 때
    / 파일 다운로드 해야할 때

 - 화면 뒷단 즉--> 백그라운드 영역에서 작업을 해야하는 경우

 - 사용자로 부터의 특별한 지시가 필요하지 않은 경우.
    음악을 재생시킨다. --> 음악이 흘러나온다.
    (특별한 지시가 있을 때까지 음악을 재생시킨다.)

 - 어플리케이션이 실행 중이지 않을 때도 작업해야하는 경우 서비스를 사용.
    (어플리케이션이 실행 중일 때만 작업해야하는 경운 스레드권장)


서비스 구현 방법??

- startService
startService() 함수를 호출해서 서비스를 시작하면, 시작타입의 서비스가 실행된다. 시작타입의 서비스는 한 번 시작되면, 백그라운드에서 무한정 실행되지만, 보통의 경우는 일처리를 다 완료하면 서비스가 종료된다. 시작타입의 서비스는 호출한 곳에 결과값을 반환하지 않고 계속해서 서비스한다. (음악재생,파일다운로드 등)
하나의 프로세스안에서 동작하며, 패키지내 컴포넌트들과 유기적으로 통신하는 역할

- boundService
bindService() 함수를 통해서 서비스하면, 연결타입의 서비스가 실행된다. 연결타입의 서비스는 서버-클라이언트 형식의 구조를 취하기 때문에 호출자(액티비티)에서 서비스에게 어떤것을 요청하고 서비스는 그 요청을 처리한 후 결과값을 반환한다. 이러한 구조때문에 액티비티가 사라지면, 서비스도 자동적으로 destroy되면서 없어진다. 또 하나의 서비스에 여러개의 액티비티가 붙을 수 있다.
다른 프로세스들 간에서도 통신이 유기적으로 가능.

- intentService
일반 Service와 다르게 요청이 끝나면, 자동으로 서비스가 종료, 또 다른 서비스들과 다르게 onHandleIntent()함수 하나만을 통해 작업을 처리할 수 있다.

    +ps 시작타입과 연결타입 서비스를 동시에 작동시킬 수 있다.


 서비스 사용시 주의할 점!!

안드로이드는 리눅스 기반이기 때문에 프레임워크단에는 리눅스로 구현되어있다.
메모리관리 또한 리눅스 정책을 따른다. 즉 리눅스 커널에 의해서 관리된다.
결국 여러 프로세스들을 커널에서 관리한다고 짐작할 수 있다. 또 하나의 프로세스안에는 어플리케이션, 4대 컴포넌트, 스레드등을 구성하고있다. 이 말은 4대컴포넌트의 운명 또한 리눅스 커널에게 달려있다는 소리다.
만약 메모리부족이나, 과부하 등과 같은 현상이 발생했을 때 리눅스 커널이 프로세스를 강제로 종료시킬 수 있다.
안드로이드 어플리케이션적으로 설명하자면, 안드로이드 ui관련 처리는 메인스레드(UI스레드)라는 녀석이 처리하는데
서비스를 그냥 생성하면, 기본적으로 이 메인스레드에게 붙는다. 만약 서비스에게 많은 일을 할당하면, 메인스레드에게 부담이 된다.
따라서 서비스에게 많은 일을 해야하는 경우가 발생하면, 서비스의 일은 곧 메인스레드이고, 메인스레드는 그일을 처리하느냐 UI업데이트를 신경쓰지 못하고 사용자는 멈춰있는 어플리케이션을 마주하게된다. 구글은 사용자가 어플리케이션 UI업데이트를 오랫동안 기다려야하고 늦은 응답에 대해 무한정 기다려주는 정책을 펼치지 않는다. 이 때 ANR이라는 것을 발동시켜 강제로 프로세스를 죽여버린다.
그렇다면, ANR을 방지하기 위해... 만약 많은 일을 서비스가 처리해야한다면, 별도의 WorksThread를(백그라운드 스레드) 만들어 처리해야한다.

*/