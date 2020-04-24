package com.taxiappz.client.ui.placesearch;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.nplus.searchplacehelplibrary.SearchListener;
import com.nplus.searchplacehelplibrary.SearchPlaceHelper;
import com.taxiappz.client.R;
import com.nplus.searchplacehelplibrary.retro.responsemodel.PredictionModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;


/**
 * Created by Mukesh on 17/02/2020.
 */

public class PlaceSearchScreen extends AppCompatActivity implements SearchListener {
    //String BaseURL = "http://13.56.246.254:3001";
    String BaseURL = "http://54.215.230.210:3001";
    PlaceSearchAdapter searchRestultAdapter;
    List<PredictionModel.PredictedData> predictedDataList = new ArrayList<>();
    RecyclerView recyclerView;
    TextView resultAddress, resultLocation;
    public static final int REQUEST_ENABEL_LOCATION = 200;
    public static final int REQUEST_PERMISSION = 9000;

    private LocationRequest mLocationRequest;

    public static String[] Array_permissions = new String[]{
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION};

    LatLng currLatLng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SearchPlaceHelper.init(BaseURL, this, PlaceSearchScreen.this);

        resultAddress = findViewById(R.id.txtViewAddress);
        resultLocation = findViewById(R.id.txtViewLocation);

        ((EditText) findViewById(R.id.edit_placeSearch)).addTextChangedListener(textWatcher);
        searchRestultAdapter = new PlaceSearchAdapter(predictedDataList, this);

        recyclerView = ((RecyclerView) findViewById(R.id.recycler_placeSearch));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(searchRestultAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !checkGranted(Array_permissions)) {
            requestPermissionsSafely(Array_permissions, REQUEST_PERMISSION);
        } else {
            if (isGpscheck(this)) {
                createLocationRequest();
                // getCurrLocation();
            } else ShowGpsDialog(this);
        }


    }


    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable != null && editable.length() > 0)
                if (currLatLng != null) {
                    Double lat = currLatLng.latitude;
                    Double lng = currLatLng.longitude;
                    SearchPlaceHelper.searchPlace(editable.toString(), lat, lng);
                } else Log.e("LocNull==", "Null");

        }
    };

    @Override
    public void onSuccessPlace(List<PredictionModel.PredictedData> predictedDataList) {
        Log.d("Result", "Got The Result");
        Toast.makeText(this, "Got the result", Toast.LENGTH_SHORT).show();
        searchRestultAdapter.addAllItems(predictedDataList);
    }

    @Override
    public void onSuccessGeocoding(PredictionModel.LocationClass locationObject) {
        Log.d("Result", "Got The Result");
        Toast.makeText(this, "Got the result", Toast.LENGTH_SHORT).show();
        if (locationObject != null && !TextUtils.isEmpty(locationObject.lat) && !TextUtils.isEmpty(locationObject.lng))
            resultLocation.setText("Location: " + locationObject.lat + "," + locationObject.lng);
    }

    @Override
    public void onFailure(Throwable t) {
        Log.d("Result", "Failed in Result");
        Toast.makeText(this, "Failed in Result", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onFailureGeocoding(Throwable t) {
        Log.d("Result", "Failed in Geocoding Result");
        Toast.makeText(this, "Failed in Geocoding Result", Toast.LENGTH_SHORT).show();
//        searchRestultAdapter.clearAllItems();

    }

    public void onItemClicked(PredictionModel.PredictedData predictedData) {
        if (predictedData != null && !TextUtils.isEmpty(predictedData.description) && !TextUtils.isEmpty(predictedData.place_id)) {
            searchRestultAdapter.clearAllItems();
            resultAddress.setText("Address: " + predictedData.description);
            SearchPlaceHelper.geocodePlace(predictedData.place_id);
        }
    }

    public static boolean isGpscheck(Context context) {
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static void ShowGpsDialog(final Activity context) {

        android.app.AlertDialog.Builder gpsBuilder = new android.app.AlertDialog.Builder(context);
        gpsBuilder.setCancelable(false);
        gpsBuilder.setTitle("No Gps Found")
                .setMessage("Turn on Gps to get Realtime update")
                .setPositiveButton("Enable Gps", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        context.startActivityForResult(intent, REQUEST_ENABEL_LOCATION);

                        dialog.dismiss();


                    }
                });
        AlertDialog gpsAlertDialog = gpsBuilder.create();
        gpsAlertDialog.show();
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean checkGranted(String[] permissions) {

        for (String per : permissions) {

            if (checkSelfPermission(per) != PackageManager.PERMISSION_GRANTED)
                return false;
        }

        return true;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissionsSafely(String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !checkGranted(Array_permissions)) {
            requestPermissionsSafely(Array_permissions, REQUEST_PERMISSION);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    createLocationRequest();
                }
            }, 1000);
        }


    }


    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(1000);
        startLocationUpdates();
    }

    protected void startLocationUpdates() {

        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(10000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);
        getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        // do work here
                        currLatLng = new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
                        Log.e("currlat", "lng==" + currLatLng);
                    }
                },
                Looper.myLooper());
    }

}