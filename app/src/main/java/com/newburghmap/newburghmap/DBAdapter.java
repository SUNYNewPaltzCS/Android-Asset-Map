package com.newburghmap.newburghmap;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.database.DatabaseUtils.queryNumEntries;

public class DBAdapter {

    private static final String TAG = "DBAdapter"; //used for logging database version changes

    // Field Names:
    public static final String KEY_ROWID = "_id";
    public static final String KEY_GROUPNAME = "groupName";
    public static final String KEY_NAME = "name";
    //public static final String KEY_GROUPES = "groupES";
    public static final String KEY_TYPE = "type";
    public static final String KEY_TYPEES = "typeES";
    public static final String KEY_SUBTYPE = "subtype";
    public static final String KEY_SUBTYPESES = "subtypeES";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_DESCRIPTIONES = "descriptionES";
    public static final String KEY_ADDRESS = "address";
    //public static final String KEY_OGADDRESS = "OriginalAddress";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_PHONE = "phone";
    //public static final String KEY_HOTLINE = "Hotline";
    //public static final String KEY_CONTACTEMAIL = "ContactEmail";
    public static final String KEY_HOURS = "hours";
    public static final String KEY_HOURSES = "hoursES";
    public static final String KEY_LINK = "link";
    // public static final String KEY_ICON = "icon";


    //Bus route field names
    public static final String KEY_BUSROWID = "_busId";
    public static final String KEY_STOPNAME = "stopName";
    public static final String KEY_TIMES = "times";
    public static final String KEY_ROUTE = "route";
    public static final String KEY_BUSLAT = "latitude";
    public static final String KEY_BUSLONG = "longitude";



    public static final String[] ALL_KEYS = new String[]{KEY_ROWID, KEY_GROUPNAME, KEY_NAME, KEY_TYPE, KEY_TYPEES, KEY_SUBTYPE, KEY_SUBTYPESES, KEY_DESCRIPTION, KEY_DESCRIPTIONES, KEY_ADDRESS, KEY_LATITUDE, KEY_LONGITUDE, KEY_PHONE, KEY_HOURS, KEY_HOURSES, KEY_LINK};
    public static final String[] ALL_BUSKEYS = new String[]{KEY_BUSROWID, KEY_STOPNAME, KEY_TIMES, KEY_ROUTE, KEY_BUSLAT, KEY_BUSLONG};


    // Column Numbers for each Field Name:
    public static final int COL_ROWID = 0;
    public static final int COL_GROUPNAME = 1;
    public static final int COL_NAME = 2;
    // public static final int COL_GROUPES = 3;
    public static final int COL_TYPE = 4;
    public static final int COL_TYPEES = 5;
    public static final int COL_SUBTYPE = 6;
    public static final int COL_SUBTYPESES = 7;
    public static final int COL_DESCRIPTION = 8;
    public static final int COL_DESCRIPTIONES = 9;
    public static final int COL_ADDRESS = 10;
    //   public static final int COL_OGADDRESS = 11;
    public static final int COL_LATITUDE = 12;
    public static final int COL_LONGITUDE = 13;
    public static final int COL_PHONE = 14;
    //  public static final int COL_HOTLINE = 15;
    //  public static final int COL_CONTACTEMAIL = 16;
    public static final int COL_HOURS = 17;
    public static final int COL_HOURSES = 18;
    public static final int COL_LINK = 19;
    //  public static final int COL_ICON = 20;

    // DataBase info:
    public static final String DATABASE_NAME = "ResourceDataBase";
    public static final String DATABASE_TABLE = "ResourceTable";
    public static final String SECOND_DATABASE_TABLE = "BusRouteTable";
    public static final int DATABASE_VERSION = 6; // The version number must be incremented each time a change to DB structure occurs.

    public static final String uniqueVals = "( " + KEY_LATITUDE + ", " + KEY_LONGITUDE + ", " + KEY_NAME + ", " + KEY_ADDRESS + " )";


