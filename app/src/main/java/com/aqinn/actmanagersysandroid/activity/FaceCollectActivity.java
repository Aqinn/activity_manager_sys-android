package com.aqinn.actmanagersysandroid.activity;

import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aqinn.actmanagersysandroid.MyApplication;
import com.aqinn.actmanagersysandroid.R;
import com.aqinn.actmanagersysandroid.data.ApiResult;
import com.aqinn.actmanagersysandroid.retrofitservice.UserFeatureService;
import com.aqinn.actmanagersysandroid.utils.CameraUtils;
import com.aqinn.actmanagersysandroid.utils.CommonUtils;
import com.aqinn.actmanagersysandroid.utils.Utils;
import com.aqinn.actmanagersysandroid.view.AutoFitTextureView;
import com.aqinn.facerecognizencnn.FaceInfo;
import com.aqinn.facerecognizencnn.FaceRecognize;
import com.qmuiteam.qmui.widget.QMUIProgressBar;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Arrays;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Aqinn
 * @date 2021/1/14 7:55 PM
 */
public class FaceCollectActivity extends BaseFragmentActivity {

    private boolean isTesting = false;

    private static final String TAG = "FaceCollectActivity";

    @BindView(R.id.circleProgressBar)
    QMUIProgressBar mCircleProgressBar;
    @BindView(R.id.texture_view)
    AutoFitTextureView mTextureView;
    @BindView(R.id.surfaceview)
    SurfaceView mSurfaceView;
    @BindView(R.id.tv_res)
    TextView tv_res;
//    @BindView(R.id.iv_outter)
//    ImageView iv_outter;
//    @BindView(R.id.iv_inner)
//    ImageView iv_inner;

    @Inject
    public UserFeatureService userFeatureService;

    protected static final int STOP = 0x10000;
    protected static final int NEXT = 0x10001;

    private ProgressHandler progressHandler = new ProgressHandler();
    private SurfaceHolder mSurfaceHolder;

    // 预览捕获线程相关
    private HandlerThread mCaptureThread;
    private Handler mCaptureHandler;

    // 推理线程相关
    private HandlerThread mInferThread;
    private Handler mInferHandler;
    private final Object lock = new Object();  // 开关推理线程 - 同步变量
    private boolean isInfering = false;

    // 相机相关
    private CameraCaptureSession mCaptureSession;
    private CameraDevice mCameraDevice;
    private ImageReader mImageReader;
    private boolean mCapturing = false;
    private boolean isFont = true;
    private Size mPreviewSize;

    // 人脸识别模型
    private FaceRecognize mFaceRecognize;
    private Integer recognizeCount = 0;
    private float faceFeatures[][] = new float[4][128];
    private float nowFaceFeature[];

    // 画笔相关 - 绘制人脸检测框
    private static Paint rectPaint = new Paint();
    private static Paint pointPaint = new Paint();
    private static Paint minRecPaint = new Paint();
    private static Paint maxRecPaint = new Paint();

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

