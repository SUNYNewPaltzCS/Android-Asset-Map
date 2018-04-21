package com.newburghmap.newburghmap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class education_fragment extends Fragment {

    ArrayList<String> type;

    public education_fragment() {
        // Required empty public constructor
    }

    public education_fragment(ArrayList<String> types) {
        type=types;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_education_fragment, container, false);

        String[] educationMenuItems = {"Education Services","Youth","Parenting",
                "Supportive Services for Intellectual and Developmental Disabilities",
                "Educational Assistance","Education for Employment","Computer Access"};

        ListView listView = (ListView) view.findViewById((R.id.educationMenu));

        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_list_item_1, type);

//maps.types("Family"
        listView.setAdapter(listViewAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position ==0){
                    Intent intent = new Intent(getActivity(), Childcare_fragment.class);
                    startActivity(intent);
                }else if(position==1){
                    Intent intent = new Intent(getActivity(), Childcare_fragment.class);
                    startActivity(intent);

                }else if(position==2){

                }
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

}