# Kotlin Study (7/9) - 2019/11/10

Kotlin을 공부하기 위해 간단한 앱부터 복잡한 앱까지 만들어 봄으로써 Kotlin에 대해 하기!

총 9개의 앱 중 일곱 번째 앱

프로젝트명 : FlashLight

기능

* 앱에서 스위치로 플래시를 켜고 끕니다.
  
* 위젯을 제공해 앱을 실행하지 않고도 플래시를 켜고 끌 수 잇습니다.
  

핵심 구성 요소

* CameraManager : 플래시를 켜는 기능을 제공하는 클래스
  
* Service : 화면이 없고 백그라운드에서 실행되는 컴포넌트
  
* App Widget : 런처에 배치하여 빠르게 앱 기능을 쓸 수 있게 하는 컴포넌트


라이브러리 설정

* Anko : 인텐트 다이얼로그, 고르 등을 구현하는 데 도움이 되는 라이브러리


## Service

1. 서비스 소개
2. 서비스의 생명 주기
3. 서비스로 손전등 기능 옮기기
4. 액티비티에서 서비스를 사용해 손전등 켜기
   
### Service 소개

서비스란 안드로이드의 4대 컴포넌트 중 하나로 화면이 없고 백그라운드에서 수행하는 작업을 작성하는 컴포넌트이다. 

플래시를 켜는 기능에 화면이 꼭 필요하지는 않다. 액티비티는 단순히 플래시를 켜고 끄는 인터페이스만을 제공한다. 




### 서비스 생명주기

서비스는 액티비티와 마찬가지로 생명주기용 콜백 메서드를 가지고 있다. 서비스를 시작으로 onCreate() 메서드가 호출되고 onStartCommand() 메서드가 호출되며 여기서 서비스의 동작을 코드로 작성한다. 서비스가 종료되면 onDestroy() 메서드가 호출된다.

>#### onCreate()

서비스가 생성될 때 호출되는 콜백 메서드이다. 초기화 등을 수행한다.

>#### onStartCommand()

서비스가 액티비티와 같은 다른 컴포넌트로부터 startService() 메서드로 호출되면 불리는 콜백 메서드이다. 실행할 작업을 여기에 작성하면 된다.

>#### onDestroy()

서비스 내부에서 stopSelf()를 호출하거나 외부에서 stopService()로 서비스를 종료하면 호출된다.

### 서비스로 손전등 기능 옮기기

```kotlin

class TorchService : Service() {

    private val torch:Torch by lazy {
        Torch(this)
    }
    
    private var isRunning = false

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            //앱에서 실행할 경우
            "on" -> {
                torch.flashOn()
                isRunning = true
            }
            "off" -> {
                torch.flashOff()
                isRunning = false
            }
            else -> {
                isRunning = !isRunning
                if(isRunning) {
                    torch.flashOn()
                }
                else {
                    torch.flashOff()
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
}


```

TorchService 서비스는 Service 클래스를 상속 받는다. 외부에서 startService() 메서드로 TorchService 서비스를 호출하면 onStartCommand() 콜백 메서드가 호출된다. 

서비스는 메모리 부족 등의 이류오 시스템에 의해서 강제로 종료될 수 있다.
onStartCommand() 메서드는 다음 중 하나를 반환한다. 이 값에 따라 시스템이 강제로 종료한 후에 시스템 자원이 회복되어 다시 서비스를 시작할 수 있을 때 어떻게 할지를 결정한다.

* START_STICKY -> null 인텐트로 다시 시작한다. 명령을 실행하지는 않지만 무기한으로 실행 중이며 작업을 기다리고 있는 미디어 플레이어와 비슷한 경우에 적합하다.
* START_NOT_STICKY -> 다시 시작하지 않음
* START_REDELIVER_INTENT -> 마지막 인텐트로 다시 시작한다. 능동적으로 수행 중인 파일 다운로드와 같은 서비스와 적합하다.

일반적인 경우에는 onStartCommand() 메서드를 호출하면 내부적으로 START_STICKY를 반환한다. 

### 액티비티에서 서비스를 사용해 손전등 켜기

```kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.intentFor

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //val torch = Torch(this)

        flashSwitch.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                startService(intentFor<TorchService>().setAction("on"))
            } else{
                startService(intentFor<TorchService>().setAction("off"))
            }
        }
    }
}

```
