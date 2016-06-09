package com.sixowlcodeparty.firedraw;

import android.content.Context;
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
import java.util.Map;
import java.util.Objects;

/**
 * Created by Michael Lensi on 6/8/2016.
 */
public class RemoteDraw extends View {

    Paint mPaint;
    Firebase ref;
    LocalDB db;
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

        db = new LocalDB(getContext(), "topdraw.db");   // TODO un-hardcode this

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
                //for (DataSnapshot snappyroots: snapshot.getChildren()) {
                    FireDrawData snapMap = snapshot.getValue(FireDrawData.class);
                    PointF p = snapMap.getPoint();
                    int iMode = snapMap.getMode();
                    db.insertCoord(p, iMode);
                //}

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
        mPaint.setColor(Color.RED);

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
