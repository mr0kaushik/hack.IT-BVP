package com.mr0kaushik.hackathon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.mr0kaushik.hackathon.Fragments.DashboardFragment;
import com.mr0kaushik.hackathon.Fragments.HeatMapFragment;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    BottomNavigationView bottomNavigationView;

    ActionBarDrawerToggle actionBarDrawerToggle;

    private static final long TIME_DELAY = 2000;
    boolean isPressedTwice = false;

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    final Fragment dashboardFragment = new DashboardFragment();
    final Fragment heatMapFragment = new HeatMapFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);


        active = dashboardFragment;
        fm.beginTransaction().add(R.id.frameLayoutForBottomNavigation, dashboardFragment, "dashboard").commit();
        fm.beginTransaction().add(R.id.frameLayoutForBottomNavigation, heatMapFragment, "heatmap").hide(heatMapFragment).commit();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Workshops");

        //DRAWER LAYOUT
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Log.d(TAG, "onCreate: creating MainActivity");

        bottomNavigationView = findViewById(R.id.bottom_nav_bar);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);


    }


    BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.menu_dashboard:
                    Objects.requireNonNull(getSupportActionBar()).setTitle("Dashboard");
                    fm.beginTransaction().hide(active).show(dashboardFragment).commit();
                    active = dashboardFragment;
                    return true;

                case R.id.menu_heat_map:
                    Objects.requireNonNull(getSupportActionBar()).setTitle("Workshops");
                    fm.beginTransaction().hide(active).show(heatMapFragment).commit();
                    active = heatMapFragment;
                    return true;
            }
            return false;
        }
    };

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (isPressedTwice) {
                super.onBackPressed();
                return;
            }

            this.isPressedTwice = true;

            View view = findViewById(android.R.id.content);
            Snackbar snackbar = Snackbar.make(view, R.string.press_again, Snackbar.LENGTH_SHORT);
            snackbar.show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isPressedTwice = false;
                }
            }, TIME_DELAY);
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){

            case R.id.menu_edit_profile:
                drawerLayout.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(this, EditProfileActivity.class);
                startActivity(intent);
                break;

            case R.id.menu_logout:
                logout();
                break;

            case R.id.menu_help:
                showSnackBar("Will add later.");
                break;

            case R.id.menu_bugReport:
                boolean sendReport = report();
                if (sendReport){
                    showSnackBar("Opening Email");
                }
                break;

            case R.id.menu_about:
                showSnackBar("Will add later.");

                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    public void showSnackBar(String msg){
        View view = findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }


    private void logout() {
        //TODO LOGOUT


        bottomNavigationView.setSelectedItemId(R.id.menu_dashboard);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item != null && item.getItemId() == android.R.id.home) {
            toggle();
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggle() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    private boolean report() {
        Intent Email = new Intent(Intent.ACTION_SEND);
        Email.setType("text/email");
        Email.putExtra(Intent.EXTRA_EMAIL, new String[] { "thor.jane.odinson@gmail.com" });
        Email.putExtra(Intent.EXTRA_SUBJECT, "Bug Report for the Hackathon app");
        Email.putExtra(Intent.EXTRA_TEXT, "Hello dev, " + "");
        startActivity(Intent.createChooser(Email, "Bug Report: "));
        return true;
    }
}