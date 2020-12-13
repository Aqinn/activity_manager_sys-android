package com.aqinn.actmanagersysandroid.activity;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.aqinn.actmanagersysandroid.utils.CommonUtil;
import com.aqinn.actmanagersysandroid.dcnn.MobilenetSSDNcnn;
import com.aqinn.actmanagersysandroid.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.ExplainReasonCallbackWithBeforeParam;
import com.permissionx.guolindev.callback.ForwardToSettingsCallback;
import com.permissionx.guolindev.callback.RequestCallback;
import com.permissionx.guolindev.request.ExplainScope;
import com.permissionx.guolindev.request.ForwardScope;
import com.qmuiteam.qmui.arch.QMUIFragmentActivity;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.util.List;

/**
 * @author Aqinn
 * @date 2020/12/2 4:55 PM
 */
public class DemoActivity extends QMUIFragmentActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static final String TAG = "DemoActivity";

    private FloatingActionButton fab_switch_camera;
    private JavaCameraView cameraView;
    private Mat rgba;
    private boolean isSave = true;

    private MobilenetSSDNcnn mobilenetssdncnn = new MobilenetSSDNcnn();
    private MobilenetSSDNcnn.Obj[] objects = null;

    private int frameCount = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        QMUIStatusBarHelper.translucent(this);
        askForPermission();
        init();
        boolean ret_init = mobilenetssdncnn.Init(getAssets());
        if (!ret_init) {
            Log.e("MainActivity", "mobilenetssdncnn Init failed");
        }
    }

    private void init() {
        fab_switch_camera = (FloatingActionButton) findViewById(R.id.fab_switch_camera);
        cameraView = findViewById(R.id.cameraView);
        int cameraId = CommonUtil.getCameraIdFromSP(this);
        if (cameraId == CameraBridgeViewBase.CAMERA_ID_ANY)
            cameraId = CameraBridgeViewBase.CAMERA_ID_BACK;
        CommonUtil.setCameraIdToSP(this, cameraView.getCameraIndex());
        cameraView.setCameraIndex(cameraId);
        cameraView.setCvCameraViewListener(this);
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        frameCount++;
        if (frameCount > 10000)
            frameCount = 0;
        rgba = inputFrame.rgba();
        // 得到当前一帧图像的内存地址
        long addr = rgba.getNativeObjAddr();
        // 对一帧图像进行处理
        Bitmap temp = Bitmap.createBitmap(rgba.cols(), rgba.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(rgba, temp);
        if (frameCount % 2 == 0)
            objects = mobilenetssdncnn.Detect(temp, false);
        // 得到一帧灰度图
//        rgba = inputFrame.gray();
//        if (!isSave) {
//            // 创建一张图片
//            Mat image = new Mat(100, 100, CvType.CV_8UC3);
//            image.setTo(new Scalar(127, 127, 127));
//            // 保存
//            File fileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "image");
//            if (!fileDir.exists()) {
//                fileDir.mkdirs();
//            }
//            // 设置存储的文件夹名字
//            File tempFile = new File(fileDir.getAbsoluteFile() + File.separator, "testimage.jpg");//设置文件名字
//            Imgcodecs.imwrite(tempFile.getAbsolutePath(), image);//保存
//            Log.d(TAG, "onCameraFrame: " + tempFile.getAbsolutePath());
//            isSave = true;
//        }
        Mat mat = showObjects(objects, temp);
        if (mat != null)
            return mat;
        return rgba;
    }

    private Mat showObjects(MobilenetSSDNcnn.Obj[] objects, Bitmap bitmap) {
        if (objects == null) {
            return null;
        }

        // draw objects on bitmap
        Bitmap rgba = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        Canvas canvas = new Canvas(rgba);

        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);

        Paint textbgpaint = new Paint();
        textbgpaint.setColor(Color.WHITE);
        textbgpaint.setStyle(Paint.Style.FILL);

        Paint textpaint = new Paint();
        textpaint.setColor(Color.BLACK);
        textpaint.setTextSize(26);
        textpaint.setTextAlign(Paint.Align.LEFT);

        for (int i = 0; i < objects.length; i++) {
            canvas.drawRect(objects[i].x, objects[i].y, objects[i].x + objects[i].w, objects[i].y + objects[i].h, paint);
            // draw filled text inside image
            {
                String text = objects[i].label + " = " + String.format("%.1f", objects[i].prob * 100) + "%";

                float text_width = textpaint.measureText(text);
                float text_height = -textpaint.ascent() + textpaint.descent();

                float x = objects[i].x;
                float y = objects[i].y - text_height;
                if (y < 0)
                    y = 0;
                if (x + text_width > rgba.getWidth())
                    x = rgba.getWidth() - text_width;

                canvas.drawRect(x, y, x + text_width, y + text_height, textbgpaint);

                canvas.drawText(text, x, y - textpaint.ascent(), textpaint);
            }
        }
        Mat mat = new Mat();
        Utils.bitmapToMat(rgba, mat);
//        Imgproc.cvtColor(mat,mat,Imgproc.COLOR_BGR2RGBA);//转换色彩空间
        return mat;
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
        } else {
            cameraView.enableView();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (cameraView != null) {
            cameraView.disableView();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraView != null) {
            cameraView.disableView();
        }
    }

    //NDK对每一帧数据进行操作
//    public static native void nativeRgba(long jrgba);

    @Override
    public void onCameraViewStarted(int width, int height) {
        //定义Mat对象
        rgba = new Mat(width, height, CvType.CV_8UC4);
    }

    /**
     * 当摄像机预览由于某种原因被停止时，这个方法就会被调用。
     * 在调用这个方法之后，不会通过onCameraFrame()回调来传递任何帧。
     */
    @Override
    public void onCameraViewStopped() {
        rgba.release();
    }

    public void switchCamera(View view) {
        if (CameraBridgeViewBase.CAMERA_ID_FRONT == cameraView.getCameraIndex()) {
            CommonUtil.setCameraIdToSP(this, CameraBridgeViewBase.CAMERA_ID_BACK);
        } else {
            CommonUtil.setCameraIdToSP(this, CameraBridgeViewBase.CAMERA_ID_FRONT);
        }
        recreate();
    }

    private void askForPermission() {
        PermissionX.init(this)
                .permissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .onExplainRequestReason(new ExplainReasonCallbackWithBeforeParam() {
                    @Override
                    public void onExplainReason(ExplainScope scope, List<String> deniedList, boolean beforeRequest) {
                        scope.showRequestReasonDialog(deniedList, "即将申请的权限是程序必须依赖的权限", "我已明白");
                    }
                })
                .onForwardToSettings(new ForwardToSettingsCallback() {
                    @Override
                    public void onForwardToSettings(ForwardScope scope, List<String> deniedList) {
                        scope.showForwardToSettingsDialog(deniedList, "您需要去应用程序设置当中手动开启权限", "我已明白");
                    }
                })
                .request(new RequestCallback() {
                    @Override
                    public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
                        if (allGranted) {
//                            Toast.makeText(MainActivity.this, "所有申请的权限都已通过", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(DemoActivity.this, "您拒绝了如下权限：" + deniedList, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}