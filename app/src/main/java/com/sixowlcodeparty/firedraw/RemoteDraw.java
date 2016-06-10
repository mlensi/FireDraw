package com.sixowlcodeparty.firedraw;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Michael Lensi on 6/8/2016.
 */
public class RemoteDraw extends View {

    Paint mPaint;
    Firebase ref;
    Map<Integer, LocalDB> mapLocalDBs = new HashMap();
    ArrayList<PointF> arr_P = new ArrayList<>();
    ArrayList<Integer> arr_Mode = new ArrayList<>();

    public RemoteDraw(Context context) {
        super(context);
        init();
    }
    public RemoteDraw(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

        // build up the HashMap (keyed array) of all local databases
        mapLocalDBs.put(MainActivity.channelRed,    new LocalDB(getContext(), MainActivity.RED_DB_NAME));
        mapLocalDBs.put(MainActivity.channelOrange, new LocalDB(getContext(), MainActivity.ORANGE_DB_NAME));
        mapLocalDBs.put(MainActivity.channelYellow, new LocalDB(getContext(), MainActivity.YELLOW_DB_NAME));
        mapLocalDBs.put(MainActivity.channelGreen,  new LocalDB(getContext(), MainActivity.GREEN_DB_NAME));
        mapLocalDBs.put(MainActivity.channelBlue1,  new LocalDB(getContext(), MainActivity.BLUE1_DB_NAME));
        mapLocalDBs.put(MainActivity.channelBlue2,  new LocalDB(getContext(), MainActivity.BLUE2_DB_NAME));

        Firebase.setAndroidContext(getContext());
        ref = new Firebase("https://firedraw-6e4c8.firebaseio.com/");

        //
        // FIREBASE CHANGE LISTENER
        //
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println(snapshot.getValue());
                //Log.d("firebase", snapshot.getValue().toString());

                // OLD:
                // was written in LocalDraw as a PointF, which Firebase converted to HashMap,
                // so cast accordingly
                //Map<String, Object> snapMap = (Map<String, Object>) snapshot.getValue();
                //mX = (double) snapMap.get("x");
                //mY = (double) snapMap.get("y");

                // NEW:
                // data saved using FireDrawData class
                //
                // update ALL databases (colors) from the snapshot
                // TODO consider offloading to Async thread ??
                int iKey;
                for (DataSnapshot snappyroots : snapshot.getChildren()) {
                    iKey = Integer.parseInt(snappyroots.getKey());
                    FireDrawData snapMap = snappyroots.getValue(FireDrawData.class);
                    PointF p = snapMap.getPoint();
                    int iMode = snapMap.getMode();
                    mapLocalDBs.get(iKey).insertCoord(p, iMode);
                }

                invalidate();

            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float paddingLeft = getPaddingLeft();
        float paddingRight= getPaddingRight();
        float paddingTop = getPaddingTop();
        float paddingBottom = getPaddingBottom();
        float width = getWidth() - (paddingLeft+paddingRight);
        float height = getHeight() - (paddingTop+paddingBottom);

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(20);

        //
        // REDRAW ALL LOCAL DATABASES, i.e. all colors
        //
        int iKey;
        LocalDB db;
        for (Map.Entry<Integer, LocalDB> entry : mapLocalDBs.entrySet()) {

            // remember, the key is also the color integer (sans alpha)
            iKey = entry.getKey();
            db = entry.getValue();

            // add alpha channel to color integer
            int r = Color.red(iKey);
            int g = Color.green(iKey);
            int b = Color.blue(iKey);
            mPaint.setColor(Color.argb(255, r, g, b));

            if (db != null) {
                arr_P.clear();
                arr_P = db.getCoordinates();

                arr_Mode.clear();
                arr_Mode = db.getModes();

                int i = 0;
                Path path = new Path();
                for (PointF p : arr_P) {
                    switch (arr_Mode.get(i++)) {
                        case 0:
                            path.lineTo(p.x, p.y);
                            break;
                        case 1:
                            path.moveTo(p.x, p.y);
                            break;
                        default:
                            path.lineTo(p.x, p.y);
                    }
                }
                canvas.drawPath(path, mPaint);
            }
        }

    }

}
