package com.android.faceunlock.camera;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Handler;
import android.view.SurfaceHolder;

import com.android.faceunlock.camera.callables.AddCallbackBufferCallable;
import com.android.faceunlock.camera.callables.AutoFocusCallable;
import com.android.faceunlock.camera.callables.CameraCallable;
import com.android.faceunlock.camera.callables.CloseCameraCallable;
import com.android.faceunlock.camera.callables.OpenCameraCallable;
import com.android.faceunlock.camera.callables.ReadParamsCallable;
import com.android.faceunlock.camera.callables.SetDisplayOrientationCallback;
import com.android.faceunlock.camera.callables.SetFaceDetectionCallback;
import com.android.faceunlock.camera.callables.SetPreviewCallbackCallable;
import com.android.faceunlock.camera.callables.StartPreviewCallable;
import com.android.faceunlock.camera.callables.StopPreviewCallable;
import com.android.faceunlock.camera.callables.WriteParamsCallable;
import com.android.faceunlock.camera.listeners.ByteBufferCallbackListener;
import com.android.faceunlock.camera.listeners.CameraListener;
import com.android.faceunlock.camera.listeners.ErrorCallbackListener;
import com.android.faceunlock.camera.listeners.FocusResultListener;
import com.android.faceunlock.camera.listeners.ReadParametersListener;

public class CameraService {
    private static final int DEFAULT_MSG_TYPE = 1;
    private final Handler mServiceHandler;

    private CameraService() {
        CameraHandlerThread cameraHandlerThread = new CameraHandlerThread();
        cameraHandlerThread.start();
        this.mServiceHandler = new Handler(cameraHandlerThread.getLooper(), message -> {
            ((CameraCallable) message.obj).run();
            return true;
        });
    }

    private static CameraService getInstance() {
        return LazyLoader.INSTANCE;
    }

    public static void openCamera(int i, ErrorCallbackListener errorCallbackListener, CameraListener cameraListener) {
        getInstance().addCallable(new OpenCameraCallable(i, errorCallbackListener, cameraListener));
    }

    public static void closeCamera(CameraListener cameraListener) {
        CameraService instance = getInstance();
        clearQueue();
        instance.addCallable(new CloseCameraCallable(cameraListener));
    }

    public static void autoFocus(boolean z, FocusResultListener focusResultListener, CameraListener cameraListener) {
        getInstance().addCallable(new AutoFocusCallable(z, focusResultListener, cameraListener));
    }

    public static void readParameters(ReadParametersListener readParametersListener, CameraListener cameraListener) {
        getInstance().addCallable(new ReadParamsCallable(readParametersListener, cameraListener));
    }

    public static void writeParameters(CameraListener cameraListener) {
        getInstance().addCallable(new WriteParamsCallable(cameraListener));
    }

    public static void startPreview(CameraListener cameraListener) {
        getInstance().addCallable(new StartPreviewCallable(cameraListener));
    }

    public static void startPreview(SurfaceTexture surfaceTexture, CameraListener cameraListener) {
        getInstance().addCallable(new StartPreviewCallable(surfaceTexture, cameraListener));
    }

    public static void startPreview(SurfaceHolder surfaceHolder, CameraListener cameraListener) {
        getInstance().addCallable(new StartPreviewCallable(surfaceHolder, cameraListener));
    }

    public static void stopPreview(CameraListener cameraListener) {
        getInstance().addCallable(new StopPreviewCallable(cameraListener));
    }

    public static void addCallbackBuffer(byte[] bArr, CameraListener cameraListener) {
        getInstance().addCallable(new AddCallbackBufferCallable(bArr, cameraListener));
    }

    public static void setPreviewCallback(ByteBufferCallbackListener byteBufferCallbackListener, boolean z, CameraListener cameraListener) {
        getInstance().addCallable(new SetPreviewCallbackCallable(byteBufferCallbackListener, z, cameraListener));
    }

    public static void setFaceDetectionCallback(Camera.FaceDetectionListener faceDetectionListener, CameraListener cameraListener) {
        getInstance().addCallable(new SetFaceDetectionCallback(faceDetectionListener, cameraListener));
    }

    public static void setDisplayOrientationCallback(int i, CameraListener cameraListener) {
        getInstance().addCallable(new SetDisplayOrientationCallback(i, cameraListener));
    }

    public static void clearQueue() {
        getInstance().mServiceHandler.removeMessages(DEFAULT_MSG_TYPE);
    }

    private void addCallable(CameraCallable cameraCallable) {
        this.mServiceHandler.sendMessage(this.mServiceHandler.obtainMessage(DEFAULT_MSG_TYPE, cameraCallable));
    }

    private static final class LazyLoader {
        private static final CameraService INSTANCE = new CameraService();

        private LazyLoader() {
        }
    }
}
