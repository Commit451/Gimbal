package com.commit451.gimbal.sample;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.commit451.gimbal.Gimbal;
import com.jawnnypoo.physicslayout.PhysicsFrameLayout;

import org.jbox2d.common.Vec2;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Show the gravity normalization of Gimbal
 */
public class GravityNormalizationActivity extends Activity {

    @Bind(R.id.physicslayout)
    PhysicsFrameLayout mPhysicsLayout;
    @Bind(R.id.image)
    ImageView mImage;

    Gimbal mGimbal;
    SensorManager mSensorManager;
    Sensor mGravitySensor;

    private final SensorEventListener mSensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
                if (mPhysicsLayout.getPhysics().getWorld() != null) {
                    mGimbal.normalizeGravityEvent(event);
                    mPhysicsLayout.getPhysics().getWorld().setGravity(new Vec2(-event.values[0], event.values[1]));
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gravity_normalization);
        ButterKnife.bind(this);
        mGimbal = new Gimbal(this);
        mGimbal.lock();
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mGravitySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        if (mGravitySensor == null) {
            Toast.makeText(GravityNormalizationActivity.this, "No gravity sensor", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorEventListener, mGravitySensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mSensorEventListener);
    }
}
