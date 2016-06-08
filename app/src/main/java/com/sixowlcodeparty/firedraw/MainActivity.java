package com.sixowlcodeparty.firedraw;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.firebase.client.Firebase;

public class MainActivity extends FragmentActivity {

    public static final String TOP_DB_NAME = "topdraw.db";
    public static final String BOTTOM_DB_NAME = "bottomdraw.db";

    Firebase ref;
    RemoteDraw mTopView;
    LocalDraw mBottomView;
    LocalDB dbTop;
    LocalDB dbBot;
    Button button_Clear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_main);

        ref = new Firebase("https://firedraw-6e4c8.firebaseio.com/");

        mTopView = (RemoteDraw) findViewById(R.id.vpSwipeTop);
        mBottomView = (LocalDraw) findViewById(R.id.vpSwipeBottom);

        dbTop = new LocalDB(getApplicationContext(), TOP_DB_NAME);
        dbBot = new LocalDB(getApplicationContext(), BOTTOM_DB_NAME);

        button_Clear = (Button) findViewById(R.id.button);
        button_Clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dbTop.deleteCoordinates();
                dbBot.deleteCoordinates();
                mTopView.invalidate();
                mBottomView.invalidate();

            }
        });

    }


}
