package parsedemo.com.demosync;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;


import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by krishnakumar on 19-08-2015.
 */
public class MyService extends Service {

    public static final long NOTIFY_INTERVAL = 3 * 60 * 1000; // 5 minutes
    //public static final long NOTIFY_INTERVAL = 1 * 60 * 1000; //1 minutes
    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();
    // timer handling
    private Timer mTimer = null;
    private NotificationManager alarmNotificationManager;
    NotificationCompat.Builder alamNotificationBuilder;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

   /* @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
    }
*/



    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }



    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }


    @Override
    public void onCreate() {


        super.onCreate();
        // cancel if already existed
        if (mTimer != null) {
            mTimer.cancel();
        } else {
            // recreate new
            mTimer = new Timer();
        }
        // schedule task
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);
    }

    class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {

                @Override
                public void run() {

                    Log.e("@@@@ SErvice ", "called ");

                    Toast.makeText(getApplicationContext(), "Service called", Toast.LENGTH_SHORT).show();


                    if (isInternetConnected(getApplicationContext())) {

                        //syncTips();

                    } else {
                        Log.e("## else", "my serice else");
                        //Toast.makeText(getApplicationContext(), "Please connect your Internet", Toast.LENGTH_LONG).show();
                    }


                }

            });
        }

    }


    private void ShowNotification(String type, String msg) {


        alarmNotificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        int notificaionType = 0;

        PendingIntent contentIntent;

            Intent newIntent = new Intent(getApplicationContext(), MainActivity.class);
            newIntent.setFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
            newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK & Intent.FLAG_ACTIVITY_CLEAR_TOP & Intent.FLAG_ACTIVITY_NEW_TASK);
            contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, newIntent, 0);
            notificaionType = 0;


        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.InboxStyle inboxStyle =
                new NotificationCompat.InboxStyle();
        // String[] events = new String[6];
        // inboxStyle.setBigContentTitle("Event tracker details:");
        //   for (int i=0; i < events.length; i++) {
        inboxStyle.addLine(msg);
        //  }

        alamNotificationBuilder = new NotificationCompat.Builder(
                getApplicationContext()).setContentTitle(type).setSmallIcon(R.mipmap.ic_launcher)
                .setStyle(inboxStyle)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound)
                .setContentText(msg);

        alamNotificationBuilder.setAutoCancel(true);
        alamNotificationBuilder.getNotification().flags |= Notification.FLAG_AUTO_CANCEL;
        alamNotificationBuilder.getNotification().defaults |= Notification.DEFAULT_LIGHTS;
        alamNotificationBuilder.setContentIntent(contentIntent);

        //Random random = new Random();
        //int randomNo = random.nextInt(9999 - 1000) + 1000;

        alarmNotificationManager.notify(notificaionType, alamNotificationBuilder.build());
    }

    public static boolean isInternetConnected(Context _context){
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
    }

    //end of main class
}
