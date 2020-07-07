package com.example.antitheft;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;


import com.example.antitheft.Adapter.AppAdapter;
import com.example.antitheft.Model.AppInfo;
import com.google.android.material.snackbar.Snackbar;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PhoneApplications extends AppCompatActivity {

        ListView listView;
        SwipeRefreshLayout swipeRefreshLayout;
        boolean mIncludeSystemApps;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xx);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView  = (ListView)findViewById(R.id.listView);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefresh);
        listView.setTextFilterEnabled(true);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
            refreshIt();

            }
        });
    }

    private void refreshIt() {
        LoadAppInfoTask loadAppInfoTask = new LoadAppInfoTask();
        loadAppInfoTask.execute(PackageManager.GET_META_DATA);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadAppInfoTask loadAppInfoTask = new LoadAppInfoTask();
        loadAppInfoTask.execute(PackageManager.GET_META_DATA);
    }

    class LoadAppInfoTask extends AsyncTask<Integer,Integer,List<AppInfo>>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected List<AppInfo> doInBackground(Integer... integers) {
            List<AppInfo>  apps = new ArrayList<>();
            PackageManager packageManager = getPackageManager();

            List<ApplicationInfo> infos = packageManager.getInstalledApplications(0);

            for(ApplicationInfo info:infos){
//                System.out.println(info.className);
                if(!mIncludeSystemApps && (info.flags & ApplicationInfo.FLAG_SYSTEM)==1){
                    continue;
                }
                AppInfo app = new AppInfo();
                app.info = info;
                app.label = (String)info.loadLabel(packageManager);
                apps.add(app);
            }
            //Sort the data before returning them

            Collections.sort(apps, new DNComparator());


            return apps;
        }



        @Override
        protected void onPostExecute(List<AppInfo> appInfos) {
            super.onPostExecute(appInfos);
            listView.setAdapter(new AppAdapter(PhoneApplications.this,appInfos));
            swipeRefreshLayout.setRefreshing(false);
            Snackbar.make(listView, appInfos.size() + "Applications Loaded", Snackbar.LENGTH_LONG).show();

        }
    }

    private class DNComparator implements Comparator<AppInfo> {

        @Override
        public int compare(AppInfo aa, AppInfo ab) {
            CharSequence  sa = aa.label;
            CharSequence  sb = ab.label;

            if(sa == null){

                sa = aa.info.packageName;
            }
            if(ab == null){
                 sb = ab.info.packageName;
            }

            return Collator.getInstance().compare(sa.toString(), sb.toString());
        }
    }
}