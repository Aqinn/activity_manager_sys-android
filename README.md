# 活动管理系统 - 安卓端

本活动管理系统围绕基于人脸识别实现签到, 拓展出具有活动创建、加入活动、签到创建、签到查询等活动全流程管理系统。

# 说明
- 尝试添加 TNN 的 MobileFacenet 实现，失败，原因在于 TNN BinaryOp with scaler not supported。
- 本次上传用以备份当前版本所有代码，准备更新下一版本。
- 下一个版本将更换移动端推理框架 TNN -> NCNN，并且更换人脸检测模型 BlazeFace -> RetinaFace MobileNet0. 25。
- 总结，TNN 毕竟是新开源的框架，对于我这种新手来说没有很多博客以及开源代码的参考很难上手，即使写出来了 MobileFaceNet 推理代码，但是报错原因是 TNN 不支持某些 OP ，我没有修改模型的能力，只能暂时搁置。