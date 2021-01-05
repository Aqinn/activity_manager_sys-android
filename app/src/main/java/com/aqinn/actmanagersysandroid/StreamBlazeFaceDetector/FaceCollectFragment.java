package com.aqinn.actmanagersysandroid.StreamBlazeFaceDetector;

import android.hardware.Camera;
import android.os.Bundle;
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

import java.io.IOException;

import butterknife.ButterKnife;

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

    /**********************************     Get Preview Advised    **********************************/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
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

        String targetDir = getActivity().getFilesDir().getAbsolutePath();

        //copy detect model to sdcard
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
                            FaceInfo[] faceInfoList;
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
        mFaceDetector.deinit();
    }


}
