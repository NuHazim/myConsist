package com.example.myconsist;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navView;
    Toolbar appBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Window window = getWindow();
        window.setStatusBarColor(Color.parseColor("#111827"));
        window.setNavigationBarColor(Color.parseColor("#111827"));
        // Initialize views
        drawerLayout = findViewById(R.id.drawerLayout);
        navView = findViewById(R.id.navView);
        appBar = findViewById(R.id.appBar);

        // Setup toolbar
        setSupportActionBar(appBar);
//        appBar.getNavigationIcon().setTint(Color.parseColor("#06b6d4"));

        // THIS WAS MISSING (hamburger toggle)
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                appBar,
                R.string.open,
                R.string.close
        );

        View headerView = navView.getHeaderView(0);
        ImageView closeBtn = headerView.findViewById(R.id.closeDrawerBtn);

        closeBtn.setOnClickListener(v -> {
            drawerLayout.closeDrawer(GravityCompat.START);
        });
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // 🔹 Load default fragment
        if (savedInstanceState == null) {
            loadFragment(new ToDoListFragment(), "To Do List", R.id.todolistFragment);
        }

        // 🔹 Handle sidebar clicks
        navView.setNavigationItemSelectedListener(item -> {

            Fragment fragment = null;
            String title = "";
            int id = item.getItemId();

            if (id == R.id.reminderFragment) {
                fragment = new ReminderFragment();
                title = "Reminders";
            } else if (id == R.id.todolistFragment) {
                fragment = new ToDoListFragment();
                title = "To Do List";
            } else if (id == R.id.habitsFragment) {
                fragment = new HabitsFragment();
                title = "Habits Tracker";
            }

            if (fragment != null) {
                loadFragment(fragment, title, id); // pass `id` directly
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }

            return false;
        });
    }

    // 🔹 Reusable fragment loader
    public void loadFragment(Fragment fragment, String title, int menuItemId) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }

        navView.setCheckedItem(menuItemId); // ✅ highlights the active item
    }
}