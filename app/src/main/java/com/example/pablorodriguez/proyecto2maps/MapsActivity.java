package com.example.pablorodriguez.proyecto2maps;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleApiClient.OnConnectionFailedListener,GoogleApiClient.ConnectionCallbacks, GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    public static double latitud,longitud;
    public static final int LOCATION_REQUEST_CODE = 1;
    private GoogleApiClient GoogleAPI;
    private static final String LOGTAG = "android-localizacion";
    public static Marker marcaOBX;
    CircleOptions circleOptions = new CircleOptions();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        GoogleAPI = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
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
        mMap.setOnMapClickListener(this);


        // Add a marker in Sydney and move the camera
        LatLng sitio = new LatLng(42.2366, -8.714552);
        mMap.addMarker(new MarkerOptions().position(sitio).title("Marca en DanielCastelao"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sitio));
        //marcaOBX.setVisible(false);

        LatLng circulo = new LatLng(42.236947, -8.713538);
        int radius = 100;

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_REQUEST_CODE);
            }
        }
        mMap.getUiSettings().setZoomControlsEnabled(true);

        circleOptions = new CircleOptions()
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

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (permissions.length > 0 &&
                    permissions[0].equals(android.Manifest.permission.ACCESS_FINE_LOCATION) &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mMap.setMyLocationEnabled(true);
            } else {
                Toast.makeText(this, "Error de permisos", Toast.LENGTH_LONG).show();


                @SuppressWarnings("MissingPermission")
                Location lastLocation =
                        LocationServices.FusedLocationApi.getLastLocation(GoogleAPI);

                updateUI(lastLocation);

            }
        }
    }

    public void calcularDistancia() {

        double earthRadius = 6372.795477598;

        double disLati = Math.toRadians(latitud-42.2366);
        double disLongi = Math.toRadians(longitud-8.714552);
        double a = Math.sin(disLati/2) * Math.sin(disLati/2) +
                Math.cos(Math.toRadians(42.2366)) * Math.cos(Math.toRadians(latitud)) *
                        Math.sin(disLongi/2) * Math.sin(disLongi/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = earthRadius * c;
        double distMarc=dist*1000;
        String distancia=String.valueOf(distMarc);

        Toast.makeText(this,"Metros que le separan de la zona de destino:" + distancia, Toast.LENGTH_LONG).show();

        if(distMarc<=20){
            //marcaOBX.setVisible(true);
        }else {
            //marcaOBX.setVisible(false);
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(GoogleAPI);
        updateUI(lastLocation);
        calcularDistancia();


    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {


        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_REQUEST_CODE);
        } else {

            Location lastLocation =
                    LocationServices.FusedLocationApi.getLastLocation(GoogleAPI);

            updateUI(lastLocation);
        }
    }
    @Override
    public void onConnectionSuspended(int i) {


        Log.e(LOGTAG, "Conexión interrumpida...");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {


        Log.e(LOGTAG, "Error en la conexión...!!");
    }
}