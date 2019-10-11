package com.mr0kaushik.hackathon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mr0kaushik.hackathon.Model.User;

import java.util.Objects;

public class ApplicationActivity extends AppCompatActivity implements OnMapReadyCallback {


    static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;


    private static final long TIME_INTERVAL_GET_LOCATION = 15000;
    private static final long TIME_FASTEST_INTERVAL_GET_LOCATION = TIME_INTERVAL_GET_LOCATION / 2;

    private static final String TAG = "ApplicationActivity";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 101;

    private static final int LOCATION_PERMISSION_TYPE = 0;
    private static final int LOCATION_PERMISSION_STRICTLY_TYPE = 1;
    private static final int NETWORK_ENABLE_TYPE = 3;
    private static final float DEFAULT_ZOOM = 10f;
    private static final int IMAGE_REQUEST = 2;

    private AppCompatImageView ivImg;

/*
    private GoogleMap mMap;
    private MapView mapView;
*/

    private TextInputEditText etDesc;
    private AppCompatAutoCompleteTextView etCategory;
    private MaterialButton btnSubmit;
    private boolean permissionGranted, isShown;
    private FusedLocationProviderClient fusedLocationProviderClient;



    private String[] permissions = {FINE_LOCATION, COARSE_LOCATION};
    private double userLat, userLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Complaint Request");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        init();


        permissionGranted = checkPermission();

        if(!checkPermission()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            getDeviceLocation();
        }

        //mapView = findViewById(R.id.mapView);
        //mapView.onCreate(savedInstanceState);


        try {
            MapsInitializer.initialize(getApplicationContext());
            initMap();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initMap() {
        Log.d(TAG, "initMap: initiating");
        //mapView.getMapAsync(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called");
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {
                for (int result : grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        permissionGranted = false;
                        Log.d(TAG, "onRequestPermissionsResult: Permission Not Granted!!");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(FINE_LOCATION)) {
                                sDialog(LOCATION_PERMISSION_TYPE);
                            } else {
                                sDialog(LOCATION_PERMISSION_STRICTLY_TYPE);
                            }
                        }
                        return;
                    }
                }
            }
            getDeviceLocation();
            permissionGranted = true;
        }
    }

    private void sDialog(int dialogType) {

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getApplicationContext(), R.style.MAlertDialogStyle);

        switch (dialogType) {
            case LOCATION_PERMISSION_TYPE:
                builder.setTitle(getString(R.string.need_permission));
                builder.setMessage("Permission required to get exact location of Polluted Area. Would you like to allow it?");
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(permissions, LOCATION_PERMISSION_REQUEST_CODE);
                            isShown = false;
                        }
                    }
                });
                break;

            case LOCATION_PERMISSION_STRICTLY_TYPE:
                builder.setTitle("Need Permission");
                builder.setMessage("Permission required to render map. You can grant them in settings.");
                builder.setPositiveButton("GO TO SETTINGS", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openPermissionSetting();
                        isShown = false;
                    }
                });

                break;

            case NETWORK_ENABLE_TYPE:
                Log.w(TAG, "showDialog: Network Not enabled");
                builder.setTitle("Enable Network");
                builder.setMessage("Network required to use, please turn on Internet Connection.");
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getDeviceLocation();
                        isShown = false;
                    }
                });

                break;

        }
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isShown = false;
            }
        });

        if (!isShown) {
            builder.show();
            builder.setCancelable(false);
            isShown = true;
        }

    }

    private void openPermissionSetting() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + getApplicationContext().getPackageName()));
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }



    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }



    private void init() {
        etDesc = findViewById(R.id.etDesc);
        etCategory = findViewById(R.id.etCategory);
        btnSubmit = findViewById(R.id.btnSubmit);
        ivImg = findViewById(R.id.img);

        ivImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, IMAGE_REQUEST);
            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etCategory.getText()!=null && etCategory.getText().equals(getString(R.string.select_category)))
                    submitForm();
            }
        });

    }

    private void submitForm() {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference;


        if(firebaseUser != null) {
            firebaseUser.getUid();
            reference = FirebaseDatabase.getInstance().getReference("Applications").child(firebaseUser.getUid());
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.i("Database Error", "onCancelled: Error = " + databaseError.toString());
                }
            });
        }





    }

    public void showSnackBar(String msg){
        View view = findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    private void moveCamera(LatLng latLng) {
        Log.d(TAG, "moveCamera: Moving camera to device location : " +
                "Latitude = " + latLng.latitude + " Longitude = " + latLng.longitude);
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, MapFragment.DEFAULT_ZOOM));
        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM);
        //mMap.animateCamera(location);
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "moveCamera: permission error to mark location");
        }
    }


    private boolean isGpsEnabled() {
        LocationManager lm = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        assert lm != null;
        try {
            Log.d(TAG, "isGpsEnabled: Enable GPS");
            return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isNetworkEnabled() {
        LocationManager lm = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        assert lm != null;
        try {
            Log.d(TAG, "isNetworkEnabled: Enabled Network");
            return lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting current location");

        if (isGpsEnabled() || isNetworkEnabled()) {

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
            if (checkPermission()) {
                fusedLocationProviderClient.getLastLocation()
                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {

                                    Log.d(TAG, "onComplete: location found");
                                    userLat = location.getLatitude();
                                    userLng = location.getLongitude();

                                } else {
                                    Log.d(TAG, "onComplete: location not found");
                                }
                            }
                        });
            } else {
                sDialog(LOCATION_PERMISSION_TYPE);
            }

        } else if (!isGpsEnabled()) {
            Log.d(TAG, "getDeviceLocation: ");
            //showDialog(GPS_ENABLE_TYPE);
        } else if (!isNetworkEnabled()) {
            sDialog(NETWORK_ENABLE_TYPE);
        }

    }



    private boolean validate(Editable seq) {
        return seq == null || seq.length() <= 0;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        /*try {
            if (googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getApplicationContext(), ))) {
                Log.d(TAG, "onMapReady: Style parsed successfully");
            } else {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }*/

        //mMap = googleMap;


        if (permissionGranted && checkPermission()) {

            Log.d(TAG, "onMapReady: Permission Granted Ready to set map");
            if (!checkPermission()) {
                Log.d(TAG, "onMapReady: Permission not granted");
                permissionGranted = false;
                return;
            }

            //mMap.setMyLocationEnabled(true);

            /*mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    Log.d(TAG, "onMyLocationButtonClick: ");
                    if (ApplicationActivity.this.isGpsEnabled() || ApplicationActivity.this.isNetworkEnabled()) {
                        Log.d(TAG, "onMapReady: MoveCamera On My location");
                        ApplicationActivity.this.getDeviceLocation();
                        if (userLat > 0 && userLng > 0) {
                            ApplicationActivity.this.moveCamera(new LatLng(userLat, userLng));
                        } else {
                            ApplicationActivity.this.getDeviceLocation();
                            ApplicationActivity.this.moveCamera(new LatLng(userLat, userLng));
                        }
                    } else {
                        ApplicationActivity.this.openLocationSetting();
                        //showDialog(GPS_ENABLE_TYPE);
                    }
                    return false;
                }
            });

            mMap.getUiSettings().setMapToolbarEnabled(false);

            getDeviceLocation();

*/

        }
    }

    private void openLocationSetting() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(getLocationRequest());
    }


    private LocationRequest getLocationRequest() {

        return LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(TIME_INTERVAL_GET_LOCATION)
                .setSmallestDisplacement(10)
                .setFastestInterval(TIME_FASTEST_INTERVAL_GET_LOCATION);
    }
}
