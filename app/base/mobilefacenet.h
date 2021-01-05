//
// Created by 钟兆锋 on 2021/1/5.
//

#ifndef ACTMANAGERSYSANDROID_MOBILEFACENET_H
#define ACTMANAGERSYSANDROID_MOBILEFACENET_H

#include "jni.h"
#include <cmath>
#include <fstream>
#include <sstream>
#include <chrono>
#include "tnn/core/macro.h"
#include "tnn/core/tnn.h"
#include "tnn/utils/blob_converter.h"
#include "tnn/utils/mat_utils.h"
#include "tnn/utils/dims_vector_utils.h"
#include <algorithm>

namespace MFN {

    class MobileFaceNet {
    public:
        MobileFaceNet(const std::string &modelPath);

        ~MobileFaceNet();

        void start(TNN_NS::Mat &inputImg, std::vector<float> &feature128);

        void getAffineMatrix(float *src_5pts, const float *dst_5pts, float *M);

        TNN_NS::Mat preProcess(TNN_NS::Mat img, int *info);

    private:
        void RecogNet(TNN_NS::Mat &img_);

        void normalize(std::vector<float> &feature);

        std::shared_ptr<TNN_NS::TNN> net_ = nullptr;
        std::shared_ptr<TNN_NS::Instance> instance_ = nullptr;
        TNN_NS::DeviceType device_type_ = TNN_NS::DEVICE_ARM;

        std::vector<float> feature_out;
        int threadnum = 1;
    };

    double calculSimilar(std::vector<float> &v1, std::vector<float> &v2, int distance_metric);

#ifdef __cplusplus

    static std::string myfdLoadFile(std::string path) {
        std::ifstream file(path, std::ios::in);
        if (file.is_open()) {
            file.seekg(0, file.end);
            int size = file.tellg();
            char *content = new char[size];
            file.seekg(0, file.beg);
            file.read(content, size);
            std::string fileContent;
            fileContent.assign(content, size);
            delete[] content;
            file.close();
            return fileContent;
        } else {
            return "";
        }
    }

    static char *myjstring2string(JNIEnv *env, jstring jstr) {
        char *rtn = NULL;
        jclass clsstring = env->FindClass("java/lang/String");
        jstring strencode = env->NewStringUTF("utf-8");
        jmethodID mid = env->GetMethodID(clsstring, "getBytes", "(Ljava/lang/String;)[B");
        jbyteArray barr = (jbyteArray) env->CallObjectMethod(jstr, mid, strencode);
        jsize alen = env->GetArrayLength(barr);
        jbyte *ba = env->GetByteArrayElements(barr, JNI_FALSE);
        if (alen > 0) {
            rtn = (char *) malloc(alen + 1);
            memcpy(rtn, ba, alen);
            rtn[alen] = 0;
        }
        env->ReleaseByteArrayElements(barr, ba, 0);
        return rtn;
    }

#endif

}

#endif //ACTMANAGERSYSANDROID_MOBILEFACENET_H
