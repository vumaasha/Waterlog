package waterlogging.pilatus.in.datacollection;

import android.app.Fragment;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

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
import java.util.Locale;

/**
 * Created by venki on 12/13/15.
 */
public class WaterLoggingFragment extends Fragment{

    private static final String TAG = "WaterLoggingFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final LatLng location = getArguments().getParcelable("latLng");
        String area = getAreaName(location);
        View view = inflater.inflate(R.layout.fragment_waterlogging_level, container, false);
        TextView streetNameView = (TextView) view.findViewById(R.id.street_name);
        streetNameView.setText(area);

        RadioGroup waterLevelGroup = (RadioGroup) view.findViewById(R.id.waterlogging_group);
        waterLevelGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                WaterLoggingLevel level = WaterLoggingLevel.NONE;
                switch (checkedId){
                    case (R.id.no_water_logging):
                        level = WaterLoggingLevel.NONE;
                        break;
                    case (R.id.feet_1_3):
                        level = WaterLoggingLevel.FEET_1_3;
                        break;
                    case (R.id.feet_3_6):
                        level = WaterLoggingLevel.FEET_3_6;
                        break;
                    case (R.id.feet_above_6):
                        level = WaterLoggingLevel.ABOVE_6;
                        break;
                }
                String android_id = Secure.getString(getActivity().getContentResolver(),
                        Secure.ANDROID_ID);
                saveLog(android_id,location.latitude,location.longitude,level);
                getFragmentManager().beginTransaction().remove(WaterLoggingFragment.this).commit();

                Toast.makeText(WaterLoggingFragment.this.getActivity(), "Thanks for your time.", Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }

    public enum WaterLoggingLevel {
        NONE(0),FEET_1_3(3),FEET_3_6(6),ABOVE_6(10);

        private final int level;

        private WaterLoggingLevel(int level){
            this.level = level;
        }

        public Integer getLevel(){
            return this.level;
        }
    }


    private void saveLog(String ANDROID_ID,Double latitude,Double longitude,WaterLoggingLevel level){
       WaterLoggingInfo log = new WaterLoggingInfo(ANDROID_ID,latitude,longitude,level);
        new PostWaterLoggingInfo().execute(log);

    }

    @Nullable
    private String getAreaName(LatLng latLng) {
        Geocoder geocoder = new Geocoder(this.getActivity(), Locale.getDefault());
        String area = null;
        try {
            List<Address> fromLocation = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (fromLocation != null & fromLocation.size() > 0) {
                Address address = fromLocation.get(0);
                String streetName = address.getThoroughfare();
                String subLocality = address.getSubLocality();
                if (streetName == null) {
                    area = subLocality;
                } else {
                    area = streetName + ", " + subLocality;
                }
                Log.i(TAG, "street name:=" + streetName + " Sublocality=" + subLocality);
            }
        } catch (IOException e) {
            Toast.makeText(this.getActivity(), "Unable to get street name for" + latLng, Toast.LENGTH_LONG).show();
        }
        return area;
    }
}
