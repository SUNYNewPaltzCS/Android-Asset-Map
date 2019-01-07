package com.newburghmap.newburghmap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;

//import android.app.Fragment;

import android.graphics.Color;
import android.location.LocationManager;
import android.support.v4.app.Fragment;

import android.content.pm.PackageManager;
import android.location.Location;

//added
import android.location.Criteria;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.Task;
import com.jakewharton.threetenabp.AndroidThreeTen;

import java.util.*;
import java.time.LocalDateTime;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


//import android.location.LocationListener;

import com.google.android.gms.maps.model.MapStyleOptions;

import android.text.method.LinkMovementMethod;

import com.google.api.client.json.JsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.android.gms.tasks.OnSuccessListener;


// above
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;


import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
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
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.fusiontables.Fusiontables;
import com.google.api.services.fusiontables.FusiontablesScopes;
import com.google.api.services.fusiontables.model.Sqlresponse;
import com.google.maps.android.data.kml.KmlLayer;

import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.app.PendingIntent.getActivity;

//extends Activity
    public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,

        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener,
        OnMarkerClickListener, GoogleMap.OnMyLocationButtonClickListener  , OnStreetViewPanoramaReadyCallback ,


        NavigationView.OnNavigationItemSelectedListener{

        private GoogleMap mMap;
        private GoogleApiClient client;
        private LocationRequest locationRequest;
        private Location lastLocation;
        private Marker currentLocationMarker;
        private boolean viewIsAtHome;
        public static final int REQUEST_LOCATION_CODE = 99;
        public LatLng latilngi;
        //int busClick = 0;
        boolean busClick = false;
        KmlLayer kml;
        KmlLayer kml1;
        Dialog myDialog;
        Dialog myDialog1;
        Dialog myDialog3;

         /********************************
         * SIMONS TABLE ID *
         *********************************/

        final String tableId = "1gy6SXW0WexuugOvx6WlkhPcoFOlWTCQoIwd6AX5p";
        final String busTableId = "1C7bjeXCA0PwM423Z2jN5A-Z8wjpnUQa2qqHkIP_8";

       //a id for google sheets bus route below
      //  final String busTableId = "1J9AqRCJlEDAp7VHYJ4lTGkTeCY58_oaLKDUpSSqKMcI";


        final String sqlTableId = "ResourceTable";
        final String sqlBusTableId = "BusRouteTable";
                //"1ImE7O7oSTm9wkj-OhizHpMOiQ-Za9h5jK-vb4qjc";
//        final String nameBold = new String("name");
//        final String addressBold = new String("address");
//        SpannableString test = new SpannableString("name");
//        android.text.SpannableString.setSpan()(new StyleSpan);

        private ArrayList<String> places =  new ArrayList<>(600);
        public ArrayList<String> latit =  new ArrayList<>();
        public ArrayList<String> longi =  new ArrayList<>();

        private DrawerLayout mDrawerLayout;
        private ActionBarDrawerToggle mToggle;

        Fragment fragment = null;
        Fragment fragment2 = new type2_fragment();

        //language toggle
        private ToggleButton langToggle;
      //  private Boolean spanish = false;

        // Google API client stuff
        final HttpTransport transport = AndroidHttp.newCompatibleTransport();
        final JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        GoogleCredential credential;
        Fusiontables fclient;
       // Sheets sclient;

        protected static final String TAG = "MapsActivity";

        String timeOfResourceTable;
        String timeOfBusTable;
        LocalDateTime rawTimeOfBusTable;
        LocalDateTime rawTimeOfResourceTable;

        //SQLITE DATABASE SETUP add db to assets folder ~BC/app/build/generated/assets/databases

        DBAdapter db;
        boolean successCreatingDBKey = false;
        boolean successUpdatingDBKey=false;
        long lastCheckedMillis = 0;

        //Using Shared Preferences now so data will persist across sessions even if app is killed
        SharedPreferences pref;
        Editor editorE;
        FusedLocationProviderClient mFusedLocationClient;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            db = new DBAdapter(this);

            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.activity_maps);

            pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
       //   pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            editorE = pref.edit();


            mDrawerLayout =  findViewById(R.id.drawerLayout);

            NavigationView navigationView =  (NavigationView) findViewById(R.id.nav_view);
            navigationView.setItemIconTintList(null);
            navigationView.setNavigationItemSelectedListener(this);

            mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
            mDrawerLayout.addDrawerListener(mToggle);
            mToggle.syncState();
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);


            //M is Marshmallow.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
             checkLocationPermission();
            }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            myDialog = new Dialog(this);
            myDialog.setContentView(R.layout.custompopup);
            myDialog3 = new Dialog(this);
            myDialog1 = new Dialog(this);


        //get header of nav
            View header = navigationView.getHeaderView(0);
        //get menu of nav
            Menu menu = navigationView.getMenu();
        //get menu items
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
            final MenuItem nav_viewall = menu.findItem(R.id.nav_viewall);


        //Language Toggle
            langToggle = (ToggleButton) header.findViewById(R.id.langToggle);

        //from the last state of the language button
            langToggle.setChecked(pref.getBoolean("langToggle",false));
            if(pref.getBoolean("spanish",false)){
                nav_education.setTitle("Educación");
                nav_employment.setTitle("Empleo");
                nav_family.setTitle("Familia");
                nav_financial.setTitle("Financiero");
                nav_food.setTitle("Comida");
                nav_health.setTitle("Salud");
                nav_housing.setTitle("Alojamiento");
                nav_legal.setTitle("Legal");
                nav_lgbtq.setTitle("LGBTQ");
                nav_transportation.setTitle("Transporte");
                nav_veteran.setTitle("Veterano");
                nav_viewall.setTitle("Ver todo");
            } else {
            // The toggle is set to english
                nav_education.setTitle("Education");
                nav_employment.setTitle("Employment");
                nav_family.setTitle("Family");
                nav_financial.setTitle("Financial");
                nav_food.setTitle("Food");
                nav_health.setTitle("Health");
                nav_housing.setTitle("Housing");
                nav_legal.setTitle("Legal");
                nav_lgbtq.setTitle("LGBTQ");
                nav_transportation.setTitle("Transportation");
                nav_veteran.setTitle("Veteran");
                nav_viewall.setTitle("View All");
            }

            langToggle.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v){
                clearMap();
                if(langToggle.isChecked())
                {
                    //setting language
                    editorE.putBoolean("spanish", true);
                    editorE.putBoolean("langToggle",true);
                    editorE.commit();

                    nav_education.setTitle("Educación");
                    nav_employment.setTitle("Empleo");
                    nav_family.setTitle("Familia");
                    nav_financial.setTitle("Financiero");
                    nav_food.setTitle("Comida");
                    nav_health.setTitle("Salud");
                    nav_housing.setTitle("Alojamiento");
                    nav_legal.setTitle("Legal");
                    nav_lgbtq.setTitle("LGBTQ");
                    nav_transportation.setTitle("Transporte");
                    nav_veteran.setTitle("Veterano");
                    nav_viewall.setTitle("Ver todo");

                } else {
                    // The toggle is disabled
                    editorE.putBoolean("spanish", false);
                    editorE.putBoolean("langToggle",false);
                    editorE.commit();

                    nav_education.setTitle("Education");
                    nav_employment.setTitle("Employment");
                    nav_family.setTitle("Family");
                    nav_financial.setTitle("Financial");
                    nav_food.setTitle("Food");
                    nav_health.setTitle("Health");
                    nav_housing.setTitle("Housing");
                    nav_legal.setTitle("Legal");
                    nav_lgbtq.setTitle("LGBTQ");
                    nav_transportation.setTitle("Transportation");
                    nav_veteran.setTitle("Veteran");
                    nav_viewall.setTitle("View All");
                }
             }
            });


            autoCompleter();
            AutoCompleteTextView teView = findViewById(R.id.autoComp);
            ArrayAdapter<String> adapt = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, places);
            teView.setAdapter(adapt);

            onBackPressed();


            try {
                displayView(R.id.map);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            AndroidThreeTen.init(this);
            // location
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


           // ProgressDialog progressDialog= ProgressDialog.show(MapsActivity.this, "",
       //         "Updating. Please wait...", true);

        //every time the app is launched, we will check when the last time we updated the sql
        //db if we have updated within 24 hours, we wont update. if the last update is longer
        //than 24 hours then we will have to run an update to the table.
            boolean boo = false;
            try {
                boo = checkandExecuteUpdate();
                if(boo == true){
                //log successfully updated
                    Log.i(TAG, "!!! Log succesfully updated !!!");
                } else{
                //no db found, but one was created
                    Log.i(TAG, "!! No database was found, but one was created !!");
                }
            } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //creates and copies the bus ft to a local sqlite db
    public boolean createAndCopyBusFTtoDB(){
        //if we have to create the db and tables for the first time

        //creates the SQLite DB
        db = new DBAdapter(this);
        //add new entry to table
        db.open();

        //hard coding in each column name in case of corruption of fusion table
        String busColumnsNames = "SELECT stopName, route, times, latitude, longitude FROM ";
        String busSel = busColumnsNames + busTableId;
        List<List<Object>> ftbusT;
        try {
            //get the data from the fusion table
            Sqlresponse ftBusTable = query(busTableId, busSel);
            //  ValueRange ftBusTable = query(busTableId, busSel);
             // ftbusT = ftBusTable.getValues();


            //get each row of the fusion table
            ftbusT = ftBusTable.getRows();

            String itemToAdd, nullString = null;
            ArrayList<String> insertArray = new ArrayList<>();

            Boolean ftqueryres = ftBusTable.isEmpty();
            Log.i(TAG, "TEST!! IF SQLRESPONSE IS EMPTY OR NOT the result is = " + ftqueryres);
            if(ftqueryres == false) // if the query was succesful aka if the ft is not empty then create the DB
            {
                for (List<Object> poi : ftbusT) {
                    // inserting using an array, clear out old items of array
                    insertArray.clear();
                    //check each item, if null, write the string null into the db
                    for (int y = 0; y < 5; y++) {
                        itemToAdd = String.valueOf(poi.get(y));
                        if (itemToAdd == null)
                            insertArray.add(nullString);
                        else
                            insertArray.add(itemToAdd);
                    }
                    long resu = db.insertArrayInBusTable(insertArray);
                    Log.i(TAG, "TEST!! row to insert into the table " + insertArray
                            + "result from insert " + resu);
                }
                //Recording the time we create the db
                //    LocalDateTime now = LocalDateTime.now();
                //    rawTimeOfBusTable = now;

                //    String isoFormat = DateTimeFormatter.ISO_INSTANT.format(now.toInstant(ZoneOffset.UTC));
                //    timeOfBusTable = isoFormat;

            } else{
                Log.i(TAG, "query of fusion table failed!!!");
            }
        }catch (ExecutionException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        //close the database
        successCreatingDBKey = true;
        editorE.putBoolean("successCreatingBusDBKey", true);
        editorE.commit();
        return true;
    }

    //creates and copies the resource ft to a local sqlite db
    public boolean createAndCopyResourceFTtoDB(){
        //if we have to create the db and tables for the first time

        //creates the SQLite DB
        db = new DBAdapter(this);
        //add new entry to table
        db.open();

        //hard coding in each column name in case of corruption of fusion table
        String listOfColumnNames = "SELECT 'group', name, type, typeES, subtype, subtypeES, description, DesctriptionES, address, latitude, longitude, phone, hours, hoursES, link FROM ";
        String sel = listOfColumnNames + tableId;

        try {
            //call the fusion table here to get ALL the data
            //grabbing a copy of the whole fusion table
            Sqlresponse ftTable = query(tableId, sel);
           // ValueRange ftTable = query(tableId, sel);

            Boolean ftqueryres = ftTable.isEmpty();
            Log.i(TAG, "TEST!! IF SQLRESPONSE IS EMPTY OR NOT " + ftqueryres);
            if(ftqueryres == false) // if the query was succesful aka if the fusion table is not empty then create the DB
            {
                List<List<Object>> localT = ftTable.getRows();
                // List<List<Object>> localT = ftTable.getValues();

                ArrayList<String> insertItemsArr = new ArrayList<>();
                String item, nullString = null;
                //for each row of the fusion table
                for (List<Object> poi : localT) {
                    //clear out old items of array
                    insertItemsArr.clear();
                    //check each item, if null, write the string null into the db
                    for(int i=0; i<15; i++){
                        item = String.valueOf(poi.get(i));
                        if(item == null)
                            insertItemsArr.add(nullString);
                        else
                            insertItemsArr.add(item);
                    }
                    long resu= db.insertArrayRow(insertItemsArr);
                    Log.i(TAG, "TEST!! row to insert into the table " + insertItemsArr
                            + " result from the insert is " + resu);

                    //log a local variable to keep track of when we create/update the local DB
                    //    LocalDateTime now = LocalDateTime.now();
                    //    String isoFormat = DateTimeFormatter.ISO_INSTANT.format(now.toInstant(ZoneOffset.UTC));
                    //    timeOfResourceTable = isoFormat;
                    //    rawTimeOfResourceTable = now;

                }
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        //close the database
        successCreatingDBKey = true;
        editorE.putBoolean("successCreatingResourceDBKey", true);
        editorE.commit();
        return true;
    }


    public boolean updateTheBusDBTable(){
        db.open();
        //hard coding in each column name in case of corruption of fusion table
        String busColumnsNames = "SELECT stopName, route, times, latitude, longitude FROM ";
        String busSel = busColumnsNames + busTableId;
        try {
            //get the data from the fusion table
            Sqlresponse localBusTable = query(busTableId, busSel);
            //ValueRange localBusTable = query(busTableId, busSel);

            Boolean locqueryres = localBusTable.isEmpty();

            //checking if the number of rows differ between ft and local sql table.
            //if the ft has less rows than sql, duplicate rows will be added to make rows match
            //so we should just create a new table if the rows differ to avoid unintentional dups

            // getting number of rows in sqlite table
            long numSqlBus = db.numEntries(sqlBusTableId);
            //number of rows in fusion table
            String getCountString = "SELECT COUNT() FROM " + busTableId;

            Sqlresponse numFTBus = query(busTableId, getCountString);

           // ValueRange numFTBus = query(busTableId, getCountString);
            String numFtRows = numFTBus.get("rows").toString();
            long numFTBusRows = Long.parseLong(numFtRows.replaceAll(".*\\[|\\].*", ""));


            if (numFTBusRows == numSqlBus) {  //continue with update as normal..
                if (!locqueryres) // if the query was succesful aka if the localtable is not empty then create the DB
                {
                    String item, dbitem = null;
                    int ind = 0;
                    boolean updateFlag = false;
                    ArrayList<String> updateArr = new ArrayList<>();

                     List<List<Object>> locbusT = localBusTable.getRows(); //get each row of the fusion table
                    //List<List<Object>> locbusT = localBusTable.getValues(); //

                    List<String> dbBT = db.getAllRowsBusList(); // gets all db results
                    Log.i(TAG, "TEST!! database bus table results is " + dbBT);

                    for (List<Object> poi : locbusT) {
                        updateArr.clear();
                        updateFlag = false;
                        for (int y = 0; y < 5; y++) {
                            item = String.valueOf(poi.get(y)); // gets token of ft row
                            try {
                                dbitem = dbBT.get(ind++);     // gets token of sql row
                            } catch (NullPointerException e) {
                            }
                            updateArr.add(item); // prep for an update by adding ft items
                            if (item != dbitem) { // if the ft and db differ set the flag to update at end of row
                                updateFlag = true;
                            }
                        }
                        if (updateFlag == true) {
                            //need to send the bus id
                            boolean resu = db.updateRowBusArray(updateArr);
                            Log.i(TAG, "TEST!! row to insert into the table " + updateArr
                                    + " result from the insert is " + resu);
                        }
                    }
                    //Recording the time we updated the db last
                    //    LocalDateTime now = LocalDateTime.now();
                    //    rawTimeOfBusTable = now;
                    //    String isoFormat = DateTimeFormatter.ISO_INSTANT.format(now.toInstant(ZoneOffset.UTC));
                    //    timeOfBusTable = isoFormat;

                } else {
                    Log.i(TAG, "query of fusion table failed!!!");
                }
                editorE.putBoolean("successUpdatingBusDBKey", true);
                editorE.commit();
             }
            else if (numFTBusRows != numSqlBus) { // the number of rows is different
                Log.i(TAG, "TEST!! number of rows differ!!!");

                  /* doing this because the update would likely be off when comparing the rows
                   and the update will add dups to match num of rows resulting in dups and
                   unnecessary/additional checks */

                //delete all rows of local table
                db.deleteAll(sqlBusTableId);
                //call a new create method
                createAndCopyBusFTtoDB();
            }

            } catch(ExecutionException e){
                e.printStackTrace();
                return false;
            } catch(InterruptedException e){
                e.printStackTrace();
                return false;
            }

        db.close();
        return true;
    }

    public boolean updateTheResourceDBTable(){
        db.open();
        //using 'group' instead of groupName, bc its for the FT not sqlite and keep misspelled desctriptionES bc ft has it
        String listOfColumnNames = "SELECT 'group', name, type, typeES, subtype, subtypeES, description, DesctriptionES, address, latitude, longitude, phone, hours, hoursES, link FROM ";
        String sel = listOfColumnNames + tableId;

        try {
            //call the fusion table here to get ALL the data
            //grabbing a copy of the whole fusion table
            Sqlresponse localTable = query(tableId, sel);

          //  ValueRange localTable = query(tableId, sel);

            Boolean locqueryres = localTable.isEmpty();
            Log.i(TAG, "TEST!! IF SQLRESPONSE IS EMPTY OR NOT " + locqueryres);

            //checking if the number of rows differ between ft and local sql table.
            //if the ft has less rows than sql, duplicate rows will be added to make rows match
            //so we should just create a new table if the rows differ to avoid unintentional dups
            //number of rows in sqlite table
            long numSqlRes = db.numEntries(sqlTableId);
            //number of rows in ft
            String getCountString = "SELECT COUNT() FROM " + tableId;

            Sqlresponse numFTRes = query(tableId, getCountString);

           // ValueRange numFTRes = query(tableId, getCountString);
            String numFtRowsRes = numFTRes.get("rows").toString();
            long numFTResRows = Long.parseLong(numFtRowsRes.replaceAll(".*\\[|\\].*", ""));

            if (numFTResRows == numSqlRes) {  //continue with update as normal..
                Log.i(TAG, "TEST!! number of rows are equal!!!");
                if (!locqueryres) // if the query was succesful aka if the localtable is not empty then create the DB
                {
                    String item, dbitem, nullString = null;
                    int ind = 0;
                    boolean updateFlag = false;
                    ArrayList<String> updateArr = new ArrayList<>();
                    List<List<Object>> localT = localTable.getRows(); // gets fusion table results

                   // List<List<Object>> localT = localTable.getValues(); // gets fusion table results
                    List<String> dbT = db.getAllRowsList(); // gets all db results
                    Log.i(TAG, "TEST!! database bus table results is " + dbT);

                    for (List<Object> poi : localT) { //for each row of the fusion table
                        //clear out old items of array & reset flag
                        updateArr.clear();
                        updateFlag = false;
                        for (int i = 0; i < 16; i++) {
                            item = String.valueOf(poi.get(i)); // gets token of ft row
                            dbitem = dbT.get(ind++);     // gets token of sql row
                            updateArr.add(item); // prep for an update by adding ft items
                            if (item != dbitem) { // if the ft and db differ set the flag to update at end of row
                                updateFlag = true;
                            }
                        }
                        if (updateFlag == true) {
                            boolean resu = db.updateRowResourceArray(updateArr);
                            Log.i(TAG, "TEST!! row to insert into the table " + updateArr
                                    + " result from the insert is " + resu);
                        }
                    }
                    //log a local variable to keep track of when we updated the local DB last
                    //    LocalDateTime now = LocalDateTime.now();
                    //    String isoFormat = DateTimeFormatter.ISO_INSTANT.format(now.toInstant(ZoneOffset.UTC));
                    //    timeOfResourceTable = isoFormat;
                    //    rawTimeOfResourceTable = now;

                }
            }else if(numFTResRows != numSqlRes){ // number of rows differ
                Log.i(TAG, "TEST!! number of rows differ!!!");

                /* doing this because the update would likely be off when comparing the rows
                   and the update will add dups to match num of rows resulting in dups and
                   unnecessary/additional checks */

                //delete all rows of table
                db.deleteAll(sqlTableId);
                //call create method to make new table from scratch
                createAndCopyResourceFTtoDB();
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        editorE.putBoolean("successUpdatingResourceDBKey", true);
        editorE.commit();
        db.close();
        return true;
    }

    public boolean checkandExecuteUpdate() throws IOException {
       // Toast.makeText(MapsActivity.this,
        //        "Update in Progress! Please wait", Toast.LENGTH_LONG).show();
      // pop up needs to show on launch of app that the update is in progress. before the map shows

      //  ProgressDialog.show(this, "Loading", "Wait while loading...");

        Log.i(TAG, "!! TEST!! check and execute  " );

        db.open();
        long sizeOfTable = db.numEntries(sqlTableId);
        long sizeOfBusTable = db.numEntries(sqlBusTableId);
        Log.i(TAG, "!! TEST!! size of bus table is !!!!  " + sizeOfBusTable);

        boolean DBBusUpdateFlag = false;
        boolean DBResourceUpdateFlag = false;

        if(sizeOfTable > 0){
            //need to do an update for resource table
            DBResourceUpdateFlag = true; // aka there is data in the table already
        } // otherwise we need to do a create
        if(sizeOfBusTable > 0 ){  // if = 0 need to a create not update bc table is empty
            //need to do an update for bus bus table
            DBBusUpdateFlag = true;
            Log.i(TAG, "!! TEST should not be in hereeeee !!!! db update flag is   " + DBBusUpdateFlag);
        }

        //for the bus table
        if((pref.getBoolean("successCreatingBusDBKey", false)) && DBBusUpdateFlag) // db was created successfully
        { // tables exist already and there is data in it...

            Calendar cal = Calendar.getInstance();
            cal.clear(Calendar.HOUR);
            cal.clear(Calendar.HOUR_OF_DAY);
            cal.clear(Calendar.MINUTE);
            cal.clear(Calendar.SECOND);
            cal.clear(Calendar.MILLISECOND);

            long now = cal.getTimeInMillis();
            long diffMillis = now - pref.getLong("lastCheckedMillis", 0);
            if( diffMillis >= (3600000  * 24) ) {
                // store now (in shared prefs)
                editorE.putLong("lastCheckedMillis", now);
                editorE.commit();

                lastCheckedMillis = now;
                boolean res1 = updateTheBusDBTable();
                // do the check
            } else {
                // too early. no update needed
            }
            // LocalDateTime timeOfEditResourceFT = null;
            // DateTime lastEditTime;
            // timeOfEditResourceFT, timeOfEditBusFT
            //compare to the local  rawtimeOfBusTable, rawtimeOfResourceTable.
        }
        else{ //table not created then we need to create one OR table is empty!!
            //sql db doesnt exist: false: then call createAndCopy__FTtoDB();
            Log.i(TAG, "!! TEST should be getting to this point!!!   ");
            boolean tfresult = createAndCopyBusFTtoDB();
            return false;
        }

        //for the resource table
        if((pref.getBoolean("successCreatingResourceDBKey", false)) && DBResourceUpdateFlag) // db was created successfully
        { // tables exist already...

            Calendar cal = Calendar.getInstance();
            cal.clear(Calendar.HOUR);
            cal.clear(Calendar.HOUR_OF_DAY);
            cal.clear(Calendar.MINUTE);
            cal.clear(Calendar.SECOND);
            cal.clear(Calendar.MILLISECOND);

            long now = cal.getTimeInMillis();
            long diffMillis = now - pref.getLong("lastCheckedMillis", 0);
            if( diffMillis >= (3600000  * 24) ) {
                // store now (in shared prefs)
                editorE.putLong("lastCheckedMillis", now);
                editorE.commit();

                lastCheckedMillis = now;
                boolean res2= updateTheResourceDBTable();
                // do the check
            } else {
                // too early. no update needed
            }
        }
        else{ //table not created then we need to create one OR table is empty!!
            //sql db doesnt exist: false: then call createAndCopy___FTtoDB();
            Log.i(TAG, "!! TEST should be getting to this point!!!   ");
            boolean tfresult = createAndCopyResourceFTtoDB();
            return false;
        }
        db.close();
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(MenuItem item) {

        try {
            displayView(item.getItemId());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch (ExecutionException e){
            e.printStackTrace();
        }
        return true;
    }

    public ArrayList<String> types(String group) {
        ArrayList<String> types =  new ArrayList<>();
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

      //  sclient = new Sheets.Builder(transport,jsonFactory, credential).setApplicationName("TestMap/1.0").build();

      //  try {

            String typ;
          //  String q = "SELECT 'group', type, typeES FROM "+tableId;

           // Sqlresponse result = null;
           // result = query(tableId,q);
           // List<List<Object>> rows1 = result.getRows();
            db.open();
            String q = "SELECT groupName, type, typeES FROM "+ sqlTableId;
            List<List<Object>> rows = db.rawArrQuery(q);

            Log.i(TAG, "!! TEST!! query results give us !!!!  " + rows);

            types.clear();
            for (List<Object> poi : rows) {
                String check = (String) poi.get(0);
                if(group.equals(check)){
                    //if(!spanish){
                        typ = (String) poi.get(1);
                        if(!types.contains(typ)){
                            types.add(typ);
                        }

                }
            }

        db.close();
        return types;
    }

    public ArrayList<ArrayList<String>> subTypes(ArrayList<String> type) {

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

       // sclient = new Sheets.Builder(transport, jsonFactory, credential).setApplicationName("TestMap/1.0").build();



        ArrayList<ArrayList<String>> subtypes = new ArrayList<ArrayList<String>>();


            String subtyp;
            db.open();
            String q = "SELECT type, subtype, subtypeES FROM " + sqlTableId;
            List<List<Object>> rows = db.rawArrQuery(q);

            Log.i(TAG, "!! TEST!! query subTypes give us !!!!  " + rows);


            //result = query(tableId, q);
            //List<List<Object>> rows = result.getRows();


            for (int i = 0; i < type.size(); i++) {
                for (List<Object> poi : rows) {
                    String check = (String) poi.get(0);
                    if (type.get(i).equals(check)) {
                        //if (!spanish) {
                            subtyp = (String) poi.get(1);
                            if(subtypes.size() > i){
                                if (!subtypes.get(i).contains(subtyp)) {
                                    subtypes.get(i).add(subtyp);
                                    Log.i(TAG, "subType " + poi.get(1));
                                }
                            }
                            else{
                                subtypes.add(new ArrayList<String>());
                                subtypes.get(i).add(subtyp);
                            }
                        /*} else {
                            subtyp = (String) poi.get(2);
                            if(subtypes.size() > i){
                                if (!subtypes.get(i).contains(subtyp)) {
                                    subtypes.get(i).add(subtyp);
                                    Log.i(TAG, "subType " + poi.get(2));
                                }
                            }
                            else{
                                subtypes.add(new ArrayList<String>());
                                subtypes.get(i).add(subtyp);
                            }
                        }*/
                    }
                }
            }
       /* } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        db.close();
        return subtypes;
    }

    public ArrayList<Spanned> locations(String subtype){
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

        //sclient = new Sheets.Builder(transport,jsonFactory,credential).setApplicationName("TestMap/1.0").build();

        ArrayList<Spanned> locations = new ArrayList<Spanned>();


            String name, address;
          //  String q = "SELECT subtype, subtypeES, name, address, latitude, longitude FROM "+tableId;
            String q = "SELECT subtype, subtypeES, name, address, latitude, longitude FROM "+sqlTableId;
            db.open();
            List<List<Object>> rows = db.rawArrQuery(q);

            Log.i(TAG, "!! TEST!! query Locations... give us !!!!  " + rows);


        //result = query(tableId,q);
            //List<List<Object>> rows = result.getRows();

            for (List<Object> poi : rows) {
                String check = (String) poi.get(0);
                    if(subtype.equalsIgnoreCase(check)){
                        name = (String) poi.get(2);
                        address = (String) poi.get(3);
                        if(!locations.contains(name)){
                            locations.add(Html.fromHtml("<b>Name:</b> " + name + "<br><b>Address:</b> " + address));
                            longi.add(""+ poi.get(5));
                            latit.add(""+ poi.get(4));
                        }
                    }
            }

        db.close();
        return locations;
    }

    public void ShowBusRoute(View v){
         //using boolean rather than an int fixes crash. resolved bug when you double click the bus button
          if(busClick==false){
            try {
                db.open();
                String q = "SELECT stopName, route, latitude, longitude FROM " + sqlBusTableId + ";";
                List<List<Object>> rows = db.rawArrQuery(q);
                Log.i(TAG, "!! TEST!! query lat lngs res give us !!!!  " + rows);
                for (List<Object> poi: rows){
                    String g = poi.get(1).toString(); // get route name

                    LatLng ltlng = new LatLng((Double.valueOf(poi.get(2).toString())), (Double.valueOf(poi.get(3).toString())));
                     mMap.addMarker(new MarkerOptions()
                            .position(ltlng)
                             .title((String)poi.get(0)) // title is the name of stop
                            .icon(busiconRetrieve(g))) // get the route icon based on kind
                            .setTag(g); //tag is the route
                     //setting tag to the route name
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(ltlng));
                }
                db.close();

                //adds bus line to show route.
                kml = new KmlLayer(mMap,R.raw.busr,getApplicationContext());
                kml.addLayerToMap();
               // kml1 = new KmlLayer(mMap,R.raw.busstop,getApplicationContext());
               // kml1.addLayerToMap();

                busClick = true;
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            clearMap();
            busClick = false;
        }
    }
    int color;
    @SuppressLint("ResourceAsColor")
    public void buspopup(View view, Marker mm) throws ExecutionException, InterruptedException {

        myDialog1.setContentView(R.layout.buspopup);
        TextView txtclose;
        Sqlresponse result;
        Sqlresponse result1;
       // String busstop = mm.getTitle().toLowerCase();

       // double busstopLat = mm.getPosition().latitude;
       // double busstopLng = mm.getPosition().longitude;
        String busstoproute = (String) mm.getTag();
        String busstopName = (String) mm.getTitle();

        db.open();
        if(busstoproute != null){

            //get all stops on the route.

             String q2 = "SELECT stopName, times FROM " + sqlBusTableId + " WHERE route LIKE '" + busstoproute + "';";
             List<List<Object>> rows2 = db.rawArrQuery(q2);
             Log.i(TAG, "!! TEST!! query rows 2 gives us!!  " + rows2);


            // if(result1.getRows() !=null){
              if(rows2 != null){

                  String busRouteLink = "<a href='http://www.transitorange.info/bus-services/local.html'> Click here for more info</a>";
                  if(busstoproute.equals("mainline")){
                      busRouteLink = "<a href='http://www.transitorange.info/bus-services/Mainline%20schedule%202016.pdf'> Click here for more info</a>";
                  }
                  else if((busstoproute.equals("route 1")) || (busstoproute.equals("route 2")) || (busstoproute.equals("route 3")) || (busstoproute.equals("route 4"))){
                      busRouteLink = "<a href='http://www.transitorange.info/bus-services/Hudson%20Transit%20Lines%20Schedule.pdf'> Click here for more info</a>";
                  }
                  else if((busstoproute.equals("broadway")) || (busstoproute.equals("southside")) || (busstoproute.equals("northside")) || (busstoproute.equals("crosstown")))
                  {
                      busRouteLink = "<a href='https://leprechaunlines.com/wp-content/uploads/2018/06/0002-1024x622.jpg'> Click here for more info</a>";
                  }
                  else if(busstoproute.equals("newbMid")){
                      busRouteLink = "<a href='https://web.coachusa.com/shortline/ss.details.asp?action=Lookup&c1=Newburgh&s1=NY&c2=Middletown&s2=NY&resultId=75555&order=&dayFilter=&scheduleChoice=&sitePageName=&nt=%2Fshortline%2Findex%2Easp&cbid=589490537850'>Click here for more info</a>";
                  }


                TableLayout stk = (TableLayout) myDialog1.findViewById(R.id.table_bus);
                TableRow tbrow0 = new TableRow(this);
                TableRow tbrow3 = new TableRow(this);

                TableRow tbrow00 = new TableRow(this);
                TableRow tbrow33 = new TableRow(this);
                TextView moreinfo = new TextView(this);

               // moreinfo.setText("Click Here for more info: " + busRouteLink);
               // Linkify.addLinks(moreinfo, Linkify.ALL);

                moreinfo.setText(Html.fromHtml(busRouteLink));
                moreinfo.setMovementMethod(LinkMovementMethod.getInstance());

                tbrow00.addView(moreinfo);
                //formatting
                  TextView yy = new TextView(this);
                  yy.setText(" ");
                  TextView xx = new TextView(this);
                  xx.setText("");
                  xx.setHeight(7);
                  tbrow00.addView(yy);
                  tbrow33.addView(xx);
                  TextView st = new TextView(this);
                  st.setText("");
                  tbrow00.addView(st);
                  stk.addView(tbrow00);
                  stk.addView(tbrow33);





                TextView stopname = new TextView(this);
                stopname.setText(" Stop Names ");
                stopname.setBackgroundColor(0xffcccccc);
                tbrow0.addView(stopname);

                TextView y = new TextView(this);
                y.setText(" ");

                TextView x = new TextView(this);
                x.setText("");
                x.setHeight(7);

                tbrow0.addView(y);
                tbrow3.addView(x);

                TextView stoptime = new TextView(this);
                stoptime.setText(" Stop Times  ==>");
                stoptime.setBackgroundColor(0xffcccccc);
                tbrow0.addView(stoptime);


                stk.addView(tbrow0);
                stk.addView(tbrow3);


                  Log.i(TAG, "!! TEST!! rows 2 .size gives us !!  " + rows2.size());

                  for(int i = 0; i < rows2.size();i++){
                    TableRow tbrow = new TableRow(this);
                    TableRow tbrow2 = new TableRow(this);
                    //
                    String g = (String)rows2.get(i).get(0); // g is the marker/stop name
                    if(busstopName.equalsIgnoreCase(g)){
                         color = 0xff888888;
                    }else{
                         color = 0xffcccccc;
                    }

                    TextView t1v = new TextView(this);
                    //
                    t1v.setText((String)rows2.get(i).get(0));
                    t1v.setGravity(Gravity.CENTER);
                    t1v.setBackgroundColor(color);
                    t1v.setPadding(15,0,15,0);


                    TextView t3v = new TextView(this);
                    t3v.setText(" ");
                    t3v.setGravity(Gravity.CENTER);

                    TextView t2v = new TextView(this);
                    // 1
                    t2v.setText((String)rows2.get(i).get(1));
                    t2v.setGravity(Gravity.CENTER);
                    t2v.setPadding(15,0,15,0);
                    t2v.setBackgroundColor(color);

                    tbrow.addView(t1v);
                    tbrow.addView(t3v);
                    tbrow.addView(t2v);
                    TextView t4v = new TextView(this);
                    t4v.setText("");
                    t4v.setGravity(Gravity.CENTER);
                    t4v.setHeight(7);
                    tbrow2.addView(t4v);
                    stk.addView(tbrow);
                    stk.addView(tbrow2);
                }

            }
        }

        txtclose = (TextView) myDialog1.findViewById(R.id.txtclose1);
           myDialog1.show();
        txtclose.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                myDialog1.dismiss();
            }
        });
        db.close();
    }

    //This opens the custompopup.xml with the streetview and other info about the marker.
    public void InfoWindow(View v, String lat, String lon) throws ExecutionException, InterruptedException {
        TextView txtclose;


        final StreetViewPanoramaFragment streetViewPanoramaFragment =
                (StreetViewPanoramaFragment) getFragmentManager()
                        .findFragmentById(R.id.streetviewpanorama);
        streetViewPanoramaFragment.getStreetViewPanoramaAsync(this);


        TextView name = (TextView)myDialog.findViewById(R.id.name);
        TextView description = (TextView)myDialog.findViewById(R.id.description);
        TextView address = (TextView)myDialog.findViewById(R.id.address);
        TextView phone = (TextView)myDialog.findViewById(R.id.phone);
        TextView hours = (TextView)myDialog.findViewById(R.id.hours);
        TextView link = (TextView)myDialog.findViewById(R.id.link);


    //    Sqlresponse result;
    //    String q = "SELECT name, description, DesctriptionES, address, phone , hours , hoursES, link  FROM "+tableId+" WHERE latitude = "+lat+" AND longitude = "+lon ;
        db.open();
        String q = "SELECT name, description, descriptionES, address, phone , hours , hoursES, link  FROM "+sqlTableId+" WHERE latitude LIKE '"+lat+"' AND longitude LIKE '"+lon +"';";
        List<List<Object>> rows = db.rawArrQuery(q);

       // result = query(tableId,q);
       // List<List<Object>> rows = result.getRows();


        //english
      //  if(!spanish){
        if(!pref.getBoolean("spanish", false)){
            name.setText("Name: "+ rows.get(0).get(0));
            if(rows.get(0).get(1).toString().isEmpty()){
                description.setText("Description: N/A");
            }else{
                description.setText("Description: " + rows.get(0).get(1));
            }

            if(rows.get(0).get(3).toString().isEmpty()){
                address.setText("Address: N/A");
            }else{
                address.setText("Address: "+ rows.get(0).get(3));
            }

            if(rows.get(0).get(4).toString().isEmpty()){
                address.setText("Phone: N/A");
            }else{
                phone.setText("Phone: " +rows.get(0).get(4));
                Linkify.addLinks(phone, Linkify.PHONE_NUMBERS);
            }

            if(rows.get(0).get(5).toString().isEmpty()){
                hours.setText("Hours: N/A");
            }else{
                hours.setText("Hours: " +rows.get(0).get(5));
            }

            if(rows.get(0).get(6).toString().isEmpty()){
                link.setText("Link: N/A");
            }else{
                link.setText("Link: " + rows.get(0).get(7));
                Linkify.addLinks(link, Linkify.WEB_URLS);

            }
        }
        //spanish
        else{
            name.setText("Nombre: "+ rows.get(0).get(0));
            if(rows.get(0).get(1).toString().isEmpty()){
                description.setText("Descripción: N/A");
            }else{
                description.setText("Descripción: " + rows.get(0).get(2));
            }

            if(rows.get(0).get(3).toString().isEmpty()){
                address.setText("Dirección: N/A");
            }else{
                address.setText("Dirección: "+ rows.get(0).get(3));
            }

            if(rows.get(0).get(4).toString().isEmpty()){
                address.setText("Teléfono: N/A");
            }else{
                phone.setText("Teléfono: " +rows.get(0).get(4));
                Linkify.addLinks(phone, Linkify.PHONE_NUMBERS);
            }

            if(rows.get(0).get(5).toString().isEmpty()){
                hours.setText("Horas: N/A");
            }else{
                hours.setText("Horas: " +rows.get(0).get(6));
            }

            if(rows.get(0).get(6).toString().isEmpty()){
                link.setText("Enlazar: N/A");
            }else{
                link.setText("Enlazar: " + rows.get(0).get(7));
                Linkify.addLinks(link, Linkify.WEB_URLS);

            }
        }

        txtclose = (TextView) myDialog.findViewById(R.id.txtclose);
        myDialog.show();
        txtclose.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                myDialog.hide();
            }
        });
    db.close();
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

    public void displayView(int viewId) throws ExecutionException, InterruptedException {

        String title = getString(R.string.app_name);
        viewIsAtHome = true;

        switch (viewId) {

            case R.id.nav_education:
                fragment = new type1_fragment(types("education"),subTypes(types("education")), fragment2);
                title = "Education";
               // clearMap();
                populateMapFromFusionTables("education");
                viewIsAtHome = false;
                break;
            case R.id.nav_employment:
                fragment = new type1_fragment(types("employment"),subTypes(types("employment")), fragment2);
                title = "Employment";
             //   clearMap();
                populateMapFromFusionTables("employment");
                viewIsAtHome = false;
                break;
            case R.id.nav_family:
                fragment = new type1_fragment(types("family"),subTypes(types("family")), fragment2);
                title = "Family";
             //   clearMap();
                populateMapFromFusionTables("family");
                viewIsAtHome = false;
                break;
            case R.id.nav_financial:
                fragment = new type1_fragment(types("financial"),subTypes(types("financial")), fragment2);
                title = "Financial";
              //  clearMap();
                populateMapFromFusionTables("financial");
                viewIsAtHome = false;
                break;
            case R.id.nav_food:
                fragment = new type1_fragment(types("food"),subTypes(types("food")), fragment2);
                title = "Food";
              //  clearMap();
                populateMapFromFusionTables("food");
                viewIsAtHome = false;
                break;
            case R.id.nav_health:
                fragment = new type1_fragment(types("health"),subTypes(types("health")), fragment2);
                title = "Health";
            //    clearMap();
                populateMapFromFusionTables("health");
                viewIsAtHome = false;
                break;
            case R.id.nav_housing:
                fragment = new type1_fragment(types("housing"),subTypes(types("housing")), fragment2);
                title = "Housing";
            //    clearMap();
                populateMapFromFusionTables("housing");
                viewIsAtHome = false;
                break;
            case R.id.nav_legal:
                fragment = new type1_fragment(types("legal"),subTypes(types("legal")), fragment2);
                title = "Legal";
              //  clearMap();
                populateMapFromFusionTables("legal");
                viewIsAtHome = false;
                break;
            case R.id.nav_lgbtq:
                fragment = new type1_fragment(types("lGBTQ"),subTypes(types("lGBTQ")), fragment2);
                title = "LGBTQ";
             //   clearMap();
                populateMapFromFusionTables("lgbtq");
                viewIsAtHome = false;
                break;
            case R.id.nav_transportation:
                fragment = new type1_fragment(types("transportation"),subTypes(types("transportation")), fragment2);
                title = "Transportation";
            //    clearMap();
                populateMapFromFusionTables("transportation");
                viewIsAtHome = false;
                break;
            case R.id.nav_veteran:
                fragment = new type1_fragment(types("veteran"),subTypes(types("veteran")), fragment2);
                title = "Veteran";
            //    clearMap();
                populateMapFromFusionTables("veteran");
                viewIsAtHome = false;
                break;
            case R.id.nav_viewall:
                populateMapFromFusionTables("all");
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
    public void onBackPressed(){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);


        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if (!viewIsAtHome) {
            try {displayView(R.id.drawerLayout);  }
            catch (ExecutionException e){e.printStackTrace();}
            catch(InterruptedException e) {  e.printStackTrace(); }

        }
        else {
            moveTaskToBack(true);
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
            //setting the style of the map to retro. defined in a json file
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(
                    this, R.raw.style_json));
            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationButtonClickListener(this);

            kml();
        }
        mMap.setOnMarkerClickListener(this);

        //start with map at center of Newburgh, NY
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(41.41698, -74.32525), 9));
    }

    //Refresh button method
    public void home(View v) {
        if (v.getId() == R.id.refresh) {
            clearMap();
            //start with map at center of Newburgh, NY
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(41.41698, -74.32525), 9));
            //reset the title
            getSupportActionBar().setTitle(getString(R.string.app_name));

            subClose(v);
            sub2Close(v);
            subClose(v);
        }
        //loc
        onMapReady(mMap);
    }

    //Close submenu button
    public void subClose(View v){
        Log.d(TAG, "!! TEST !! subclose called ");
        //close fragment
        if (fragment != null) // need check to avoid null error
        {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.addToBackStack(null);
            ft.remove(fragment);
            ft.commit();
            //reset title
            getSupportActionBar().setTitle(getString(R.string.app_name));
         }
    }

    //Close 2nd submenu
    public void sub2Close(View v){
        //close fragment
        if (fragment2 != null) // need check to avoid null error
        {
            Log.d(TAG, "!! TEST !! subclose2 not null ");
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.addToBackStack(null);
            ft.remove(fragment2);
            ft.commit();
            //reset title
            getSupportActionBar().setTitle(getString(R.string.app_name));
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
        //    sclient = new Sheets.Builder(transport,jsonFactory,credential).setApplicationName("TestMap/1.0").build();



                db.open();
                String q2 = "SELECT name, latitude, longitude, groupName FROM "+ sqlTableId;
                List<List<Object>> rows = db.rawArrQuery(q2);

                for (List<Object> poi : rows) {
                    String name = (String) poi.get(0);

                    if( location.equalsIgnoreCase(name)){


                        String lat = poi.get(1).toString();
                        String lng = poi.get(2).toString();
                        LatLng latLng = new LatLng(Double.valueOf(lat), Double.valueOf(lng));
                        String group = (String) poi.get(3);

                        clearMap();
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(name)
                                .icon(iconRetrieve(group)))
                                //title should be marker name
                                //tag can be g
                                .setTag("places");
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                        break;
                    }
                }
        }
        db.close();
    }


    protected synchronized void buildGoogleApiClient() {
        client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

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
                   // if(!spanish){
                    if(!pref.getBoolean("spanish", false)){
                        Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(this, "Permiso denegado", Toast.LENGTH_LONG).show();
                    }

                }
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        //check if we want to actually clear the map when we try to get the user location
        clearMap();

        try {
            Task<Location> locTest = mFusedLocationClient.getLastLocation().addOnSuccessListener(
               this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        // Logic to handle location object
                        Log.d(TAG, "button clicked and got current location: lat: " + location.getLatitude() + ", lng: " + location.getLongitude());

                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latLng);
                        markerOptions.title("LocationMarker");
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

                        if(!pref.getBoolean("spanish", false)){
                            markerOptions.title("Current Location");
                        } else {
                            markerOptions.title("Ubicación actual");
                        }

                        mMap.addMarker(markerOptions).setTag("LocationMarker");

                        //add circle with 1/4 radius around current location
                        mMap.addCircle(new CircleOptions()
                                .center(latLng)
                                .radius(402.336)
                                .strokeColor(Color.LTGRAY));

                        //add nearby items to the map
                        populateMapCurrentLocation(location);

                        //update map to zoom to circle
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerOptions.getPosition(),15));
                    }
                }
            });

            }catch(SecurityException e){
                Log.e(TAG, "Security exception: " + e);
            }

                if (!pref.getBoolean("spanish", false)) {
                    Toast.makeText(this, "Services within a 1/4 mile of your current location.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Servicios dentro de 1/4 de milla de su ubicación.", Toast.LENGTH_LONG).show();
                }

            // Return false so that we don't consume the event and the default behavior still occurs
            // (the camera animates to the user's current position).

        return false;

    }

     @Override
     public void onLocationChanged(Location location) {
         lastLocation = location;

         if (currentLocationMarker != null) {
             currentLocationMarker.remove();
             //added
             // locationManager.removeUpdates((android.location.LocationListener) this);
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
       // sclient = new Sheets.Builder(transport,jsonFactory,credential).setApplicationName("TestMap/1.0").build();


        // try {

          //  Sqlresponse result = null;
          //  String q = "SELECT name  FROM "+tableId;
          //  result = query(tableId,q);
          //  List<List<Object>> rows = result.getRows();

            db.open();
            String q = "SELECT name FROM "+ sqlTableId;
            List<List<Object>> rows = db.rawArrQuery(q);


            places.clear();
            for (List<Object> poi : rows) {
                String name = (String) poi.get(0);
                places.add(name);
            }

            db.close();

       //} catch (NullPointerException e) {
       //     e.printStackTrace();
       // }

    }

    public com.google.android.gms.maps.model.BitmapDescriptor busiconRetrieve(String group ){
        switch(group) {
            case "broadway":
                return BitmapDescriptorFactory.fromResource(R.drawable.broadway);
            case "crosstown":
                return BitmapDescriptorFactory.fromResource(R.drawable.crosstown);
            case "mainline":
                return BitmapDescriptorFactory.fromResource(R.drawable.mainline);
            case "newbMid":
                return BitmapDescriptorFactory.fromResource(R.drawable.newbmid);
            case "northside":
                return BitmapDescriptorFactory.fromResource(R.drawable.northside);
            case "route 1":
                return BitmapDescriptorFactory.fromResource(R.drawable.route1);
            case "route 2":
                return BitmapDescriptorFactory.fromResource(R.drawable.route2);
            case "route 3":
                return BitmapDescriptorFactory.fromResource(R.drawable.route3);
            case "route 4":
                return BitmapDescriptorFactory.fromResource(R.drawable.route4);
            case "southside":
                return BitmapDescriptorFactory.fromResource(R.drawable.southside);
            default:
                return null;
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

    public void populateMapFromFusionTable(String subtype) {

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
      //  sclient = new Sheets.Builder(transport,jsonFactory,credential).setApplicationName("TestMap/1.0").build();


        //  try {

       //     Sqlresponse result = null;
       //     String q = "SELECT latitude, longitude, 'group', subtype FROM "+tableId;
       //     result = query(tableId,q);
       //     List<List<Object>> rows = result.getRows();
        db.open();
        String q = "SELECT name, latitude, longitude, groupName, subtype FROM "+sqlTableId;
        List<List<Object>> rows = db.rawArrQuery(q);

            Log.i(TAG, "Got " + rows.size() + " POIs from fusion tables.");

            if (mMap != null) {

                for (List<Object> poi : rows) {
                    String lat = poi.get(1).toString();
                    String lng = poi.get(2).toString();
                    Double latd = Double.valueOf(lat);
                    Double lngd = Double.valueOf(lng);
                    LatLng latLng = new LatLng(latd, lngd);

                    String name = (String) poi.get(0);
                    String g = (String) poi.get(3);
                    String st = (String) poi.get(4);

                    //used for populating map by subtype
                    if(subtype.equals(st)){
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(name)
                                .icon(iconRetrieve(g)))
                                //title should be marker name
                                //tag can be g
                                .setTag("places");
                    }
                }

            } else {
                Log.i(TAG, "mMap is null, not placing markers.");
            }

    /*    } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        db.close();
    }

   public void populateMapFromFusionTables(String group) {


            // TODO: to make credentialsJSON work, you need to browse to https://console.developers.google.com/iam-admin/serviceaccounts/
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
       //sclient = new Sheets.Builder(transport,jsonFactory,credential).setApplicationName("TestMap/1.0").build();


       db.open();
            //initalize incase group is null
            List<List<Object>> rows = new ArrayList<>();
            if(group.equals("all"))
            {   // query the db for all rows.
                String q = "SELECT name, latitude, longitude, groupName  FROM "+sqlTableId + ";";
                rows = db.rawArrQuery(q);
            }
            else if(!group.equals("all")){
                //group isnt all, then we just query the db for specific group needed...
                String q = "SELECT name, latitude, longitude, groupName  FROM "+sqlTableId + " WHERE groupName LIKE '"+ group +"' ;";
                rows = db.rawArrQuery(q);
            }
                if (mMap != null) {
                    for (List<Object> poi : rows) {

                        Double latt = new Double(poi.get(1).toString());
                        Double lonn = new Double(poi.get(2).toString());
                        LatLng latLng = new LatLng(latt, lonn);

                        String g = (String) poi.get(3);
                        String name = (String) poi.get(0);

                        //add each marker in rows. no checking needed bc we only queried what we need
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(name)
                                .icon(iconRetrieve(g)))
                                //title should be marker name
                                //tag can be g
                                .setTag("places");
                    }

                } else {
                    Log.i(TAG, "mMap is null, not placing markers.");
                }
            db.close();
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
      //  sclient = new Sheets.Builder(transport,jsonFactory,credential).setApplicationName("TestMap/1.0").build();


        //   try {
       //     Sqlresponse result = null;
       //     String q = "SELECT latitude, longitude, 'group' FROM "+tableId;
       //     result = query(tableId, q);
       //     List<List<Object>> rows =result.getRows();
        db.open();
        String q = "SELECT name, latitude, longitude, groupName FROM "+sqlTableId;
        List<List<Object>> rows =db.rawArrQuery(q);

            Log.i(TAG, "Got " + rows.size() + " POIs from fusion tables.");

            if (mMap != null) {

                for (List<Object> poi : rows) {
                    String lat = poi.get(1).toString();
                    String lng = poi.get(2).toString();
                    Double latd = Double.valueOf(lat);
                    Double lngd = Double.valueOf(lng);
                    LatLng latLng = new LatLng(latd, lngd);

                   // LatLng latLng = new LatLng(lat.doubleValue(), lon.doubleValue());
                    String group = (String) poi.get(3);
                    String name = (String) poi.get(0);
                    //needed to know if point is within 1/4 mile of location
                    Location test = new Location("");
                    test.setLatitude(latd);
                    test.setLongitude(lngd);


                    float distanceInMeters = center.distanceTo(test);
                    if(distanceInMeters < 402.336 ){

                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(name)
                                .icon(iconRetrieve(group)))
                                //title should be marker name
                                //tag can be g
                                .setTag("places");
                    }
                }
            } else {
                Log.i(TAG, "mMap is null, not placing markers.");
            }

        /*  } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
      db.close();
    }
    public void locationPopUp(LatLng latilngi, Marker marker){
        //locpopup.xml
        myDialog3.setContentView(R.layout.locpopup);
        TextView curloc = (TextView)myDialog3.findViewById(R.id.locText);
        curloc.setText(marker.getTitle() +": " + '\n' + latilngi.toString());
        myDialog3.show();
        //don't really need the (X) to close out pop up bc you just click anywhere
        //TextView txtclose3 = (TextView) myDialog3.findViewById(R.id.txtclose3);
        /*txtclose3.setOnClickListener(new View.OnClickListener(){
          public void onClick(View v){
           myDialog3.hide(); }
          });*/
    }

    //method to create pop up with marker's info when marker is clicked.
    @Override
    public boolean onMarkerClick(Marker marker) {
        System.out.println(marker.getClass());
        System.out.println(marker.getTitle());
        if(marker.getTag().toString().equalsIgnoreCase("LocationMarker")){
            latilngi = marker.getPosition();
            locationPopUp(latilngi, marker);
        }else if(marker.getTag().toString().equalsIgnoreCase("places")){
            latilngi = marker.getPosition();
            try {
                InfoWindow(findViewById(R.id.streetviewpanorama), ""+marker.getPosition().latitude ,""+marker.getPosition().longitude);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else{
            try {
                buspopup(findViewById(R.id.table_bus),marker);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    public Sqlresponse query(String tabID , String que) throws ExecutionException, InterruptedException {
        // Inspired from: https://github.com/digitalheir/fusion-tables-android/blob/master/src/com/google/fusiontables/ftclient/FtClient.java
        // It instantiates a GetTableTask class, calls execute, which calls doInBackground

       // return new GetTableTask(sclient,que).execute(tabID).get();
        return new GetTableTask(fclient,que).execute(tabID).get();
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

    protected class GetTableTask extends AsyncTask<String, Void, Sqlresponse> {
        Fusiontables fclient;
        String que;
        //Sheets sclient;

         public GetTableTask(Fusiontables fclient, String que) {
            this.fclient = fclient;
            this.que = que;
        }

        /*  public GetTableTask(Sheets sclient, String que) {
            this.sclient = sclient;
            this.que = que;
            }
        */

        protected void doInBackground(){}

        @Override
        //replace valuerange with Sqlresponse
        protected Sqlresponse doInBackground(String... params) {
            String tableId = params[0];
            Log.i(TAG, "doInBackground table id: " + tableId);
            Sqlresponse sqlresponse = null;
           // ValueRange sqlres = null;

            try {
                Fusiontables.Query.SqlGet sql = fclient.query().sqlGet(que);// +" WHERE 'subtype' = '"+parenting+"'");
                sqlresponse = sql.execute();

              /*  ValueRange response = sclient.spreadsheets().values()
                        .get(tableId, que)
                        .execute();*/
             //  response.getValues().;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return sqlresponse;
        }

    } // end gettabletask class
  } // end mapsactivity class