    //SQL statement to create database
    private static final String DATABASE_CREATE_SQL =
            "CREATE TABLE " + DATABASE_TABLE
                    + " ("
                    + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + KEY_GROUPNAME + " TEXT,"
                    + KEY_NAME + " TEXT,"
                    //        + KEY_GROUPES + " TEXT,"
                    + KEY_TYPE + " TEXT,"
                    + KEY_TYPEES + " TEXT,"
                    + KEY_SUBTYPE + " TEXT,"
                    + KEY_SUBTYPESES + " TEXT,"
                    + KEY_DESCRIPTION + " TEXT,"
                    + KEY_DESCRIPTIONES + " TEXT,"
                    //         + KEY_OGADDRESS + " TEXT,"
                    + KEY_ADDRESS + " TEXT,"
                    + KEY_LATITUDE + " TEXT,"
                    + KEY_LONGITUDE + " TEXT,"
                    + KEY_PHONE + " TEXT,"
                    //          + KEY_HOTLINE + " TEXT,"
                    //          + KEY_CONTACTEMAIL + " TEXT,"
                    + KEY_HOURS + " TEXT,"
                    + KEY_HOURSES + " TEXT,"
                    + KEY_LINK + " TEXT" +
                    // ", UNIQUE " + "( "+ KEY_LATITUDE + ", " + KEY_LONGITUDE + ", " + KEY_NAME + ", " + KEY_ADDRESS + " )" +
                    " );";


    //create statement for the bus table
    private static final String DATABASE_CREATE_SQL2 =
            "CREATE TABLE " + SECOND_DATABASE_TABLE + "(" + KEY_BUSROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + KEY_STOPNAME + " TEXT," + KEY_ROUTE + " TEXT," + KEY_TIMES + " TEXT," + KEY_BUSLAT + " TEXT," + KEY_BUSLONG + " TEXT"
                   + ");";


    private final Context context;
    private DatabaseHelper myDBHelper;
    private SQLiteDatabase db;


    public DBAdapter(Context ctx) {
        this.context = ctx;
        myDBHelper = new DatabaseHelper(context);
    }

    // Open the database connection.
    public DBAdapter open() {
        db = myDBHelper.getWritableDatabase();
        return this;
    }

    // Close the database connection.
    public void close() {
        myDBHelper.close();
    }

    // Add a new set of values to be inserted into the database.
    public long insertRow(String groupName, String name, String type, String typeES, String subtype,
                          String subtypeES, String description, String descriptionES, String address,
                          String latitude, String longitude, String phone, String hours,
                          String hoursES, String link) {
        String x = null;
        ContentValues initialValues = new ContentValues();
        // row id should be passed null, because it auto increments itself
        initialValues.put(KEY_ROWID, x);
        initialValues.put(KEY_GROUPNAME, groupName);
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_TYPE, type);
        initialValues.put(KEY_TYPEES, typeES);
        initialValues.put(KEY_SUBTYPE, subtype);
        initialValues.put(KEY_SUBTYPESES, subtypeES);
        initialValues.put(KEY_DESCRIPTION, description);
        initialValues.put(KEY_DESCRIPTIONES, descriptionES);
        initialValues.put(KEY_ADDRESS, address);
        initialValues.put(KEY_LATITUDE, latitude);
        initialValues.put(KEY_LONGITUDE, longitude);
        initialValues.put(KEY_PHONE, phone);
        initialValues.put(KEY_HOURS, hours);
        initialValues.put(KEY_HOURSES, hoursES);
        initialValues.put(KEY_LINK, link);

