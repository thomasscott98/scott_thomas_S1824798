/**
 * Thomas Scott - S1824798
 * Description - This is used to show the journey planner page and unfortunately cannot plan a journey.
 * I was able to get the map fragment to open however when clicking on the button
 * The relevant details are stored from the UI like postcode which was going to be reversed into lat long later on.
 * However could not pass the details to the MapFragment using bundle.
 */
package com.example.tscott202mpd_cw;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.LinkedList;

public class JourneyPlannerFragment extends Fragment {

    Button getJourneyBtn;
    EditText startPostcode;
    EditText endPostcode;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_journey_planner, container, false);

        getJourneyBtn = view.findViewById(R.id.getJourneyPlanBtn);
        startPostcode = view.findViewById(R.id.StartPostcodeEntry);
        endPostcode = view.findViewById(R.id.EndPostcodeEntry);

        getJourneyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String start = startPostcode.getText().toString();
                String end = endPostcode.getText().toString();

                Bundle bundle = new Bundle();
                bundle.putString("startPostcode", start);
                bundle.putString("endPostcode", end);

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                JourneyPlannerMapFragment journeyPlannerMapFragment = new JourneyPlannerMapFragment();
                journeyPlannerMapFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, journeyPlannerMapFragment).addToBackStack(null).commit();
            }
        });

        return view;
    }
}
