package com.phivle.yo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MainActivity extends Activity implements LocationListener, View.OnClickListener {

    private LocationManager mLocationManager;
    private Location mCurrentLocation;
    private String mProvider;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        //mProvider = mLocationManager.getBestProvider(criteria, true);
        mProvider = LocationManager.GPS_PROVIDER;
        mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLocationManager.removeUpdates(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onClick(View view) {

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        String shareBody;

        sharingIntent.setType("text/plain");

        mLocationManager.requestLocationUpdates(mProvider, 1000l, 0f, this);
        mCurrentLocation = mLocationManager.getLastKnownLocation(mProvider);

        shareBody = getMyLocationAddress();
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public String getMyLocationAddress() {

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String myLocationAddress = "";

        try {
            List<Address> addresses = geocoder.getFromLocation(mCurrentLocation.getLatitude(),
                                                                mCurrentLocation.getLongitude(), 1);

            if (addresses != null && addresses.size() > 0) {
                Address fetchedAddress = addresses.get(0);
                StringBuilder strAddress = new StringBuilder();

                for (int i = 0; i < fetchedAddress.getMaxAddressLineIndex(); ++i) {
                     strAddress.append(fetchedAddress.getAddressLine(i)).append("\n");
                }

                myLocationAddress = strAddress.toString();

            }

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),
                           "Could not get address",
                           Toast.LENGTH_LONG)
                           .show();
        }

        return myLocationAddress;
    }
}
