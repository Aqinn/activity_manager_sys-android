package com.aqinn.actmanagersysandroid.StreamBlazeFaceDetector;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.aqinn.actmanagersysandroid.BlazeFaceDetector;
import com.aqinn.actmanagersysandroid.FaceInfo;
import com.aqinn.actmanagersysandroid.FileUtils;
import com.aqinn.actmanagersysandroid.FpsCounter;
import com.aqinn.actmanagersysandroid.MyApplication;
import com.aqinn.actmanagersysandroid.R;
import com.aqinn.actmanagersysandroid.fragment.BaseFragment;
import com.aqinn.actmanagersysandroid.utils.Utils;
import com.aqinn.facerecognize.MobileFaceNetRecognize;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

/**
 * @author Aqinn
 * @date 2021/1/4 10:46 PM
 */
public class FaceCollectFragment extends BaseFragment {

    private static final String TAG = "FaceCollectFragment";

    /**********************************     Define    **********************************/

    private View root;
    private Button btnSwitchCamera;
    private SurfaceView mPreview;

    private DrawView mDrawView;
    private int mCameraWidth;
    private int mCameraHeight;
    private static final int NET_H_INPUT = 128;
    private static final int NET_W_INPUT = 128;
    Camera mOpenedCamera;
    int mOpenedCameraId = 0;
    MySurfaceHolder mDemoSurfaceHolder = null;

    int mCameraFacing = -1;
    int mRotate = -1;
    SurfaceHolder mSurfaceHolder;

    private BlazeFaceDetector mFaceDetector = new BlazeFaceDetector();
    private MobileFaceNetRecognize mFaceRecognize = new MobileFaceNetRecognize();
    private double threshold = 0.5;
    private boolean mIsDetectingFace = false;
    private FpsCounter mFpsCounter = new FpsCounter();
    private boolean mIsCountFps = false;

    private ToggleButton mGPUSwitch;
    private boolean mUseGPU = false;
    //add for npu
    private ToggleButton mHuaweiNPUswitch;
    private boolean mUseHuaweiNpu = false;
    private TextView HuaweiNpuTextView;

    private boolean mDeviceSwiched = false;

    private float[] previousFeature = null;

    private boolean flag = true;

    /**********************************     Get Preview Advised    **********************************/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        if (!flag) {
            //拷贝模型到sd卡
            String sdPath = getActivity().getCacheDir().getAbsolutePath() + "/facem/";
            Utils.copyFileFromAsset(getActivity(), "mobilefacenet.bin", sdPath + File.separator + "mobilefacenet.bin");
            Utils.copyFileFromAsset(getActivity(), "mobilefacenet.param", sdPath + File.separator + "mobilefacenet.param");
            //模型初始化
            mFaceRecognize.init(sdPath);
            flag = false;
        }

