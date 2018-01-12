package com.gls.vz_lock.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;

import com.gls.vz_lock.HomeActivity;

import java.util.List;

/**
 * Created by Santosh on 13-Dec-17.
 */

public class CheckRunningActivity extends Thread{
    ActivityManager am = null;
    Context context = null;

    public CheckRunningActivity(Context con){
        context = con;
        am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    }

    public void run(){
        while(true){
            List< ActivityManager.RunningTaskInfo > taskInfo = am.getRunningTasks(1);
            String currentRunningActivityName = taskInfo.get(0).topActivity.getPackageName();
            Log.e("Package",currentRunningActivityName);
            if (currentRunningActivityName.equals("com.sonyericsson.organizer")) {
                Intent startIntent = new Intent(context, HomeActivity.class);
                startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(startIntent);
            }
        }
    }
}
