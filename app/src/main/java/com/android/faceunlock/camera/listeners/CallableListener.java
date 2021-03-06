package com.android.faceunlock.camera.listeners;

public interface CallableListener {
    void onComplete(Object value);

    void onError(Exception exc);
}
