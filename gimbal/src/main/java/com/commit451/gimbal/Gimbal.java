package com.commit451.gimbal;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.hardware.SensorEvent;
import android.view.Display;
import android.view.Surface;

import java.lang.ref.WeakReference;

/**
 * Tool to help out with rotation based things
 */
public class Gimbal {

    private WeakReference<Activity> mActivityWeakReference;
    private Point mNormalizationPoint;
    private Point mDisplayPoint;

    public Gimbal(Activity activity) {
        mActivityWeakReference = new WeakReference<>(activity);
    }

    /**
     * Locks the activity to the orientation the user was in when they entered the activity.
     * @return true if lock was successful, false if it was not
     */
    public boolean lock() {
        Activity activity = mActivityWeakReference.get();
        if (activity == null) {
            return false;
        }
        Display display = activity.getWindowManager().getDefaultDisplay();
        int rotation = display.getRotation();
        int height;
        int width;
        if (mDisplayPoint == null) {
            mDisplayPoint = new Point();
        }
        display.getSize(mDisplayPoint);
        height = mDisplayPoint.y;
        width = mDisplayPoint.x;
        switch (rotation) {
            case Surface.ROTATION_90:
                if (width > height) {
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
                else {
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
                }
                break;
            case Surface.ROTATION_180:
                if (height > width) {
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
                }
                else {
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                }
                break;
            case Surface.ROTATION_270:
                if (width > height) {
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                }
                else {
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
                break;
            case Surface.ROTATION_0:
            default :
                if (height > width) {
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
                else {
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
        }
        return true;
    }

    /**
     * Unlock the previously locked activity
     */
    public boolean unlock() {
        Activity activity = mActivityWeakReference.get();
        if (activity == null) {
            return false;
        }
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        return true;
    }

    /**
     * Normalize a {@link SensorEvent} from the gravity sensor for the current orientation. Has chance to fail at normalization if the activity no longer exists or has a window
     * @param event the {@link SensorEvent} which will be normalized, typically acquired in a {@link android.hardware.SensorEventListener} event
     */
    public void normalizeGravityEvent(SensorEvent event) {
        Activity activity = mActivityWeakReference.get();
        if (activity == null) {
            return;
        }
        Display display = activity.getWindow().getWindowManager().getDefaultDisplay();
        int rotation = display.getRotation();
        int height;
        int width;
        if (mNormalizationPoint == null) {
            mNormalizationPoint = new Point();
        }
        display.getSize(mNormalizationPoint);
        height = mNormalizationPoint.y;
        width = mNormalizationPoint.x;
        float x;
        float y;
        switch (rotation) {
            case Surface.ROTATION_90:
                if (width > height) {
                    //landscape
                    x = -event.values[1];
                    y = event.values[0];
                }
                else {
                    //reverse portrait
                    x = -event.values[0];
                    y = -event.values[1];
                }
                break;
            case Surface.ROTATION_180:
                if (height > width) {
                    //reverse portrait
                    x = -event.values[0];
                    y = -event.values[1];
                }
                else {
                    //reverse landscape
                    x = event.values[1];
                    y = -event.values[0];
                }
                break;
            case Surface.ROTATION_270:
                if (width > height) {
                    //reverse landscape
                    x = event.values[1];
                    y = -event.values[0];
                }
                else {
                    //portrait
                    x = event.values[0];
                    y = event.values[1];
                }
                break;
            case Surface.ROTATION_0:
            default :
                if (height > width) {
                    //portrait
                    x = event.values[0];
                    y = event.values[1];
                }
                else {
                    //landscape
                    x = -event.values[1];
                    y = event.values[0];
                }
        }
        event.values[0] = x;
        event.values[1] = y;
    }
}
