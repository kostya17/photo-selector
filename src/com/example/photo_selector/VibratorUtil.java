package com.example.photo_selector;

import android.content.Context;
import android.os.Vibrator;

public class VibratorUtil {

    private static Vibrator mVibrator;

    public static Vibrator init(Context context){
        if (mVibrator == null){
            mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        }
        return mVibrator;
    }

    // Vibrate constantly for the specified period of time (in milliseconds).
    public static void vibrate(int milliseconds){
        if (mVibrator == null){
            throw new IllegalStateException("call init method first");
        }
        mVibrator.vibrate(milliseconds);
    }

    /*
    Vibrate with a given pattern.
    Pass in an array of ints that are the durations for which to turn on or off the vibrator in milliseconds.
    The first value indicates the number of milliseconds to wait before turning the vibrator on.
    The next value indicates the number of milliseconds for which to keep the vibrator on before turning it off.
    Subsequent values alternate between durations in milliseconds to turn the vibrator off or to turn the vibrator on.
    To cause the pattern to repeat, pass the index into the pattern array at which to start the repeat, or -1 to disable repeating.
    */
    public static void vibrate(long[] pattern, int repeat){
        if (mVibrator == null){
            throw new IllegalStateException("call init method first");
        }
        mVibrator.vibrate(pattern, repeat);
    }
}
