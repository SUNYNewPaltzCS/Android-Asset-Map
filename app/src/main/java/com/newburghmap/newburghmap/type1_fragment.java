package com.newburghmap.newburghmap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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


    ArrayList<String> type;
    ExpandableListView expandableListView;
    //private String[][] examples = {{"Ed","uc","ation"},{"Emp", "ploy","ment"},{"fa","mi","ly"}};
    private ArrayList<ArrayList<String>> mainArrayList = new ArrayList<ArrayList<String>>();
    private ArrayList<String> subArrayList = new ArrayList<String>();
    private ArrayList<String> subArrayList2 = new ArrayList<String>();





//    @SuppressLint("ValidFragment")
//    public type1_fragment(ArrayList<String> types) {
//        type=types;
//    }

    @SuppressLint("ValidFragment")
    public type1_fragment(ArrayList<String> types, ArrayList<ArrayList<String>> mainArrayList) {

        this.type = types;
        this.mainArrayList = mainArrayList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.fragment_type1_fragment, container, false);

                //possible lines needed for expandable list
//        ExpandableListView elv = (ExpandableListView) view.findViewById(R.id.type1Menu);
//        elv.setAdapter(new ExpandableListViewAdapter(type, examples));



//        ListView listView = (ListView) view.findViewById((R.id.type1Menu));
//
//        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(
//                getActivity(), android.R.layout.simple_list_item_1, type);
//
//        listView.setAdapter(listViewAdapter);


        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        subArrayList.add("girls");
//        subArrayList.add("Drool");
//        subArrayList.add("boys");
//        subArrayList.add("rule");
//
//        subArrayList2.add("ring");
//        subArrayList2.add("around");
//        subArrayList2.add("the");
//        subArrayList2.add("rosie");
//
//        mainArrayList.add((subArrayList));
//        mainArrayList.add((subArrayList2));

        String[][] array2d = new String[mainArrayList.size()][];
        for (int i = 0; i < mainArrayList.size(); i++) {
            ArrayList<String> row = mainArrayList.get(i);
            array2d[i] = row.toArray(new String[row.size()]);
        }

        expandableListView = (ExpandableListView) view.findViewById(R.id.type1Menu);
        ExpandableListViewAdapter expandableListViewAdapter = new ExpandableListViewAdapter(getActivity(),type,array2d);
        expandableListView.setAdapter(expandableListViewAdapter);


    }
}