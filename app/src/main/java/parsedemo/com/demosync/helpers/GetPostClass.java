package parsedemo.com.demosync.helpers;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dhruvil on 04-05-2015.
 */

public abstract class GetPostClass implements Interaction {

    public abstract void response(String response);

    public abstract void error(String error);

    private String url;
    private EnumType type;


    public GetPostClass(String url, EnumType type) {
        this.url = url;
        this.type = type;
    }


    public synchronized final GetPostClass call() {

        switch (type) {
            case GET:
                new OperationGet().execute();
                break;


        }
        return this;

    }



    public static String httpGet(String url) {

        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httppost = new HttpGet(URI.create(url));
        String responseBody = null;
        JSONObject jObject = null;
        try {
            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            int responseCode = response.getStatusLine().getStatusCode();
            responseBody = EntityUtils.toString(response.getEntity());
            jObject = new JSONObject(responseBody);

        } catch (ClientProtocolException e) {

        } catch (IOException e) {

        } catch (JSONException e) {
            // Oops
        } catch (Exception e) {
        }
        return responseBody.toString();
    }



    public class OperationGet extends AsyncTask<Void, Void, Void> {

        String response = null;

        @Override
        protected Void doInBackground(Void... params) {

            try {
                response = httpGet(url);
            } catch (Exception e) {
                e.printStackTrace();
                error("Error");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                Log.e("Response:", response.toString());
                if (response == null) {
                    error("Server Error");
                } else {
                    response(response.toString());
                }
            }catch (Exception e){
                Log.e("Response:", "exc");
            }
        }
    }




}

