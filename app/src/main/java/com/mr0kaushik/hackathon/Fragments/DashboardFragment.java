package com.mr0kaushik.hackathon.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.mr0kaushik.hackathon.ApplicationActivity;
import com.mr0kaushik.hackathon.ApplicationFragment;
import com.mr0kaushik.hackathon.R;
import com.mr0kaushik.hackathon.Utils.BottomNavigationViewBehavior;

public class DashboardFragment extends Fragment {
    private static final String TAG = "DashboardFragment";

    private View fragmentView;
    private Context context;
    private FragmentActivity fragmentActivity;
    private RecyclerView recyclerView;

    private FloatingActionButton btnAdd;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        context = fragmentView.getContext();
        fragmentActivity = this.getActivity();
        recyclerView = fragmentView.findViewById(R.id.recyclerView);


        btnAdd = fragmentView.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(), ApplicationActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout noItemsLayout = fragmentView.findViewById(R.id.noContentLayout);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        Log.d(TAG, "onCreateView: recycler view items count " + recyclerView.getItemDecorationCount());
        if (recyclerView.getItemDecorationCount()>0){
            noItemsLayout.setVisibility(View.GONE);
        }

        //Bottom Behaviour
        BottomNavigationView bottomNavigationView = fragmentActivity.findViewById(R.id.bottom_nav_bar);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationViewBehavior());

        return fragmentView;
    }



    private void showSnackBar(String msg){
        View view = fragmentView.findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }



}
