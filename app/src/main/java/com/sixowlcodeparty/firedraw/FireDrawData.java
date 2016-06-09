package com.sixowlcodeparty.firedraw;

import android.graphics.PointF;

/**
 * Created by Michael Lensi on 6/9/2016.
 */

// DATA as stored (set/retrieved) on Firebase
// NOTE THAT FIREBASE SERIALIZES THIS TO JSON BASED ON THE GETTER AND SETTER METHODS
// AND FURTHERMORE THE NAMES USED IN THE DATABASE ARE DERIVED DIRECTLY FROM THE METHOD NAME
// e.g. getTaco will save an element on Firebase named "taco"

public class FireDrawData {

    //
    // data fields
    //

    // coord of drawn point
    private PointF point;

    // iMode:
    // 0: drawing lines
    // 1: starting a new line
    // 2: clear command (will issue local database clear command to all devices)
    private int mode = 0;

    //
    // constructors
    //

    public FireDrawData() {}

    public FireDrawData(PointF p, int iMode) {
        this.point = p;
        this.mode = iMode;
    }

    //
    // methods
    //

    public PointF getPoint() {
        return point;
    }

    public int getMode() {
        return mode;
    }

}
