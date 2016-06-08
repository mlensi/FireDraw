package com.sixowlcodeparty.firedraw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Michael Lensi on 6/8/2016.
 */
public class LocalDraw extends View {

    Paint mPaint;
    float mX = -1f;
    float mY = -1f;

    public LocalDraw(Context context) {
        super(context);
        //init();
    }
    public LocalDraw(Context context, AttributeSet attrs) {
        super(context, attrs);
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

        if (mX > 0 && mX < width) {
            if (mY > 0 && mY < height) {
                canvas.drawCircle(mX, mY, 5f, mPaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        mX = event.getX();
        mY = event.getY();
        invalidate();

        return true;
    }
}
