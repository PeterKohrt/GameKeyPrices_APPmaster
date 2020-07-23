package com.mcvm_app.gamekeyprices_app;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Looper;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
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

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    //variables for user mobility -> request depends on determined country
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private double longitude;
    private double latitude;
    private String country;
    public String mCountryFromMain;
    public String mRegionFromMain;

    //progressbar on app start
    private ProgressBar start_progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        Menu menu = navigationView.getMenu();

        // changes color of menu title
        MenuItem category = menu.findItem(R.id.nav_spotlight);
        SpannableString s = new SpannableString(category.getTitle());
        s.setSpan(new TextAppearanceSpan(this, R.style.MenuTitleStyle), 0, s.length(), 0);
        category.setTitle(s);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_favorites, R.id.nav_deals, R.id.nav_search, R.id.nav_startpage, R.id.nav_action, R.id.nav_adventure, R.id.nav_fps,
                R.id.nav_horror, R.id.nav_management, R.id.nav_mmo, R.id.nav_racing, R.id.nav_rpg, R.id.nav_sports, R.id.nav_strategy)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //init progressbar
        start_progressBar = findViewById(R.id.progressBar_start);

        check_permissions();

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    // check location permissions
    private void check_permissions() {

        if(ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String [] {ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
        }
        else{
            getCurrentLocation();
        }

    }
    // actions depending on result of location permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length >0)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //determine country and region
               getCurrentLocation();
            } else {
                //set default to US us
                mCountryFromMain="&country=US";mRegionFromMain="&region=us";
                Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
                start_progressBar.setVisibility(View.INVISIBLE);

            }

        }

    }
    // get current country and location
    private void getCurrentLocation() {

        //until position is not defined progressbar is visible
        start_progressBar.setVisibility(View.VISIBLE);

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

                            // get country in country code
                            try {
                                Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                country = addresses.get(0).getCountryCode();
                                mCountryFromMain = "&country=" + country;
                                mRegionFromMain = "";

                                //set country and region for requests
                                //EU1
                                if(mCountryFromMain.equals("&country=AL")){mRegionFromMain="&region=eu1";String region=mRegionFromMain.substring(8);Toast.makeText(getApplicationContext(), "country: " + country + " region: " + region, Toast.LENGTH_SHORT).show();}
                                if(mCountryFromMain.equals("&country=AD")){mRegionFromMain="&region=eu1";String region=mRegionFromMain.substring(8);Toast.makeText(getApplicationContext(), "country: " + country + " region: " + region, Toast.LENGTH_SHORT).show();}
                                if(mCountryFromMain.equals("&country=AT")){mRegionFromMain="&region=eu1";String region=mRegionFromMain.substring(8);Toast.makeText(getApplicationContext(), "country: " + country + " region: " + region, Toast.LENGTH_SHORT).show();}
                                if(mCountryFromMain.equals("&country=BE")){mRegionFromMain="&region=eu1";String region=mRegionFromMain.substring(8);Toast.makeText(getApplicationContext(), "country: " + country + " region: " + region, Toast.LENGTH_SHORT).show();}
                                if(mCountryFromMain.equals("&country=DK")){mRegionFromMain="&region=eu1";String region=mRegionFromMain.substring(8);Toast.makeText(getApplicationContext(), "country: " + country + " region: " + region, Toast.LENGTH_SHORT).show();}
                                if(mCountryFromMain.equals("&country=FI")){mRegionFromMain="&region=eu1";String region=mRegionFromMain.substring(8);Toast.makeText(getApplicationContext(), "country: " + country + " region: " + region, Toast.LENGTH_SHORT).show();}
                                if(mCountryFromMain.equals("&country=FR")){mRegionFromMain="&region=eu1";String region=mRegionFromMain.substring(8);Toast.makeText(getApplicationContext(), "country: " + country + " region: " + region, Toast.LENGTH_SHORT).show();}
                                if(mCountryFromMain.equals("&country=DE")){mRegionFromMain="&region=eu1";String region=mRegionFromMain.substring(8);Toast.makeText(getApplicationContext(), "country: " + country + " region: " + region, Toast.LENGTH_SHORT).show();}
                                if(mCountryFromMain.equals("&country=IE")){mRegionFromMain="&region=eu1";String region=mRegionFromMain.substring(8);Toast.makeText(getApplicationContext(), "country: " + country + " region: " + region, Toast.LENGTH_SHORT).show();}
                                if(mCountryFromMain.equals("&country=LI")){mRegionFromMain="&region=eu1";String region=mRegionFromMain.substring(8);Toast.makeText(getApplicationContext(), "country: " + country + " region: " + region, Toast.LENGTH_SHORT).show();}
                                if(mCountryFromMain.equals("&country=LU")){mRegionFromMain="&region=eu1";String region=mRegionFromMain.substring(8);Toast.makeText(getApplicationContext(), "country: " + country + " region: " + region, Toast.LENGTH_SHORT).show();}
                                if(mCountryFromMain.equals("&country=MK")){mRegionFromMain="&region=eu1";String region=mRegionFromMain.substring(8);Toast.makeText(getApplicationContext(), "country: " + country + " region: " + region, Toast.LENGTH_SHORT).show();}
                                if(mCountryFromMain.equals("&country=NL")){mRegionFromMain="&region=eu1";String region=mRegionFromMain.substring(8);Toast.makeText(getApplicationContext(), "country: " + country + " region: " + region, Toast.LENGTH_SHORT).show();}
                                if(mCountryFromMain.equals("&country=SE")){mRegionFromMain="&region=eu1";String region=mRegionFromMain.substring(8);Toast.makeText(getApplicationContext(), "country: " + country + " region: " + region, Toast.LENGTH_SHORT).show();}
                                if(mCountryFromMain.equals("&country=CH")){mRegionFromMain="&region=eu1";String region=mRegionFromMain.substring(8);Toast.makeText(getApplicationContext(), "country: " + country + " region: " + region, Toast.LENGTH_SHORT).show();}

                                //EU2
                                if(mCountryFromMain.equals("&country=BA")){mRegionFromMain="&region=eu2";Toast.makeText(getApplicationContext(), "country: " + country + " region: ", Toast.LENGTH_SHORT).show();}
                                if(mCountryFromMain.equals("&country=BG")){mRegionFromMain="&region=eu2";String region=mRegionFromMain.substring(8);Toast.makeText(getApplicationContext(), "country: " + country + " region: " + region, Toast.LENGTH_SHORT).show();}
                                if(mCountryFromMain.equals("&country=HR")){mRegionFromMain="&region=eu2";String region=mRegionFromMain.substring(8);Toast.makeText(getApplicationContext(), "country: " + country + " region: " + region, Toast.LENGTH_SHORT).show();}
                                if(mCountryFromMain.equals("&country=CY")){mRegionFromMain="&region=eu2";String region=mRegionFromMain.substring(8);Toast.makeText(getApplicationContext(), "country: " + country + " region: " + region, Toast.LENGTH_SHORT).show();}
                                if(mCountryFromMain.equals("&country=CZ")){mRegionFromMain="&region=eu2";String region=mRegionFromMain.substring(8);Toast.makeText(getApplicationContext(), "country: " + country + " region: " + region, Toast.LENGTH_SHORT).show();}
                                if(mCountryFromMain.equals("&country=GR")){mRegionFromMain="&region=eu2";String region=mRegionFromMain.substring(8);Toast.makeText(getApplicationContext(), "country: " + country + " region: " + region, Toast.LENGTH_SHORT).show();}
                                if(mCountryFromMain.equals("&country=HU")){mRegionFromMain="&region=eu2";String region=mRegionFromMain.substring(8);Toast.makeText(getApplicationContext(), "country: " + country + " region: " + region, Toast.LENGTH_SHORT).show();}
                                if(mCountryFromMain.equals("&country=IT")){mRegionFromMain="&region=eu2";String region=mRegionFromMain.substring(8);Toast.makeText(getApplicationContext(), "country: " + country + " region: " + region, Toast.LENGTH_SHORT).show();}
                                if(mCountryFromMain.equals("&country=MT")){mRegionFromMain="&region=eu2";String region=mRegionFromMain.substring(8);Toast.makeText(getApplicationContext(), "country: " + country + " region: " + region, Toast.LENGTH_SHORT).show();}
                                if(mCountryFromMain.equals("&country=MC")){mRegionFromMain="&region=eu2";String region=mRegionFromMain.substring(8);Toast.makeText(getApplicationContext(), "country: " + country + " region: " + region, Toast.LENGTH_SHORT).show();}
                                if(mCountryFromMain.equals("&country=ME")){mRegionFromMain="&region=eu2";String region=mRegionFromMain.substring(8);Toast.makeText(getApplicationContext(), "country: " + country + " region: " + region, Toast.LENGTH_SHORT).show();}
                                if(mCountryFromMain.equals("&country=NO")){mRegionFromMain="&region=eu2";String region=mRegionFromMain.substring(8);Toast.makeText(getApplicationContext(), "country: " + country + " region: " + region, Toast.LENGTH_SHORT).show();}
                                if(mCountryFromMain.equals("&country=PL")){mRegionFromMain="&region=eu2";String region=mRegionFromMain.substring(8);Toast.makeText(getApplicationContext(), "country: " + country + " region: " + region, Toast.LENGTH_SHORT).show();}
                                if(mCountryFromMain.equals("&country=PT")){mRegionFromMain="&region=eu2";String region=mRegionFromMain.substring(8);Toast.makeText(getApplicationContext(), "country: " + country + " region: " + region, Toast.LENGTH_SHORT).show();}
                                if(mCountryFromMain.equals("&country=RO")){mRegionFromMain="&region=eu2";String region=mRegionFromMain.substring(8);Toast.makeText(getApplicationContext(), "country: " + country + " region: " + region, Toast.LENGTH_SHORT).show();}
                                if(mCountryFromMain.equals("&country=SM")){mRegionFromMain="&region=eu2";String region=mRegionFromMain.substring(8);Toast.makeText(getApplicationContext(), "country: " + country + " region: " + region, Toast.LENGTH_SHORT).show();}
                                if(mCountryFromMain.equals("&country=RS")){mRegionFromMain="&region=eu2";String region=mRegionFromMain.substring(8);Toast.makeText(getApplicationContext(), "country: " + country + " region: " + region, Toast.LENGTH_SHORT).show();}
                                if(mCountryFromMain.equals("&country=SK")){mRegionFromMain="&region=eu2";String region=mRegionFromMain.substring(8);Toast.makeText(getApplicationContext(), "country: " + country + " region: " + region, Toast.LENGTH_SHORT).show();}
                                if(mCountryFromMain.equals("&country=SI")){mRegionFromMain="&region=eu2";String region=mRegionFromMain.substring(8);Toast.makeText(getApplicationContext(), "country: " + country + " region: " + region, Toast.LENGTH_SHORT).show();}
                                if(mCountryFromMain.equals("&country=ES")){mRegionFromMain="&region=eu2";String region=mRegionFromMain.substring(8);Toast.makeText(getApplicationContext(), "country: " + country + " region: " + region, Toast.LENGTH_SHORT).show();}
                                if(mCountryFromMain.equals("&country=VA")){mRegionFromMain="&region=eu2";String region=mRegionFromMain.substring(8);Toast.makeText(getApplicationContext(), "country: " + country + " region: " + region, Toast.LENGTH_SHORT).show();}
                                if(mCountryFromMain.equals("&country=EE")){mRegionFromMain="&region=eu2";String region=mRegionFromMain.substring(8);Toast.makeText(getApplicationContext(), "country: " + country + " region: " + region, Toast.LENGTH_SHORT).show();}
                                if(mCountryFromMain.equals("&country=LV")){mRegionFromMain="&region=eu2";String region=mRegionFromMain.substring(8);Toast.makeText(getApplicationContext(), "country: " + country + " region: " + region, Toast.LENGTH_SHORT).show();}
                                if(mCountryFromMain.equals("&country=LT")){mRegionFromMain="&region=eu2";String region=mRegionFromMain.substring(8);Toast.makeText(getApplicationContext(), "country: " + country + " region: " + region, Toast.LENGTH_SHORT).show();}

                                //US
                                if(mCountryFromMain.equals("&country=US")){mRegionFromMain="&region=us";String region=mRegionFromMain.substring(8);Toast.makeText(getApplicationContext(), "country: " + country + " region: " + region, Toast.LENGTH_SHORT).show();}
                                //GB
                                if(mCountryFromMain.equals("&country=GB")){mRegionFromMain="&region=uk";String region=mRegionFromMain.substring(8);Toast.makeText(getApplicationContext(), "country: " + country + " region: " + region, Toast.LENGTH_SHORT).show();}
                                //CA
                                if(mCountryFromMain.equals("&country=CA")){mRegionFromMain="&region=ca";String region=mRegionFromMain.substring(8);Toast.makeText(getApplicationContext(), "country: " + country + " region: " + region, Toast.LENGTH_SHORT).show();}
                                //BR
                                if(mCountryFromMain.equals("&country=BR")){mRegionFromMain="&region=br2";String region=mRegionFromMain.substring(8);Toast.makeText(getApplicationContext(), "country: " + country + " region: " + region, Toast.LENGTH_SHORT).show();}
                                //AU
                                if(mCountryFromMain.equals("&country=AU")){mRegionFromMain="&region=au2";String region=mRegionFromMain.substring(8);Toast.makeText(getApplicationContext(), "country: " + country + " region: " + region, Toast.LENGTH_SHORT).show();}
                                //RU
                                if(mCountryFromMain.equals("&country=RU")){mRegionFromMain="&region=ru";String region=mRegionFromMain.substring(8);Toast.makeText(getApplicationContext(), "country: " + country + " region: " + region, Toast.LENGTH_SHORT).show();}
                                //TR
                                if(mCountryFromMain.equals("&country=TR")){mRegionFromMain="&region=tr";String region=mRegionFromMain.substring(8);Toast.makeText(getApplicationContext(), "country: " + country + " region: " + region, Toast.LENGTH_SHORT).show();}
                                //CN
                                if(mCountryFromMain.equals("&country=CN")){mRegionFromMain="&region=cn";String region=mRegionFromMain.substring(8);Toast.makeText(getApplicationContext(), "country: " + country + " region: " + region, Toast.LENGTH_SHORT).show();}

                                //progressbar invisible
                                start_progressBar.setVisibility(View.INVISIBLE);
                            }
                            catch (IOException e){
                                e.printStackTrace();
                                Toast.makeText(MainActivity.this, "Error: " + e, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }, Looper.getMainLooper());

    }
}
