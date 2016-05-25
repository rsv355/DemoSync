package parsedemo.com.demosync;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.WindowManager;

import com.evernote.android.job.JobRequest;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import github.nisrulz.easydeviceinfo.EasyDeviceInfo;
import parsedemo.com.demosync.helpers.EnumType;
import parsedemo.com.demosync.helpers.GetPostClass;


public class MainActivity extends AppCompatActivity {
    private List<Movie> movieList = new ArrayList<>();
    RecyclerView rv;
    MoviesAdapter mAdapter;
    GcmNetworkManager mGcmNetworkManager;
    PeriodicTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mGcmNetworkManager = GcmNetworkManager.getInstance(this);

        rv = (RecyclerView) findViewById(R.id.rv);
        prepareMovieData();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(mLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(mAdapter);

        EasyDeviceInfo easyDeviceInfo = new EasyDeviceInfo(MainActivity.this);

        Intent servicePointer =  new Intent(MainActivity.this,MyService.class);

        if(isMyServiceRunning(MyService.class)) {
            stopService(servicePointer);
            startService(servicePointer);
          //  syncData();
        }else {
            startService(servicePointer);
            //syncData();
        }


        /*
        if (easyDeviceInfo.getManufacturer().contains("Xiaomi") || easyDeviceInfo.getDevice().contains("Xiaomi") ||
                easyDeviceInfo.getModel().contains("Xiaomi")) {
            Log.e("Xiaomi device", "mi device");
           /* mGcmNetworkManager.getInstance(MainActivity.this).cancelAllTasks(MyTaskService.class);
            task = new PeriodicTask.Builder()
                    .setService(MyTaskService.class)
                    .setTag("NM CALLES")
                    .setPeriod(30L)
                    .setPersisted(true)
                    .build();

            mGcmNetworkManager.schedule(task);*/

         /*   AlarmManager am=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
            Intent i = new Intent(MainActivity.this, AlramReciver.class);
            PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
            am.setInexactRepeating (AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 120000, pi); // Millisec * Second * Minute
*/
        //    startService(new Intent(MainActivity.this,MyService.class));

    //    }else{
    //        final String PREFS_NAME = "MyPrefsFile";
    //        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
//
      //      if (settings.getBoolean("my_first_time", true)) {
                //the app is being launched for first time, do something
       //         Log.e("Comments", "First time");

               /* task = new PeriodicTask.Builder()
                        .setService(MyTaskService.class)
                        .setTag("NM CALLES")
                        .setPeriod(30L)
                        .setPersisted(true)
                        .build();

                mGcmNetworkManager.schedule(task);*/

          /*  AlarmManager am=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
            Intent i = new Intent(MainActivity.this, AlramReciver.class);
            PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
            am.setInexactRepeating (AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 120000, pi); // Millisec * Second * Minute

               */

         //       settings.edit().putBoolean("my_first_time", false).commit();

            //}
        //}

    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void syncData() {

        new GetPostClass("http://www.edubuzz.info/EduBuzzApp/rest/register/testService", EnumType.GET) {

            @Override
            public void response(String response) {
                try {
                    Data currentData = new GsonBuilder().create().fromJson(response, Data.class);

                    DatabaseHandler handler = new DatabaseHandler(getApplicationContext());

                    Log.e("## Total rcvd in serv", "" + currentData.getData().size());

                    ArrayList<Movie> mainData = new ArrayList<Movie>();

                    for (int i = 0; i < currentData.getData().size(); i++) {
                        Movie mve = new Movie();
                        mve.setTitle(currentData.getData().get(i).getItem1());
                        mve.setGenre(currentData.getData().get(i).getItem2());
                        mve.setYear(currentData.getData().get(i).getItem3());

                        mainData.add(mve);
                    }

                    handler.openDataBase();
                    handler.saveMovie(mainData);

                    handler.close();

                      ShowNotification("New Message Received");

                } catch (Exception e) {
                    Log.e("## EXC in SERV", e.toString());
                }
            }

            @Override
            public void error(String error) {

            }
        }.call();
    }
    private void ShowNotification(String msg) {


        NotificationManager alarmNotificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

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

        NotificationCompat.Builder alamNotificationBuilder  = new NotificationCompat.Builder(
                getApplicationContext()).setContentTitle("Demo Sync").setSmallIcon(R.mipmap.ic_launcher)
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
    private void prepareMovieData() {

        try {
            DatabaseHandler handler = new DatabaseHandler(MainActivity.this);
            handler.openDataBase();
            movieList = handler.getMovie();

            mAdapter = new MoviesAdapter(movieList);
            mAdapter.notifyDataSetChanged();
            handler.close();
        } catch (Exception e) {
            Log.e("## EXC fetch data", e.toString());
        }

    }
}
