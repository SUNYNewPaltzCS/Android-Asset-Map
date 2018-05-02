package com.newburghmap.newburghmap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spanned;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 */
public class type2_fragment extends Fragment {

    private String[] examples = {"Ed","uc","ation"};
    private String subtype;

    public type2_fragment() {
        // Required empty public constructor
    }
    //@SuppressLint("ValidFragment")

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_type2_fragment, container, false);
        return  view;
    }
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        this.subtype = this.getArguments().getStringArrayList("key").get(0);
        Activity act = getActivity();
        act = (MapsActivity) act;

        final ListView listView = (ListView) view.findViewById((R.id.locationList));
        ArrayAdapter<Spanned> listViewAdapter = new ArrayAdapter<Spanned>
                (getActivity(), android.R.layout.simple_list_item_1, ((MapsActivity) act).locations(subtype));
        listView.setAdapter(listViewAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3)
            {
                Spanned value = (Spanned) adapter.getItemAtPosition(position);

                Activity act2 = getActivity();
                act2 = (MapsActivity) act2;
                try {
                    ((MapsActivity) act2).latilngi = new LatLng(Double.parseDouble(((MapsActivity) act2).latit.get(position)), Double.parseDouble(((MapsActivity) act2).longi.get(position)));
                    ((MapsActivity) act2).InfoWindow(listView, ((MapsActivity) act2).latit.get(position), ((MapsActivity) act2).longi.get(position));
                }catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }



//                Toast.makeText(getActivity(), value,
//                        Toast.LENGTH_LONG).show();
            }
        });



    }

    public void sub2Close(View v){
    }

}
