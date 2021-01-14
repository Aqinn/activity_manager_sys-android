package com.aqinn.facerecognize;

/**
 * Created by hasee on 2017/12/19.
 */

public class MobileFaceNetRecognize {

    //人脸检测模型导入
    public native boolean init(String faceDetectionModelPath);

    //人脸检测模型反初始化
    public native void deInit();

    //人脸识别
    public native float[] recognize(byte[] faceData,int w,int h, int[] landmarks);

    //人脸验证
    public native double compare(float[] feature1, float[] feature2);

    public MobileFaceNetRecognize() {
        System.loadLibrary("Face");
    }
}
