package com.sixowlcodeparty.firedraw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
    double mX;
    double mY;

    public RemoteDraw(Context context) {
        super(context);
        init();
    }
    public RemoteDraw(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

        //db = new LocalDB(getContext());

        ref = new Firebase("https://firedraw-6e4c8.firebaseio.com/");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println(snapshot.getValue());
                //Log.d("firebase", snapshot.getValue().toString());

                Map<String, Object> snapMap = (Map<String, Object>) snapshot.getValue();

                mX = (double) snapMap.get("x");
                mY = (double) snapMap.get("y");

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
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.RED);

        if (mX > 0 && mX < width) {
            if (mY > 0 && mY < height) {
                canvas.drawCircle((float) mX, (float) mY, 5f, mPaint);
            }
        }

    }

}
