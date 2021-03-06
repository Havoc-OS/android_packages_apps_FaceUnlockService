package com.android.faceunlock.camera.callables;

import android.hardware.Camera;
import android.util.Log;

import com.android.faceunlock.camera.CameraHandlerThread;
import com.android.faceunlock.camera.listeners.CameraListener;
import com.android.faceunlock.util.Util;

@SuppressWarnings("rawtypes")
public class CloseCameraCallable extends CameraCallable {
    private static final String TAG = CloseCameraCallable.class.getSimpleName();

    public CloseCameraCallable(CameraListener cameraListener) {
        super(cameraListener);
    }

    @SuppressWarnings("deprecation")
    @Override
    public CallableReturn call() {
        CameraHandlerThread.CameraData cameraData = getCameraData();
        Camera camera = cameraData.mCamera;
        if (camera == null) {
            return new CallableReturn(new Exception("Camera isn't opened"));
        }
        if (Util.DEBUG) {
            Log.d(TAG, "releasing camera");
        }
        camera.setErrorCallback(null);
        camera.release();
        cameraData.mCamera = null;
        cameraData.mCameraId = -1;
        cameraData.mParameters = null;
        return new CallableReturn(null);
    }

    @Override
    public String getTag() {
        return TAG;
    }
}
