package com.gls.vz_lock;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import com.gls.vz_lock.adapters.MenuAdapter;
import com.gls.vz_lock.beans.AppInfo;
import com.gls.vz_lock.utils.AppData;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    int adminCnt = 0;
    ArrayList<AppInfo> unlockedApps;
    GridView menuGrid;
    MenuAdapter menuAdapter;
    public static Context context;
    public static ArrayList<String> unlockPackages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        context = getApplicationContext();
        menuGrid = (GridView) findViewById(R.id.menuGrid);
        unlockedApps = new ArrayList<>();
        unlockPackages = new AppData(getApplicationContext()).getUnlockedPackageList();
        unlockPackages.add("com.gls.vz_lock");
        startService(new Intent(getApplicationContext(), LockService.class));
        setMenu();
    }

    public void setMenu(){
        menuAdapter = new MenuAdapter(this, R.layout.menu_item, unlockedApps);
        menuGrid.setAdapter(menuAdapter);
    }

    public void openAdmin(View v){
        adminCnt++;
        if(adminCnt > 4){
            Intent adminIntent = new Intent(getApplicationContext(), AdminActivity.class);
            startActivity(adminIntent);
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        unlockedApps.clear();
        adminCnt = 0;
        try {
            unlockedApps.addAll(new AppData(this).getMenuAppList());
            menuAdapter.notifyDataSetChanged();
        } catch (Exception e){
            unlockedApps = new ArrayList<>();
            Log.e("ERROR", e+"");
        }
        unlockPackages = new AppData(getApplicationContext()).getUnlockedPackageList();
        unlockPackages.add("com.gls.vz_lock");
    }
}
