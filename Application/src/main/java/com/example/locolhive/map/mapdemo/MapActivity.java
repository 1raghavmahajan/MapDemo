package com.example.locolhive.map.mapdemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.Calendar;
import java.util.Random;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private LatLng initLatLong = new LatLng(-33.87365, 151.20689);
    private static final double DEFAULT_RADIUS_METERS = 250;
    Circle mCircle;
    SeekBar seekBar;
    TextView tv_rad;

    static final private int mStrokeWidth = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            double lat = extras.getDouble("Lat", -33.87365);
            double lng = extras.getDouble("Lng", 151.20689);
            initLatLong = new LatLng(lat,lng);
        }

        seekBar = findViewById(R.id.radius);
        seekBar.setProgress(250);

        tv_rad = findViewById(R.id.tv_rad);
        tv_rad.setText(seekBar.getProgress()+" meters");

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        // Override the default content description on the view, for accessibility mode.
        map.setContentDescription(getString(R.string.map_circle_description));

        int mFillColorArgb = Color.HSVToColor(
                81, new float[]{214, 1, 1});
        int mStrokeColorArgb = Color.HSVToColor(
                79, new float[]{202, 1, 1});

//        map.addMarker(new MarkerOptions()
//                .position(initLatLong)K
//                .draggable(false));

        Random randomGen = new Random(Calendar.getInstance().getTime().getTime());
        double v = randomGen.nextInt(200);
        v=v/111000.0;
        if(randomGen.nextBoolean()){
            v=v*-1;
        }
        double v2 = randomGen.nextInt(200);
        v2=v2/111000.0;
        if(randomGen.nextBoolean()){
            v2=v2*-1;
        }
        LatLng rand = new LatLng(initLatLong.latitude+v,initLatLong.longitude+v2);

        mCircle = map.addCircle(new CircleOptions()
                .center(rand)
                .radius(DEFAULT_RADIUS_METERS)
                .strokeWidth(mStrokeWidth)
                .strokeColor(mStrokeColorArgb)
                .fillColor(mFillColorArgb)
                .clickable(false));
        mCircle.setStrokePattern(null);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                tv_rad.setText(progress+" meters");
                mCircle.setRadius(progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // Move the map so that it is centered on the initial circle
        LatLng latLng1 = new LatLng(rand.latitude+0.009,rand.longitude);
        LatLng latLng2 = new LatLng(rand.latitude-0.009,rand.longitude);

        LatLngBounds latLngBounds =
                LatLngBounds.builder()
                        .include(rand)
                        .include(latLng1)
                        .include(latLng2).build();

        map.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds,10));
    }

}