package waterlogging.pilatus.in.datacollection;

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
public class WaterLoggingInfo {

    private String androidID;
    private Double latitude;
    private Double longitude;
    private WaterLoggingFragment.WaterLoggingLevel level;

    public WaterLoggingInfo(String androidID, Double latitude, Double longitude, WaterLoggingFragment.WaterLoggingLevel level) {
        this.androidID = androidID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.level = level;
    }

    public String getAndroidID() {
        return androidID;
    }

    public Double getLatitude() {
        return latitude;
    }

    public WaterLoggingFragment.WaterLoggingLevel getLevel() {
        return level;
    }

    public Double getLongitude() {
        return longitude;
    }

}
