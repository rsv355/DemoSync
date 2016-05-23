package parsedemo.com.demosync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;

/**
 * Created by krishnakumar on 23-05-2016.
 */
public class MyTaskService extends GcmTaskService{

    @Override
    public int onRunTask(TaskParams taskParams) {

        Log.e("################","SERVICE CALLED ###########");
        return GcmNetworkManager.RESULT_SUCCESS;
    }


}
