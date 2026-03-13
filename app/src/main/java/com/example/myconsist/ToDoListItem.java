package com.example.myconsist;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class ToDoListItem extends AppCompatTextView{
    boolean checked=false;
    public ToDoListItem(Context context){
        super(context);
        initialize();
    }
    public ToDoListItem(Context context, AttributeSet attrs){
        super(context,attrs);
        initialize();
    }
    public void initialize(){
        setPadding(10,10,10,10);
        setTextSize(18);
        setOnClickListener(view->{
            checked=!checked;
            if(checked){
                setPaintFlags(getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
            else{
                setPaintFlags(getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }
        });
    }
}
