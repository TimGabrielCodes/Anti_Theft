package Services;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.app.ActivityManager.*;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class StickyService  extends Service {
    //StalkList = A collection of Variables that the Sticky
    private Collection stalkList;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Toast.makeText(this, "Anti Theft is protecting your phone", Toast.LENGTH_LONG).show();

        Timer timer  =  new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run()
            {
                final ActivityManager activityManager  =  (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
                final List<RunningTaskInfo> services  =  activityManager.getRunningTasks(Integer.MAX_VALUE);
                for (int i = 0; i < services.size(); i++) {
                    if(!stalkList.contains(services.get(i).baseActivity.getPackageName()))
                    {
                        // you may broadcast a new application launch here.
                        stalkList.add(services.get(i).baseActivity.getPackageName());
                    }
                }

                List<RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
                for(int i = 0; i < procInfos.size(); i++) {

                    ArrayList<String> runningPkgs = new ArrayList<String>(Arrays.asList(procInfos.get(i).pkgList));

                    Collection diff = subtractSets(runningPkgs, stalkList);

                    if(diff != null)
                    {
                        stalkList.removeAll(diff);
                    }
                }


            }
        }, 20000, 6000);  // every 6 seconds


        return START_STICKY;
    }

    private RunningAppProcessInfo getForegroundApp() {
        RunningAppProcessInfo result = null, info = null;

        final ActivityManager activityManager  =  (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);

        List<RunningAppProcessInfo> l = activityManager.getRunningAppProcesses();
        Iterator<RunningAppProcessInfo> i = l.iterator();
        while(i.hasNext()) {
            info = i.next();
            if(info.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                    && !isRunningService(info.processName)) {
                result = info;
                break;
            }
        }
        return result;
    }

    private boolean isRunningService(String processName) {
        if(processName == null)
            return false;

        RunningServiceInfo service;

        final ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);

        List <ActivityManager.RunningServiceInfo> l = activityManager.getRunningServices(9999);
        Iterator <ActivityManager.RunningServiceInfo> i = l.iterator();
        while(i.hasNext()){
            service = i.next();
            if(service.process.equals(processName))
                return true;
        }
        return false;
    }

    private boolean isRunningApp(String processName) {
        if(processName == null)
            return false;

        RunningAppProcessInfo app;

        final ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);

        List <RunningAppProcessInfo> l = activityManager.getRunningAppProcesses();
        Iterator <RunningAppProcessInfo> i = l.iterator();
        while(i.hasNext()){
            app = i.next();
            if(app.processName.equals(processName) && app.importance != RunningAppProcessInfo.IMPORTANCE_SERVICE)
                return true;
        }
        return false;
    }


    private boolean checkifThisIsActive(RunningAppProcessInfo target){
        boolean result = false;
        ActivityManager.RunningTaskInfo info;

        if(target == null)
            return false;

        final ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);

        List <ActivityManager.RunningTaskInfo> l = activityManager.getRunningTasks(9999);
        Iterator <ActivityManager.RunningTaskInfo> i = l.iterator();

        while(i.hasNext()){
            info=i.next();
            if(info.baseActivity.getPackageName().equals(target.processName)) {
                result = true;
                break;
            }
        }

        return result;
    }


    // what is in b that is not in a ?

    public static Collection subtractSets(Collection a, Collection b)
    {
        Collection result = new ArrayList(b);
        result.removeAll(a);
        return result;
    }
}