        System.loadLibrary("tnn_wrapper");
        //start SurfaceHolder
        mDemoSurfaceHolder = new MySurfaceHolder(this);
        String modelPath = initModel();
    }

    @Override
    protected View onCreateView() {
        root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_face_collect, null);
        initAllView();
        init();
        return root;
    }

    private String initModel() {
        Log.d(TAG, "initModel: 开始拷贝人脸检测模型和参数");
        String targetDir = getActivity().getFilesDir().getAbsolutePath();
        String[] modelPathsDetector = {
                "blazeface.tnnmodel",
                "blazeface.tnnproto",
        };
        for (int i = 0; i < modelPathsDetector.length; i++) {
            String modelFilePath = modelPathsDetector[i];
            String interModelFilePath = targetDir + "/" + modelFilePath;
            FileUtils.copyAsset(getActivity().getAssets(), "blazeface/" + modelFilePath, interModelFilePath);
        }
        FileUtils.copyAsset(getActivity().getAssets(), "blazeface/blazeface_anchors.txt", targetDir + "/blazeface_anchors.txt");
        Log.d(TAG, "initModel: 人脸检测模型和参数拷贝完成");
        return targetDir;
    }

    private void restartCamera() {
        closeCamera();
        openCamera(mCameraFacing);
        startPreview(mSurfaceHolder);
    }

    private void initAllView() {
        mPreview = root.findViewById(R.id.live_detection_preview);
//        btnSwitchCamera = root.findViewById(R.id.switch_camera);
        mDrawView = root.findViewById(R.id.drawView);
    }

    private void init() {
//        btnSwitchCamera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                closeCamera();
//                if (mCameraFacing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
//                    openCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
//                } else {
//                    openCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
//                }
//                startPreview(mSurfaceHolder);
//            }
//        });
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();
        if (null != mDemoSurfaceHolder) {
            SurfaceHolder holder = mPreview.getHolder();
            holder.setKeepScreenOn(true);
            mDemoSurfaceHolder.setSurfaceHolder(holder);
        }
    }

    /**********************************     Camera    **********************************/


    public void openCamera() {
        openCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
    }

    private void openCamera(int cameraFacing) {
        mIsDetectingFace = true;
        mCameraFacing = cameraFacing;
        try {
            int numberOfCameras = Camera.getNumberOfCameras();
            if (numberOfCameras < 1) {
                Log.e(TAG, "no camera device found");
            } else if (1 == numberOfCameras) {
                mOpenedCamera = Camera.open(0);
                mOpenedCameraId = 0;
            } else {
                Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
                for (int i = 0; i < numberOfCameras; i++) {
                    Camera.getCameraInfo(i, cameraInfo);
                    if (cameraInfo.facing == cameraFacing) {
                        mOpenedCamera = Camera.open(i);
                        mOpenedCameraId = i;
                        break;
                    }
                }
            }
            if (mOpenedCamera == null) {
                Log.e(TAG, "can't find camera");
            } else {

                int r = CameraSetting.initCamera(getActivity().getApplicationContext(), mOpenedCamera, mOpenedCameraId);
                if (r == 0) {
                    //设置摄像头朝向
                    CameraSetting.setCameraFacing(cameraFacing);

                    Camera.Parameters parameters = mOpenedCamera.getParameters();
                    mRotate = CameraSetting.getRotate(getActivity().getApplicationContext(), mOpenedCameraId, mCameraFacing);
                    mCameraWidth = parameters.getPreviewSize().width;
                    mCameraHeight = parameters.getPreviewSize().height;
                    String modelPath = initModel();
                    int device = 0;
                    if (mUseHuaweiNpu) {
                        device = 2;
                    } else if (mUseGPU) {
                        device = 1;
                    }
                    int ret = mFaceDetector.init(modelPath, NET_W_INPUT, NET_H_INPUT, 0.975f, 0.23f, 1, device);
                    if (ret == 0) {
                        mIsDetectingFace = true;
                    } else {
                        mIsDetectingFace = false;
                        Log.e(TAG, "Face detector init failed " + ret);
                    }
                    ret = mFpsCounter.init();
                    if (ret == 0) {
                        mIsCountFps = true;
                    } else {
                        mIsCountFps = false;
                        Log.e(TAG, "Fps Counter init failed " + ret);
                    }
                } else {
                    Log.e(TAG, "Failed to init camera");
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "open camera failed:" + e.getLocalizedMessage());
        }
    }

    public void startPreview(SurfaceHolder surfaceHolder) {
        try {
            if (null != mOpenedCamera) {
                Log.i(TAG, "start preview, is previewing");
                mOpenedCamera.setPreviewCallback(new Camera.PreviewCallback() {
                    @Override
                    public void onPreviewFrame(byte[] data, Camera camera) {
                        if (mIsDetectingFace) {
                            Camera.Parameters mCameraParameters = camera.getParameters();
                            FaceInfo[] faceInfoList = null;
                            // reinit
                            if (mDeviceSwiched) {
                                String modelPath = getActivity().getFilesDir().getAbsolutePath();
                                int device = 0;
                                if (mUseHuaweiNpu) {
                                    device = 2;
                                } else if (mUseGPU) {
                                    device = 1;
                                }
                                int ret = mFaceDetector.init(modelPath, NET_W_INPUT, NET_H_INPUT, 0.975f, 0.23f, 1, device);
                                if (ret == 0) {
                                    mIsDetectingFace = true;
                                    mFpsCounter.init();
                                } else {
                                    mIsDetectingFace = false;
                                    Log.e(TAG, "Face detector init failed " + ret);
                                }
                                mDeviceSwiched = false;
                            }
                            if (mIsCountFps) {
                                mFpsCounter.begin("BlazeFaceDetect");
                            }
                            faceInfoList = mFaceDetector.detectFromStream(data, mCameraWidth, mCameraHeight, mDrawView.getWidth(), mDrawView.getHeight(), mRotate);
//                            if (faceInfoList != null && faceInfoList.length != 0) {
//                                Log.d(TAG, "onPreviewFrame: mRotate => " + mRotate);
//                                byteArr2BitmapAndLogRecognizeResult(data, camera, mDrawView.getWidth(), mDrawView.getHeight(), floatArr2IntArr(faceInfoList[0].landmarks));
//                            }
                            if (mIsCountFps) {
                                mFpsCounter.end("BlazeFaceDetect");
                                double fps = mFpsCounter.getFps("BlazeFaceDetect");
                                String monitorResult = "device: ";
                                if (mUseGPU) {
                                    monitorResult += "opencl\n";
                                } else if (mUseHuaweiNpu) {
                                    monitorResult += "huawei_npu\n";
                                } else {
                                    monitorResult += "arm\n";
                                }
                                monitorResult += "fps: " + String.format("%.02f", fps);
                                TextView monitor_result_view = (TextView) root.findViewById(R.id.monitor_result);
                                monitor_result_view.setText(monitorResult);
                            }
                            Log.i(TAG, "detect from stream ret " + faceInfoList);
                            int faceCount = 0;
                            if (faceInfoList != null) {
                                faceCount = faceInfoList.length;
                            }
                            mDrawView.addFaceRect(faceInfoList);
//                            Log.d("justtest", faceInfoList[0].toString());
                        } else {
                            Log.i(TAG, "No face");
                        }
                    }
                });
                mOpenedCamera.setPreviewDisplay(surfaceHolder);
                mOpenedCamera.startPreview();
                mSurfaceHolder = surfaceHolder;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    public void closeCamera() {
        Log.i(TAG, "closeCamera");
        mIsDetectingFace = false;
        if (mOpenedCamera != null) {
            try {
                mOpenedCamera.stopPreview();
                mOpenedCamera.setPreviewCallback(null);
                Log.i(TAG, "stop preview, not previewing");
            } catch (Exception e) {
                e.printStackTrace();
                Log.i(TAG, "Error setting camera preview: " + e.toString());
            }
            try {
                mOpenedCamera.release();
                mOpenedCamera = null;
            } catch (Exception e) {
                e.printStackTrace();
                Log.i(TAG, "Error setting camera preview: " + e.toString());
            } finally {
                mOpenedCamera = null;
            }
        }
        mFaceDetector.deInit();
    }

    private void byteArr2BitmapAndLogRecognizeResult(byte[] data, Camera camera, int w, int h, int[] landmarks) {
        Log.d(TAG, "byteArr2BitmapAndLogRecognizeResult: 1");
        Bitmap bitmap = byteArr2Bitmap(data, camera);
        Log.d(TAG, "byteArr2BitmapAndLogRecognizeResult: 2");
        float features[] = mFaceRecognize.recognize(getPixelsRGBA(bitmap), w, h, landmarks);
        Log.d(TAG, "byteArr2BitmapAndLogRecognizeResult: 3");
        if (previousFeature == null) {
            previousFeature = features;
            return;
        }
        Log.d(TAG, "byteArr2BitmapAndLogRecognizeResult: " + features);
        Log.d(TAG, "byteArr2BitmapAndLogRecognizeResult: 是同一个人吗？: " + (mFaceRecognize.compare(features, previousFeature) >= threshold ? "是" : "不是"));
        previousFeature = features;
    }

    private Bitmap byteArr2Bitmap(byte[] data, Camera camera) {
        byte[] rawImage;
        Bitmap bitmap;
        camera.setOneShotPreviewCallback(null);
        //处理data
        Camera.Size previewSize = camera.getParameters().getPreviewSize();//获取尺寸,格式转换的时候要用到
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        YuvImage yuvimage = new YuvImage(
                data,
                ImageFormat.NV21,
                previewSize.width,
                previewSize.height,
                null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        yuvimage.compressToJpeg(new Rect(0, 0, previewSize.width, previewSize.height), 100, baos);// 80--JPG图片的质量[0-100],100最高
        rawImage = baos.toByteArray();
        //将rawImage转换成bitmap
        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        bitmap = BitmapFactory.decodeByteArray(rawImage, 0, rawImage.length, options);
        return bitmap;
    }

    //提取像素点
    private byte[] getPixelsRGBA(Bitmap image) {
        // calculate how many bytes our image consists of
        int bytes = image.getByteCount();
        ByteBuffer buffer = ByteBuffer.allocate(bytes); // Create a new buffer
        image.copyPixelsToBuffer(buffer); // Move the byte data to the buffer
        byte[] temp = buffer.array(); // Get the underlying array containing the
        return temp;
    }

    private int[] floatArr2IntArr(float f[]) {
        int arr[] = new int[f.length];
        for (int i = 0; i < f.length; i++) {
            arr[i] = (int) f[i];
        }
        return arr;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mFaceDetector.deInit();
        mFaceRecognize.deInit();
    }
}
