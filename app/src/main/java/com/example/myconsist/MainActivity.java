package com.example.myconsist;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar appBar=findViewById(R.id.appBar);
        setSupportActionBar(appBar);
        BottomNavigationView bottomNav=findViewById(R.id.bottomNav);
        if(savedInstanceState==null){
            loadFragment(new ToDoListFragment(),"To Do List");
            bottomNav.setSelectedItemId(R.id.todolistFragment);
        }
        bottomNav.setOnItemSelectedListener(item->{
            Fragment selectedFrag=null;
            String title="";
            int id=item.getItemId();
            if(id==R.id.todolistFragment){
                selectedFrag=new ToDoListFragment();
                title="To Do List";
            }
            else if(id==R.id.habitsFragment){
                selectedFrag=new HabitsFragment();
                title="Habits Tracker";
            }
            else if(id==R.id.reminderFragment){
                selectedFrag=new ReminderFragment();
                title="Reminders";
            }
            if(selectedFrag!=null){
                loadFragment(selectedFrag,title);
                return true;
            }
           return false;
        });
    }
    public void loadFragment(Fragment fragment,String title){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer,fragment)
                .addToBackStack(null)
                .commit();
        getSupportActionBar().setTitle((title));
    }
}