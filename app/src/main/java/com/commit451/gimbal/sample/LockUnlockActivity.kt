package com.commit451.gimbal.sample

import android.content.Intent
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity

import com.commit451.gimbal.Gimbal
import kotlinx.android.synthetic.main.activity_lock_unlock.*

class LockUnlockActivity : AppCompatActivity() {

    private lateinit var gimbal: Gimbal
    private var isLocked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gimbal = Gimbal(this)
        //only lock on the first creation, so that unlock will work. Otherwise, we would unlock,
        //and once the user rotated, the lock would be enabled again. Most of the time if you are
        // not allowing lock/unlock, you will not need this

        setContentView(R.layout.activity_lock_unlock)

        if (savedInstanceState != null) {
            isLocked = savedInstanceState.getBoolean("locked")
        }
        buttonUnlock.setOnClickListener {
            if (isLocked) {
                gimbal.unlock()
            } else {
                gimbal.lock()
            }
            isLocked = !isLocked
            bindButtonState()
        }
        buttonEventNormalization.setOnClickListener {
            startActivity(Intent(this@LockUnlockActivity, GravityNormalizationActivity::class.java))
        }
        bindButtonState()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("locked", isLocked)
    }

    private fun bindButtonState() {
        if (isLocked) {
            buttonUnlock.text = "Unlock"
        } else {
            buttonUnlock.text = "Lock"
        }
    }
}
