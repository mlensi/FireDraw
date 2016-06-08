package com.sixowlcodeparty.firedraw;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;

import com.firebase.client.Firebase;

public class MainActivity extends FragmentActivity {


    Firebase ref;
    LocalDraw mBottom;
    public LocalDB db;
    Button button_PressMe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_main);

        ref = new Firebase("https://firedraw-6e4c8.firebaseio.com/");

        mBottom = (LocalDraw) findViewById(R.id.vpSwipeBottom);

        db = new LocalDB(getApplicationContext());

        button_PressMe = (Button) findViewById(R.id.button);
        button_PressMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.deleteCoordinates();
                mBottom.invalidate();

            }
        });

    }


}
