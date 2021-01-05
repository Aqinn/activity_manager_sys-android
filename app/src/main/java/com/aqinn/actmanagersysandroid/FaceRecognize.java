package com.aqinn.actmanagersysandroid;

import android.graphics.Bitmap;

/**
 * @author Aqinn
 * @date 2021/1/5 11:59 AM
 */
public class FaceRecognize {

    public native int init(String modelPath);

    //人脸识别
    public native float[] faceRecognize(Bitmap imageSource, int w, int h, int[] landmarks);


    public native void deinit();


}
