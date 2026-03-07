package com.commit451.gimbal.sample

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.commit451.gimbal.Gimbal
import com.commit451.gimbal.sample.databinding.ActivityLockUnlockBinding

class LockUnlockActivity : AppCompatActivity() {

    private lateinit var gimbal: Gimbal
    private var isLocked = false
    private lateinit var binding: ActivityLockUnlockBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gimbal = Gimbal(this)
        // only lock on the first creation, so that unlock will work. Otherwise, we would unlock,
        // and once the user rotated, the lock would be enabled again. Most of the time if you are
        // not allowing lock/unlock, you will not need this

        binding = ActivityLockUnlockBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState != null) {
            isLocked = savedInstanceState.getBoolean("locked")
        }
        binding.buttonUnlock.setOnClickListener {
            if (isLocked) {
                gimbal.unlock()
            } else {
                gimbal.lock()
            }
            isLocked = !isLocked
            bindButtonState()
        }
        binding.buttonEventNormalization.setOnClickListener {
            startActivity(Intent(this@LockUnlockActivity, GravityNormalizationActivity::class.java))
        }
        bindButtonState()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("locked", isLocked)
    }

    private fun bindButtonState() {
        binding.buttonUnlock.text = if (isLocked) "Unlock" else "Lock"
    }
}
