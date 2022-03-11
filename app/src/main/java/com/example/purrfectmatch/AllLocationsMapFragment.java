package com.example.purrfectmatch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.navigation.Navigation;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.purrfectmatch.model.AppLocalDb;
import com.example.purrfectmatch.model.Model;
import com.example.purrfectmatch.model.Pet;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;


public class AllLocationsMapFragment extends Fragment {
    private GoogleMap mMap;
    SearchView searchView;
    View view;
    String email;
    boolean focusMyLocation = false;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            Marker myMarker;
            LiveData<List<Pet>> petList = Model.instance.getAll();

            for (int i=0; i<petList.getValue().size(); i++) {
                Pet currentPet = petList.getValue().get(i);
                if (focusMyLocation == false && currentPet.getEmail() == email) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentPet.getLatitude(), currentPet.getLongitude()), 10));
                }
                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(currentPet.getLatitude(),
                                    currentPet.getLongitude()))
                            .title(currentPet.getName())
                            .snippet(currentPet.getEmail()));
            }

            if (focusMyLocation == true) {
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentPet.getLatitude(), currentPet.getLongitude()), 10))
            }
//            myMarker = mMap.addMarker(new MarkerOptions()
//                    .position(new LatLng(31.951339, 34.805291))
//                    .title("RomHouse")
//                    .snippet("123@gmail.com"));

            mMap.setOnMarkerClickListener(marker -> {
                String petId = marker.getSnippet();
                Navigation.findNavController(view).navigate(AllLocationsMapFragmentDirections.actionAllLocationsMapFragmentToPetDetailsFragment(petId));
                return false;
            });
//            LatLng sydney = new LatLng(-34, 151);
//            googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            email = AllLocationsMapFragmentArgs.fromBundle(getArguments()).getEmail();
        }

        if (email == null) {
            focusMyLocation = true;
        }
        view = inflater.inflate(R.layout.fragment_user_location_map, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.userLocationMap);

        searchView = view.findViewById(R.id.userLocationSv);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                String location = searchView.getQuery().toString();

                List<Address> addressList = null;

                if (location != null || location.equals("")) {
                    Geocoder geocoder = new Geocoder(getContext());
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (addressList.isEmpty() == false) {
                        Address address = addressList.get(0);

                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}