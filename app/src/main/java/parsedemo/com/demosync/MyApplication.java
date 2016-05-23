package parsedemo.com.demosync;

import android.app.Application;
import android.text.TextUtils;

import com.evernote.android.job.JobManager;

/**
 * Application class that called once when application is installed for the first time on device.
 * This class includes the integration of Volly [third party framework for calling webservices]
 */
public class MyApplication extends Application {

    /**
     * Log or request TAG
     */
    public static final String TAG = "VolleyPatterns";

    /**
     * Global request queue for Volley
     */

    /**
     * A singleton instance of the application class for easy access in other places
     */
    private static MyApplication sInstance;
    /**
     * A class that helps to store database file from assets to
     */
    static int halfscreenSize;

    @Override
    public void onCreate() {
        super.onCreate();
        // initialize the singleton
        sInstance = this;
        DatabaseHandler handler = new DatabaseHandler(getApplicationContext());
        JobManager.create(this).addJobCreator(new DemoJobCreator());
        try {
            handler.createDataBase();
        } catch (Exception e) {

        }
    }

    /**
     * @return ApplicationController singleton instance
     */
    public static synchronized MyApplication getInstance() {
        return sInstance;
    }



}
