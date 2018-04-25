package com.newburghmap.newburghmap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ExpandableListViewAdapter extends BaseExpandableListAdapter {

    ArrayList<String> type;
    String[][] examples;

    Context context;

    public ExpandableListViewAdapter(Context context) {

        this.context = context;
    }

    @SuppressLint("ValidFragment")
    public ExpandableListViewAdapter(ArrayList<String> types, String[][] examples) {
        this.type=types;
        this.examples=examples;
    }


    @Override
    public int getGroupCount() {
        return type.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return examples[groupPosition].length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return type.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return examples[groupPosition][childPosition];
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup parent) {

        final TextView txtview = new TextView(context);
        txtview.setText(type.get(groupPosition));
        txtview.setPadding(100,0,0,0);
        txtview.setTextColor(Color.BLUE);
        return txtview;

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {

        final TextView txtView = new TextView(context);
        txtView.setText(examples[groupPosition][childPosition]);
        txtView.setPadding(100,0,0,0);

        txtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, txtView.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });


        return txtView;


    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
