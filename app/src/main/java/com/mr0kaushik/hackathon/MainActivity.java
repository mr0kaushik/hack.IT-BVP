package com.mr0kaushik.hackathon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.gesture.GestureLibraries;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mr0kaushik.hackathon.Fragments.DashboardFragment;
import com.mr0kaushik.hackathon.Fragments.HeatMapFragment;
import com.mr0kaushik.hackathon.Model.User;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

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


        fm.beginTransaction().add(R.id.frameLayoutForBottomNavigation, heatMapFragment, "heatmap").hide(heatMapFragment).commit();
        fm.beginTransaction().add(R.id.frameLayoutForBottomNavigation, dashboardFragment, "dashboard").commit();
        active = dashboardFragment;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Dashboard");

        //DRAWER LAYOUT
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        setUpData(header);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        bottomNavigationView = findViewById(R.id.bottom_nav_bar);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

    }

    private void setUpData(View navigationView) {
        final CircleImageView ivProfile = navigationView.findViewById(R.id.profilePic);
        final AppCompatTextView tvName = navigationView.findViewById(R.id.tvName);
        final AppCompatTextView tvEmail = navigationView.findViewById(R.id.tvEmail);
        final AppCompatTextView tvRewards = navigationView.findViewById(R.id.tvRewardPoints);


        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference;

        if(firebaseUser != null) {
            firebaseUser.getUid();
            reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        if(user.getName()!=null)
                            tvName.setText(user.getName());

                        if(user.getEmail()!=null)
                            tvEmail.setText(user.getEmail());

                        tvRewards.setText(String.valueOf(user.getReward_count()));

                        if (user.getImg_url() != null && user.getImg_url().equals("default")) {
                            Glide.with(getApplicationContext()).load(R.drawable.ic_boss).into(ivProfile);
                        } else {
                            Glide.with(getApplicationContext()).load(user.getImg_url()).into(ivProfile);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.i("Database Error", "onCancelled: Error = " + databaseError.toString());
                }
            });
        }


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
                    Objects.requireNonNull(getSupportActionBar()).setTitle("HeatMap");
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
        FirebaseAuth.getInstance().signOut();
        Log.i("LOGOUT", "onOptionsItemSelected: LOGOUT ");
        Toast.makeText(this, "Successfully Logout!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(MainActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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