package com.commit451.gimbal.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.commit451.gimbal.Gimbal;


public class LockUnlockActivity extends AppCompatActivity {

    Gimbal mGimbal;
    boolean isLocked = false;
    Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGimbal = new Gimbal(this);
        //only lock on the first creation, so that unlock will work. Otherwise, we would unlock,
        //and once the user rotated, the lock would be enabled again. Most of the time if you are
        // not allowing lock/unlock, you will not need this

        setContentView(R.layout.activity_lock_unlock);

        if (savedInstanceState != null) {
            isLocked = savedInstanceState.getBoolean("locked");
        }
        mButton = (Button) findViewById(R.id.button_unlock);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLocked) {
                    mGimbal.unlock();
                } else {
                    mGimbal.lock();
                }
                isLocked = !isLocked;
                bindButtonState();
            }
        });
        findViewById(R.id.button_event_normalization).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LockUnlockActivity.this, GravityNormalizationActivity.class));
            }
        });
        bindButtonState();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("locked", isLocked);
    }

    private void bindButtonState() {
        if (isLocked) {
            mButton.setText("Unlock");
        } else {
            mButton.setText("Lock");
        }
    }
}
