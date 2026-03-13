package com.example.myconsist;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ToDoListGroupItem extends LinearLayout {
    EditText itemName;
    Button addItemButton;
    LinearLayout itemBox;
    TextView groupName;
    boolean expanded=true;
    public ToDoListGroupItem(Context context){
        super(context);
        initialize(context);
    }
    public ToDoListGroupItem(Context context, AttributeSet attrs){
        super(context,attrs);
        initialize(context);
    }
    public void initialize(Context context){
        setOrientation(VERTICAL);
        LayoutInflater.from(context).inflate(R.layout.todo_layout, this, true);
        itemName=findViewById(R.id.itemName);
        addItemButton=findViewById(R.id.addItemButton);
        itemBox=findViewById(R.id.itemBox);
        groupName=findViewById(R.id.groupName);
        groupName.setOnClickListener(view->{
            expanded=!expanded;
            if(expanded){
                itemBox.setVisibility(VISIBLE);
                addItemButton.setVisibility(VISIBLE);
                itemName.setVisibility(VISIBLE);
            }else{
                itemBox.setVisibility(GONE);
                addItemButton.setVisibility(GONE);
                itemName.setVisibility(GONE);
            }
        });
        addItemButton.setOnClickListener(view->{
            ToDoListItem item=new ToDoListItem(getContext());
            item.setText(itemName.getText().toString());
            itemBox.addView(item);
        });
    }
}
