/**
 * Thomas Scott - S1824798
 * Description - This class is used to get the travel information.
 * It allows the user to select to either get roadworks, incidents or future roadworks.
 * The linked list which is retrieved from activitymain is used to access relevant data.
 * This data is then sent to the listview using the arrayadapter and the user can then see the info.
 * Initial plan was to let users click on an item and it would open a map of that location with a
 * marker but time ran out.
 */
package com.example.tscott202mpd_cw;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.LinkedList;

public class TravelInformationFragment extends Fragment {

    LinkedList<Roadwork> roadworkLinkedList = MainActivity.roadworkLinkedList;
    LinkedList<FutureRoadwork> futureRoadworkLinkedList = MainActivity.futureRoadworkLinkedList;
    LinkedList<Incident> incidentLinkedList = MainActivity.incidentLinkedList;
    ListView list;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_travel_information, container, false);

        Button viewRoadworksBtn = view.findViewById(R.id.ViewRoadworksBtn);
        Button viewFutureRoadworksBtn = view.findViewById(R.id.ViewFutureRoadworksBtn);
        Button viewIncidentsBtn = view.findViewById(R.id.ViewIncidentsBtn);
        list = view.findViewById(R.id.listData);

        viewRoadworksBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, roadworkLinkedList);
                list.setAdapter(arrayAdapter);

            }
        });

        viewFutureRoadworksBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, futureRoadworkLinkedList);
                list.setAdapter(arrayAdapter);

            }
        });

        viewIncidentsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, incidentLinkedList);
                list.setAdapter(arrayAdapter);

            }
        });

        return view;
    }
}
