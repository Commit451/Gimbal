package com.commit451.gimbal

import android.app.Activity
import android.content.pm.ActivityInfo
import android.graphics.Point
import android.hardware.SensorEvent
import android.view.Surface

import java.lang.ref.WeakReference

/**
 * Tool to help out with rotation and orientation based events
 */
class Gimbal
/**
 * Create a new instance of Gimbal, using the context and values of the activity
 * @param activity the activity to get values from and potentially modify
 */
(activity: Activity) {

    private val activityWeakReference: WeakReference<Activity> = WeakReference(activity)
    private val normalizationPoint: Point by lazy(LazyThreadSafetyMode.NONE) {
        Point()
    }
    private val displayPoint: Point by lazy(LazyThreadSafetyMode.NONE) {
        Point()
    }

    /**
     * Locks the activity to the orientation the user was in when they entered the activity.
     * @return true if lock was successful, false if it was not
     */
    fun lock(): Boolean {
        val activity = activityWeakReference.get() ?: return false
        val display = activity.windowManager.defaultDisplay
        val rotation = display.rotation
        display.getSize(displayPoint)
        val height = displayPoint.y
        val width = displayPoint.x
        when (rotation) {
            Surface.ROTATION_90 -> if (width > height) {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            } else {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
            }
            Surface.ROTATION_180 -> if (height > width) {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
            } else {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
            }
            Surface.ROTATION_270 -> if (width > height) {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
            } else {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
            Surface.ROTATION_0 -> if (height > width) {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            } else {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }
            else -> if (height > width) {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            } else {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }
        }
        return true
    }

    /**
     * Unlock the previously locked activity
     * @return true if successfully unlocked. False otherwise
     */
    fun unlock(): Boolean {
        val activity = activityWeakReference.get() ?: return false
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        return true
    }

    /**
     * Normalize a [SensorEvent] from the gravity sensor for the current orientation.
     * @param event the [SensorEvent] which will be normalized, typically acquired in a [android.hardware.SensorEventListener] event
     * @return true if event has been normalized. False otherwise
     */
    fun normalizeGravityEvent(event: SensorEvent): Boolean {
        val activity = activityWeakReference.get() ?: return false
        val display = activity.window.windowManager.defaultDisplay
        val rotation = display.rotation
        display.getSize(normalizationPoint)
        val height = normalizationPoint.y
        val width = normalizationPoint.x
        val x: Float
        val y: Float
        when (rotation) {
            Surface.ROTATION_90 -> if (width > height) {
                //landscape
                x = -event.values[1]
                y = event.values[0]
            } else {
                //reverse portrait
                x = -event.values[0]
                y = -event.values[1]
            }
            Surface.ROTATION_180 -> if (height > width) {
                //reverse portrait
                x = -event.values[0]
                y = -event.values[1]
            } else {
                //reverse landscape
                x = event.values[1]
                y = -event.values[0]
            }
            Surface.ROTATION_270 -> if (width > height) {
                //reverse landscape
                x = event.values[1]
                y = -event.values[0]
            } else {
                //portrait
                x = event.values[0]
                y = event.values[1]
            }
            Surface.ROTATION_0 -> if (height > width) {
                //portrait
                x = event.values[0]
                y = event.values[1]
            } else {
                //landscape
                x = -event.values[1]
                y = event.values[0]
            }
            else -> if (height > width) {
                x = event.values[0]
                y = event.values[1]
            } else {
                x = -event.values[1]
                y = event.values[0]
            }
        }
        event.values[0] = x
        event.values[1] = y
        return true
    }
}
