package parsedemo.com.demosync;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import parsedemo.com.demosync.helpers.EnumType;
import parsedemo.com.demosync.helpers.GetPostClass;


public class MainActivity extends AppCompatActivity {
    private List<Movie> movieList = new ArrayList<>();
    RecyclerView rv;
    MoviesAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        rv = (RecyclerView)findViewById(R.id.rv);
        prepareMovieData();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(mLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(mAdapter);

        final String PREFS_NAME = "MyPrefsFile";
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        if (settings.getBoolean("my_first_time", true)) {
            //the app is being launched for first time, do something
            Log.e("Comments", "First time");

            //startService(new Intent(MainActivity.this,MyService.class));

            AlarmManager am=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
            Intent i = new Intent(MainActivity.this, AlramReciver.class);
            PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this, 0, i, 0);
            am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 3 * 60 * 1000, pi); // Millisec * Second * Minute


            settings.edit().putBoolean("my_first_time", false).commit();
        }

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

                  //  ShowNotification("New Message Received");

                }catch (Exception e){
                    Log.e("## EXC in SERV",e.toString());
                }
            }

            @Override
            public void error(String error) {

            }
        }.call();
    }

    private void prepareMovieData() {

        try {
            DatabaseHandler handler = new DatabaseHandler(MainActivity.this);
            handler.openDataBase();
            movieList = handler.getMovie();

            mAdapter = new MoviesAdapter(movieList);
            mAdapter.notifyDataSetChanged();
            handler.close();
        }catch (Exception e){
            Log.e("## EXC fetch data",e.toString());
        }

}
}
