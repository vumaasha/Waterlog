package waterlogging.pilatus.in.datacollection;

import android.app.Fragment;
import android.location.Address;
import android.location.Geocoder;
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

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by venki on 12/13/15.
 */
public class WaterLoggingFragment extends Fragment {

    private static final String TAG = "WaterLoggingFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final LatLng location = getArguments().getParcelable("latLng");
        final Address address= getAddress(location);
        String displayName = null;
        if (address != null ){
            displayName = getDisplayName(address);
        } else {
            displayName = location.toString();
        }
        View view = inflater.inflate(R.layout.fragment_waterlogging_level, container, false);
        TextView streetNameView = (TextView) view.findViewById(R.id.street_name);
        streetNameView.setText(displayName);

        RadioGroup waterLevelGroup = (RadioGroup) view.findViewById(R.id.waterlogging_group);
        waterLevelGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                WaterLoggingLevel level = WaterLoggingLevel.NONE;
                float markerColor = BitmapDescriptorFactory.HUE_GREEN;
                String title = getString(R.string.logging_level_none);
                switch (checkedId) {
                    case (R.id.no_water_logging):
                        level = WaterLoggingLevel.NONE;
                        break;
                    case (R.id.feet_1_3):
                        level = WaterLoggingLevel.FEET_1_3;
                        markerColor = BitmapDescriptorFactory.HUE_YELLOW;
                        title = getString(R.string.logging_level_low);
                        break;
                    case (R.id.feet_3_6):
                        level = WaterLoggingLevel.FEET_3_6;
                        markerColor = BitmapDescriptorFactory.HUE_ORANGE;
                        title = getString(R.string.logging_level_medium);
                        break;
                    case (R.id.feet_above_6):
                        level = WaterLoggingLevel.ABOVE_6;
                        markerColor = BitmapDescriptorFactory.HUE_RED;
                        title = getString(R.string.logging_level_high);
                        break;
                }
                String android_id = Secure.getString(getActivity().getContentResolver(),
                        Secure.ANDROID_ID);
                if (location != null) {
                    saveLog(android_id, location.latitude, location.longitude, level,address);
                    MarkerOptions markerOptions = new MarkerOptions().position(location).title(title).icon(BitmapDescriptorFactory.defaultMarker(markerColor));
                    MapsActivity mapsActivity = (MapsActivity) getActivity();
                    mapsActivity.addMarker(markerOptions);
                }
                getFragmentManager().beginTransaction().remove(WaterLoggingFragment.this).commit();

                Toast.makeText(WaterLoggingFragment.this.getActivity(), "Thanks for your time.", Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }

    public enum WaterLoggingLevel {
        NONE(0), FEET_1_3(3), FEET_3_6(6), ABOVE_6(10);

        private final int level;

        WaterLoggingLevel(int level) {
            this.level = level;
        }

        public Integer getLevel() {
            return this.level;
        }
    }


    private void saveLog(String ANDROID_ID, Double latitude, Double longitude, WaterLoggingLevel level,Address address) {
        WaterLoggingInfo log;
        log = new WaterLoggingInfo(ANDROID_ID, latitude, longitude, level,address);
        new PostWaterLoggingInfo().execute(log);
    }

    private Address getAddress(LatLng latLng) {
        Geocoder geocoder = new Geocoder(this.getActivity(), Locale.getDefault());
        List<Address> fromLocation = null;
        try {
            fromLocation = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
        } catch (IOException e) {
            Toast.makeText(WaterLoggingFragment.this.getActivity(), "Unable to get address.", Toast.LENGTH_LONG).show();
        }
        Address address = null;
        if (fromLocation != null & fromLocation.size() > 0) {
            address = fromLocation.get(0);
        }
        return address;
    }

    @Nullable
    private String getDisplayName(Address address) {
        String area = null;
        String streetName = address.getThoroughfare();
        String subLocality = address.getSubLocality();
        if (streetName == null) {
            area = subLocality;
        } else {
            area = streetName + ", " + subLocality;
        }
        Log.i(TAG, "street name:=" + streetName + " Sublocality=" + subLocality);
        return area;
    }
}
