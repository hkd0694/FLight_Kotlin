package com.example.flashlight

import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager

class Torch(context: Context) {

    private var cameraId: String? = null

    //context의 getSystemService() 메서드는 안드로이드 시스템에서 제공하는 각종 서비스를 관리하는 매너저 클래스를 생성.
    //인자로 Context 클래스에 정의된 서비스를 정의한 상수를 지정한다. 이 메서드는 Object형을 반환하기 때문에
    // as 연산자를 사용하여 CameraService형으로 형변환을 한다.
    private val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager


    init{
        cameraId = getCameraId()
    }

    fun flashOn(){
        cameraManager.setTorchMode(cameraId.toString(),true)
    }

    fun flashOff(){
        cameraManager.setTorchMode(cameraId.toString(),false)
    }

    private fun getCameraId(): String? {
        val cameraIds = cameraManager.cameraIdList
        for(id in cameraIds){
            //각 ID 별로 세부 정보를 가지는 객체
            val info = cameraManager.getCameraCharacteristics(id)
            //플래시 가능 여부
            val flashAvailable = info.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)
            //카메라의 렌즈 방향
            val lensFacing = info.get(CameraCharacteristics.LENS_FACING)
            
            //플래시가 사용 가능하고 카메라가 기기의 뒷면을 향하고 있는 카메라의 ID를 찾는다면 id 반환 아니면 null 반환
            if(flashAvailable != null && flashAvailable && lensFacing != null && lensFacing == CameraCharacteristics.LENS_FACING_BACK){
                return id
            }
        }
        return null
    }
}
