package com.commit451.gimbal.sample

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.commit451.gimbal.Gimbal

import org.jbox2d.common.Vec2

import kotlinx.android.synthetic.main.activity_gravity_normalization.*

/**
 * Show the gravity normalization of Gimbal
 */
class GravityNormalizationActivity : AppCompatActivity() {

    private lateinit var gimbal: Gimbal
    private lateinit var sensorManager: SensorManager
    private var gravitySensor: Sensor? = null

    private val sensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            if (event.sensor.type == Sensor.TYPE_GRAVITY) {
                if (physicslayout.physics.world != null) {
                    gimbal.normalizeGravityEvent(event)
                    physicslayout.physics.world.gravity = Vec2(-event.values[0], event.values[1])
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gravity_normalization)
        gimbal = Gimbal(this)
        gimbal.lock()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)
        if (gravitySensor == null) {
            Toast.makeText(this@GravityNormalizationActivity, "No gravity sensor", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(sensorEventListener, gravitySensor, SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(sensorEventListener)
    }
}
