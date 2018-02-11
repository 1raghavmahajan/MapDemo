package com.example.locolhive.map.mapdemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private LatLng initLatLong = new LatLng(-33.87365, 151.20689);
    private static final double DEFAULT_RADIUS_METERS = 250;

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

        map.addMarker(new MarkerOptions()
                .position(initLatLong)
                .draggable(false));

        Circle mCircle = map.addCircle(new CircleOptions()
                .center(initLatLong)
                .radius(DEFAULT_RADIUS_METERS)
                .strokeWidth(mStrokeWidth)
                .strokeColor(mStrokeColorArgb)
                .fillColor(mFillColorArgb)
                .clickable(false));
        mCircle.setStrokePattern(null);

        // Move the map so that it is centered on the initial circle
        LatLng latLng1 = new LatLng(initLatLong.latitude+0.009,initLatLong.longitude);
        LatLng latLng2 = new LatLng(initLatLong.latitude-0.009,initLatLong.longitude);

        LatLngBounds latLngBounds =
                LatLngBounds.builder()
                        .include(initLatLong)
                        .include(latLng1)
                        .include(latLng2).build();

        map.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds,10));
    }

}