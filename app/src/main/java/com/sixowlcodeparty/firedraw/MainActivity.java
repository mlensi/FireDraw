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


    public static final String TOP_DB_NAME = "topdraw.db";
    public static final String BOTTOM_DB_NAME = "bottomdraw.db";

    Firebase ref;
    RemoteDraw mTop;
    LocalDraw mBottom;
    LocalDB dbTop;
    LocalDB dbBot;
    Button button_PressMe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_main);

        ref = new Firebase("https://firedraw-6e4c8.firebaseio.com/");

        mTop = (RemoteDraw) findViewById(R.id.vpSwipeTop);
        mBottom = (LocalDraw) findViewById(R.id.vpSwipeBottom);

        dbTop = new LocalDB(getApplicationContext(), TOP_DB_NAME);
        dbBot = new LocalDB(getApplicationContext(), BOTTOM_DB_NAME);

        button_PressMe = (Button) findViewById(R.id.button);
        button_PressMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dbTop.deleteCoordinates();
                dbBot.deleteCoordinates();
                mTop.invalidate();
                mBottom.invalidate();

            }
        });

    }


}
