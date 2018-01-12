package com.gls.vz_lock.beans;

import java.util.ArrayList;

/**
 * Created by Santosh on 12-Dec-17.
 */

public class UnlockedApps {
    ArrayList<AppInfo> unlockedAppList = new ArrayList<>();

    public ArrayList<AppInfo> getUnlockedAppList() {
        return unlockedAppList;
    }

    public void setUnlockedAppList(ArrayList<AppInfo> unlockedAppList) {
        this.unlockedAppList = unlockedAppList;
    }
}
