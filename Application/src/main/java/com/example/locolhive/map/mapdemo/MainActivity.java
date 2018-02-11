package com.example.locolhive.map.mapdemo;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class MainActivity extends AppCompatActivity implements PlaceSelectionListener {

    private static String TAG = MainActivity.class.getSimpleName() + "YOYO";

    private TextView mPlaceDetailsText;
    private TextView mPlaceAttribution;
    private static final int REQUEST_PLACE_PICKER = 1;
    Place placeSelected;
    Button btnViewRegion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setCountry("IN")
                .build();

        autocompleteFragment.setOnPlaceSelectedListener(this);
        autocompleteFragment.setFilter(typeFilter);

        mPlaceDetailsText = findViewById(R.id.place_details);
        mPlaceAttribution = findViewById(R.id.place_attribution);
        btnViewRegion  = findViewById(R.id.btn_viewRegion);

        btnViewRegion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(placeSelected!=null){
                    if(placeSelected.isDataValid()){
                        Intent i = new Intent(MainActivity.this,MapActivity.class);
                        i.putExtra("Lat",placeSelected.getLatLng().latitude);
                        i.putExtra("Lng",placeSelected.getLatLng().longitude);
                        startActivity(i);
                    }
                }
            }
        });
    }

    @Override
    public void onPlaceSelected(Place place) {
        Log.i(TAG, "Place Selected: " + place.getName());
        LatLng latLng = place.getLatLng();
        Log.i(TAG, "Latlong: " + latLng.toString());

        LatLng latLng1 = new LatLng(latLng.latitude-0.01, latLng.longitude);
        LatLng latLng2 = new LatLng(latLng.latitude+0.01, latLng.longitude);
        LatLngBounds latLngBounds =
                LatLngBounds.builder()
                        .include(latLng)
                        .include(latLng1)
                        .include(latLng2).build();

        try {
            PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder().setLatLngBounds(latLngBounds);
            Intent intent = intentBuilder.build(this);
            // Start the Intent by requesting a result, identified by a request code.
            startActivityForResult(intent, REQUEST_PLACE_PICKER);

        } catch (GooglePlayServicesRepairableException e) {
            GooglePlayServicesUtil
                    .getErrorDialog(e.getConnectionStatusCode(), this, 0);
        } catch (GooglePlayServicesNotAvailableException e) {
            Toast.makeText(this, "Google Play Services is not available.",
                    Toast.LENGTH_LONG)
                    .show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // BEGIN_INCLUDE(activity_result)
        if (requestCode == REQUEST_PLACE_PICKER) {
            // This result is from the PlacePicker dialog.


            if (resultCode == AppCompatActivity.RESULT_OK) {
                /* User has picked a place, extract data.
                   Data is extracted from the returned intent by retrieving a Place object from
                   the PlacePicker.
                 */
                final Place place = PlacePicker.getPlace(data, this);
                placeSelected = place;
                btnViewRegion.setEnabled(true);
                ///         Format the returned place's details and display them in the TextView.
                mPlaceDetailsText.setText(formatPlaceDetails(getResources(), place.getName(), place.getId(),
                        place.getAddress(), place.getPhoneNumber(), place.getWebsiteUri()));

                CharSequence attributions = place.getAttributions();
                if (!TextUtils.isEmpty(attributions)) {
                    mPlaceAttribution.setText(Html.fromHtml(attributions.toString()));
                } else {
                    mPlaceAttribution.setText("");
                }

            } else {
                // User has not selected a place, hide the card.
                placeSelected = null;
                btnViewRegion.setEnabled(false);
               Log.e(TAG, "No place Selected");
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
        // END_INCLUDE(activity_result)
    }

    @Override
    public void onError(Status status) {
        Log.e(TAG, "onError: Status = " + status.toString());

        Toast.makeText(this, "Place selection failed: " + status.getStatusMessage(),
                Toast.LENGTH_SHORT).show();
    }

    private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
            CharSequence address, CharSequence phoneNumber, Uri websiteUri) {
        Log.e(TAG, res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));
        return Html.fromHtml(res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));

    }

}
