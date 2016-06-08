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
 */

public class LocalDB {

    // database name and version
    public static final String DB_NAME = "localdraw.db";
    public static final int DB_VERSION = 1;

    // Location table
    public static final String COORD_TABLE = "coord";

    public static final String COORD_ID = "_id";
    public static final int COORD_ID_COL = 0;

    public static final String COORD_Y = "coordY";
    public static final int COORD_Y_COL = 1;

    public static final String COORD_X = "coordX";
    public static final int COORD_X_COL = 2;

    public static final String LOCATION_TIME = "time";
    public static final int LOCATION_TIME_COL = 3;

    /** Database SQL **/
    public static final String CREATE_COORD_TABLE =
            "CREATE TABLE " + COORD_TABLE + " (" +
                    COORD_ID        + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COORD_Y         + " REAL, " +
                    COORD_X         + " REAL, " +
                    LOCATION_TIME   + " INTEGER)";

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

    public LocalDB(Context context) {
        mCtx = context;
        dbHelper = new RunTrackerDBHelper(context, DB_NAME, null, DB_VERSION);
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

    public void insertCoord(PointF p) {
        this.openWriteableDB();

        ContentValues cv = new ContentValues();
        cv.put(COORD_X, p.x);
        cv.put(COORD_Y, p.y);
        cv.put(LOCATION_TIME, 0);
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
            long time = cursor.getLong(LOCATION_TIME_COL);

            PointF p = new PointF(cx, cy);

            list.add(p);
        }
        if (cursor != null){
            cursor.close();
        }
        this.close();
        return list;
    }

    public void deleteCoordinates() {
        this.openWriteableDB();
        db.delete(COORD_TABLE, null, null);
        this.close();
    }


}