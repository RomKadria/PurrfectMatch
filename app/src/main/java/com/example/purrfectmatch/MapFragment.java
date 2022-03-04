package com.example.purrfectmatch;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment implements OnMapReadyCallback {

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        // Retrieve the content view that renders the map.
//        setContentView(R.layout.fragment_map);
//
//        // Get the SupportMapFragment and request notification when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//    }


    View view;
    FragmentManager manager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_map, container, false);
//
//        SupportMapFragment mapFragment = (SupportMapFragment) getParentFragmentManager ()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);

        manager = getParentFragmentManager();

        FragmentTransaction transaction = manager.beginTransaction();
        SupportMapFragment fragment = new SupportMapFragment();
        transaction.add(R.id.mapView, fragment);
        transaction.commit();

        fragment.getMapAsync(this);

        return view;
    }
    @SuppressLint("PotentialBehaviorOverride")
    @Override
    public void onMapReady(GoogleMap map) {
        Marker myMarker;
        myMarker = map.addMarker(new MarkerOptions()
                .position(new LatLng(31.951339, 34.805291))
                .title("RomHouse")
                .snippet("123@gmail.com"));

        map.setOnMarkerClickListener(marker -> {
            String markerName = marker.getTitle();
            String petId = marker.getSnippet();
            Navigation.findNavController(view).navigate(MapFragmentDirections.actionMapFragmentToPetDetailsFragment2(petId));
            return false;
        });
    }
}