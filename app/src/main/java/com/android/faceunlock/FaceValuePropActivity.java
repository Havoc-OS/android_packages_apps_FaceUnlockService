package com.android.faceunlock;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.android.faceunlock.util.Util;
import com.android.faceunlock.widget.VideoListener;

public class FaceValuePropActivity extends Activity implements View.OnClickListener {
    private VideoListener mVideoListener;

    @Override
    protected void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.face_value_prop);
        Button btnCancel = findViewById(R.id.vp_cancel);
        btnCancel.setOnClickListener(this);
        Button btnNext = findViewById(R.id.vp_next);
        btnNext.setOnClickListener(this);
        mVideoListener = new VideoListener(this,
                findViewById(R.id.video),
                R.raw.face_settings,
                true);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.vp_cancel) {
            setResult(0);
            finish();
        } else if (view.getId() == R.id.vp_next) {
            setResult(-1);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mVideoListener != null) {
            mVideoListener.startVideo();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoListener.stopVideo();
    }
}
