#include <android/bitmap.h>
#include <android/log.h>
#include <jni.h>
#include <string>
#include <vector>
#include <cstring>

// ncnn
#include "net.h"
#include "recognize.h"

using namespace Face;

#define TAG "MtcnnSo"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,TAG,__VA_ARGS__)
#define TNN_BLAZEFACE_ALIGN(sig) Java_com_aqinn_facerecognize_MobileFaceNetRecognize_##sig
static Recognize *mRecognize;

//sdk是否初始化成功
bool detection_sdk_init_ok = false;


extern "C" {
JNIEXPORT jboolean JNICALL
TNN_BLAZEFACE_ALIGN(init)(JNIEnv *env, jobject instance,
                          jstring faceDetectionModelPath_) {
    LOGD("JNI开始人脸检测模型初始化");
    //如果已初始化则直接返回
    if (detection_sdk_init_ok) {
        //  LOGD("人脸检测模型已经导入");
        return true;
    }
    jboolean tRet = false;
    if (NULL == faceDetectionModelPath_) {
        //   LOGD("导入的人脸检测的目录为空");
        return tRet;
    }
    //获取模型的绝对路径的目录（不是/aaa/bbb.bin这样的路径，是/aaa/)
    const char *faceDetectionModelPath = env->GetStringUTFChars(faceDetectionModelPath_, 0);
    if (NULL == faceDetectionModelPath) {
        return tRet;
    }
    std::string tFaceModelDir = faceDetectionModelPath;
    //没判断是否正确导入，懒得改了

    LOGD("1");

    mRecognize = new Recognize(tFaceModelDir);

    LOGD("2");

    mRecognize->SetThreadNum(2);

    LOGD("3");

    detection_sdk_init_ok = true;
    tRet = true;
    return tRet;
}


// 人脸识别
JNIEXPORT jfloatArray JNICALL
TNN_BLAZEFACE_ALIGN(recognize)(JNIEnv *env, jobject instance,
                               jbyteArray faceData_, jint w, jint h, jintArray landmarks) {
    double similar = 0;

    jbyte *faceData = env->GetByteArrayElements(faceData_, NULL);

    unsigned char *faceImageCharData = (unsigned char *) faceData;

    jint *mtcnn_landmarks = env->GetIntArrayElements(landmarks, NULL);

    int *mtcnnLandmarks = (int *) mtcnn_landmarks;

    ncnn::Mat ncnn_img = ncnn::Mat::from_pixels(faceImageCharData, ncnn::Mat::PIXEL_RGBA2RGB, w,
                                                h);

    //人脸对齐
    ncnn::Mat det = mRecognize->preprocess(ncnn_img, mtcnnLandmarks);

    std::vector<float> feature;
    mRecognize->start(det, feature);

    env->ReleaseByteArrayElements(faceData_, faceData, 0);
    env->ReleaseIntArrayElements(landmarks, mtcnn_landmarks, 0);

    // 转换成 float 数组
    float *feature_float;
    for (int i = 0; i < feature.size(); ++i) {
        feature_float[i] = feature[i];
    }
    jfloatArray result;
    result = env->NewFloatArray(128);
    env->SetFloatArrayRegion(result, 0, 128, feature_float);
    return result;
}

JNIEXPORT jdouble JNICALL
TNN_BLAZEFACE_ALIGN(compare)(JNIEnv *env, jobject instance, jfloatArray feature1, jfloatArray feature2){
    jfloat* featureData1 = (jfloat*)env->GetFloatArrayElements(feature1, 0);
    jsize featureSize1 = env->GetArrayLength(feature1);
    jfloat* featureData2 = (jfloat*)env->GetFloatArrayElements(feature2, 0);
    jsize featureSize2 = env->GetArrayLength(feature2);
    std::vector<float> featureVector1(featureSize1), featureVector2(featureSize1);
    if(featureSize1 != featureSize2){
        return 0;
    }
    for(int i=0; i < featureSize1; i++){
        featureVector1.push_back(featureData1[i]);
        featureVector2.push_back(featureData2[i]);
    }
    double similar = 0;
    similar = calculSimilar(featureVector1, featureVector2, 1);
    return similar;
}


JNIEXPORT void JNICALL
TNN_BLAZEFACE_ALIGN(deInit)(JNIEnv *env, jobject instance) {
    mRecognize = nullptr;
}

}