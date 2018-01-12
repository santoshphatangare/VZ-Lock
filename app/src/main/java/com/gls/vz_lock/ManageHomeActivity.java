package com.gls.vz_lock;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.gls.vz_lock.adapters.HideMenuAdapter;
import com.gls.vz_lock.beans.AppInfo;
import com.gls.vz_lock.utils.AppData;

import java.util.ArrayList;

public class ManageHomeActivity extends AppCompatActivity {

    ListView homeAppList;
    HideMenuAdapter adapter;
    ArrayList<AppInfo> homeApps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        homeAppList = (ListView) findViewById(R.id.homeAppList);
        homeApps = new AppData(this).getUnlockedAppList();
        adapter = new HideMenuAdapter(this,R.layout.app_row,homeApps);
        homeAppList.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
