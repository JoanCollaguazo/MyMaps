package com.example.mymaps.ui.home;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.mymaps.R;
import com.example.mymaps.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private Button btnUb, btnMap;
    private EditText txtLat;
    private EditText txtLong;
    private EditText txtAlt;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        btnUb = root.findViewById(R.id.buttonUbicacion);
        btnMap = root.findViewById(R.id.buttonMap);
        txtLat = root.findViewById(R.id.editLong);
        txtLong = root.findViewById(R.id.editLat);
        txtAlt = root.findViewById(R.id.editAlt);
        //DEBO PONERLE THIS A LOS BOTONES:
        btnUb.setOnClickListener(this);
        btnMap.setOnClickListener(this);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v) {
        if (v == btnUb) {
            miposicion();

            //Toast.makeText(getContext(),"clic bton",Toast.LENGTH_LONG).show();
        }
        if (v == btnMap) {
            Intent intentMap = new Intent();

        }
    }

    private void miposicion() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) getContext(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.INTERNET}, 1000);
        }

        LocationManager objLocation = null;
        LocationListener objLocListener;

        objLocation = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        objLocListener = new Miposicion();
        objLocation.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, objLocListener);

        if (objLocation.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            txtLong.setText(Miposicion.longitud + "");
            txtLat.setText(Miposicion.latitud + "");
            txtAlt.setText(Miposicion.altitud+"");

        } else {
            Toast.makeText(getContext(), "GPS DESABILITADO", Toast.LENGTH_LONG).show();
        }

    }

}