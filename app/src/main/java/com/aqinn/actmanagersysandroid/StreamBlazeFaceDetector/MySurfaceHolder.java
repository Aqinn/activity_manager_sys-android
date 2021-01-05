package com.aqinn.actmanagersysandroid.StreamBlazeFaceDetector;

import android.util.Log;
import android.view.SurfaceHolder;

import com.aqinn.actmanagersysandroid.fragment.BaseFragment;

/**
 * @author Aqinn
 * @date 2021/1/4 11:27 PM
 */
public class MySurfaceHolder implements SurfaceHolder.Callback {
    private static final String TAG = "MySurfaceHolder";

    private FaceCollectFragment mFaceLiveFragment;

    private SurfaceHolder mHolder;


    public MySurfaceHolder(FaceCollectFragment faceLiveFragment) {
        mFaceLiveFragment = faceLiveFragment;
    }

    /**
     * @param surfaceHolder SurfaceView的holder
     */
    public void setSurfaceHolder(SurfaceHolder surfaceHolder) {
        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = surfaceHolder;
        mHolder.addCallback(this);
    }


    /********************************
     * SurfaceHolder.Callback function start
     *********************************/

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        Log.i(TAG, "surfaceCreated");
        mFaceLiveFragment.openCamera();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        Log.i(TAG, "surfaceChanged");
        if (mHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }

        mFaceLiveFragment.startPreview(surfaceHolder);
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i(TAG, "surfaceDestroyed");
        holder.removeCallback(this);

        mFaceLiveFragment.closeCamera();
    }

    /********************************
     * SurfaceHolder.Callback function end
     *********************************/

}
