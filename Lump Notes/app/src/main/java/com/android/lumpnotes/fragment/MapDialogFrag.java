package com.android.lumpnotes.fragment;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import com.android.lumpnotes.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MapDialogFrag  extends DialogFragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private double longitude;
    private double latitude;
    private View view;
    private TextView addressTV;

    public MapDialogFrag(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.map_popup_layout, container, false);
        } catch (InflateException e) {
            e.printStackTrace();
        }
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        addressTV = view.findViewById(R.id.tvCityName);
        mapFragment.getMapAsync(this);
        //Close the popup in click of X button
        view.findViewById(R.id.tvCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        getCurrentLocation();
    }
    //current location
    private void getCurrentLocation() {
        mMap.clear();
        if(latitude!=0 && longitude!=0) {
            LatLng myLoc = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(myLoc));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLoc, 15));

            mMap.getUiSettings().setZoomGesturesEnabled(true);
            findLocation();
        } else {
            if (ActivityCompat.checkSelfPermission(this.getContext(), ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(),
                    ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this.getActivity(), new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, 123);
            } else {
                FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.getContext());
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(this.getActivity(), new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    //longitude and latitude
                                    longitude = location.getLongitude();
                                    latitude = location.getLatitude();
                                    LatLng myLoc = new LatLng(latitude, longitude);
                                    mMap.addMarker(new MarkerOptions().position(myLoc));
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLoc, 15));

                                    mMap.getUiSettings().setZoomGesturesEnabled(true);
                                    findLocation();
                                }
                            }
                        });
            }
        }
    }

    private void findLocation() {
        try {
            Geocoder geocoder = new Geocoder(this.getContext());
            List<Address> addresses = null;
            addresses = geocoder.getFromLocation(latitude,longitude,1);
            if(addressTV!=null) {
                addressTV.setText(addresses.get(0).getAddressLine(0));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this.getContext(),"Error:"+e,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        mMap.setMyLocationEnabled(true);
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.getContext());
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this.getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            //longitude and latitude
                            longitude = location.getLongitude();
                            latitude = location.getLatitude();
                            LatLng myLoc = new LatLng(latitude, longitude);
                            mMap.addMarker(new MarkerOptions().position(myLoc));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLoc, 15));

                            mMap.getUiSettings().setZoomGesturesEnabled(true);
                            findLocation();
                        }
                    }
                });
    }


}
