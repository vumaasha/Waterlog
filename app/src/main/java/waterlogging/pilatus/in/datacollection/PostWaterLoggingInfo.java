package waterlogging.pilatus.in.datacollection;

import android.location.Address;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by venki on 12/19/15.
 */
public class PostWaterLoggingInfo extends AsyncTask<WaterLoggingInfo,Integer,Integer> {

    @Override
    protected Integer doInBackground(WaterLoggingInfo... logs) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://waterlog-pvu.rhcloud.com/waterlog");
        for (WaterLoggingInfo log:logs) {
            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
            nameValuePair.add(new BasicNameValuePair("android_id",log.getAndroidID()));
            nameValuePair.add(new BasicNameValuePair("lat", log.getLatitude().toString() ));
            nameValuePair.add(new BasicNameValuePair("long", log.getLongitude().toString() ));
            nameValuePair.add(new BasicNameValuePair("level", log.getLevel().getLevel().toString()));
            Address address = log.getAddress();
            if (address != null){
                nameValuePair.add(new BasicNameValuePair("area", address.getLocality()));
                nameValuePair.add(new BasicNameValuePair("sublocality", address.getSubLocality()));
                nameValuePair.add(new BasicNameValuePair("throughfare", address.getThoroughfare()));
                nameValuePair.add(new BasicNameValuePair("postcode", address.getPostalCode()));
            }

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));

            } catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
            try {
                Log.d("Http Post ", httpPost.getURI().toASCIIString());
                HttpResponse response = httpClient.execute(httpPost);
                // write response to log
                Log.d("Http Post Response:", response.toString());
                Log.d("Http Post Response:", response.getStatusLine().toString());
            } catch (ClientProtocolException e) {
                // Log exception
                e.printStackTrace();
            } catch (IOException e) {
                // Log exception
                e.printStackTrace();
            }

        }
        return 0;
    }
}