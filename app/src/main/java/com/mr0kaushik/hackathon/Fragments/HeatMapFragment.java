package com.mr0kaushik.hackathon.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.mr0kaushik.hackathon.R;
import com.mr0kaushik.hackathon.Utils.BottomNavigationViewBehavior;

public class HeatMapFragment extends Fragment {
    private static final String TAG = "HeatMapFragment";

    private View fragmentView;
    private LinearLayout noItemsLayout;
    private MaterialButton btnNext;
    private Context context;
    private FragmentActivity fragmentActivity;
    private RecyclerView recyclerView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_heatmap, container, false);
        context = fragmentView.getContext();
        fragmentActivity = this.getActivity();
        recyclerView = fragmentView.findViewById(R.id.recyclerView);

        //Setting Title
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("HeatMap");


        //Bottom Behaviour
        BottomNavigationView bottomNavigationView = fragmentActivity.findViewById(R.id.bottom_nav_bar);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationViewBehavior());



        return fragmentView;
    }




}
