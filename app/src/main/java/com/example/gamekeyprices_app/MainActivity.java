package com.example.gamekeyprices_app;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Looper;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

// API_KEY 0dfaaa8b017e516c145a7834bc386864fcbd06f5

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private double longitude;
    private double latitude;
    private String country;
    public String mCountryFromMain;
    public String mRegionFromMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        check_permissions();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        Menu menu = navigationView.getMenu();
        // changes color of menu title
        MenuItem category = menu.findItem(R.id.nav_category);
        SpannableString s = new SpannableString(category.getTitle());
        s.setSpan(new TextAppearanceSpan(this, R.style.MenuTitleStyle), 0, s.length(), 0);
        category.setTitle(s);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_favorites, R.id.nav_deals, R.id.nav_search, R.id.nav_all, R.id.nav_category, R.id.nav_action, R.id.nav_adventure, R.id.nav_fps,
                R.id.nav_horror, R.id.nav_management, R.id.nav_mmo, R.id.nav_racing, R.id.nav_rpg, R.id.nav_sports, R.id.nav_strategy)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    // CHECK LOCATION PERMISSIONS
    private void check_permissions() {

        if(ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String [] {ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
        }
        else{
            getCurrentLocation();
        }

    }
    // EVALUATE PERMISSION REQUEST RESULTS
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length >0)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               getCurrentLocation();
            } else {
                Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();

            }

        }

    }
    // GET CURRENT CITY AND COUNTRY
    private void getCurrentLocation() {

        final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(30000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(MainActivity.this)
                .requestLocationUpdates(locationRequest,new LocationCallback(){
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(MainActivity.this)
                                .removeLocationUpdates(this);
                        if(locationResult != null && locationResult.getLocations().size() > 0 ){
                            int latestLocationIndex = locationResult.getLocations().size() -1;
                            latitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                            longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();

                            // GET CITY AND COUNTRY NAME

                            try {
                                Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                country = addresses.get(0).getCountryCode();
                                mCountryFromMain = "&country=" + country;
                                mRegionFromMain = "";
                                if(mCountryFromMain.equals("&country=US")){mRegionFromMain="&region=us";}
                                if(mCountryFromMain.equals("&country=DE")){mRegionFromMain="&region=eu1";}

                                Toast.makeText(getApplicationContext(), mCountryFromMain, Toast.LENGTH_SHORT).show();
                                Toast.makeText(getApplicationContext(), mRegionFromMain, Toast.LENGTH_SHORT).show();

                            }
                            catch (IOException e){
                                e.printStackTrace();
                                Toast.makeText(MainActivity.this, "Error: " + e, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }, Looper.getMainLooper());

    }
}
