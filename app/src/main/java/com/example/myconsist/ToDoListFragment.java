package com.example.myconsist;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ToDoListFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_to_do_list, container, false);
        TextView groupName=view.findViewById(R.id.groupName);
        LinearLayout theGroupDetails=view.findViewById(R.id.theGroupDetails);
        groupName.setOnClickListener(v->{
            if(theGroupDetails.getVisibility()==View.VISIBLE){
                theGroupDetails.setVisibility(View.GONE);
            }else{
                theGroupDetails.setVisibility((View.VISIBLE));
            }
        });
        return view;
    }
}