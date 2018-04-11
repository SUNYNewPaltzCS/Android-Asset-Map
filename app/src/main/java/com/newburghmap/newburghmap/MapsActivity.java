package com.newburghmap.newburghmap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;

//import android.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;


import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.StreetViewPanoramaLocation;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.fusiontables.Fusiontables;
import com.google.api.services.fusiontables.FusiontablesScopes;
import com.google.api.services.fusiontables.model.Sqlresponse;
import com.google.maps.android.data.kml.KmlLayer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,

        LocationListener, OnMarkerClickListener, GoogleMap.OnMyLocationButtonClickListener  , OnStreetViewPanoramaReadyCallback ,


        NavigationView.OnNavigationItemSelectedListener{


    private GoogleMap mMap;
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentLocationMarker;
    private boolean viewIsAtHome;
    public static final int REQUEST_LOCATION_CODE = 99;
    public static LatLng latilngi;
    int busClick = 0;
    KmlLayer kml;
    Dialog myDialog;

    /********************************
     * SIMONS TABLE ID
     *********************************
     */
    final String tableId = "1ImE7O7oSTm9wkj-OhizHpMOiQ-Za9h5jK-vb4qjc";

    private ArrayList<String> places =  new ArrayList<>(600);

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    //language toggle
    private ToggleButton langToggle;
    private Boolean spanish = false;


    // Google API client stuff
    final HttpTransport transport = AndroidHttp.newCompatibleTransport();
    final JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

    GoogleCredential credential;
    Fusiontables fclient;
    protected static final String TAG = "MapsActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mDrawerLayout =  findViewById(R.id.drawerLayout);

        NavigationView navigationView =  (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);


        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.custompopup);


        //get header of nav
        View header = navigationView.getHeaderView(0);
        //get menu of nav
        Menu menu = navigationView.getMenu();
        //get menu items
        final MenuItem nav_childcare = menu.findItem(R.id.nav_childcare);
        final MenuItem nav_education = menu.findItem(R.id.nav_education);
        final MenuItem nav_employment = menu.findItem(R.id.nav_employment);
        final MenuItem nav_family = menu.findItem(R.id.nav_family);
        final MenuItem nav_financial = menu.findItem(R.id.nav_financial);
        final MenuItem nav_food = menu.findItem(R.id.nav_food);
        final MenuItem nav_health = menu.findItem(R.id.nav_health);
        final MenuItem nav_housing = menu.findItem(R.id.nav_housing);
        final MenuItem nav_legal = menu.findItem(R.id.nav_legal);
        final MenuItem nav_lgbtq = menu.findItem(R.id.nav_lgbtq);
        final MenuItem nav_transportation = menu.findItem(R.id.nav_transportation);
        final MenuItem nav_veteran = menu.findItem(R.id.nav_veteran);



        //Language Toggle
        langToggle = (ToggleButton) header.findViewById(R.id.langToggle);
        langToggle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                clearMap();

                if(langToggle.isChecked())
                {
                    spanish = true;

                    nav_childcare.setTitle("Cuidado de niños");
                    nav_education.setTitle("Educación");
                    nav_employment.setTitle("Empleo");
                    nav_family.setTitle("Familia");
                    nav_financial.setTitle("Financiero");
                    nav_food.setTitle("Comida");
                    nav_health.setTitle("Salud");
                    nav_housing.setTitle("Alojamiento");
                    nav_transportation.setTitle("Transporte");
                    nav_veteran.setTitle("Veterano");

                } else {
                    // The toggle is disabled
                    spanish = false;

                    nav_childcare.setTitle("Childcare");
                    nav_education.setTitle("Education");
                    nav_employment.setTitle("Employment");
                    nav_family.setTitle("Family");
                    nav_financial.setTitle("Financial");
                    nav_food.setTitle("Food");
                    nav_health.setTitle("Health");
                    nav_housing.setTitle("Housing");
                    nav_transportation.setTitle("Transportation");
                    nav_veteran.setTitle("Veteran");

                }
            }
        });




        autoCompleter();
        AutoCompleteTextView teView = findViewById(R.id.autoComp);
        ArrayAdapter<String> adapt = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, places);
        teView.setAdapter(adapt);

        onBackPressed();
        displayView(R.id.map);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(MenuItem item) {


        displayView(item.getItemId());
        return true;
    }


    public void ShowBusRoute(View v){
        if(busClick==0){
            try {
                kml = new KmlLayer(mMap,R.raw.finalroutesbuses,getApplicationContext());
                kml.addLayerToMap();
                busClick++;
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            kml.removeLayerFromMap();
            busClick--;
        }
    }

    //This opens the custompopup.xml with the streetview and other info about the marker.
    public void InfoWindow(View v) {
        TextView txtclose;
        final StreetViewPanoramaFragment streetViewPanoramaFragment =
                (StreetViewPanoramaFragment) getFragmentManager()
                        .findFragmentById(R.id.streetviewpanorama);
        streetViewPanoramaFragment.getStreetViewPanoramaAsync(this);

        txtclose = (TextView) myDialog.findViewById(R.id.txtclose);
        myDialog.show();
        txtclose.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                myDialog.hide();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_drawer, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void displayView(int viewId) {

        Fragment fragment = null;
        String title = getString(R.string.app_name);
        viewIsAtHome = true;
        switch (viewId) {
            case R.id.nav_childcare:
                fragment = new Childcare_fragment();
                title  = "Childcare";
                viewIsAtHome = false;
                break;
            case R.id.nav_education:
                fragment = new education_fragment();
                title = "Education";
                viewIsAtHome = false;
                break;
            case R.id.nav_employment:
                fragment = new education_fragment();
                title = "Employment";
                viewIsAtHome = false;
                break;
            case R.id.nav_family:
                fragment = new education_fragment();
                title = "Family";
                viewIsAtHome = false;
                break;
            case R.id.nav_financial:
                fragment = new education_fragment();
                title = "Financial";
                viewIsAtHome = false;
                break;
            case R.id.nav_food:
                fragment = new education_fragment();
                title = "Food";
                viewIsAtHome = false;
                break;
            case R.id.nav_health:
                fragment = new education_fragment();
                title = "Health";
                viewIsAtHome = false;
                break;
            case R.id.nav_housing:
                fragment = new education_fragment();
                title = "Housing";
                viewIsAtHome = false;
                break;
            case R.id.nav_legal:
                fragment = new education_fragment();
                title = "Legal";
                viewIsAtHome = false;
                break;
            case R.id.nav_lgbtq:
                fragment = new education_fragment();
                title = "LGBTQ";
                viewIsAtHome = false;
                break;
            case R.id.nav_transportation:
                fragment = new education_fragment();
                title = "Transportation";
                viewIsAtHome = false;
                break;
            case R.id.nav_veteran:
                fragment = new education_fragment();
                title = "Veteran";
                viewIsAtHome = false;
                break;


        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.addToBackStack(null);
            ft.replace(R.id.fragmentFrame, fragment);
            ft.commit();

            //getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fragmentFrame,fragment).commit();
        }

        // set the toolbar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawer.closeDrawer(GravityCompat.START);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);


        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if (!viewIsAtHome) { //if the current view is not the News fragment
            displayView(R.id.drawerLayout); //display the News fragment
        }
        else {
            moveTaskToBack(true);  //If view is in News fragment, exit application
        }

    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationButtonClickListener(this);
            kml();
        }
        mMap.setOnMarkerClickListener(this);

        //commented out so that it is called by button
        // populateMapFromFusionTables();

        //start with map at center of Newburgh, NY
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(41.41698, -74.32525), 9));
    }

    //Home button method
    public void home(View v) {
        if (v.getId() == R.id.B_home || v.getId() == R.id.refresh) {
            clearMap();
            //start with map at center of Newburgh, NY
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(41.41698, -74.32525), 9));
        }
    }

    //Clear Map button method
    public void onClick2(View v) {
        if (v.getId() == R.id.B_clear) {
            clearMap();
        }
    }

    public void clearMap(){
        mMap.clear();
        kml();
    }

    //Go! button method
    public void onClick(View v) {

        //if user clicked on search button
        if (v.getId() == R.id.B_search) {
            //get what user typed in search box
            EditText tf_location = findViewById(R.id.autoComp);
            //then convert to string
            String location = tf_location.getText().toString();

            InputStream credentialsJSON = getResources().openRawResource(getResources().getIdentifier("service_account_credentials", "raw", getPackageName()));
            try {
                credential = GoogleCredential
                        .fromStream(credentialsJSON, transport, jsonFactory)
                        .createScoped(Collections.singleton(FusiontablesScopes.FUSIONTABLES_READONLY));
            } catch (IOException e) {
                e.printStackTrace();
            }

            fclient = new Fusiontables.Builder(
                    transport, jsonFactory, credential).setApplicationName("TestMap/1.0")
                    .build();

            try {

                Sqlresponse result;
                result = query(tableId);
                List<List<Object>> rows = result.getRows();

                for (List<Object> poi : rows) {
                    String name = (String) poi.get(0);
                    if( location.equalsIgnoreCase(name)){
                        BigDecimal lat = (BigDecimal) poi.get(1);
                        BigDecimal lon = (BigDecimal) poi.get(2);
                        LatLng latLng = new LatLng(lat.doubleValue(), lon.doubleValue());
                        String group = (String) poi.get(3);

                        clearMap();
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(name)
                                //.snippet(description)
                                .icon(iconRetrieve(group)));
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                        break;
                    }
                }


            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }


    protected synchronized void buildGoogleApiClient() {
        client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        client.connect();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission is granted
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if (client == null) {
                            buildGoogleApiClient();
                        }

                        mMap.setMyLocationEnabled(true);
                        mMap.setOnMyLocationButtonClickListener(this);

                    }
                } else //permission denied
                {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show();
                }
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {

        clearMap();

        try {
            Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(client);

            Log.d(TAG, "button clicked and got current location: lat: " + currentLocation.getLatitude() + ", lng: " + currentLocation.getLongitude());

            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            markerOptions.title("Current Location");

            //add marker for current location
            currentLocationMarker = mMap.addMarker(markerOptions);

            //add circle with 1/4 radius around current location
            mMap.addCircle(new CircleOptions()
                    .center(latLng)
                    .radius(402.336)
                    .strokeColor(0xFAF0F8FF));

            populateMapCurrentLocation(currentLocation);

            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomBy(10));

        }catch(SecurityException e){
            Log.e(TAG, "Security exception: " + e);
        }

        Toast.makeText(this, "Services within a 1/4 mile of your current location.", Toast.LENGTH_LONG).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;

        if (currentLocationMarker != null) {
            currentLocationMarker.remove();
        }

        if (client != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(client, this);
        }
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);

            }
            return false;
        } else
            return false;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();

        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void autoCompleter() {


        InputStream credentialsJSON = getResources().openRawResource(getResources().getIdentifier("service_account_credentials", "raw", getPackageName()));
        try {
            credential = GoogleCredential
                    .fromStream(credentialsJSON, transport, jsonFactory)
                    .createScoped(Collections.singleton(FusiontablesScopes.FUSIONTABLES_READONLY));
        } catch (IOException e) {
            e.printStackTrace();
        }

        fclient = new Fusiontables.Builder(
                transport, jsonFactory, credential).setApplicationName("TestMap/1.0")
                .build();

        try {

            Sqlresponse result = null;

            result = query(tableId);
            List<List<Object>> rows = result.getRows();

            places.clear();
            for (List<Object> poi : rows) {
                String name = (String) poi.get(0);
                places.add(name);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public com.google.android.gms.maps.model.BitmapDescriptor iconRetrieve(String group ){
        switch(group) {
            case "education":
                return BitmapDescriptorFactory.fromResource(R.drawable.smeducation);
            case "employment":
                return BitmapDescriptorFactory.fromResource(R.drawable.smemployment);
            case "family":
                return BitmapDescriptorFactory.fromResource(R.drawable.smfamily);
            case "financial":
                return BitmapDescriptorFactory.fromResource(R.drawable.smfinancial);
            case "food":
                return BitmapDescriptorFactory.fromResource(R.drawable.smfood);
            case "health":
                return BitmapDescriptorFactory.fromResource(R.drawable.smhealth);
            case "housing":
                return BitmapDescriptorFactory.fromResource(R.drawable.smhousing);
            case "legal":
                return BitmapDescriptorFactory.fromResource(R.drawable.smlegal);
            case "lgbtq":
                return BitmapDescriptorFactory.fromResource(R.drawable.smlgbtq);
            case "transportation":
                return BitmapDescriptorFactory.fromResource(R.drawable.smtransportation);
            case "veteran":
                return BitmapDescriptorFactory.fromResource(R.drawable.smvets);
            default:
                return null;
        }
    }

    //copy and pasted from github repo!!
    public void populateMapFromFusionTables(View v) {

        if (v.getId() == R.id.B_populate) {
            // TODO: to mak credentialsJSON work, you need to browse to https://console.developers.google.com/iam-admin/serviceaccounts/
            // create a service account with role "project > service account actor" (generate key), download the json file
            // rename it to service_account_credentials.json and place it under app/res/raw/
            InputStream credentialsJSON = getResources().openRawResource(getResources().getIdentifier("service_account_credentials", "raw", getPackageName()));
            try {
                credential = GoogleCredential
                        .fromStream(credentialsJSON, transport, jsonFactory)
                        .createScoped(Collections.singleton(FusiontablesScopes.FUSIONTABLES_READONLY));
            } catch (IOException e) {
                e.printStackTrace();
            }

            fclient = new Fusiontables.Builder(
                    transport, jsonFactory, credential).setApplicationName("TestMap/1.0")
                    .build();

            try {

                Sqlresponse result = null;

                result = query(tableId);

                List<List<Object>> rows = result.getRows();

                Log.i(TAG, "Got " + rows.size() + " POIs from fusion tables.");

                if (mMap != null) {

                    for (List<Object> poi : rows) {
                        /*DEBUG!
                        Log.i(TAG, (String) poi.get(0));
                        Log.i(TAG, "Lat " + poi.get(1));
                        Log.i(TAG, "Lon " + poi.get(2));
                        */

                        //group, name, group(spanish), type, type(sp), subtype, subtype(sp), description, des(sp),
                        // address, orig address, latitude, longitude, phone, hotline, contact, hours, hours(sp), link, icon
                        String name  = (String) poi.get(0);

                        BigDecimal lat = (BigDecimal) poi.get(1);
                        BigDecimal lon = (BigDecimal) poi.get(2);
                        LatLng latLng = new LatLng(lat.doubleValue(), lon.doubleValue());

                        String group = (String) poi.get(3);

                        String description;
                        //english
                        if(!spanish){
                            description = (String) poi.get(4);
                        }
                        //spanish
                        else{
                            description = (String) poi.get(5);
                        }

                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(name)
                                .snippet(description)
                                .icon(iconRetrieve(group)));
                    }

                } else {
                    Log.i(TAG, "mMap is null, not placing markers.");
                }

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void populateMapCurrentLocation(Location center) {

        // TODO: to mak credentialsJSON work, you need to browse to https://console.developers.google.com/iam-admin/serviceaccounts/
        // create a service account with role "project > service account actor" (generate key), download the json file
        // rename it to service_account_credentials.json and place it under app/res/raw/
        InputStream credentialsJSON = getResources().openRawResource(getResources().getIdentifier("service_account_credentials", "raw", getPackageName()));
        try {
            credential = GoogleCredential
                    .fromStream(credentialsJSON, transport, jsonFactory)
                    .createScoped(Collections.singleton(FusiontablesScopes.FUSIONTABLES_READONLY));
        } catch (IOException e) {
            e.printStackTrace();
        }

        fclient = new Fusiontables.Builder(
                transport, jsonFactory, credential).setApplicationName("TestMap/1.0")
                .build();

        try {

            Sqlresponse result = null;

            result = query(tableId);

            List<List<Object>> rows = result.getRows();

            Log.i(TAG, "Got " + rows.size() + " POIs from fusion tables.");

            if (mMap != null) {

                for (List<Object> poi : rows) {
                    //DEBUG!
                    // Log.i(TAG, (String) poi.get(0));
                    // Log.i(TAG, "Lat " + poi.get(1));
                    // Log.i(TAG, "Lon " + poi.get(2));

                    //group, name, group(spanish), type, type(sp), subtype, subtype(sp), description, des(sp),
                    // address, orig address, latitude, longitude, phone, hotline, contact, hours, hours(sp), link, icon
                    String name = (String) poi.get(0);

                    BigDecimal lat = (BigDecimal) poi.get(1);
                    BigDecimal lon = (BigDecimal) poi.get(2);
                    LatLng latLng = new LatLng(lat.doubleValue(), lon.doubleValue());
                    String group = (String) poi.get(3);

                    //needed to know if point is within 1/4 mile of location
                    Location test = new Location("");
                    test.setLatitude(lat.doubleValue());
                    test.setLongitude(lon.doubleValue());


                    float distanceInMeters = center.distanceTo(test);
                    if(distanceInMeters < 402.336 ){

                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(name)
                                //.snippet(description)
                                .icon(iconRetrieve(group)));
                    }
                }
            } else {
                Log.i(TAG, "mMap is null, not placing markers.");
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //method to create snackbar with marker's info when marker is clicked.
    @Override
    public boolean onMarkerClick(Marker marker) {
        latilngi = marker.getPosition();
        InfoWindow(findViewById(R.id.streetviewpanorama));
        return false;
    }


    protected Sqlresponse query(String q) throws ExecutionException, InterruptedException {
        // Inspired from: https://github.com/digitalheir/fusion-tables-android/blob/master/src/com/google/fusiontables/ftclient/FtClient.java
        // It instantiates a GetTableTask class, calls execute, which calls doInBackground
        return new GetTableTask(fclient).execute(q).get();
    }

    //This opens the custompopup.xml with the streetview of the marker.Checks if there is a street view it displays it else ...
    @Override
    public void onStreetViewPanoramaReady(final StreetViewPanorama streetViewPanorama) {
        streetViewPanorama.setPosition(new LatLng(latilngi.latitude,latilngi.longitude));

        streetViewPanorama.setOnStreetViewPanoramaChangeListener(new StreetViewPanorama.OnStreetViewPanoramaChangeListener() {
            @Override
            public void onStreetViewPanoramaChange(StreetViewPanoramaLocation streetViewPanoramaLocation) {
                if (streetViewPanoramaLocation != null && streetViewPanoramaLocation.links != null) {
                    streetViewPanorama.setPosition(new LatLng(latilngi.latitude,latilngi.longitude));
                } else {
                    // location not available

                }
            }
        });

    }

    protected class GetTableTask extends AsyncTask<String, Void, Sqlresponse> {
        Fusiontables fclient;

        public GetTableTask(Fusiontables fclient) {
            this.fclient = fclient;
        }

        @Override
        protected Sqlresponse doInBackground(String... params) {
            String tableId = params[0];
            Log.i(TAG, "doInBackground table id: " + tableId);
            Sqlresponse sqlresponse = null;
            try {
                //String parenting = "parenting";
                Fusiontables.Query.SqlGet sql = fclient.query().sqlGet("SELECT name, latitude, longitude, 'group', description, DesctriptionES FROM " + tableId);// +" WHERE 'subtype' = '"+parenting+"'");
                sqlresponse = sql.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sqlresponse;
        }
    }

    // Adding KML Layer to get the outline of Orange County. Method is called @onMapReady().
    public void kml(){
        try {
            KmlLayer kml = new KmlLayer(mMap,R.raw.orange_county,getApplicationContext());
            kml.addLayerToMap();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}