        // Insert the data into the database.
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //insert a row to db if passed an arraylist of items
    public long insertArrayRow(ArrayList insertItems) {
        ContentValues rowValues = new ContentValues();
        String n = null;
        if (insertItems.size() == 15) { //check to make sure we have 15 items in the array passed
            // row id should be passed null, because it auto increments itself
            rowValues.put(KEY_ROWID, n);
            rowValues.put(KEY_GROUPNAME, (String) insertItems.get(0));
            rowValues.put(KEY_NAME, (String) insertItems.get(1));
            rowValues.put(KEY_TYPE, (String) insertItems.get(2));
            rowValues.put(KEY_TYPEES, (String) insertItems.get(3));
            rowValues.put(KEY_SUBTYPE, (String) insertItems.get(4));
            rowValues.put(KEY_SUBTYPESES, (String) insertItems.get(5));
            rowValues.put(KEY_DESCRIPTION, (String) insertItems.get(6));
            rowValues.put(KEY_DESCRIPTIONES, (String) insertItems.get(7));
            rowValues.put(KEY_ADDRESS, (String) insertItems.get(8));
            rowValues.put(KEY_LATITUDE, (String) insertItems.get(9));
            rowValues.put(KEY_LONGITUDE, (String) insertItems.get(10));
            rowValues.put(KEY_PHONE, (String) insertItems.get(11));
            rowValues.put(KEY_HOURS, (String) insertItems.get(12));
            rowValues.put(KEY_HOURSES, (String) insertItems.get(13));
            rowValues.put(KEY_LINK, (String) insertItems.get(14));

            // Insert the data into the database.
            return db.insert(DATABASE_TABLE, null, rowValues);
        } else {
            Log.i(TAG, "ARRAY PASSED DOES NOT CONTAIN THE RIGHT NUMBER OF ITEMS!!!!!");
            return 1;
        }
    }

    //insert statement for the bus table when passed an array
    public long insertArrayInBusTable(ArrayList itemstoput) {
        ContentValues busRowValues = new ContentValues();
        Log.i(TAG, "ARRAY PASSED num of items!!!!!  is " + itemstoput.size());

        String ns = null;
        if (itemstoput.size() == 5) { //check to make sure we have 3 items in the array passed
            // row id should be passed null, because it auto increments itself
            busRowValues.put(KEY_BUSROWID, ns); //autoincrements so we just pass it null
            busRowValues.put(KEY_STOPNAME, (String) itemstoput.get(0));
            busRowValues.put(KEY_ROUTE, (String) itemstoput.get(1));
            busRowValues.put(KEY_TIMES, (String) itemstoput.get(2));
            busRowValues.put(KEY_BUSLAT, (String) itemstoput.get(3));
            busRowValues.put(KEY_BUSLONG, (String) itemstoput.get(4));


            return db.insert(SECOND_DATABASE_TABLE, null, busRowValues);
        } else {
            Log.i(TAG, "ARRAY PASSED DOES NOT CONTAIN THE RIGHT NUMBER OF ITEMS!!!!!");
            return 1;
        }
    }

    //insert statement for the bus table when passed strings
    public long insertStringsInBusTable(String stopName, String times, String route, String lat, String lng) {
        ContentValues busRowValuesStrings = new ContentValues();
        String ns = null;
        // row id should be passed null, because it auto increments itself
        busRowValuesStrings.put(KEY_BUSROWID, ns);
        busRowValuesStrings.put(KEY_STOPNAME, stopName);
        busRowValuesStrings.put(KEY_ROUTE, route);
        busRowValuesStrings.put(KEY_TIMES, times);
        busRowValuesStrings.put(KEY_BUSLAT, lat);
        busRowValuesStrings.put(KEY_BUSLONG, lng);
        return db.insert(SECOND_DATABASE_TABLE, null, busRowValuesStrings);

    }

    // Delete a row from the database, by rowId (primary key)
    public boolean deleteRow(long rowId, String tableName) {
        String theidNeeded;
        if (tableName == DATABASE_TABLE) {
            theidNeeded = KEY_ROWID;
        } else {
            theidNeeded = KEY_BUSROWID;
        }
        String where = theidNeeded + "=" + rowId;
        return db.delete(tableName, where, null) != 0;
    }

    public void deleteAll(String tableName) {
        Cursor c = null;
        //c.moveToFirst();
        c = getAllRows(tableName);
        // c.moveToFirst();
        String idNeeded;
        if (tableName == DATABASE_TABLE) {
            idNeeded = KEY_ROWID;
        } else {
            idNeeded = KEY_BUSROWID;
        }
        long rowId = c.getColumnIndexOrThrow(idNeeded);
        if (c.moveToFirst()) {
            do {
                deleteRow(c.getLong((int) rowId), tableName);
            } while (c.moveToNext());
        }
        c.close();
    }


    public long numEntries(String tableName) {
        Long entries = null;
        entries = queryNumEntries(db, tableName, null);
        return entries;
    }


