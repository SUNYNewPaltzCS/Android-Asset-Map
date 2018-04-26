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
    //ExpandableListView expandableListView;
    //private String[][] examples = {{"Ed","uc","ation"},{"Emp", "ploy","ment"},{"fa","mi","ly"}};


    public type1_fragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public type1_fragment(ArrayList<String> types) {
        type=types;
    }

//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//         examples = new String[][] {{"Ed","uc","ation"},{"Emp", "ploy","ment"},{"fa","mi","ly"}};
//
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.fragment_type1_fragment, container, false);

                //possible lines needed for expandable list
//        ExpandableListView elv = (ExpandableListView) view.findViewById(R.id.type1Menu);
//        elv.setAdapter(new ExpandableListViewAdapter(type, examples));



        ListView listView = (ListView) view.findViewById((R.id.type1Menu));

        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_list_item_1, type);


        listView.setAdapter(listViewAdapter);


                //Item click listener code
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if(position ==0){
////                    Intent intent = new Intent(getActivity(), Childcare_fragment.class);
////                    startActivity(intent);
//                }else if(position==1){
////                    Intent intent = new Intent(getActivity(), Childcare_fragment.class);
////                    startActivity(intent);
//
//                }else if(position==2){
//
//                }
//            }
//        });

        // Inflate the layout for this fragment
        return view;
    }

//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        expandableListView = (ExpandableListView) expandableListView.findViewById(R.id.type1Menu);
//        ExpandableListViewAdapter expandableListViewAdapter = new ExpandableListViewAdapter(type,examples);
//        expandableListView.setAdapter(expandableListViewAdapter);
//
//    }
}