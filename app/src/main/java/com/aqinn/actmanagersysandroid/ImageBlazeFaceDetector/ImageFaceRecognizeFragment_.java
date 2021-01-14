package com.aqinn.actmanagersysandroid.ImageBlazeFaceDetector;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.aqinn.actmanagersysandroid.FaceInfo;
import com.aqinn.actmanagersysandroid.FaceRecognize;
import com.aqinn.actmanagersysandroid.FileUtils;
import com.aqinn.actmanagersysandroid.R;
import com.aqinn.actmanagersysandroid.StreamBlazeFaceDetector.DrawView;
import com.aqinn.actmanagersysandroid.StreamBlazeFaceDetector.StreamBaseFragment;

import java.util.ArrayList;


public class ImageFaceRecognizeFragment_ extends StreamBaseFragment {

    private final static String TAG = "ImageFaceRecognizeFragment_";
    private FaceRecognize mFaceRecognize = new FaceRecognize();

    private static final String IMAGE = "test_blazeface.jpg";
    private static final int NET_H_INPUT = 128;
    private static final int NET_W_INPUT = 128;
    private Paint mPaint = new Paint();
    private DrawView mDrawView;
    private ToggleButton mGPUSwitch;
    private Button mRunButton;
    private boolean mUseGPU = false;
    //add for npu
    private ToggleButton mHuaweiNPUswitch;
    private boolean mUseHuaweiNpu = false;
    private TextView HuaweiNpuTextView;

    /**********************************     Get Preview Advised    **********************************/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.loadLibrary("tnn_wrapper");
        String modelPath = initModel();
    }

    private String initModel() {

        String targetDir = getActivity().getFilesDir().getAbsolutePath();

        //copy detect model to sdcard
        String[] modelPathsDetector = {
                "mobilefacenet.bin",
                "mobilefacenet.param",
        };

        for (int i = 0; i < modelPathsDetector.length; i++) {
            String modelFilePath = modelPathsDetector[i];
            String interModelFilePath = targetDir + "/" + modelFilePath;
            FileUtils.copyAsset(getActivity().getAssets(), "mobilefacenet/" + modelFilePath, interModelFilePath);
        }
        return targetDir;
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.back_rl) {
            clickBack();
        }
    }

    private void clickBack() {
        if (getActivity() != null) {
            (getActivity()).finish();
        }
    }

    @Override
    public void setFragmentView() {
        setView(R.layout.fragment_image_detector);
        setTitleGone();
        $$(R.id.back_rl);

        mDrawView = (DrawView) $(R.id.drawView);
        mRunButton = $(R.id.run_button);
        mRunButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDetect();
            }
        });

        Bitmap originBitmap = FileUtils.readBitmapFromFile(getActivity().getAssets(), IMAGE);
        ImageView source = (ImageView) $(R.id.origin);
        source.setImageBitmap(originBitmap);
    }

    @Override
    public void openCamera() {

    }

    @Override
    public void startPreview(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void closeCamera() {

    }


    private void startDetect() {

        Bitmap originBitmap = FileUtils.readBitmapFromFile(getActivity().getAssets(), IMAGE);
        Bitmap scaleBitmap = Bitmap.createScaledBitmap(originBitmap, NET_W_INPUT, NET_H_INPUT, false);
        ImageView source = (ImageView) $(R.id.origin);
        source.setImageBitmap(originBitmap);
        String modelPath = initModel();
        Log.d(TAG, "Init classify " + modelPath);
        int result = mFaceRecognize.init(modelPath);
        Log.d(TAG, "init result => " + result);
        int landmark[] = new int[]{1,2,3,3,2,1,1,1,1,1};
        float[] features = mFaceRecognize.faceRecognize(originBitmap, originBitmap.getWidth(), originBitmap.getHeight(), landmark);
        Log.d(TAG, "startDetect: features => " + features);
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");

        getFocus();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.i(TAG, "onStop");
        super.onStop();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

    private void preview() {
        Log.i(TAG, "preview");

    }

    private void getFocus() {
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    clickBack();
                    return true;
                }
                return false;
            }
        });
    }

}
