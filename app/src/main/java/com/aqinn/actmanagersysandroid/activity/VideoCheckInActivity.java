package com.aqinn.actmanagersysandroid.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.aqinn.actmanagersysandroid.MyApplication;
import com.aqinn.actmanagersysandroid.R;
import com.aqinn.actmanagersysandroid.adapter.CheckedAdapter;
import com.aqinn.actmanagersysandroid.data.ApiResult;
import com.aqinn.actmanagersysandroid.retrofitservice.UserAttendService;
import com.aqinn.actmanagersysandroid.retrofitservice.UserFeatureService;
import com.aqinn.actmanagersysandroid.service.CheckSelfCheckinService;
import com.aqinn.actmanagersysandroid.utils.CameraUtils;
import com.aqinn.actmanagersysandroid.utils.CommonUtils;
import com.aqinn.actmanagersysandroid.utils.Utils;
import com.aqinn.actmanagersysandroid.view.AutoFitTextureView;
import com.aqinn.facerecognizencnn.FaceInfo;
import com.aqinn.facerecognizencnn.FaceRecognize;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Aqinn
 * @date 2021/1/15 10:55 PM
 */
public class VideoCheckInActivity extends BaseFragmentActivity {

    private static final String TAG = "VideoCheckInActivity";

    @BindView(R.id.texture_view)
    AutoFitTextureView mTextureView;
    @BindView(R.id.surfaceview)
    SurfaceView mSurfaceView;
    @BindView(R.id.bt_switch_camera)
    Button bt_switch_camera;
    @BindView(R.id.rv_checked)
    RecyclerView rv_checked;

    @Inject
    public UserFeatureService userFeatureService;
    @Inject
    public UserAttendService userAttendService;

    // 当前正在签到的签到 id
    private Long attendId;

    private SurfaceHolder mSurfaceHolder;
    private static CheckedAdapter mCheckedAdapter;

