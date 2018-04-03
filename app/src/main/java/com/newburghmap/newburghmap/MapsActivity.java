package com.newburghmap.newburghmap;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.ActionBar;
import android.widget.Toolbar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, OnMarkerClickListener, GoogleMap.OnMyLocationButtonClickListener {

    private GoogleMap mMap;
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentLocationMarker;
    public static final int REQUEST_LOCATION_CODE = 99;
    Dialog myDialog;

    private DrawerLayout mDrawerLayout;
    ExpandableListAdapter mMenuAdapter;
    ExpandableListView expandableList;
    List<ExpandedMenuModel> listDataHeader;
    HashMap<ExpandedMenuModel, List<String>> listDataChild;

    private ActionBarDrawerToggle mToggle;


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

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        expandableList = (ExpandableListView) findViewById(R.id.navigationmenu);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
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

        //Sub menu
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        prepareListData();
        mMenuAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild, expandableList);

        // setting list adapter
        expandableList.setAdapter(mMenuAdapter);

        expandableList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                //Log.d("DEBUG", "submenu item clicked");
                return false;
            }
        });
        expandableList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                //Log.d("DEBUG", "heading clicked");
                return false;
            }
        });
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<ExpandedMenuModel>();
        listDataChild = new HashMap<ExpandedMenuModel, List<String>>();

        ExpandedMenuModel item1 = new ExpandedMenuModel();
        item1.setIconName("Health");
        // Adding data header
        listDataHeader.add(item1);

        ExpandedMenuModel item2 = new ExpandedMenuModel();
        item2.setIconName("Food");
        listDataHeader.add(item2);

        ExpandedMenuModel item3 = new ExpandedMenuModel();
        item3.setIconName("Housing");
        listDataHeader.add(item3);

        ExpandedMenuModel item4 = new ExpandedMenuModel();
        item4.setIconName("Employment");
        listDataHeader.add(item4);

        ExpandedMenuModel item5 = new ExpandedMenuModel();
        item5.setIconName("Childcare");
        listDataHeader.add(item5);

        ExpandedMenuModel item6 = new ExpandedMenuModel();
        item6.setIconName("Financial");
        listDataHeader.add(item6);

        ExpandedMenuModel item7 = new ExpandedMenuModel();
        item7.setIconName("Lgbtq");
        listDataHeader.add(item7);

        ExpandedMenuModel item8 = new ExpandedMenuModel();
        item8.setIconName("Legal");
        listDataHeader.add(item8);

        ExpandedMenuModel item9 = new ExpandedMenuModel();
        item9.setIconName("Vets");
        listDataHeader.add(item9);

        ExpandedMenuModel item10 = new ExpandedMenuModel();
        item10.setIconName("Transportation");
        listDataHeader.add(item10);

        ExpandedMenuModel item11 = new ExpandedMenuModel();
        item11.setIconName("Education");
        listDataHeader.add(item11);


        // Adding child data
        List<String> Health = new ArrayList<String>();
        Health.add("Addiction");
        Health.add("Counseling");
        Health.add("Services");
        Health.add("Mental");

        List<String> Food = new ArrayList<String>();
        Food.add("a");
        Food.add("b");
        Food.add("c");

        List<String> Housing = new ArrayList<String>();
        Housing.add("a");
        Housing.add("b");
        Housing.add("c");

        List<String> Employment = new ArrayList<String>();
        Employment.add("a");
        Employment.add("b");
        Employment.add("c");

        List<String> Childcare = new ArrayList<String>();
        Childcare.add("a");
        Childcare.add("b");
        Childcare.add("c");

        List<String> Financial = new ArrayList<String>();
        Financial.add("a");
        Financial.add("b");
        Financial.add("c");

        List<String> Lgbtq = new ArrayList<String>();
        Lgbtq.add("a");
        Lgbtq.add("b");
        Lgbtq.add("c");

        List<String> Vets = new ArrayList<String>();
        Vets.add("a");
        Vets.add("b");
        Vets.add("c");

        List<String> Legal = new ArrayList<String>();
        Legal.add("a");
        Legal.add("b");
        Legal.add("c");

        List<String> Transportation = new ArrayList<String>();
        Transportation.add("a");
        Transportation.add("b");
        Transportation.add("c");

        List<String> Education = new ArrayList<String>();
        Education.add("a");
        Education.add("b");
        Education.add("c");

        // Header, Child data

        listDataChild.put(listDataHeader.get(0), Health);
        listDataChild.put(listDataHeader.get(1), Food);
        listDataChild.put(listDataHeader.get(2), Housing);
        listDataChild.put(listDataHeader.get(3), Employment);
        listDataChild.put(listDataHeader.get(4), Childcare);
        listDataChild.put(listDataHeader.get(5), Financial);
        listDataChild.put(listDataHeader.get(6), Lgbtq);
        listDataChild.put(listDataHeader.get(7), Legal);
        listDataChild.put(listDataHeader.get(8), Vets);
        listDataChild.put(listDataHeader.get(9), Transportation);
        listDataChild.put(listDataHeader.get(10), Education);

    }


    public void ShowPopup(View v) {
        TextView txtclose;
        myDialog.setContentView(R.layout.custompopup);
        txtclose = (TextView) myDialog.findViewById(R.id.txtclose);
        txtclose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if (mToggle.onOptionsItemSelected(item)) {
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
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

        //commented out so that it is called by button
        // populateMapFromFusionTables();


        //start with map at center of Newburgh, NY
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(41.41698, -74.32525), 9));
    }

    //Home button method
    public void home(View v) {
        if (v.getId() == R.id.B_home) {
            //start with map at center of Newburgh, NY
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(41.41698, -74.32525), 9));
        }
    }

    //Clear Map button method
    public void onClick2(View v) {
        if (v.getId() == R.id.B_clear) {
            mMap.clear();
            kml();
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
            EditText tf_location = findViewById(R.id.TF_location);
            //then convert to string
            String location = tf_location.getText().toString();

            List<Address> addressList = null;
            MarkerOptions mo = new MarkerOptions();

            //check if user actually entered something or not
            if (!location.equals("")) {
                // will return list of addresses that are known to describe the current location (set max num of results to 5)
                //this will have to be modified to only return results from fusion table
                Geocoder geocoder = new Geocoder(this);
                try {
                    addressList = geocoder.getFromLocationName(location, 5);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //put markers of returned locations
                for (int i = 0; i < addressList.size(); i++) {
                    Address myAddress = addressList.get(i); //get address
                    LatLng latLng = new LatLng(myAddress.getLatitude(), myAddress.getLongitude()); //create latLang for it
                    mo.position(latLng); //pass position to marker, could set the title and icon here too
                    mMap.addMarker(mo); //add marker to map

                    //you can leave it like this, or have camera move to last result.
                    //we will move it to the last result
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                }
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
                /********************************
                * SIMONS TABLE ID
                *********************************
                 */
                String tableId = "1ImE7O7oSTm9wkj-OhizHpMOiQ-Za9h5jK-vb4qjc";
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
                        String name = (String) poi.get(0);

                        BigDecimal lat = (BigDecimal) poi.get(1);
                        BigDecimal lon = (BigDecimal) poi.get(2);
                        LatLng latLng = new LatLng(lat.doubleValue(), lon.doubleValue());

                        String group = (String) poi.get(3);

                        switch(group) {
                            case "education":
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(name)
                                        //.snippet(description)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.smeducation)));
                                break;
                            case "employment":
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(name)
                                        //.snippet(description)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.smemployment)));
                                break;
                            case "family":
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(name)
                                        //.snippet(description)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.smfamily)));
                                break;
                            case "financial":
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(name)
                                        //.snippet(description)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.smfinancial)));
                                break;
                            case "food":
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(name)
                                        //.snippet(description)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.smfood)));
                                break;
                            case "health":
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(name)
                                        //.snippet(description)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.smhealth)));
                                break;
                            case "housing":
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(name)
                                        //.snippet(description)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.smhousing)));
                                break;
                            case "legal":
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(name)
                                        //.snippet(description)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.smlegal)));
                                break;
                            case "lgbtq":
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(name)
                                        //.snippet(description)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.smlgbtq)));
                                break;
                            case "transportation":
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(name)
                                        //.snippet(description)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.smtransportation)));
                                break;
                            case "veteran":
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(name)
                                        //.snippet(description)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.smvets)));
                                break;
                            default:
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(name));
                                //.snippet(description)
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
            /********************************
             * SIMONS TABLE ID
             *********************************
             */
            String tableId = "1ImE7O7oSTm9wkj-OhizHpMOiQ-Za9h5jK-vb4qjc";
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

                        switch(group) {
                            case "education":
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(name)
                                        //.snippet(description)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.smeducation)));
                                break;
                            case "employment":
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(name)
                                        //.snippet(description)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.smemployment)));
                                break;
                            case "family":
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(name)
                                        //.snippet(description)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.smfamily)));
                                break;
                            case "financial":
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(name)
                                        //.snippet(description)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.smfinancial)));
                                break;
                            case "food":
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(name)
                                        //.snippet(description)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.smfood)));
                                break;
                            case "health":
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(name)
                                        //.snippet(description)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.smhealth)));
                                break;
                            case "housing":
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(name)
                                        //.snippet(description)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.smhousing)));
                                break;
                            case "legal":
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(name)
                                        //.snippet(description)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.smlegal)));
                                break;
                            case "lgbtq":
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(name)
                                        //.snippet(description)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.smlgbtq)));
                                break;
                            case "transportation":
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(name)
                                        //.snippet(description)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.smtransportation)));
                                break;
                            case "veteran":
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(name)
                                        //.snippet(description)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.smvets)));
                                break;
                            default:
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(name));
                                //.snippet(description)
                        }
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

        return false;
    }


    protected Sqlresponse query(String q) throws ExecutionException, InterruptedException {
        // Inspired from: https://github.com/digitalheir/fusion-tables-android/blob/master/src/com/google/fusiontables/ftclient/FtClient.java
        // It instantiates a GetTableTask class, calls execute, which calls doInBackground
        return new GetTableTask(fclient).execute(q).get();
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
                Fusiontables.Query.SqlGet sql = fclient.query().sqlGet("SELECT name, latitude, longitude, 'group' FROM " + tableId);// +" WHERE 'subtype' = '"+parenting+"'");
                sqlresponse = sql.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return sqlresponse;
        }
    }

//    private class DownloadKmlFile extends AsyncTask<String, Void, byte[]> {
//        private final String mUrl;
//
//        public DownloadKmlFile(String url) {
//            mUrl = url;
//        }
//
//        protected byte[] doInBackground(String... params) {
//            try {
//                InputStream is = new URL(mUrl).openStream();
//                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
//                int nRead;
//                byte[] data = new byte[16384];
//                while ((nRead = is.read(data, 0, data.length)) != -1) {
//                    buffer.write(data, 0, nRead);
//                }
//                buffer.flush();
//                return buffer.toByteArray();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//    }

    // Adding KML Layer to get the outline of Orange County. Method is called @onMapReady()& onClick2()
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