package com.mr0kaushik.hackathon;

import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.mr0kaushik.hackathon.R;

public class ApplicationFragment extends AppCompatActivity {

    public static final String TAG = "ApplicationDialogFragme";

    private BottomSheetBehavior mBehavior;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_layout);



    }


    @Override
    public void onStart() {
        super.onStart();
    }


}
