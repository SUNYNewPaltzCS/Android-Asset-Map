package com.newburghmap.newburghmap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class type1_fragment extends Fragment {

    Fragment fragment2;
    ArrayList<String> type;
    ExpandableListView expandableListView;
    //private String[][] examples = {{"Ed","uc","ation"},{"Emp", "ploy","ment"},{"fa","mi","ly"}};
    private ArrayList<ArrayList<String>> mainArrayList = new ArrayList<ArrayList<String>>();
    private ArrayList<String> subArrayList = new ArrayList<String>();
    private ArrayList<String> subArrayList2 = new ArrayList<String>();


    public type1_fragment(){

    }


//    @SuppressLint("ValidFragment")
//    public type1_fragment(ArrayList<String> types) {
//        type=types;
//    }

    @SuppressLint("ValidFragment")
    public type1_fragment(ArrayList<String> types, ArrayList<ArrayList<String>> mainArrayList, Fragment fragment2) {

        this.type = types;
        this.mainArrayList = mainArrayList;
        this.fragment2 = fragment2;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_type1_fragment, container, false);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //turns an arraylist into a String[][]
        String[][] array2d = new String[mainArrayList.size()][];
        for (int i = 0; i < mainArrayList.size(); i++) {
            ArrayList<String> row = mainArrayList.get(i);
            array2d[i] = row.toArray(new String[row.size()]);
        }

        expandableListView = (ExpandableListView) view.findViewById(R.id.type1Menu);
        ExpandableListViewAdapter expandableListViewAdapter = new ExpandableListViewAdapter(getActivity(),type,array2d);
        expandableListView.setAdapter(expandableListViewAdapter);

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.addToBackStack(null);
                ft.replace(R.id.fragmentFrame, fragment2);
                Bundle subtypeBun = new Bundle();
                ArrayList<String> bbt = new ArrayList<String>();
                bbt.add(mainArrayList.get(groupPosition).get(childPosition));
                subtypeBun.putStringArrayList("key", bbt);


                fragment2.setArguments(subtypeBun);

                ft.commit();

                Activity act = getActivity();
                act = (MapsActivity) act;
                ((MapsActivity) act).clearMap();
                ((MapsActivity) act).populateMapFromFusionTable(mainArrayList.get(groupPosition).get(childPosition));


                return false;
            }
        });//mainArrayList.get(groupPosition).get(childPosition)


    }

    public void subClose(View v){
    }

}