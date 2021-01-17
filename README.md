# 活动管理系统 - 安卓端
[![](https://img.shields.io/badge/license-MIT-blue)](https://github.com/Aqinn/activity_manager_sys-android/blob/master/LICENSE)
[![](https://img.shields.io/badge/version-0.9%20Beta-brightgreen)](https://github.com/Aqinn/activity_manager_sys-android)
[![](https://img.shields.io/badge/移动端推理框架-NCNN-important)](https://github.com/Tencent/ncnn)
[![](https://img.shields.io/badge/QMUI-2.0.0%20alpha10-blue)](https://github.com/Tencent/QMUI_Android)
[![](https://img.shields.io/badge/Dagger-2.17-green)](https://github.com/square/dagger)
[![](https://img.shields.io/badge/rxjava-2.2.20-red)](https://github.com/ReactiveX/RxJava)
[![](https://img.shields.io/badge/rxandroid-2.1.1-orange)](https://github.com/ReactiveX/RxAndroid)
[![](https://img.shields.io/badge/retrofit-2.9.0-blueviolet)](https://github.com/square/retrofit)
[![](https://img.shields.io/badge/okhttp3-4.9.0-ff69b4)](https://github.com/square/okhttp)
[![](https://img.shields.io/badge/litepal-3.2.2-9cf)](https://github.com/guolindev/LitePal)
[![](https://img.shields.io/badge/butterknife-10.1.0-yellow)](https://github.com/JakeWharton/butterknife)

本活动管理系统围绕基于人脸识别实现签到, 拓展出具有活动创建、加入活动、签到创建、签到查询等活动全流程管理系统。

关键词: 活动管理、人脸识别

This activity management system focuses on the realization of check-in based on face recognition, and extends the whole process management system with activity creation, joining activity, check-in creation, check-in query and so on.

Keywords: Activity Management, Face Recognition

## 1. 简介（Introduction）
### 1.1 背景（Background）
该项目是本人的本科毕业设计作品，由本人负责全栈开发（[传送门: 活动管理系统服务端](https://github.com/Aqinn/activity_manager_sys-server)），同时也旨在通过该项目熟悉多个 Android 主流框架的使用，所以也特意使用了如 Dagger 这样的框架（从项目体量上来看，其实没必要用 Dagger）。才疏学浅，其中必有可优化的地方，但由于时间关系本项目随缘更新（或许2021.4前还会再完善一个版本？）。
### 1.2 技术点（Technical Points）
- 通过 NCNN 移动端神经网络前向计算框架实现人脸检测、识别模型的复现，具体可参考本人另一项目（[传送门: 人脸识别 Android Demo By NCNN](https://github.com/Aqinn/facerecognizedemo_ncnn)）
- UI 框架 QMUI，腾讯的开源前端框架，像 QQ 邮箱以及微信读书都是基于 QMUI 写的。
- 依赖注入框架 Dagger2（个人觉得这一堆框架里最难上手的一个，依然没能实现随心所欲地使用），本项目中主要用于实现单例模式、网络请求的封装等等。
- Rxjava、RxAndroid 官方解释: "A library for composing asynchronous and event-based programs using observable sequences for the Java VM."（译文: 是一个通过使用可观察序列来编写异步和基于事件的程序的库。）。个人理解就是可以让我们编写异步代码更方便优雅。本项目中主要结合网络请求框架来使用。
- 网络请求框架 Retrofit2 + Okhttp3，如果让我来封装 Okhttp3 我怎么都不会想到将其封装成接口形式调用，tql，新思路 get。
- LitePal，郭大佬写的 Android 数据库框架，也是非常好用。
- ButterKnife，黄油刀，彻底(?有一些场景还没学会怎么用ButterKnife，以后再研究一下)从 findViewById() 中解放出来。
### 1.3 功能点（Features）
1. 注册登录
2. 活动的增删查改
3. 签到的增删查改
4. 人脸采集（仿 iPhone 的人脸录入过程，不过看情形，模仿得好像有点失败）
5. 人脸识别实现签到
    1. 自助签到（活动参与者在自己手机上识别一下人脸就能实现签到，没有添加定位功能，可以作为后期更新项）
    2. 视频签到（活动创建者的手机开启摄像头架在活动场所门口，活动参与者走过就能识别人脸实现签到）

## 2. 安装（Install）
下面的步骤很简略，不足以指引你成功安装本项目。我刚开始学 Android 的时候确实连如何导入并 Run 别人的项目都研究了好久，深知初学者的不易，但现阶段我的能力和时间也有限，没办法详细成体系地教初学者如何打开一个项目，以后有空了可以专门开一个项目~~教~~学这个。所以如果你在以下步骤中遇到问题，可以提 Issue，有时间我就会帮你看看。
### 2.1 克隆本项目（Clone This Project）
略。
### 2.2 为项目配置 NDK（Add NDK）
本项目开发使用的 NDK 版本是 r21d，[Google.NDK.downloads](https://developer.android.google.cn/ndk/downloads/)，自己对照找一下，下载并配置。
### 2.2 跑起来...（Run it）
Android Studio 如果报编译不过，大概率是 gradle 很久都下载不下来或者需要改某些文件，参考一下这两篇文章（[Android Studio 导入 github 项目详解](https://blog.csdn.net/lyhhj/article/details/48789705)）（[AndroidStudio 导入项目一直卡在 Building gradle project info 最快速解决方案](https://www.cnblogs.com/baron89/p/4843113.html)）

## 3. 功能展示（Show Features）
### 3.1 人脸采集
人脸采集功能预览，带指示框版本。为了更好地指引用户进行人脸采集，我模仿 iPhone 的人脸采集界面，设置了一个黑屏+中间镂空的圆框，同时添加了人脸位置检测规则，只有当人脸检测框位置位于两个框之间时，才能成功采集。
![image](https://github.com/Aqinn/activity_manager_sys-android/previewimages/face_collect_with_box.gif)
人脸采集功能预览，无框版本。
![image](https://github.com/Aqinn/activity_manager_sys-android/previewimages/face_collect.gif)
### 3.2 自助签到
![image](https://github.com/Aqinn/activity_manager_sys-android/previewimages/self_check_in.gif)
### 3.3 视频签到
![image](https://github.com/Aqinn/activity_manager_sys-android/previewimages/video_check_in.gif)

## 4. 特别鸣谢
忠心感谢本人多年好友（大佬）从 2020.3 毕设选题至今给予我的大力帮助，没有他的指导，我无以开始计算机视觉方面的学习以及攻克本课题的难点，感谢！

[大佬 Github 主页](https://github.com/NicoledyChen)

# License
[MIT](https://github.com/Aqinn/activity_manager_sys-android/blob/master/LICENSE) © [Aqinn](https://github.com/Aqinn)
