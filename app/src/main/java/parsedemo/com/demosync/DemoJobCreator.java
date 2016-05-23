package parsedemo.com.demosync;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

/**
 * Created by krishnakumar on 23-05-2016.
 */
public class DemoJobCreator implements JobCreator {

    @Override
    public Job create(String tag) {
        switch (tag) {
                case DemoJob.TAG:
                return new DemoJob();
            default:
                return null;
        }
    }
}
