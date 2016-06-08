package com.sixowlcodeparty.firedraw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
    float mX = -1f;
    float mY = -1f;
    LocalDB db;
    ArrayList<PointF> arr_P = new ArrayList<>();
    Firebase ref;

    public LocalDraw(Context context) {
        super(context);
        db = new LocalDB(getContext());
        ref = new Firebase("https://firedraw-6e4c8.firebaseio.com/");
    }
    public LocalDraw(Context context, AttributeSet attrs) {
        super(context, attrs);
        db = new LocalDB(getContext());
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
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.BLUE);

        arr_P.clear();
        arr_P = db.getCoordinates();
        for (PointF p : arr_P) {
            if (p.x > 0 && p.x < width) {
                if (p.y > 0 && p.y < height) {
                    canvas.drawCircle(p.x, p.y, 5f, mPaint);
                }
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        PointF p = new PointF(event.getX(), event.getY());
        db.insertCoord(p);

        ref.setValue(p);

        invalidate();

        return true;
    }
}
