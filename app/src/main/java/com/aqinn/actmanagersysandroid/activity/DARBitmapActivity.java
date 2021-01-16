package com.aqinn.actmanagersysandroid.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.aqinn.actmanagersysandroid.MyApplication;
import com.aqinn.actmanagersysandroid.R;
import com.aqinn.actmanagersysandroid.data.ApiResult;
import com.aqinn.actmanagersysandroid.retrofitservice.UserFeatureService;
import com.aqinn.actmanagersysandroid.utils.CameraUtils;
import com.aqinn.actmanagersysandroid.utils.CommonUtils;
import com.aqinn.actmanagersysandroid.utils.Utils;
import com.aqinn.facerecognizencnn.FaceInfo;
import com.aqinn.facerecognizencnn.FaceRecognize;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DARBitmapActivity extends Activity {

    private static final String TAG = "DARBitmapActivity";

    @Inject
    public UserFeatureService userFeatureService;

    private ImageView imageView1;
    private Bitmap yourSelectedImage1 = null;
    private Bitmap faceImage1 = null;
    TextView faceInfo1, cmpResult;       //显示face 检测的结果和compare的结果
    private byte[] imageDate1;
    private int mtcnn_landmarks1[] = new int[10];  //存放mtcnn人脸关键点

    // 初始参数设置，可以按需修改
    private int minFaceSize = 40;
    private int testTimeCount = 1;
    private int threadsNumber = 2;
    private double threshold = 0.5;            // 人脸余弦距离的阈值

    private FaceRecognize mFaceRecognize = new FaceRecognize();

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};

    public void verifyStoragePermissions() {
        askForPermission();
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_darbitmap);
        MyApplication.getApplicationComponent().inject(this);
        verifyStoragePermissions();

        initModel();

        //左边的图片
        imageView1 = (ImageView) findViewById(R.id.imageView1);
        faceInfo1 = (TextView) findViewById(R.id.faceInfo1);
        Button buttonImage1 = (Button) findViewById(R.id.select1);
        buttonImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(i, 1);
            }
        });
        //第一张图片人脸检测
        Button buttonDetect1 = (Button) findViewById(R.id.detect1);
        buttonDetect1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (yourSelectedImage1 == null)
                    return;

                //人脸检测
                faceImage1 = null;
                int width = yourSelectedImage1.getWidth();
                int height = yourSelectedImage1.getHeight();
                imageDate1 = getPixelsRGBA(yourSelectedImage1);

                long timeDetectFace = System.currentTimeMillis();   //检测起始时间
                float tempArr[] = mFaceRecognize.detectFromBitmap(yourSelectedImage1); //只检测最大人脸，速度有较大提升
                FaceInfo faceInfo = CommonUtils.floatArr2FaceInfo(tempArr);
                timeDetectFace = System.currentTimeMillis() - timeDetectFace; //人脸检测时间

                mtcnn_landmarks1 = CommonUtils.getUsefulLandmarksFromFaceInfo(faceInfo);

                if (faceInfo != null) {       //检测到人脸
                    faceInfo1.setText("pic1 detect time:" + timeDetectFace);
                    imageView1.setImageBitmap(CameraUtils.drawFaceRegion(yourSelectedImage1, faceInfo));
                    faceImage1 = Bitmap.createBitmap(yourSelectedImage1, (int) faceInfo.x1, (int) faceInfo.y1, (int) (faceInfo.x2 - faceInfo.x1), (int) (faceInfo.y2 - faceInfo.y1));
//                    imageView1.setImageBitmap(faceImage1);
                } else {     //没有人脸
                    faceInfo1.setText("no face");
                }
            }
        });

        //人脸识别(compare)
        cmpResult = (TextView) findViewById(R.id.textView1);
        Button cmpImage = (Button) findViewById(R.id.facecmp);
        cmpImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (faceImage1 == null) { //检测的人脸图片为空
                    cmpResult.setText("没有检测到人脸");
                    return;
                }
                //人脸识别
                float features1[] = mFaceRecognize.recognize(imageDate1, yourSelectedImage1.getWidth(), yourSelectedImage1.getHeight(), mtcnn_landmarks1);
                cmpResult.setText("识别成功");
//                selfFaceRecognize(features1);
                videoFaceRecognize(features1);
            }
        });
    }

    private void selfFaceRecognize(float features1[]) {
        Observable<ApiResult> observable = userFeatureService.selfFaceRecognize(9L, 23L, CommonUtils.arr2String(features1));
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ApiResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d("facerecognize", "onSubscribe: ");
                    }

                    @Override
                    public void onNext(@NonNull ApiResult apiResult) {
                        Log.d("facerecognize", "onNext: ");
                        if (apiResult.success) {
                            Log.d("facerecognize", "onNext: 人脸识别成功，你的身份信息是: " + (String) apiResult.data);
                            cmpResult.setText((String) apiResult.data);
                        } else {
                            Log.d("facerecognize", "onNext: 人脸识别失败，错误信息是: " + apiResult.errMsg);
                            cmpResult.setText("人脸识别失败，不认识这个人");
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("facerecognize", "onError: ");
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Log.d("facerecognize", "onComplete: ");
                    }
                });
    }

    private void videoFaceRecognize(float features1[]) {
        Observable<ApiResult> observable = userFeatureService.videoFaceRecognize(9L, CommonUtils.arr2String(features1));
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ApiResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d("facerecognize", "onSubscribe: ");
                    }

                    @Override
                    public void onNext(@NonNull ApiResult apiResult) {
                        Log.d("facerecognize", "onNext: ");
                        if (apiResult.success) {
                            Log.d("facerecognize", "onNext: 人脸识别成功，你的身份信息是: " + (String) apiResult.data);
                            cmpResult.setText((String) apiResult.data);
                        } else {
                            Log.d("facerecognize", "onNext: 人脸识别失败，错误信息是: " + apiResult.errMsg);
                            cmpResult.setText("人脸识别失败，不认识这个人");
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("facerecognize", "onError: ");
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Log.d("facerecognize", "onComplete: ");
                    }
                });
    }

    private void initModel() {
        mFaceRecognize = new FaceRecognize();
        mFaceRecognize.initRetainFace(getAssets());

        //拷贝模型到sd卡
        String sdPath = getCacheDir().getAbsolutePath() + "/facem/";
        Utils.copyFileFromAsset(this, "mobilefacenet.bin", sdPath + File.separator + "mobilefacenet.bin");
        Utils.copyFileFromAsset(this, "mobilefacenet.param", sdPath + File.separator + "mobilefacenet.param");
        //模型初始化
        mFaceRecognize.initMobileFacenet(sdPath);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            try {
                if (requestCode == 1) {
                    Bitmap bitmap = decodeUri(selectedImage);

                    Bitmap rgba = bitmap.copy(Bitmap.Config.ARGB_8888, true);

                    // resize to 227x227
                    //yourSelectedImage1 = Bitmap.createScaledBitmap(rgba, 227, 227, false);
                    yourSelectedImage1 = rgba;

                    imageView1.setImageBitmap(yourSelectedImage1);
                }
            } catch (FileNotFoundException e) {
                Log.e("MainActivity", "FileNotFoundException");
                return;
            }
        }
    }

    private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 400;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE
                    || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);
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

    public void askForPermission() {
        //检测权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.w(TAG, "didnt get permission,ask for it!");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1024);
        }
    }
}
