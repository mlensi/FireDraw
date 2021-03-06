package com.sixowlcodeparty.firedraw;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.graphics.PointF;
import android.location.Location;

import java.util.ArrayList;

/**
 * Created by Michael Lensi on 6/8/2016.
 *
 * Contains drawing coordinates and an int indicating whether or not to start a new line.
 */

public class LocalDB {

    // database name and version
    //public static final String DB_NAME = "localdraw.db";
    public static final int DB_VERSION = 2;

    // Location table
    public static final String COORD_TABLE = "coord";

    public static final String COORD_ID = "_id";
    public static final int COORD_ID_COL = 0;

    public static final String COORD_Y = "coordY";
    public static final int COORD_Y_COL = 1;

    public static final String COORD_X = "coordX";
    public static final int COORD_X_COL = 2;

    public static final String IMODE = "iMode";
    public static final int IMODE_COL = 3;

    /** Database SQL **/
    public static final String CREATE_COORD_TABLE =
            "CREATE TABLE " + COORD_TABLE + " (" +
                    COORD_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COORD_Y   + " REAL, " +
                    COORD_X   + " REAL, " +
                    IMODE     + " INTEGER)";

    public static final String DROP_COORD_TABLE =
            "DROP TABLE IF EXISTS " + COORD_TABLE;

    private static class RunTrackerDBHelper extends SQLiteOpenHelper {

        public RunTrackerDBHelper(Context context, String name,
                                  SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(LocalDB.CREATE_COORD_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db,
                              int _oldVersion, int _newVersion) {
            db.execSQL(LocalDB.DROP_COORD_TABLE);
            onCreate(db);
        }
    }

    private SQLiteDatabase db;
    private RunTrackerDBHelper dbHelper;

    Context mCtx;

    public LocalDB(Context context, String strDbName) {
        mCtx = context;
        dbHelper = new RunTrackerDBHelper(context, strDbName, null, DB_VERSION);
    }

    private void openReadableDB() {
        db = dbHelper.getReadableDatabase();
    }

    private void openWriteableDB() {
        db = dbHelper.getWritableDatabase();
    }

    private void close() {
        if (db != null)
            db.close();
    }

    public void insertCoord(PointF p, int iMode) {
        this.openWriteableDB();

        ContentValues cv = new ContentValues();
        cv.put(COORD_X, p.x);
        cv.put(COORD_Y, p.y);
        cv.put(IMODE, iMode);
        db.insert(COORD_TABLE, null, cv);

        this.close();
    }

    public ArrayList<PointF> getCoordinates() {
        ArrayList<PointF> list = new ArrayList<PointF>();
        this.openReadableDB();
        Cursor cursor = db.query(COORD_TABLE,
                null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            float cx = cursor.getFloat(COORD_X_COL);
            float cy = cursor.getFloat(COORD_Y_COL);
            //int iMode = cursor.getInt(IMODE_COL);

            PointF p = new PointF(cx, cy);

            list.add(p);
        }
        if (cursor != null){
            cursor.close();
        }
        this.close();
        return list;
    }

    public ArrayList<Integer> getModes() {
        ArrayList<Integer> list = new ArrayList<Integer>();
        this.openReadableDB();
        Cursor cursor = db.query(COORD_TABLE,
                null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            int iMode = cursor.getInt(IMODE_COL);
            list.add(iMode);
        }
        if (cursor != null){
            cursor.close();
        }
        this.close();
        return list;
    }

    // deleteCoordinates, well...
    // not just coordinates, but the whole db
    public void deleteCoordinates() {
        this.openWriteableDB();
        db.delete(COORD_TABLE, null, null);
        this.close();
    }


}