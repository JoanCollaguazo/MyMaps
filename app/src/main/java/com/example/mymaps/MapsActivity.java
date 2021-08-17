package com.example.mymaps;

import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Camera;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.mymaps.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, View.OnClickListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private double longitud, latitud;
    SharedPreferences preferences;
    private Button btnfavoritos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        btnfavoritos = (Button) findViewById(R.id.fav);
        btnfavoritos.setOnClickListener(this);
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
        //OBTENGO LOS DATOS DE LATITUD Y LONGITUD
        preferences = getSharedPreferences("My preferences", Context.MODE_PRIVATE);
        longitud = Double.parseDouble(getIntent().getStringExtra("longitud"));
        latitud = Double.parseDouble(getIntent().getStringExtra("latitud"));
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(latitud, longitud);

        mMap.addMarker(new MarkerOptions().position(sydney).title("Mi ubicacion"));
        //mover la camara a mi ubicacion
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        //habilito los controles de zoom
        mMap.getUiSettings().setZoomControlsEnabled(true);
        //doy zoom 16
        CameraUpdate ZoomCam = CameraUpdateFactory.zoomTo(16);
        mMap.animateCamera(ZoomCam);

        //fijo el long
        mMap.setOnMapLongClickListener(this);


    }

    public void guardarPreferences(LatLng latLng) {

        SharedPreferences.Editor editor = preferences.edit();

        editor.putFloat("latitud", (float) latLng.latitude);
        editor.putFloat("longitud", (float) latLng.longitude);
        editor.commit();
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        Toast.makeText(MapsActivity.this,
                "Click Posicion" + latLng.latitude + latLng.longitude, Toast.LENGTH_LONG).show();
        mMap.addMarker(new MarkerOptions().position(latLng).title("Mi Ubicacion"));
        guardarPreferences(latLng);
    }

    public void cargarPreferencias() {
        double lat = preferences.getFloat("latitud", 0);
        double log = preferences.getFloat("longitud", 0);
        if (lat != 0) {
            LatLng puntoPref = new LatLng(lat, log);
            mMap.addMarker(new MarkerOptions().position(puntoPref).title("Mi ubicacion"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(puntoPref));
        }else{
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("No tiene ningun sitio favorito");
            alert.setPositiveButton("OK",null);
            alert.create().show();
        }

        Toast.makeText(MapsActivity.this, "mi favorito es: " + lat + log, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        if (v == btnfavoritos) {
            cargarPreferencias();
        }
    }
}