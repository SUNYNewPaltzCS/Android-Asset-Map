package com.newburghmap.newburghmap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class type2_fragment extends Fragment {

    private String[] examples = {"Ed","uc","ation"};
    private ArrayList<String> locations;

    public type2_fragment() {
        // Required empty public constructor
    }
    @SuppressLint("ValidFragment")
    public type2_fragment(ArrayList<String> locations){
        this.locations=locations;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_type2_fragment, container, false);
        return  view;
    }
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        ListView listView = (ListView) view.findViewById((R.id.locationList));
        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>
                (getActivity(), android.R.layout.simple_list_item_1, examples);
        listView.setAdapter(listViewAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3)
            {
                String value = (String)adapter.getItemAtPosition(position);
                Toast.makeText(getActivity(), value,
                        Toast.LENGTH_LONG).show();
            }
        });

        Activity act = getActivity();
        act = (MapsActivity) act;
        ((MapsActivity) act).locations("fin_lit");
    }

    public void sub2Close(View v){
    }

}
