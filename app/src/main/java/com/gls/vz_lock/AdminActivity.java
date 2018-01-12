package com.gls.vz_lock;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.gls.vz_lock.adapters.LockAppAdapter;
import com.gls.vz_lock.beans.AppInfo;
import com.gls.vz_lock.utils.AppData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class AdminActivity extends AppCompatActivity {
    LinearLayout passwordLL;
    RelativeLayout adminHomeLL;
    EditText password;
    ListView appList;
    ArrayList<AppInfo> menuApps = new ArrayList<>();
    LockAppAdapter appAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        passwordLL = (LinearLayout) findViewById(R.id.passwordLL);
        adminHomeLL = (RelativeLayout) findViewById(R.id.adminHomeLL);
        password = (EditText) findViewById(R.id.password);
        appList = (ListView) findViewById(R.id.appList);
        appAdapter = new LockAppAdapter(this, R.layout.app_row, menuApps);
        appList.setAdapter(appAdapter);
    }

    public void enterAsAdmin(View v){
        if(password.getText().toString().trim().equals("98530")){
            passwordLL.setVisibility(View.GONE);
            adminHomeLL.setVisibility(View.VISIBLE);
            password.setText("");
            loadAppList();
        } else {
            password.setText("");
            Toast.makeText(getApplicationContext(),"WRONG PASSWORD", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        passwordLL.setVisibility(View.VISIBLE);
        adminHomeLL.setVisibility(View.GONE);
        menuApps.clear();
    }

    public void loadAppList(){

        final PackageManager packageManager = getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resInfos = packageManager.queryIntentActivities(intent, 0);

        HashSet<String> packageNames = new HashSet<String>(0);
        List<ApplicationInfo> appInfos = new ArrayList<ApplicationInfo>(0);

        for(ResolveInfo resolveInfo : resInfos) {
            packageNames.add(resolveInfo.activityInfo.packageName);
        }
        for(String packageName : packageNames) {
            Log.e("ALL APPS", packageName);
            try {
                appInfos.add(packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA));
            } catch (PackageManager.NameNotFoundException e) { }
        }

        Collections.sort(appInfos, new ApplicationInfo.DisplayNameComparator(packageManager));
        for(int i = 0; i<appInfos.size();i++){
            AppInfo app = new AppInfo();
            app.setIcon(packageManager.getApplicationIcon(appInfos.get(i)));
            app.setAppname(packageManager.getApplicationLabel(appInfos.get(i)).toString());
            app.setAppPackage(appInfos.get(i).packageName);
            menuApps.add(app);
        }

        checkLockedApps();
    }

    public void checkLockedApps(){
        ArrayList<AppInfo> unlockedApps;
        try {
            unlockedApps = new AppData(this).getUnlockedAppList();
        } catch (Exception e){
            unlockedApps = new ArrayList<>();
            Log.e("ERROR", e+"");
        }
        for(int i = 0; i < unlockedApps.size(); i++){
            for(int j = 0; j < menuApps.size();j++){
                if(menuApps.get(j).getAppname().equals(unlockedApps.get(i).getAppname())){
                    menuApps.get(j).setLocked(false);
                }
            }
        }
        appAdapter.notifyDataSetChanged();
    }

    public void manageHomeScreen(View v){
        startActivity(new Intent(getApplicationContext(), ManageHomeActivity.class));
    }
}
