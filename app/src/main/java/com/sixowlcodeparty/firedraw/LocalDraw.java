package com.sixowlcodeparty.firedraw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.firebase.client.Firebase;

import java.util.ArrayList;

/**
 * Created by Michael Lensi on 6/8/2016.
 */
public class LocalDraw extends View {

    Paint mPaint;
    LocalDB db;
    ArrayList<PointF> arr_P = new ArrayList<>();
    ArrayList<Integer> arr_Mode = new ArrayList<>();
    Firebase ref;

    public LocalDraw(Context context) {
        super(context);
        init();
    }
    public LocalDraw(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        db = new LocalDB(getContext(), MainActivity.BOTTOM_DB_NAME);
        Firebase.setAndroidContext(getContext());
        ref = new Firebase("https://firedraw-6e4c8.firebaseio.com/");
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
        mPaint.setColor(Color.BLACK);

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

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        PointF p = new PointF(event.getX(), event.getY());

        //
        // FIREBASE WRITE OP
        // OLD: will write the PointF object as a HashMap with "x" and "y" keys
        // NEW: will serialize a FireDrawData object
        //
        int iMode = 0;  // assume in line-draw mode, unless...
        if (event.getAction() != event.ACTION_MOVE) {
            iMode = 1;  // ok, let's tell everyone to start a new line
        }
        FireDrawData fdd = new FireDrawData(p, iMode);

        // the color serves as the child node key
        // this sets up a "color channel", if you will
        // and the RemoteDraw class takes this string key and converts it into a useable color
        ref.child(String.valueOf(MainActivity.mCurrentColor)).setValue(fdd);  // was ref.setValue(p);

        db.insertCoord(p, iMode);

        invalidate();

        return true;
    }
}
