package com.sixowlcodeparty.firedraw;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;

import com.firebase.client.Firebase;

public class MainActivity extends FragmentActivity {


    Firebase ref;
    LocalDraw mBottom;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_main);

        ref = new Firebase("https://firedraw-6e4c8.firebaseio.com/");

        mBottom = (LocalDraw) findViewById(R.id.vpSwipeBottom);

    }


}
