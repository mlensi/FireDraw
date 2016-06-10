package com.sixowlcodeparty.firedraw;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.firebase.client.Firebase;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity {

    // list of local databases corresponding to all available Firebase channels
    // plus the local control ("bottomdraw.db")
    public static final String RED_DB_NAME    = "redChannel.db";
    public static final String ORANGE_DB_NAME = "orangeChannel.db";
    public static final String YELLOW_DB_NAME = "yellowChannel.db";
    public static final String GREEN_DB_NAME  = "greenChannel.db";
    public static final String BLUE1_DB_NAME  = "blue1Channel.db";
    public static final String BLUE2_DB_NAME  = "blue2Channel.db";
    public static final String BOTTOM_DB_NAME = "bottomdraw.db";

    // channel colors
    // these will be converted to 32-bit (i.e. with alpha) in RemoteDraw
    public static final int channelRed    = 0xE52B50;
    public static final int channelOrange = 0xFF9900;
    public static final int channelYellow = 0xFDE910;
    public static final int channelGreen  = 0xCCFF00;
    public static final int channelBlue1  = 0x6495ED;
    public static final int channelBlue2  = 0x00CCCC;

    Firebase ref;
    RemoteDraw mTopView;
    LocalDraw mBottomView;
    ArrayList<LocalDB> arr_dbChannels;
    LocalDB dbBot;
    Button button_Clear;
    public static Spinner spinner_ColorChannel;
    public static int mCurrentColor = channelRed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_main);

        ref = new Firebase("https://firedraw-6e4c8.firebaseio.com/");

        mTopView = (RemoteDraw) findViewById(R.id.vpSwipeTop);
        mBottomView = (LocalDraw) findViewById(R.id.vpSwipeBottom);

        // this array holds all the local databases here so we can easily flip through
        // during button click
        arr_dbChannels = new ArrayList<>();
        arr_dbChannels.add(new LocalDB(getApplicationContext(), RED_DB_NAME));
        arr_dbChannels.add(new LocalDB(getApplicationContext(), ORANGE_DB_NAME));
        arr_dbChannels.add(new LocalDB(getApplicationContext(), YELLOW_DB_NAME));
        arr_dbChannels.add(new LocalDB(getApplicationContext(), GREEN_DB_NAME));
        arr_dbChannels.add(new LocalDB(getApplicationContext(), BLUE1_DB_NAME));
        arr_dbChannels.add(new LocalDB(getApplicationContext(), BLUE2_DB_NAME));
        dbBot = new LocalDB(getApplicationContext(), BOTTOM_DB_NAME);

        button_Clear = (Button) findViewById(R.id.button);
        button_Clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (LocalDB db : arr_dbChannels) {
                    db.deleteCoordinates();
                }
                dbBot.deleteCoordinates();

                mTopView.invalidate();
                mBottomView.invalidate();

            }
        });

        spinner_ColorChannel = (Spinner) findViewById(R.id.spinner);
        spinner_ColorChannel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                switch (position) {
                    case 0:
                        mCurrentColor = MainActivity.channelRed;
                        break;
                    case 1:
                        mCurrentColor = MainActivity.channelOrange;
                        break;
                    case 2:
                        mCurrentColor = MainActivity.channelYellow;
                        break;
                    case 3:
                        mCurrentColor = MainActivity.channelGreen;
                        break;
                    case 4:
                        mCurrentColor = MainActivity.channelBlue1;
                        break;
                    case 5:
                        mCurrentColor = MainActivity.channelBlue2;
                        break;
                    default:
                        mCurrentColor = MainActivity.channelRed;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }


}
