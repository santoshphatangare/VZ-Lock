package com.gls.vz_lock.adapters;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gls.vz_lock.R;
import com.gls.vz_lock.beans.AppInfo;
import com.gls.vz_lock.utils.AppData;

import java.util.ArrayList;

/**
 * Created by Santosh on 15-Dec-17.
 */

public class HideMenuAdapter extends ArrayAdapter {
    ArrayList<AppInfo> menuApps;
    AppCompatActivity context;
    int resource;
    LayoutInflater inflater;
    PackageManager pm;
    public HideMenuAdapter(AppCompatActivity context, int resource, ArrayList<AppInfo> menuApps) {
        super(context, resource, menuApps);
        this.context = context;
        this.resource = resource;
        this.menuApps = menuApps;
        inflater = context.getLayoutInflater();
        pm = context.getPackageManager();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout row = (LinearLayout) inflater.inflate(resource, null);
        ImageView icon = (ImageView) row.findViewById(R.id.appIcon);
        TextView label = (TextView) row.findViewById(R.id.appName);
        CheckBox locked = (CheckBox) row.findViewById(R.id.appLocked);
        icon.setImageDrawable(menuApps.get(position).getIcon());
        label.setText(menuApps.get(position).getAppname());
        locked.setChecked(menuApps.get(position).isOnHome());
        locked.setTag(menuApps.get(position).getAppId());
        locked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                int appId = (int) cb.getTag();
                if(cb.isChecked()){
                    new AppData(context).showApp(appId);
                } else {
                    new AppData(context).hideApp(appId);
                }
            }
        });
        return row;
    }
}
