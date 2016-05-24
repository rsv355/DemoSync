package parsedemo.com.demosync;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by krishnakumar on 24-05-2016.
 */
public class ConnectivityChangeReciever extends WakefulBroadcastReceiver {


    Context _ctx;
    @Override
    public void onReceive(Context context, Intent intent) {

        _ctx = context;

        Intent servicePointer =  new Intent(context,MyService.class);
        if(isMyServiceRunning(MyService.class)) {
            context.stopService(servicePointer);
            context.startService(servicePointer);
        }else {
            context.startService(servicePointer);
        }

    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) _ctx.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