    // 主线程 handler
    public static Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            String userInfo = String.valueOf(msg.obj);
            // 把这个用户信息刷新到 RecycleView 中
            mCheckedAdapter.addChecked(userInfo);
            return true;
        }
    });

    // 预览捕获线程相关
    private HandlerThread mCaptureThread;
    private Handler mCaptureHandler;

    // 推理线程相关
    private HandlerThread mInferThread;
    private Handler mInferHandler;
    private final Object lock = new Object();  // 开关推理线程 - 同步变量
    private boolean isInfering = false;

    // 网络请求线程
    private HandlerThread mNetworkThread;
    private Handler mNetworkHandler;

    // 相机相关
    private CameraCaptureSession mCaptureSession;
    private CameraDevice mCameraDevice;
    private ImageReader mImageReader;
    private boolean mCapturing = false;
    private boolean isFont = true;
    private Size mPreviewSize;

    // 人脸识别模型
    private FaceRecognize mFaceRecognize;
    private volatile float preFinishRecognizeFeature[] = null;  // 每一帧人脸特征向量与上一条成功识别的人脸特征向量对比，不是同一个人的才发送使用，降低网络压力
    private double threshold = 0.5;

    // 画笔相关 - 绘制人脸检测框
    private static final Paint rectPaint = new Paint();
    private static final Paint pointPaint = new Paint();
    private static final Paint minRecPaint = new Paint();
    private static final Paint maxRecPaint = new Paint();

    // 画笔相关 - 绘制人脸检测框
    static {
        rectPaint.setColor(Color.GREEN);
        rectPaint.setStyle(Paint.Style.STROKE);
        rectPaint.setStrokeWidth(4);
        pointPaint.setColor(Color.GREEN);
        pointPaint.setStyle(Paint.Style.FILL);

        minRecPaint.setColor(Color.YELLOW);
        minRecPaint.setStrokeWidth(4);
        minRecPaint.setStyle(Paint.Style.STROKE);

        maxRecPaint.setColor(Color.RED);
        maxRecPaint.setStrokeWidth(4);
        maxRecPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_check_in);
        askForPermission();
        MyApplication.getApplicationComponent().inject(this);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        Log.d(TAG, "onCreate: attendId => " + intent.getLongExtra("attendId", -1L));
        Log.d(TAG, "onCreate: isSelfCheck => " + intent.getBooleanExtra("isSelfCheck", false));
        attendId = intent.getLongExtra("attendId", -1L);
        if (attendId.equals(-1L)) {
            setResult(-1);
            finish();
        }
        boolean isSelfCheck = intent.getBooleanExtra("isSelfCheck", false);
        if (isSelfCheck) {
            // 开启增量获得自助签到成功信息的网络请求服务
            Log.d(TAG, "准备启动 增量获得自助签到成功信息的网络请求服务");
            Intent serviceIntent = new Intent(this, CheckSelfCheckinService.class);
            serviceIntent.putExtra("attendId", attendId);
            startService(serviceIntent);
        }
        initAllView();
        initModel();
        initData();
    }

    private void initAllView() {
        // 设置预览画面的长宽比为 4:3
        mTextureView.setAspectRatio(4, 3);

        // 设置 SurfaceView 处于顶层以及设为透明
        mSurfaceView.setZOrderOnTop(true);//处于顶层
        mSurfaceView.getHolder().setFormat(PixelFormat.TRANSPARENT);//设置surface为透明

        // 获取 SurfaceView 的 Handler
        mSurfaceHolder = mSurfaceView.getHolder();

        // 初始化已签到人员列表
        mCheckedAdapter = new CheckedAdapter(this, new ArrayList<>());
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv_checked.setAdapter(mCheckedAdapter);
        rv_checked.setLayoutManager(llm);
    }

    private void initModel() {
        // 人脸检测模型初始化
        mFaceRecognize = new FaceRecognize();
        mFaceRecognize.initRetainFace(getAssets());
        // 拷贝人脸识别模型到sd卡
        String sdPath = getCacheDir().getAbsolutePath() + "/facem/";
        Utils.copyFileFromAsset(this, "mobilefacenet.bin", sdPath + File.separator + "mobilefacenet.bin");
        Utils.copyFileFromAsset(this, "mobilefacenet.param", sdPath + File.separator + "mobilefacenet.param");
        // 人脸识别模型初始化
        mFaceRecognize.initMobileFacenet(sdPath);
    }

    private void initData() {
        Observable<ApiResult> observable = userAttendService.getVideoUserAttendAfterTime(attendId, 1L);
        Disposable d = observable.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(apiResult -> {
                    if (apiResult.success) {
                        Log.d(TAG, "获取新增的视频签到信息成功，正在准备推送至 RecycleView 中显示");
                        Log.d(TAG, "initData: " + String.valueOf(apiResult.data));
                        JSONArray ja = JSONArray.parseArray(String.valueOf(apiResult.data));
                        for (int i = ja.size() - 1; i >= 0; i--) {
                            Message msg = Message.obtain();
                            msg.obj = ja.getJSONObject(i).get("msg");
                            mHandler.sendMessage(msg);
                        }
                    } else {
                        Log.d(TAG, "获取新增的自助签到信息成功，但后台返回了false");
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                    Log.d(TAG, "initData: 初始化已视频签到的网络请求失败，错误信息如下: " + throwable.getMessage());
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        prepareAllThread();
        prepareCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopAllThread();
        closeCamera();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopAllThread();
        closeCamera();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopAllThread();
        closeCamera();
        Intent serviceIntent = new Intent(this, CheckSelfCheckinService.class);
        stopService(serviceIntent);
    }

    // 开启所有工作线程
    private void prepareAllThread() {
        startCaptureThread();
        startInferThread();
        startNetworkThread();
    }

    // 关闭所有工作线程
    private void stopAllThread() {
        stopCaptureThread();
        stopInferThread();
        stopNetworkThread();
    }

    // 处理相机捕获的图像
    private void predict() {
        // 获取相机捕获的图像
        Bitmap bitmap = mTextureView.getBitmap();
        try {
//            Log.d(TAG, "predict: 用以预测的 Bitmap 尺寸（未Resize） w:" + bitmap.getWidth() + ", h:" + bitmap.getHeight());
            float[][] result = mFaceRecognize.detectTest(bitmap, bitmap.getWidth(), bitmap.getHeight(), 3);
            if (result == null) {
                drawRectBySurface(null);
                return;
            }
//            Log.d(TAG, "predict: 检测到几张人脸？result.length => " + result.length);
            if (result.length != 0) {
                FaceInfo faceInfos[] = new FaceInfo[result.length];
                for (int i = 0; i < faceInfos.length; i++) {
                    FaceInfo faceInfo = CommonUtils.floatArr2FaceInfo(result[i]);
                    faceInfos[i] = faceInfo;
                    float faceFeature[] = mFaceRecognize.recognize(CommonUtils.getPixelsRGBA(bitmap), mTextureView.getWidth(), mTextureView.getHeight(), CommonUtils.getUsefulLandmarksFromFaceInfo(faceInfos[i]));
                    // 多次发起请求，验证检测到的人脸身份。制造条件判断以减缓网络压力
                    if (preFinishRecognizeFeature == null || threshold > mFaceRecognize.compare(faceFeature, preFinishRecognizeFeature)) {
                        Message msg = Message.obtain();
                        msg.obj = CommonUtils.arr2String(faceFeature);
                        mNetworkHandler.sendMessage(msg);
                    }
                }
                drawRectBySurface(faceInfos);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 开启捕获预览线程
     * 勿私自调用，应该调用 prepareAllThread()
     */
    private void startCaptureThread() {
        mCaptureThread = new HandlerThread("capture");
        mCaptureThread.start();
        mCaptureHandler = new Handler(mCaptureThread.getLooper());
    }

    /**
     * 关闭捕获预览线程
     * 勿私自调用，应该调用 stopAllThread()
     */
    private void stopCaptureThread() {
        try {
            if (mCaptureThread != null) {
                mCaptureThread.quitSafely();
                mCaptureThread.join();
            }
            mCaptureThread = null;
            mCaptureHandler = null;
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 准备相机相关事宜
     * 勿私自调用，应在 onResume() 中调用
     */
    private void prepareCamera() {
        if (mTextureView.isAvailable()) {
            startCapture();
        } else {
            mTextureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
                @Override
                public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                    startCapture();
                }

                @Override
                public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
                }

                @Override
                public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                    return true;
                }

                @Override
                public void onSurfaceTextureUpdated(SurfaceTexture surface) {
                }
            });
        }
    }

    /**
     * 开始利用相机捕获预览画面
     * 勿私自调用，应在 prepareCamera() 中调用
     */
    private void startCapture() {
        // 判断是否正处于捕获图片的状态
        if (mCapturing)
            return;
        mCapturing = true;
        final CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        // 查看可用的摄像头
        String cameraIdAvailable = null;
        try {
            assert manager != null;
            for (final String cameraId : manager.getCameraIdList()) {
                final CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);

                final Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                // 设置相机前摄像头或者后摄像头
                if (isFont) {
                    if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT) {
                        cameraIdAvailable = cameraId;
                        break;
                    }
                } else {
                    if (facing != null && facing == CameraCharacteristics.LENS_FACING_BACK) {
                        cameraIdAvailable = cameraId;
                        break;
                    }
                }
            }
        } catch (CameraAccessException e) {
            Log.e(TAG, "启动图片捕获异常 ", e);
        }

        // 开启摄像头
        try {
            assert cameraIdAvailable != null;
            final CameraCharacteristics characteristics =
                    manager.getCameraCharacteristics(cameraIdAvailable);

            final StreamConfigurationMap map =
                    characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            assert map != null;
            mPreviewSize = CameraUtils.chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class),
                    mTextureView.getWidth(),
                    mTextureView.getHeight());
            Log.d("mPreviewSize", String.valueOf(mPreviewSize));
            mTextureView.setAspectRatio(mPreviewSize.getHeight(), mPreviewSize.getWidth());
            manager.openCamera(cameraIdAvailable, new CameraDevice.StateCallback() {
                @Override
                public void onOpened(@NonNull CameraDevice camera) {
                    mCameraDevice = camera;
                    createCaptureSession();
                }

                @Override
                public void onDisconnected(@NonNull CameraDevice camera) {
                    camera.close();
                    mCameraDevice = null;
                    mCapturing = false;
                }

                @Override
                public void onError(@NonNull CameraDevice camera, final int error) {
                    Log.e(TAG, "打开相机错误 =  " + error);
                    camera.close();
                    mCameraDevice = null;
                    mCapturing = false;
                }
            }, mCaptureHandler);
        } catch (CameraAccessException | SecurityException e) {
            mCapturing = false;
            Log.e(TAG, "启动图片捕获异常 ", e);
        }
    }

    /**
     * 创建捕获图片session
     * 设置为“预览模式”，设置一些相机参数，设置消息处理者 mCaptureHandler
     * 勿私自调用，应在 startCapture() 中调用
     */
    private void createCaptureSession() {
        try {
            final SurfaceTexture texture = mTextureView.getSurfaceTexture();
            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());

            final Surface surface = new Surface(texture);
            final CaptureRequest.Builder captureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);

            mImageReader = ImageReader.newInstance(
                    mPreviewSize.getWidth(), mPreviewSize.getHeight(), ImageFormat.JPEG, 10);

            mCameraDevice.createCaptureSession(
                    Arrays.asList(surface, mImageReader.getSurface()),
                    new CameraCaptureSession.StateCallback() {

                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                            if (null == mCameraDevice) {
                                return;
                            }
                            mCaptureSession = cameraCaptureSession;
                            try {
                                captureRequestBuilder.set(
                                        CaptureRequest.CONTROL_AF_MODE,
                                        CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                                captureRequestBuilder.set(
                                        CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);

                                CaptureRequest previewRequest = captureRequestBuilder.build();
                                mCaptureSession.setRepeatingRequest(
                                        previewRequest, new CameraCaptureSession.CaptureCallback() {
                                            @Override
                                            public void onCaptureProgressed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureResult partialResult) {
                                                super.onCaptureProgressed(session, request, partialResult);
                                            }

                                            @Override
                                            public void onCaptureFailed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureFailure failure) {
                                                super.onCaptureFailed(session, request, failure);
                                                Log.d(TAG, "onCaptureFailed = " + failure.getReason());
                                            }

                                            @Override
                                            public void onCaptureSequenceCompleted(@NonNull CameraCaptureSession session, int sequenceId, long frameNumber) {
                                                super.onCaptureSequenceCompleted(session, sequenceId, frameNumber);
                                                Log.d(TAG, "onCaptureSequenceCompleted");
                                            }
                                        }, mCaptureHandler);
                            } catch (final CameraAccessException e) {
                                Log.e(TAG, "onConfigured exception ", e);
                            }
                        }

                        @Override
                        public void onConfigureFailed(@NonNull final CameraCaptureSession cameraCaptureSession) {
                            Log.e(TAG, "onConfigureFailed ");
                        }
                    },
                    null);
        } catch (final CameraAccessException e) {
            Log.e(TAG, "创建捕获图片session异常 ", e);
        }
    }

    /**
     * 销毁相机相关事宜
     * 勿私自调用，应在 onPause() 中调用
     */
    private void closeCamera() {
        if (mCaptureSession != null) {
            mCaptureSession.close();
            mCaptureSession = null;
        }
        if (mCameraDevice != null) {
            mCameraDevice.close();
            mCameraDevice = null;
        }
        if (mImageReader != null) {
            mImageReader.close();
            mImageReader = null;
        }
        mCapturing = false;
    }

    /**
     * 启动预测线程
     * 勿私自调用，应在 prepareAllThread() 中调用
     */
    private void startInferThread() {
        mInferThread = new HandlerThread("inference");
        mInferThread.start();
        mInferHandler = new Handler(mInferThread.getLooper());
        synchronized (lock) {
            isInfering = true;
        }
        mInferHandler.post(periodicInfer);
    }

    // 推理线程需要做的事情
    private final Runnable periodicInfer =
            new Runnable() {
                @Override
                public void run() {
                    synchronized (lock) {
                        if (isInfering) {
                            // 开始预测前要判断相机是否已经准备好
                            if (getApplicationContext() != null && mCameraDevice != null) {
                                predict();
                            }
                        }
                    }
                    if (mInferThread != null && mInferHandler != null && mCaptureHandler != null && mCaptureThread != null) {
                        mInferHandler.post(periodicInfer);  // 相当于回调自身，递归调用自身
                    }
                }
            };

    /**
     * 关闭预测线程
     * 勿私自调用，应在 stopAllThread() 中调用
     */
    private void stopInferThread() {
        try {
            if (mInferThread != null) {
                mInferThread.quitSafely();
                mInferThread.join();
            }
            mInferThread = null;
            mInferHandler = null;
            synchronized (lock) {
                isInfering = false;
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 开启网络请求线程
     * 勿私自调用，应该调用 prepareAllThread()
     */
    private void startNetworkThread() {
        mNetworkThread = new HandlerThread("network");
        mNetworkThread.start();
        mNetworkHandler = new Handler(mNetworkThread.getLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String tempFeature = String.valueOf(msg.obj);
                Observable<ApiResult> observable = userFeatureService.videoFaceRecognize(attendId, tempFeature);
                observable.subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .subscribe(new Observer<ApiResult>() {
                            @Override
                            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                                Log.d("facerecognize", "onSubscribe: ");
                            }

                            @Override
                            public void onNext(@io.reactivex.annotations.NonNull ApiResult apiResult) {
                                if (apiResult.success) {
                                    Log.d("facerecognize", "onNext: 人脸识别成功，你的身份信息是: " + (String) apiResult.data);
                                    Message msg = Message.obtain();
                                    msg.obj = apiResult.data;
                                    mHandler.sendMessage(msg);
                                    preFinishRecognizeFeature = CommonUtils.string2Arr(tempFeature);
                                } else {
                                    Log.d("facerecognize", "onNext: 人脸识别失败，错误信息是: " + apiResult.errMsg);
                                }
                            }

                            @Override
                            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                                Log.d("facerecognize", "onError: ");
                                e.printStackTrace();
                            }

                            @Override
                            public void onComplete() {
                                Log.d("facerecognize", "onComplete: ");
                            }
                        });
            }
        };
    }

    /**
     * 关闭网络请求线程
     * 勿私自调用，应该调用 stopAllThread()
     */
    private void stopNetworkThread() {
        try {
            if (mNetworkThread != null) {
                mNetworkThread.quitSafely();
                mNetworkThread.join();
            }
            mNetworkThread = null;
            mNetworkHandler = null;
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    // 绘制人脸检测框
    private void drawRectBySurface(FaceInfo[] faceInfos) {
        if (faceInfos == null) {
            Canvas canvas = new Canvas();
            canvas = mSurfaceHolder.lockCanvas();
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR); //清楚掉上一次的画框。
            mSurfaceHolder.unlockCanvasAndPost(canvas);
            return;
        }
        Canvas canvas = mSurfaceHolder.lockCanvas();
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR); //清楚掉上一次的画框。
        for (FaceInfo faceInfo : faceInfos) {
            canvas.drawRect(faceInfo.x1, faceInfo.y1, faceInfo.x2, faceInfo.y2, rectPaint);
            for (int j = 0; j < 5; j++) {
                canvas.drawCircle(faceInfo.keypoints[j][0], faceInfo.keypoints[j][1], 6f, pointPaint);
            }
        }
        mSurfaceHolder.unlockCanvasAndPost(canvas);
    }

    @OnClick(value = R.id.bt_switch_camera)
    public void setBt_switch_camera(View view) {
        stopAllThread();
        closeCamera();
        isFont = !isFont;
        prepareCamera();
        prepareAllThread();
    }

}