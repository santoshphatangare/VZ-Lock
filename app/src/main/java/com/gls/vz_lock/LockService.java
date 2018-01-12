package com.gls.vz_lock;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class LockService extends Service {
    private Handler handler;
    Runnable runnable;
    @Override
    public void onCreate() {
        super.onCreate();
        PackageManager packageManager = getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resInfos = packageManager.queryIntentActivities(intent, 0);

        final ArrayList<String> packageNames = new ArrayList<String>(0);

        for(ResolveInfo resolveInfo : resInfos) {
            if(!resolveInfo.activityInfo.packageName.startsWith("com.google") && !resolveInfo.activityInfo.packageName.startsWith("com.google.android.youtube")){
                packageNames.add(resolveInfo.activityInfo.packageName);
            }
        }
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
                        ComponentName componentInfo = taskInfo.get(0).topActivity;
                        String packageName = componentInfo.getPackageName();
                        if (!HomeActivity.unlockPackages.contains(packageName)) {
                            if(packageNames.contains(packageName) || packageName.startsWith("com.android")) {
                                Intent startIntent = new Intent(HomeActivity.context, HomeActivity.class);
                                startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                HomeActivity.context.startActivity(startIntent);
                            }
                        }
                        Object statusBarService = getSystemService("statusbar");
                        Class<?> statusBarManager = null;
                        try {
                            statusBarManager = Class.forName("android.app.StatusBarManager");
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        Method collapseStatusBar = null;
                        try {
                            if (Build.VERSION.SDK_INT > 16) {
                                collapseStatusBar = statusBarManager .getMethod("collapsePanels");
                            } else {
                                collapseStatusBar = statusBarManager .getMethod("collapse");
                            }
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        }
                        collapseStatusBar.setAccessible(true);
                        try {
                            collapseStatusBar.invoke(statusBarService);
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();
                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(runnable, 1000);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(this, LockService.class);
        startService(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

}
