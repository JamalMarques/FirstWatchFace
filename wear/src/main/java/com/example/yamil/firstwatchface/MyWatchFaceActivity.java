package com.example.yamil.firstwatchface;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.BatteryManager;
import android.os.Bundle;
import android.service.wallpaper.WallpaperService;
import android.support.wearable.view.WatchViewStub;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MyWatchFaceActivity extends Activity {

    //private static final String baseAssetsAccess = "fonts/Roboto/";
    private static final String typeface = "fonts/Roboto/Roboto-Medium.ttf";

    private static final IntentFilter INTENT_FILTER;  //Intent that matches with some of your Actions will be detected by broadcast
    static{
        INTENT_FILTER = new IntentFilter();
        INTENT_FILTER.addAction(Intent.ACTION_TIME_TICK);
        INTENT_FILTER.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        INTENT_FILTER.addAction(Intent.ACTION_TIME_CHANGED);
    }

    private String time_format_display = "mm";
    private TextView mTimeHours,mTimeMinutes,mBattery;

    private BroadcastReceiver mTimeInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mTimeHours.setText(String.valueOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY))/*new SimpleDateFormat(time_format_display).format(Calendar.getInstance().getTime())*/);
            mTimeMinutes.setText(new SimpleDateFormat(time_format_display).format(Calendar.getInstance().getTime())/*String.valueOf(Calendar.getInstance().get(Calendar.MINUTE))*/);
        }
    };

    private BroadcastReceiver mBatteryInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mBattery.setText(String.valueOf(intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0))+"%");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_watch_face);

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTimeHours = (TextView) stub.findViewById(R.id.watch_time_hours);
                mTimeMinutes = (TextView) stub.findViewById(R.id.watch_time_minutes);
                mBattery = (TextView) stub.findViewById(R.id.watch_battery);
                setTypeface(mTimeHours);
                setTypeface(mTimeMinutes);
                setTypeface(mBattery);

                mTimeInfoReceiver.onReceive(MyWatchFaceActivity.this, registerReceiver(null,INTENT_FILTER));
                registerReceiver(mTimeInfoReceiver, INTENT_FILTER);
                registerReceiver(mBatteryInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            }
        });

    }

    private void setTypeface(TextView textView){
        Typeface typefaceAux = Typeface.createFromAsset(getAssets(),typeface);
        textView.setTypeface(typefaceAux);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mTimeInfoReceiver);
        unregisterReceiver(mBatteryInfoReceiver);
    }
}