    // 人脸位置校验相关
    private boolean isProgressFinish = false;
    private static int progressCount = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        askForPermission();
        MyApplication.getApplicationComponent().inject(this);
        setContentView(R.layout.activity_face_collect);
        ButterKnife.bind(this);
        initAllView();
        initModel();
    }

    private void initAllView() {
        // 绑定圆形进度条的 Handler
        progressHandler.setProgressBar(mCircleProgressBar);
        mCircleProgressBar.setOnProgressChangeListener(new QMUIProgressBar.OnProgressChangeListener() {
            @Override
            public void onProgressChange(QMUIProgressBar progressBar, int currentValue, int maxValue) {
                if (progressCount == 0) {
                    tv_res.setText("正在进行人脸采集");
                    synchronized (recognizeCount) {
                        recognizeCount = 0;
                        faceFeatures = new float[4][128];
                    }
                    return;
                }
                if (currentValue < 40) {
                    tv_res.setText("就是这样！保持！");
                } else if (currentValue < 75) {
                    tv_res.setText("快完成了！");
                } else if (currentValue >= 100) {
                    tv_res.setText("人脸采集已完成！");
                    onFaceCollectFinish();
                }

                if (currentValue >= 100 || recognizeCount >= 4)
                    return;
                synchronized (recognizeCount) {
                    if (currentValue == 20 || currentValue == 40 || currentValue == 60 || currentValue == 80) {
                        // 人脸特征向量存起来
                        faceFeatures[recognizeCount] = nowFaceFeature;
                        ++recognizeCount;
                    }
                }
            }
        });

        // 设置预览画面的长宽比为 4:3
        mTextureView.setAspectRatio(4, 3);

        // 设置 SurfaceView 处于顶层以及设为透明
        mSurfaceView.setZOrderOnTop(true);//处于顶层
        mSurfaceView.getHolder().setFormat(PixelFormat.TRANSPARENT);//设置surface为透明

        // 获取 SurfaceView 的 Handler
        mSurfaceHolder = mSurfaceView.getHolder();

        // 让 ImageView 旋转起来
//        Animation outAnimation = AnimationUtils.loadAnimation(this, R.anim.rotaterepeat_clockwise);
//        iv_inner.startAnimation(outAnimation);
//        Animation inAnimation = AnimationUtils.loadAnimation(this, R.anim.rotaterepeat_unclockwise);
//        iv_outter.startAnimation(inAnimation);
    }

    private void onFaceCollectFinish() {
        Observable<ApiResult> observable = userFeatureService.collectFace(CommonUtils.getNowUserIdFromSP(this),
                CommonUtils.arr2String(faceFeatures[0]),
                CommonUtils.arr2String(faceFeatures[1]),
                CommonUtils.arr2String(faceFeatures[2]),
                CommonUtils.arr2String(faceFeatures[3]));
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ApiResult>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                        Log.d("facesend", "onSubscribe: ");
                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull ApiResult apiResult) {
                        Log.d("facesend", "onNext: ");
                        if (apiResult.success) {
                            Log.d("facesend", "onNext: 人脸采集完成，后端已接受数据");
                            Toast.makeText(FaceCollectActivity.this, "将在两秒后返回主界面", Toast.LENGTH_SHORT).show();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(2000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    finish();
                                }
                            }).start();
                        } else {
                            Log.d("facesend", "onNext: 人脸采集失败，后端返回 false");
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        Log.d("facesend", "onError: ");
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Log.d("facesend", "onComplete: ");
                    }
                });
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
    }

    // 开启所有工作线程
    private void prepareAllThread() {
        startCaptureThread();
        startInferThread();
    }

    // 关闭所有工作线程
    private void stopAllThread() {
        stopCaptureThread();
        stopInferThread();
    }

    // 处理相机捕获的图像
    private void predict() {
        // 获取相机捕获的图像
        Bitmap bitmap = mTextureView.getBitmap();
        try {
            Log.d(TAG, "predict: 用以预测的 Bitmap 尺寸（未Resize） w:" + bitmap.getWidth() + ", h:" + bitmap.getHeight());
            float[][] result = mFaceRecognize.detectTest(bitmap, bitmap.getWidth(), bitmap.getHeight(), 3);
            if (result == null) {
                drawRectBySurface(null);
                if (progressCount < 100) {
                    mCircleProgressBar.setProgress(0);
                    progressCount = 0;
                }
                return;
            }
            Log.d(TAG, "predict: 检测到几张人脸？result.length => " + result.length);
            if (result.length != 0) {
                FaceInfo faceInfos[] = new FaceInfo[result.length];
                for (int i = 0; i < faceInfos.length; i++) {
                    FaceInfo faceInfo = CommonUtils.floatArr2FaceInfo(result[i]);
                    faceInfos[i] = faceInfo;
                }
                nowFaceFeature = mFaceRecognize.recognize(CommonUtils.getPixelsRGBA(bitmap), mTextureView.getWidth(), mTextureView.getHeight(), CommonUtils.getUsefulLandmarksFromFaceInfo(faceInfos[0]));
                if (isTesting)
                    drawRectBySurface(faceInfos);
                if (progressCount < 100) {
                    boolean isRightLocation = verifyFaceLocation(faceInfos[0]);
                    if (isRightLocation) {
                        mCircleProgressBar.setProgress(++progressCount);
                    } else {
                        mCircleProgressBar.setProgress(0);
                        progressCount = 0;
                    }
                }
                // 测试提取人脸特征
//                test(bitmap, mTextureView.getWidth(), mTextureView.getHeight(), CommonUtils.getUsefulLandmarksFromFaceInfo(faceInfos[0]));
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
    private Runnable periodicInfer =
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

    // 绘制人脸检测框
    private void drawRectBySurface(FaceInfo[] faceInfos) {
        if (faceInfos == null) {
            Canvas canvas = new Canvas();
            canvas = mSurfaceHolder.lockCanvas();
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR); //清楚掉上一次的画框。
            mSurfaceHolder.unlockCanvasAndPost(canvas);
            return;
        }
        Canvas canvas = new Canvas();
        canvas = mSurfaceHolder.lockCanvas();
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR); //清楚掉上一次的画框。
        for (int i = 0; i < faceInfos.length; i++) {
            canvas.drawRect(faceInfos[i].x1, faceInfos[i].y1, faceInfos[i].x2, faceInfos[i].y2, rectPaint);
            if (isTesting)
                test(canvas);
            for (int j = 0; j < 5; j++) {
                canvas.drawCircle(faceInfos[i].keypoints[j][0], faceInfos[i].keypoints[j][1], 6f, pointPaint);
            }
        }
        mSurfaceHolder.unlockCanvasAndPost(canvas);
        return;
    }

    private void test(Canvas canvas) {
        float sqrt2 = 1.4142135f;
        int stroke = 18;
        int radius = mCircleProgressBar.getRight() - mCircleProgressBar.getLeft() - 2 * stroke;
        int squareSide = (int) (radius * sqrt2);
        int errorValue = (mCircleProgressBar.getRight() - mCircleProgressBar.getLeft() - squareSide) / 2;

        int diagonalLeft = mCircleProgressBar.getLeft() - errorValue;
        int diagonalRight = mCircleProgressBar.getRight() + errorValue;
        int diagonalTop = mCircleProgressBar.getTop() - errorValue;
        int diagonalBottom = mCircleProgressBar.getBottom() + errorValue;

        int hopeMinBottom = (diagonalBottom + mCircleProgressBar.getBottom()) / 2 - 120;
        int hopeMaxBottom = mCircleProgressBar.getBottom() + 5;

        int hopeMinTop = diagonalTop + 80;
        int hopeMaxTop = (int) ((mCircleProgressBar.getTop() + diagonalTop) / 2 - (diagonalTop - mCircleProgressBar.getTop()) / 6);

        int hopeMinLeft = diagonalLeft + (diagonalLeft - mCircleProgressBar.getLeft()) / 6 + 80;
        int hopeMaxLeft = diagonalLeft - 30;

        int hopeMinRight = diagonalRight - (mCircleProgressBar.getRight() - diagonalRight) / 6 - 80;
        int hopeMaxRight = diagonalRight + 30;

        canvas.drawRect(hopeMinLeft, hopeMinTop, hopeMinRight, hopeMinBottom, minRecPaint);
        canvas.drawRect(hopeMaxLeft, hopeMaxTop, hopeMaxRight, hopeMaxBottom, maxRecPaint);
        Log.d("verifyFaceLocation", "hopeMinLeft=" + hopeMinLeft + ", hopeMinTop=" + hopeMinTop + ", hopeMinRight=" + hopeMinRight + ", hopeMinBottom=" + hopeMinBottom);
        Log.d("verifyFaceLocation", "hopeMaxLeft=" + hopeMaxLeft + ", hopeMaxTop=" + hopeMaxTop + ", hopeMaxRight=" + hopeMaxRight + ", hopeMaxBottom=" + hopeMaxBottom);
    }

    /**
     * 验证检测出的人脸是否在我们想要的区域内
     *
     * @param faceInfo
     * @return
     */
    private boolean verifyFaceLocation(FaceInfo faceInfo) {
        int left = (int) faceInfo.x1, top = (int) faceInfo.y1, right = (int) faceInfo.x2, bottom = (int) faceInfo.y2;
        float sqrt2 = 1.4142135f;
        int stroke = 18;
        int radius = mCircleProgressBar.getRight() - mCircleProgressBar.getLeft() - 2 * stroke;
        int squareSide = (int) (radius * sqrt2);
        int errorValue = (mCircleProgressBar.getRight() - mCircleProgressBar.getLeft() - squareSide) / 2;

        int diagonalLeft = mCircleProgressBar.getLeft() - errorValue;
        int diagonalRight = mCircleProgressBar.getRight() + errorValue;
        int diagonalTop = mCircleProgressBar.getTop() - errorValue;
        int diagonalBottom = mCircleProgressBar.getBottom() + errorValue;

        int hopeMinBottom = (diagonalBottom + mCircleProgressBar.getBottom()) / 2 - 120;
        int hopeMaxBottom = mCircleProgressBar.getBottom() + 5;

        int hopeMinTop = diagonalTop + 80;
        int hopeMaxTop = (int) ((mCircleProgressBar.getTop() + diagonalTop) / 2 - (diagonalTop - mCircleProgressBar.getTop()) / 6);

        int hopeMinLeft = diagonalLeft + (diagonalLeft - mCircleProgressBar.getLeft()) / 6 + 80;
        int hopeMaxLeft = diagonalLeft - 30;

        int hopeMinRight = diagonalRight - (mCircleProgressBar.getRight() - diagonalRight) / 6 - 80;
        int hopeMaxRight = diagonalRight + 30;

        boolean isVerify = true;

        if (!(hopeMaxLeft <= left && left <= hopeMinLeft))
            isVerify = false;
        if (!(hopeMaxTop <= top && top <= hopeMinTop))
            isVerify = false;
        if (!(hopeMinRight <= right && right <= hopeMaxRight))
            isVerify = false;
        if (!(hopeMinBottom <= bottom && bottom <= hopeMaxBottom))
            isVerify = false;

        return isVerify;
    }

    // 处理圆形进度条
    private static class ProgressHandler extends Handler {
        private WeakReference<QMUIProgressBar> weakCircleProgressBar;

        void setProgressBar(QMUIProgressBar circleProgressBar) {
            weakCircleProgressBar = new WeakReference<>(circleProgressBar);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case STOP:
                    break;
                case NEXT:
                    if (!Thread.currentThread().isInterrupted()) {
                        if (weakCircleProgressBar.get() != null) {
                            weakCircleProgressBar.get().setProgress(msg.arg1);
                        }
                    }
            }

        }
    }

}
