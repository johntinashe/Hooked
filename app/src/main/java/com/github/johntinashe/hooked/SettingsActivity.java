package com.github.johntinashe.hooked;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.github.johntinashe.hooked.Utils.Utils;
import com.github.johntinashe.hooked.model.Setting;
import com.github.johntinashe.hooked.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.SetOptions;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import locationprovider.davidserrano.com.LocationProvider;

public class SettingsActivity extends AppCompatActivity {

    private FirebaseFirestore mDB;
    private FirebaseAuth mAuth;

    @BindView(R.id.settings_location) TextView locationTV;
    @BindView(R.id.switch1) Switch pushNotification;
    @BindView(R.id.seekBar2) SeekBar seekBar;
    @BindView(R.id.search_radius_tv) TextView searchRadiusTv;
    @BindView(R.id.gender_pref_tv) TextView genderPref;

    AlertDialog alertDialog1;
    CharSequence[] values = {" Male "," Female "," Any "};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        toolbar();

        Utils.checkAuth(this);
        mAuth = FirebaseAuth.getInstance();
        mDB =FirebaseFirestore.getInstance();
        getDetails();

        Permissions.check(this, Manifest.permission.ACCESS_FINE_LOCATION, null, new PermissionHandler() {
            @Override
            public void onGranted() {
                getLocation();
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                // permission denied, block the feature.
            }
        });

        pushNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                   String uid = mAuth.getUid();
                   if (uid != null) {
                       Map<String,Boolean> map = new HashMap<>();
                       map.put("notification",isChecked);
                       mDB.collection("users").document(uid).collection("settings")
                               .document(uid).set(map,SetOptions.merge());
                   }
               }
        });

        seekBar.setMax(100);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                searchRadiusTv.setText(progress+" km");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

                searchRadiusTv.setText(seekBar.getProgress()+" km");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                searchRadiusTv.setText(seekBar.getProgress()+" km");
                if (mAuth.getUid() != null) {

                    Map<String,Integer> map = new HashMap<>();
                    map.put("radius",seekBar.getProgress());

                    mDB.collection("users").document(mAuth.getUid()).collection("settings")
                            .document(mAuth.getUid())
                            .set(map,SetOptions.merge());
                }
            }
        });
    }


    private void toolbar() {
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        Utils.setToolbar(this,getSupportActionBar());
    }

    private void getLocation() {

        LocationProvider.LocationCallback callback = new LocationProvider.LocationCallback() {
            @Override
            public void onNewLocationAvailable(float lat, float lon) {
                setNewLocation(lat,lon);
            }

            @Override
            public void locationServicesNotEnabled() {
                //failed finding a location
            }

            @Override
            public void updateLocationInBackground(float lat, float lon) {
                setNewLocation(lat,lon);
            }

            @Override
            public void networkListenerInitialised() {
                //when the library switched from GPS only to GPS & network
            }

            @Override
            public void locationRequestStopped() {

            }
        };

        //initialise an instance with the two required parameters
        LocationProvider locationProvider = new LocationProvider.Builder()
                .setContext(this)
                .setListener(callback)
                .create();

        //start getting location
        locationProvider.requestLocation();
    }


    void setNewLocation(double lat,double lon) {

        String uid = mAuth.getUid();
        GeoPoint point = new GeoPoint(lat,lon);
        Map<String,GeoPoint> map = new HashMap<>();
        map.put("location",point);
        if (uid != null) mDB.collection("users").document(uid)
                .collection("settings")
                .document(uid)
                .set(map,SetOptions.merge());
    }


    private void getDetails() {
        String id = mAuth.getUid();
        if (id!= null)
        mDB.collection("users").document(id).collection("settings").document(id).addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
              if (e == null) {
                  if (documentSnapshot != null) {
                      Setting settings = documentSnapshot.toObject(Setting.class);
                      pushNotification.setChecked(settings.isNotification());
                      searchRadiusTv.setText(settings.getRadius()+ " km");
                      seekBar.setProgress(settings.getRadius());
                      Address address = getAddress(settings.getLocation().getLatitude(),settings.getLocation().getLongitude());
                      if (address != null)
                      locationTV.setText(address.getLocality() +"," +address.getCountryName());
                  }
              }
            }
        });
    }

    public Address getAddress(double latitude, double longitude) {

        Geocoder geocoder;
        List addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            // Here 1 represent max location result to returned, by documents it recommended 1 to 5
           addresses = geocoder.getFromLocation(latitude,longitude, 1);
           if (addresses != null) {
               return (Address)addresses.get(0);
           }else {
               return null;
           }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void genderPref(View view) {
        selectGenderPref();
    }

    public void selectGenderPref(){


        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);

        builder.setTitle("Select Your Gender Preference");
        builder.setCancelable(true);

        builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                switch(item)
                {
                    case 0:

                        Toast.makeText(SettingsActivity.this, "First Item Clicked", Toast.LENGTH_LONG).show();
                        break;
                    case 1:

                        Toast.makeText(SettingsActivity.this, "Second Item Clicked", Toast.LENGTH_LONG).show();
                        break;
                    case 2:

                        Toast.makeText(SettingsActivity.this, "Third Item Clicked", Toast.LENGTH_LONG).show();
                        break;
                }
                alertDialog1.dismiss();
            }
        });
        alertDialog1 = builder.create();
        alertDialog1.show();

    }

}
