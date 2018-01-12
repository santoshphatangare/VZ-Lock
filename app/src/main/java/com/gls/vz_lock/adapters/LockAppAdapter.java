package com.gls.vz_lock.adapters;

import android.content.Context;
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
 * Created by Santosh on 12-Dec-17.
 */

public class LockAppAdapter extends ArrayAdapter{
    ArrayList<AppInfo> menuApps;
    AppCompatActivity context;
    int resource;
    LayoutInflater inflater;
    public LockAppAdapter(AppCompatActivity context, int resource, ArrayList<AppInfo> menuApps) {
        super(context, resource, menuApps);
        this.context = context;
        this.resource = resource;
        this.menuApps = menuApps;
        inflater = context.getLayoutInflater();
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
        locked.setChecked(menuApps.get(position).isLocked());
        locked.setTag(menuApps.get(position));
        locked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                AppInfo app = (AppInfo) cb.getTag();
                if(cb.isChecked()){
                    new AppData(context).lockApp(app);
                } else {
                    new AppData(context).unlockApp(app);
                }
            }
        });
        return row;
    }
}
