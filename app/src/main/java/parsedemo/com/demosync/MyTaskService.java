package parsedemo.com.demosync;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import parsedemo.com.demosync.helpers.EnumType;
import parsedemo.com.demosync.helpers.GetPostClass;

/**
 * Created by krishnakumar on 23-05-2016.
 */
public class MyTaskService extends GcmTaskService{
    private NotificationManager alarmNotificationManager;
    NotificationCompat.Builder alamNotificationBuilder;

    @Override
    public int onRunTask(TaskParams taskParams) {

        Log.e("################","SERVICE CALLED ###########");
        if (isInternetConnected(getApplicationContext())) {

            syncData();

        } else {

            Log.e("## else", "my serice else");
            //Toast.makeText(getApplicationContext(), "Please connect your Internet", Toast.LENGTH_LONG).show();
        }

        return GcmNetworkManager.RESULT_SUCCESS;
    }
    private void syncData(){

        new GetPostClass("http://www.edubuzz.info/EduBuzzApp/rest/register/testService", EnumType.GET){

            @Override
            public void response(String response) {
                try{
                    Data currentData = new GsonBuilder().create().fromJson(response,Data.class);

                    DatabaseHandler handler = new DatabaseHandler(getApplicationContext());

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


            }

            @Override
            public void error(String error) {

            }
        }.call();
    }


    private void ShowNotification(String msg) {


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
