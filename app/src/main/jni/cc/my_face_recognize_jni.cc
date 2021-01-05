//
// Created by 钟兆锋 on 2021/1/5.
//

#include <android/bitmap.h>
#include <android/log.h>
#include <jni.h>
#include <string>
#include <vector>
#include <cstring>
#include "mobilefacenet.h"

using namespace MFN;

#define TAG "mobilefacenet"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,TAG,__VA_ARGS__)

static MobileFaceNet *mRecognize;
//sdk是否初始化成功
bool detection_sdk_init_ok = false;


extern "C" {


JNIEXPORT jint JNICALL
Java_com_aqinn_actmanagersysandroid_FaceRecognize_init(JNIEnv *env, jobject thiz,
                                                       jstring modelPath) {
    std::string modelPathStr(myjstring2string(env, modelPath));
    mRecognize = new MobileFaceNet(modelPathStr);
    return 0;
}


// 人脸识别
JNIEXPORT jfloatArray JNICALL
Java_com_aqinn_actmanagersysandroid_FaceRecognize_faceRecognize(JNIEnv *env, jobject thiz,
                                                                jobject imageSource, jint w, jint h,
                                                                jintArray landmarks) {
    if (!detection_sdk_init_ok)
        return nullptr;

    AndroidBitmapInfo sourceInfocolor;
    void *sourcePixelscolor;

    if (AndroidBitmap_getInfo(env, imageSource, &sourceInfocolor) < 0) {
        return nullptr;
    }

    if (sourceInfocolor.format != ANDROID_BITMAP_FORMAT_RGB_565) {
        return nullptr;
    }

    if (AndroidBitmap_lockPixels(env, imageSource, &sourcePixelscolor) < 0) {
        return nullptr;
    }

    TNN_NS::DimsVector target_dims = {1, 3, h, w};
    auto input_mat = std::make_shared<TNN_NS::Mat>(TNN_NS::DEVICE_ARM, TNN_NS::N8UC4, target_dims,
                                                   sourcePixelscolor);

    jint *mtcnn_landmarks = env->GetIntArrayElements(landmarks, NULL);
    int *mtcnnLandmarks = (int *) mtcnn_landmarks;
    //人脸对齐
    TNN_NS::Mat det1 = mRecognize->preProcess(*input_mat, mtcnnLandmarks);
    std::vector<float> feature;
    mRecognize->start(det1, feature);
    env->ReleaseIntArrayElements(landmarks, mtcnn_landmarks, 0);


    float *feature_float;
    for (int i = 0; i < feature.size(); ++i) {
        feature_float[i] = feature[i];
    }
    jfloatArray result;
    result = env->NewFloatArray(128);
    env->SetFloatArrayRegion(result, 0, 128, feature_float);
    return result;
}

JNIEXPORT void JNICALL
Java_com_aqinn_actmanagersysandroid_FaceRecognize_deinit(JNIEnv *env, jobject thiz) {
    mRecognize = nullptr;
}

}


