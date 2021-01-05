//
// Created by 钟兆锋 on 2021/1/5.
//

#include <android/bitmap.h>
#include "mobilefacenet.h"

namespace MFN {


    /**
     * 传入模型和模型参数，构建模型
     * 构造成功后 net_ 和 instance_ 会被赋值
     * TODO 把初始化状态返回出来
     * @param modelPath
     */
    MobileFaceNet::MobileFaceNet(const std::string &modelPath) {
        std::string paramContent, binContent;
        std::string binPathStr(modelPath + "/mobilefacenet.bin");
        std::string paramPathStr(modelPath + "/mobilefacenet.param");
        paramContent = myfdLoadFile(paramPathStr);
        binContent = myfdLoadFile(binPathStr);
        TNN_NS::Status status;
        TNN_NS::ModelConfig config;
        config.model_type = TNN_NS::MODEL_TYPE_NCNN;
        config.params = {paramContent, binContent};
        auto net = std::make_shared<TNN_NS::TNN>();
        status = net->Init(config);
        net_ = net;
        device_type_ = TNN_NS::DEVICE_ARM;
        TNN_NS::InputShapesMap shapeMap;
        TNN_NS::NetworkConfig network_config;
        network_config.library_path = {""};
        network_config.device_type = device_type_;
        auto instance = net_->CreateInst(network_config, status, shapeMap);
        instance_ = instance;
        if (status != TNN_NS::TNN_OK) {
            LOGE("TNN init failed %d", (int) status);
        }
    }

    void MobileFaceNet::start(TNN_NS::Mat &inputImg, std::vector<float> &feature128){
        RecogNet(inputImg);
        feature128 = feature_out;
    }

    void MobileFaceNet::getAffineMatrix(float *src_5pts, const float *dst_5pts, float *M){
        float src[10], dst[10];
        memcpy(src, src_5pts, sizeof(float)*10);
        memcpy(dst, dst_5pts, sizeof(float)*10);

        float ptmp[2];
        ptmp[0] = ptmp[1] = 0;
        for (int i = 0; i < 5; ++i) {
            ptmp[0] += src[i];
            ptmp[1] += src[5+i];
        }
        ptmp[0] /= 5;
        ptmp[1] /= 5;
        for (int i = 0; i < 5; ++i) {
            src[i] -= ptmp[0];
            src[5+i] -= ptmp[1];
            dst[i] -= ptmp[0];
            dst[5+i] -= ptmp[1];
        }

        float dst_x = (dst[3]+dst[4]-dst[0]-dst[1])/2, dst_y = (dst[8]+dst[9]-dst[5]-dst[6])/2;
        float src_x = (src[3]+src[4]-src[0]-src[1])/2, src_y = (src[8]+src[9]-src[5]-src[6])/2;
        float theta = atan2(dst_x, dst_y) - atan2(src_x, src_y);

        float scale = sqrt(pow(dst_x, 2) + pow(dst_y, 2)) / sqrt(pow(src_x, 2) + pow(src_y, 2));
        float pts1[10];
        float pts0[2];
        float _a = sin(theta), _b = cos(theta);
        pts0[0] = pts0[1] = 0;
        for (int i = 0; i < 5; ++i) {
            pts1[i] = scale*(src[i]*_b + src[i+5]*_a);
            pts1[i+5] = scale*(-src[i]*_a + src[i+5]*_b);
            pts0[0] += (dst[i] - pts1[i]);
            pts0[1] += (dst[i+5] - pts1[i+5]);
        }
        pts0[0] /= 5;
        pts0[1] /= 5;

        float sqloss = 0;
        for (int i = 0; i < 5; ++i) {
            sqloss += ((pts0[0]+pts1[i]-dst[i])*(pts0[0]+pts1[i]-dst[i])
                       + (pts0[1]+pts1[i+5]-dst[i+5])*(pts0[1]+pts1[i+5]-dst[i+5]));
        }

        float square_sum = 0;
        for (int i = 0; i < 10; ++i) {
            square_sum += src[i]*src[i];
        }
        for (int t = 0; t < 200; ++t) {
            _a = 0;
            _b = 0;
            for (int i = 0; i < 5; ++i) {
                _a += ((pts0[0]-dst[i])*src[i+5] - (pts0[1]-dst[i+5])*src[i]);
                _b += ((pts0[0]-dst[i])*src[i] + (pts0[1]-dst[i+5])*src[i+5]);
            }
            if (_b < 0) {
                _b = -_b;
                _a = -_a;
            }
            float _s = sqrt(_a*_a + _b*_b);
            _b /= _s;
            _a /= _s;

            for (int i = 0; i < 5; ++i) {
                pts1[i] = scale*(src[i]*_b + src[i+5]*_a);
                pts1[i+5] = scale*(-src[i]*_a + src[i+5]*_b);
            }

            float _scale = 0;
            for (int i = 0; i < 5; ++i) {
                _scale += ((dst[i]-pts0[0])*pts1[i] + (dst[i+5]-pts0[1])*pts1[i+5]);
            }
            _scale /= (square_sum*scale);
            for (int i = 0; i < 10; ++i) {
                pts1[i] *= (_scale / scale);
            }
            scale = _scale;

            pts0[0] = pts0[1] = 0;
            for (int i = 0; i < 5; ++i) {
                pts0[0] += (dst[i] - pts1[i]);
                pts0[1] += (dst[i+5] - pts1[i+5]);
            }
            pts0[0] /= 5;
            pts0[1] /= 5;

            float _sqloss = 0;
            for (int i = 0; i < 5; ++i) {
                _sqloss += ((pts0[0]+pts1[i]-dst[i])*(pts0[0]+pts1[i]-dst[i])
                            + (pts0[1]+pts1[i+5]-dst[i+5])*(pts0[1]+pts1[i+5]-dst[i+5]));
            }
            if (abs(_sqloss - sqloss) < 1e-2) {
                break;
            }
            sqloss = _sqloss;
        }

        for (int i = 0; i < 5; ++i) {
            pts1[i] += (pts0[0] + ptmp[0]);
            pts1[i+5] += (pts0[1] + ptmp[1]);
        }

        M[0] = _b*scale;
        M[1] = _a*scale;
        M[3] = -_a*scale;
        M[4] = _b*scale;
        M[2] = pts0[0] + ptmp[0] - scale*(ptmp[0]*_b + ptmp[1]*_a);
        M[5] = pts0[1] + ptmp[1] - scale*(-ptmp[0]*_a + ptmp[1]*_b);
    }

