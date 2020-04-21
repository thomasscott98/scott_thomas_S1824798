/**
 * Thomas Scott - S1824798
 * Description - This class gets the view to see all future roadworks. It gets the future roadworks linked list from activity main
 * then gets the list from the layout.
 * The data is then put into the list using ArrayAdapter.
 */
package com.example.tscott202mpd_cw;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.LinkedList;

public class FutureRoadworksFragment extends Fragment {

    LinkedList<FutureRoadwork> futureRoadworkLinkedList = MainActivity.futureRoadworkLinkedList;
    ListView list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_future_roadworks, container, false);
        list = view.findViewById(R.id.listData);

        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, futureRoadworkLinkedList);
        list.setAdapter(arrayAdapter);

        return view;
    }
}