    // Return all data in a db table as a cursor.
    public Cursor getAllRows(String tableName) {
        String where = null;
        String[] allCols = null;
        if (tableName == DATABASE_TABLE) {
            allCols = ALL_KEYS;
        } else {
            allCols = ALL_BUSKEYS;
        }
        Cursor c = db.query(true, tableName, allCols, where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    //return all data in resource table as a list for comparing
    public List<String> getAllRowsList() {
        String where = null;
        Cursor c = db.query(true, DATABASE_TABLE, ALL_KEYS, where, null, null, null, null, null);
        if(c == null){
            return null;
        }
        if (c != null) {
            c.moveToFirst();
        }
        List<String> res = null;
        while (!c.isAfterLast()) {
            res.add(c.getString(c.getColumnIndex(KEY_ROWID))); //add the item
            res.add(c.getString(c.getColumnIndex(KEY_GROUPNAME))); //add the item
            res.add(c.getString(c.getColumnIndex(KEY_NAME))); //add the item
            res.add(c.getString(c.getColumnIndex(KEY_TYPE))); //add the item
            res.add(c.getString(c.getColumnIndex(KEY_TYPEES))); //add the item
            res.add(c.getString(c.getColumnIndex(KEY_SUBTYPE))); //add the item
            res.add(c.getString(c.getColumnIndex(KEY_SUBTYPESES))); //add the item
            res.add(c.getString(c.getColumnIndex(KEY_DESCRIPTION))); //add the item
            res.add(c.getString(c.getColumnIndex(KEY_DESCRIPTIONES))); //add the item
            res.add(c.getString(c.getColumnIndex(KEY_ADDRESS))); //add the item
            res.add(c.getString(c.getColumnIndex(KEY_LATITUDE))); //add the item
            res.add(c.getString(c.getColumnIndex(KEY_LONGITUDE))); //add the item
            res.add(c.getString(c.getColumnIndex(KEY_PHONE))); //add the item
            res.add(c.getString(c.getColumnIndex(KEY_HOURS))); //add the item
            res.add(c.getString(c.getColumnIndex(KEY_HOURSES))); //add the item
            res.add(c.getString(c.getColumnIndex(KEY_LINK))); //add the item
            c.moveToNext();
        }
        return res;
    }

    //return all data in bus table as a list for comparing
    public List<String> getAllRowsBusList() {
        String where = null;
        Cursor c = db.query(true, SECOND_DATABASE_TABLE, ALL_BUSKEYS, where, null, null, null, null, null);
        List<String> res = new ArrayList<>();

        if (c != null) {
            c.moveToFirst();

            while (!c.isAfterLast()) {
                res.add(c.getString(c.getColumnIndex(KEY_BUSROWID))); //add the item
                res.add(c.getString(c.getColumnIndex(KEY_STOPNAME))); //add the item
                res.add(c.getString(c.getColumnIndex(KEY_ROUTE))); //add the item
                res.add(c.getString(c.getColumnIndex(KEY_TIMES))); //add the item
                res.add(c.getString(c.getColumnIndex(KEY_BUSLAT))); //add the item
                res.add(c.getString(c.getColumnIndex(KEY_BUSLONG))); //add the item

                c.moveToNext();
            }
        }
        return res;
    }


    // Get a specific row (looking it up by the stop name or name)
    public Cursor getRow(String name, String tableName) {
        String col = null;
        String[] colsoftable;
        if (tableName == DATABASE_TABLE) {
            colsoftable = ALL_KEYS;
            col = KEY_NAME;
        } else {
            col = KEY_STOPNAME;
            colsoftable = ALL_BUSKEYS;
        }
        //where statement

        //check if this needs spaces between the =
        String where = col + "=" + name;
        //the query
        Cursor c = db.query(true, tableName, colsoftable,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }


    public List<List<Object>> rawArrQuery(String queryString) {
        List<List<Object>> resres = new ArrayList<>();
        List<Object> res = new ArrayList<>();

        Cursor cursor = db.rawQuery(queryString, null);
        String[] columnNames = cursor.getColumnNames(); //get all column names from the raw query results
      //  int colCount = cursor.getColumnCount();
       // Log.i(TAG, "!! TEST!! raw query column count gives us !!  " + colCount);
      //  int colRowCount = cursor.getCount();
      //  Log.i(TAG, "!! TEST!! raw query number of rows gives us !!  " + colRowCount);

            //int xy=0;
            if (cursor.moveToFirst()){
                do{
        //            Log.i(TAG, "!! TEST!! raw query move to next  !!  " + xy++);
                    res = new ArrayList<>();
                    for (String x: columnNames) { //for each item in the column names results
                        res.add(cursor.getString(cursor.getColumnIndex(x))); // add each item at column index
          //              Log.i(TAG, "!! TEST!! raw query res.add value  !!  " + cursor.getString(cursor.getColumnIndex(x)));
                    }
                    resres.add(res);
            //        Log.i(TAG, "!! TEST!! raw query res added  !!  " + res);
              //      Log.i(TAG, "!! TEST!! raw query theres is  !!  " + resres);
                }while (cursor.moveToNext());
            }

        cursor.close();
        return resres;

    }


    public boolean updateRowResourceArray(ArrayList insertItems) {
        String n = null;
        if (insertItems.size() == 15) {
            String where = KEY_NAME + " = '" + (String) insertItems.get(1) + "' AND " + KEY_LONGITUDE + " = '" + (String) insertItems.get(10) + "'";
            ContentValues newValues = new ContentValues();
            newValues.put(KEY_GROUPNAME, (String) insertItems.get(0));
            newValues.put(KEY_NAME, (String) insertItems.get(1));
            newValues.put(KEY_TYPE, (String) insertItems.get(2));
            newValues.put(KEY_TYPEES, (String) insertItems.get(3));
            newValues.put(KEY_SUBTYPE, (String) insertItems.get(4));
            newValues.put(KEY_SUBTYPESES, (String) insertItems.get(5));
            newValues.put(KEY_DESCRIPTION, (String) insertItems.get(6));
            newValues.put(KEY_DESCRIPTIONES, (String) insertItems.get(7));
            newValues.put(KEY_ADDRESS, (String) insertItems.get(8));
            newValues.put(KEY_LATITUDE, (String) insertItems.get(9));
            newValues.put(KEY_LONGITUDE, (String) insertItems.get(10));
            newValues.put(KEY_PHONE, (String) insertItems.get(11));
            newValues.put(KEY_HOURS, (String) insertItems.get(12));
            newValues.put(KEY_HOURSES, (String) insertItems.get(13));
            newValues.put(KEY_LINK, (String) insertItems.get(14));

            // Insert it into the database.
            return db.update(DATABASE_TABLE, newValues, where, null) != 0;
        }
        else{
            Log.i(TAG, "ARRAY PASSED DOES NOT CONTAIN THE RIGHT NUMBER OF ITEMS!!!!!");
            return false;
        }
    }

    public boolean updateRowBusArray(ArrayList insertItems) {
        String n = null;
        if (insertItems.size() == 5) {
            String where = KEY_STOPNAME + " = '" + (String) insertItems.get(0) + "' AND " + KEY_ROUTE + " = '" + (String) insertItems.get(1) +"'";
            ContentValues newValues = new ContentValues();
            newValues.put(KEY_STOPNAME, (String) insertItems.get(0));
            newValues.put(KEY_ROUTE, (String) insertItems.get(1));
            newValues.put(KEY_TIMES, (String) insertItems.get(2));
            newValues.put(KEY_BUSLAT, (String) insertItems.get(3));
            newValues.put(KEY_BUSLONG, (String) insertItems.get(4));

            // Insert it into the database.
            return db.update(SECOND_DATABASE_TABLE, newValues, where, null) != 0;
        }
        else{
            Log.i(TAG, "ARRAY PASSED DOES NOT CONTAIN THE RIGHT NUMBER OF ITEMS!!!!!");
            return false;
        }
    }


    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase _db) {

            //check this is legal/functional

            //creating first table
            _db.execSQL(DATABASE_CREATE_SQL);
            //creating second table
            _db.execSQL(DATABASE_CREATE_SQL2);
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading application's database from version " + oldVersion
                    + " to " + newVersion + ", which will destroy all old data!");

            // Destroy old database:
            _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            _db.execSQL("DROP TABLE IF EXISTS " + SECOND_DATABASE_TABLE);

            // Recreate new database:
            onCreate(_db);
        }
    }
}