    TNN_NS::Mat MobileFaceNet::preProcess(TNN_NS::Mat img, int *info){
        int image_w = 112; //96 or 112
        int image_h = 112;

        float dst[10] = {30.2946, 65.5318, 48.0252, 33.5493, 62.7299,
                         51.6963, 51.5014, 71.7366, 92.3655, 92.2041};

        if (image_w == 112)
            for (int i = 0; i < 5; i++)
                dst[i] += 8.0;

        float src[10];
        for (int i = 0; i < 10; i++) {
            src[i] = info[i];
        }

        float M[6];
        getAffineMatrix(src, dst, M);

        TNN_NS::WarpAffineParam param;
        param.transform[0][0] = M[0];
        param.transform[0][1] = M[1];
        param.transform[0][2] = M[2];
        param.transform[1][0] = M[3];
        param.transform[1][1] = M[4];
        param.transform[1][2] = M[5];
        // static Status WarpAffine(Mat& src, Mat& dst, WarpAffineParam param, void* command_queue);
        // Mat(DeviceType device_type, MatType mat_type, DimsVector shape_dims);
        TNN_NS::Mat out(img.GetDeviceType(), img.GetMatType(), {1, 3, image_h, image_w}, img.GetData());
        TNN_NS::MatUtils::WarpAffine(img, out, param, NULL);
        return out;
    }

    void MobileFaceNet::RecogNet(TNN_NS::Mat &img_){
        // 1. 设置输入图像
        std::shared_ptr<TNN_NS::Mat> input_mat = std::make_shared<TNN_NS::Mat>(img_);
        TNN_NS::MatConvertParam input_cvt_param;
        input_cvt_param.scale = {1.0 / (255 * 0.229), 1.0 / (255 * 0.224), 1.0 / (255 * 0.225), 0.0};
        input_cvt_param.bias  = {-0.485 / 0.229, -0.456 / 0.224, -0.406 / 0.225, 0.0};
        auto status = instance_->SetInputMat(input_mat, input_cvt_param);

        // 2. 进行前向传播
        status = instance_->ForwardAsync(nullptr);

        // 3. 获取计算结果
        std::shared_ptr<TNN_NS::Mat> output_features = nullptr;
        status = instance_->GetOutputMat(output_features);

        feature_out.resize(128);
        auto *scores_data = (float *)output_features->GetData();
        for (int j = 0; j < 128; j++)
        {
            feature_out[j] = scores_data[j];
        }
        normalize(feature_out);  // feature normalize(l2)
    }

    void MobileFaceNet::normalize(std::vector<float> &feature){
        float sum = 0;
        for (auto it = feature.begin(); it != feature.end(); it++) {
            sum += (float)*it * (float)*it;
        }
        sum = sqrt(sum);
        for (auto it = feature.begin(); it != feature.end(); it++) {
            *it /= sum;
        }
    }


    double MFN::calculSimilar(std::vector<float> &v1, std::vector<float> &v2,
                                        int distance_metric){
        if(v1.size() != v2.size()||!v1.size())
            return 0;
        double ret = 0.0, mod1 = 0.0, mod2 = 0.0, dist = 0.0, diff = 0.0;

        if (distance_metric == 0) {         // Euclidian distance
            for (std::vector<double>::size_type i = 0; i != v1.size(); ++i) {
                diff = v1[i] - v2[i];
                dist += (diff * diff);
            }
            dist = sqrt(dist);
        }
        else {                              // Distance based on cosine similarity
            for (std::vector<double>::size_type i = 0; i != v1.size(); ++i) {
                ret += v1[i] * v2[i];
                mod1 += v1[i] * v1[i];
                mod2 += v2[i] * v2[i];
            }
            dist = ret / (sqrt(mod1) * sqrt(mod2));
        }
        return dist;
    }

    MobileFaceNet::~MobileFaceNet() {
        net_ = nullptr;
        instance_ = nullptr;
    }

}