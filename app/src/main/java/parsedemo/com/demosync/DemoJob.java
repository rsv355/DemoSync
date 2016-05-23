package parsedemo.com.demosync;

import android.support.annotation.NonNull;
import android.util.Log;

import com.evernote.android.job.Job;

/**
 * Created by krishnakumar on 23-05-2016.
 */
public class DemoJob extends Job {

    public static final String TAG = "job_demo_tag";

    @Override
    @NonNull
    protected Result onRunJob(Params params) {
        // run your job
        Log.e("################","SERVICE CALLED ###########");
        return Result.SUCCESS;
    }
}
