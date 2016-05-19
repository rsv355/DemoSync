package parsedemo.com.demosync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by krishnakumar on 19-05-2016.
 */
public class AlramReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context,MyService.class));
    }
}
