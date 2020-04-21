/**
 * Thomas Scott - S1824798
 * Description - This is the map fragment which shows the map.
 * Could not get the info passed from the JourneyPlannerFragment using Bundle so I skipped that part
 * IT DOES show the map and places the icons for all roadworks on it. When clicked these icons show the title.
 * The LatLong had to be extracted from each Roadwork in the linked list and then added to the map.
 */
package com.example.tscott202mpd_cw;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JourneyPlannerMapFragment extends Fragment implements OnMapReadyCallback {

    // Getting the linked list which is in the MainActivity.
    LinkedList<Roadwork> roadworkLinkedList = MainActivity.roadworkLinkedList;
    //    Bundle bundle = getArguments();
//    String start = bundle.getString("startPostcode");
//    String end = bundle.getString("endPostcode");
    // Creating instance of google maps.
    private GoogleMap map;

    public JourneyPlannerMapFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_journey_planner_map, null, false);
        // Gets the map fragment from the layout file to get a reference to it.
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        // Gets the map
        mapFragment.getMapAsync(this);

        return view;
    }


    // Once the map is ready the data is loaded. The roadwork locations are retrieved and then placed onto the map.
    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;

        for (Roadwork r : roadworkLinkedList) {

            String title = r.getTitle();
            String coordinates = r.getCoordinates();
            String lat = "";
            String lon = "";

            // Looks for a pattern in this case a space to separate the data and get the lat long and place in relevant variables.
            Pattern pattern = Pattern.compile(" ");
            Matcher matcher = pattern.matcher(coordinates);
            if (matcher.find()) {
                lat = coordinates.substring(0, matcher.start());
                lon = coordinates.substring(matcher.end());
            }

            // Parsing the strings to doubles.
            Double latitude = Double.parseDouble(lat);
            Double longitude = Double.parseDouble(lon);

            // Creating a new latlng type.
            LatLng points = new LatLng(latitude, longitude);

            // Adding the marker.
            map.addMarker(new MarkerOptions().position(points).title(title));

        }
    }
}
