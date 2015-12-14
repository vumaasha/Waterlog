package waterlogging.pilatus.in.datacollection;

import android.app.Fragment;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
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
        LatLng location = getArguments().getParcelable("latLng");
        String area = getAreaName(location);
        View view = inflater.inflate(R.layout.fragment_waterlogging_level, container, false);
        TextView streetNameView = (TextView) view.findViewById(R.id.street_name);
        streetNameView.setText(area);

        RadioGroup waterLevelGroup = (RadioGroup) view.findViewById(R.id.waterlogging_group);
        waterLevelGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Toast.makeText(WaterLoggingFragment.this.getActivity(),"Thanks for your time.",Toast.LENGTH_LONG).show();
                getFragmentManager().beginTransaction().remove(WaterLoggingFragment.this).commit();
            }
        });
        return view;
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
