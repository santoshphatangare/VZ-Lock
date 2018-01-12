package com.gls.vz_lock.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.gls.vz_lock.beans.AppInfo;
import com.gls.vz_lock.beans.UnlockedApps;
import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Santosh on 12-Dec-17.
 */

public class AppData extends SQLiteOpenHelper{
    public static final String DATABASE_NAME = "VZ_LOCK.db";
    public AppData(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    public boolean unlockApp(AppInfo app) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", app.getAppname());
        contentValues.put("package", app.getAppPackage());
        contentValues.put("onHome", "true");
        db.insert("unlockApps", null, contentValues);
        db.close();
        return true;
    }

    public void hideApp(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("onHome", "false");
        db.update("unlockApps",contentValues,"id=?",new String[]{id+""});
        db.close();
    }

    public void showApp(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("onHome", "true");
        db.update("unlockApps",contentValues,"id=?",new String[]{id+""});
        db.close();
    }

    public ArrayList<AppInfo> getUnlockedAppList(){
        ArrayList<AppInfo> appList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from unlockApps", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            AppInfo app = new AppInfo();
            app.setAppId(res.getInt(res.getColumnIndex("id")));
            app.setAppname(res.getString(res.getColumnIndex("name")));
            app.setAppPackage(res.getString(res.getColumnIndex("package")));
            String str = res.getString(res.getColumnIndex("onHome"));
            if(str.equals("true")) {
                app.setOnHome(true);
            } else {
                app.setOnHome(false);
            }
            appList.add(app);
            res.moveToNext();
        }
        db.close();
        return appList;
    }

    public ArrayList<AppInfo> getMenuAppList(){
        ArrayList<AppInfo> appList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from unlockApps where onHome = 'true'", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            AppInfo app = new AppInfo();
            app.setAppId(res.getInt(res.getColumnIndex("id")));
            app.setAppname(res.getString(res.getColumnIndex("name")));
            app.setAppPackage(res.getString(res.getColumnIndex("package")));
            String str = res.getString(res.getColumnIndex("onHome"));
            if(str.equals("true")) {
                app.setOnHome(true);
            } else {
                app.setOnHome(false);
            }
            appList.add(app);
            res.moveToNext();
        }
        db.close();
        return appList;
    }

    public ArrayList<String> getUnlockedPackageList(){
        ArrayList<String> appList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from unlockApps", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            appList.add(res.getString(res.getColumnIndex("package")));
            res.moveToNext();
        }
        db.close();
        return appList;
    }

    public void lockApp(AppInfo app){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("unlockApps", "name = ? ", new String[] { app.getAppname() });
        db.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table unlockApps(id integer primary key, name text,package text, onHome text)" );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
