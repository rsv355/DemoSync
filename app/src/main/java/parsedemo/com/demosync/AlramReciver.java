package parsedemo.com.demosync;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import parsedemo.com.demosync.helpers.EnumType;
import parsedemo.com.demosync.helpers.GetPostClass;

/**
 * Created by krishnakumar on 19-05-2016.
 */
public class AlramReciver extends WakefulBroadcastReceiver {
    private NotificationManager alarmNotificationManager;
    NotificationCompat.Builder alamNotificationBuilder;
    Context _ctx;
    Intent inten;
    @Override
    public void onReceive(Context context, Intent intent) {
        inten = intent;
        _ctx =context;
        Log.e("@@@@ SErvice ", "called ");

        Toast.makeText(context, "Service called", Toast.LENGTH_SHORT).show();


        if (isInternetConnected(context)) {

            syncData();

        } else {
            completeWakefulIntent(inten);
            Log.e("## else", "my serice else");
            //Toast.makeText(getApplicationContext(), "Please connect your Internet", Toast.LENGTH_LONG).show();
        }

    }
    private void syncData(){

        new GetPostClass("http://www.edubuzz.info/EduBuzzApp/rest/register/testService", EnumType.GET){

            @Override
            public void response(String response) {
                try{
                    Data currentData = new GsonBuilder().create().fromJson(response,Data.class);

                    DatabaseHandler handler = new DatabaseHandler(_ctx);

                    Log.e("## Total rcvd in serv",""+currentData.getData().size());

                    ArrayList<Movie> mainData = new ArrayList<Movie>();

                    for(int i=0;i<currentData.getData().size();i++){
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

                }catch (Exception e){
                    Log.e("## EXC in SERV",e.toString());
                }

                completeWakefulIntent(inten);
            }

            @Override
            public void error(String error) {

            }
        }.call();
    }


    private void ShowNotification(String msg) {


        alarmNotificationManager = (NotificationManager) _ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        int notificaionType = 0;

        PendingIntent contentIntent;

        Intent newIntent = new Intent(_ctx, MainActivity.class);
        newIntent.setFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK & Intent.FLAG_ACTIVITY_CLEAR_TOP & Intent.FLAG_ACTIVITY_NEW_TASK);
        contentIntent = PendingIntent.getActivity(_ctx, 0, newIntent, 0);
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
                _ctx).setContentTitle("Demo Sync").setSmallIcon(R.mipmap.ic_launcher)
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
}
