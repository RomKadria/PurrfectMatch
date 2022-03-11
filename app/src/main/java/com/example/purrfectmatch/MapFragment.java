package com.example.purrfectmatch;

import android.annotation.SuppressLint;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

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
    SearchView searchView;
    GoogleMap map;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_map, container, false);
//
//        SupportMapFragment mapFragment = (SupportMapFragment) getParentFragmentManager ()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);

        manager = getParentFragmentManager();
        searchView = view.findViewById(R.id.idSearchView);
        searchView.setIconified(false);
        searchView.setQueryHint("Search here");


        FragmentTransaction transaction = manager.beginTransaction();
        SupportMapFragment fragment = new SupportMapFragment();
        transaction.add(R.id.mapView, fragment);
        transaction.commit();

        fragment.getMapAsync(this);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // on below line we are getting the
                // location name from search view.
                String location = searchView.getQuery().toString();

                // below line is to create a list of address
                // where we will store the list of all address.
                List<Address> addressList = null;

                // checking if the entered location is null or not.
                if (location != null || location.equals("")) {
                    // on below line we are creating and initializing a geo coder.
                    Geocoder geocoder = new Geocoder(getContext());
                    try {
                        // on below line we are getting location from the
                        // location name and adding that location to address list.
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // on below line we are getting the location
                    // from our list a first position.
                    if(addressList.isEmpty() == false) {

                        Address address = addressList.get(0);
                        // on below line we are creating a variable for our location
                        // where we will add our locations latitude and longitude.
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                        // on below line we are adding marker to that position.
                        map.addMarker(new MarkerOptions().position(latLng).title(location));

                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return view;
    }
    @SuppressLint("PotentialBehaviorOverride")
    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        Marker myMarker;
        myMarker = map.addMarker(new MarkerOptions()
                .position(new LatLng(31.951339, 34.805291))
                .title("RomHouse")
                .snippet("123@gmail.com"));

        map.setOnMapClickListener(point -> {
            Toast.makeText(getContext(), "Map clicked [" + point.latitude + " / " + point.longitude + "]", Toast.LENGTH_SHORT).show();

            //Do your stuff with LatLng here
            //Then pass LatLng to other activity
        });

        map.setOnMarkerClickListener(marker -> {
            String petId = marker.getSnippet();
//            Navigation.findNavController(view).navigate(MapFragmentDirections.actionMapFragmentToPetDetailsFragment2(petId));
            return false;
        });
    }
}