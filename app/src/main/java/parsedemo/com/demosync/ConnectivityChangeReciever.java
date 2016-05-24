package parsedemo.com.demosync;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by krishnakumar on 24-05-2016.
 */
public class ConnectivityChangeReciever extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent servicePointer =  new Intent(context,MyService.class);

        context.stopService(servicePointer);

        context.startService(servicePointer);
    }
}
