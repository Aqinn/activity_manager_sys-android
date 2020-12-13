# 活动管理系统 - 安卓端

本活动管理系统围绕基于人脸识别实现签到, 拓展出具有活动创建、加入活动、签到创建、签到查询等活动全流程管理系统。

# 克隆步骤

## 1. clone 本项目到本地

具体操作略

## 2. 添加 NCNN 预编译库

**添加路径: app/src/main/jni/ncnn-android-vulkan-lib**

自行 build, 参考 https://github.com/Tencent/ncnn#howto
或
下载 Tencent/ncnn 已经预构建好的预编译库 https://github.com/Tencent/ncnn/releases
本人采用预构建的版本: android ios 预编译库 20201208 0508315/ncnn-android-vulkan-lib.zip

## 3. 添加 OpenCV 动态链接库

**添加路径: app/src/main/jniLibs**

1. 下载 OpenCV-android-sdk
   下载地址(官网): https://opencv.org/releases.html, 下载稳定版本3.4.3, 直接点击Android pack进行下载, 下载后解压即可.
2. 解压并拷贝动态库
   解压 OpenCV-android-sdk 后, 其子目录路径 sdk/native/libs 底下的所有文件夹拷贝至 app/src/main/jniLibs 底下(自行创建 jniLibs文件夹)

# 4. Build and Run

