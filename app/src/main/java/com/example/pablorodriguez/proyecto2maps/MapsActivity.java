package com.example.pablorodriguez.proyecto2maps;

import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public static double latitud, longitud;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng marca = new LatLng(42.2366, -8.714552);
        mMap.addMarker(new MarkerOptions().position(marca).title("Marca en DanielCastelao"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(marca));

        LatLng circulo = new LatLng(42.236947, -8.713538);
        int radius = 100;

        CircleOptions circleOptions = new CircleOptions()
                .center(circulo)
                .radius(radius)
                .strokeColor(Color.parseColor("#050016"))
                .strokeWidth(4)
                .fillColor(Color.argb(32, 33, 150, 243));

        Circle circleObx = mMap.addCircle(circleOptions);

    }

    private void updateUI(Location loc) {

        if (loc != null) {
            latitud = loc.getLatitude();
            longitud = loc.getLongitude();

        } else {
            Toast.makeText(this, "No se reconoce la latitud y longitud", Toast.LENGTH_LONG).show();

        }


    }

}
