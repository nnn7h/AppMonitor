package com.android.appmonitor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import util.AppInfo;
import util.PackageInfoAdapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends Activity implements OnItemClickListener {

    private SharedPreferences pkgsPref;
    private List<String> selectedApp;
    private List<AppInfo> appInfoList;
    private boolean[] isSeleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button save = (Button) findViewById(R.id.saveButton);
        Button exit = (Button) findViewById(R.id.exitButton);
        ListView packageList = (ListView) findViewById(R.id.packageList);
        appInfoList = new ArrayList<AppInfo>();
        selectedApp = new ArrayList<String>();
        pkgsPref = this.getSharedPreferences("pkgs", Context.MODE_WORLD_READABLE);

        getPkgList();
        loadInit();

        PackageInfoAdapter packgaeAdapter = new PackageInfoAdapter(this, appInfoList, isSeleted);
        packageList.setAdapter(packgaeAdapter);
        packageList.setOnItemClickListener(this);
        packageList.setAlwaysDrawnWithCacheEnabled(true);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                whichSelect(appInfoList, isSeleted);
                saveData(selectedApp);
                Toast.makeText(MainActivity.this.getApplicationContext(), "monitor begin", Toast.LENGTH_SHORT).show();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void saveData(List<String> selectedApp)
    {
        try
        {
            SharedPreferences.Editor edit = pkgsPref.edit();
            edit.clear();
            edit.putStringSet("pkgs", new HashSet<String>(selectedApp));
            edit.apply();
        } catch (Throwable e)
        {
            System.out.println(e.getMessage());
        }
    }

    //inti array isSelected and checkbox
    public void loadInit() {
        isSeleted = new boolean[appInfoList.size()];
        Set<String> pkgs = pkgsPref.getStringSet("pkgs", null);

        if (pkgs != null){
            for (AppInfo appinfo : appInfoList) {
                if (pkgs.contains(appinfo.getPkgName())) {
                    int i = appInfoList.indexOf(appinfo);
                    isSeleted[i] = true;
                }
            }
        }
    }

    //get all app packagename
    public void getPkgList() {
        PackageManager packManager = this.getPackageManager();
        List<PackageInfo> packageInfoList = packManager.getInstalledPackages(0);

        for (int i = 0; i < packageInfoList.size(); i++) {
            AppInfo appInfo = new AppInfo();
            PackageInfo packageInfo = packageInfoList.get(i);
            appInfo.setAppIcon(packManager
                    .getApplicationIcon(packageInfo.applicationInfo));
            appInfo.setAppLabel(packManager.getApplicationLabel(
                    packageInfo.applicationInfo).toString());
            appInfo.setPkgName(packageInfo.applicationInfo.packageName);

            appInfoList.add(appInfo);
        }
    }

    //mark which app is selected
    public void whichSelect(List<AppInfo> appInfo, boolean[] selected) {
        for (int i = 0; i < selected.length; i++) {
            if (selected[i]) {
                selectedApp.add(appInfo.get(i).getPkgName());

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        String pkgName = appInfoList.get(arg2).getPkgName();
        System.out.println(pkgName);
        RelativeLayout lr = (RelativeLayout) arg1;
        CheckBox tmp = (CheckBox) lr.getChildAt(2);
        tmp.toggle();
        isSeleted[arg2] = tmp.isChecked();
    }

}
