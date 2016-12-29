package com.dhananjay.erudite.Map;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dhananjay.erudite.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class GMapFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener ,LocationListener{

    View view;
    GoogleMap googleMap;
    boolean mapReady=false;
    GoogleApiClient googleApiClient;
    LocationRequest locationRequest;
    Location lastLocation;
    Marker currLocationMarker;
    String TAG="GMapFragment";


    public GMapFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gmap, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view=view;
        MapFragment mapFragment= (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.gmap);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mapReady=true;
        googleMap=map;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        Log.d(TAG, "onMapReady: ");
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                googleMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            googleMap.setMyLocationEnabled(true);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        Log.d(TAG, "buildGoogleApiClient: ");
        googleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected: ");
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "onConnected: permission granted");
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended: ");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: ");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged: ");
        lastLocation = location;
        if (currLocationMarker != null) {
            currLocationMarker.remove();
        }
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraPosition target=CameraPosition.builder().target(latLng).zoom(11).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(target));
        getNearbyPlaces("hospital");
        Button hospitals= (Button) view.findViewById(R.id.hospitals);
        hospitals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNearbyPlaces("hospital");
            }
        });
        Button doctors= (Button) view.findViewById(R.id.doctors);
        doctors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNearbyPlaces("doctor");
            }
        });
        Button pharmacies= (Button) view.findViewById(R.id.pharmacies);
        pharmacies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNearbyPlaces("pharmacy");
            }
        });
        if (googleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }
    }

    private void getNearbyPlaces(String type) {
        Log.d(TAG, "getNearbyHospitals: ");
        String url = "https://maps.googleapis.com/maps/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MapsAPI service = retrofit.create(MapsAPI.class);
        Call<ResponseObject> call = service.getNearbyPlaces(type, lastLocation.getLatitude() + "," + lastLocation.getLongitude(), 5000);
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                try {
                    googleMap.clear();
                    MarkerOptions currMarkerOptions = new MarkerOptions();
                    currMarkerOptions.position(new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude()));
                    currMarkerOptions.title("Current Position");
                    currMarkerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_current));
                    currLocationMarker = googleMap.addMarker(currMarkerOptions);
                    Log.d(TAG, "onResponse: "+response.body().getStatus());
                    for (int i = 0; i < response.body().getResults().size(); i++) {
                        Double lat = response.body().getResults().get(i).getGeometry().getLocation().getLat();
                        Double lng = response.body().getResults().get(i).getGeometry().getLocation().getLng();
                        String placeName = response.body().getResults().get(i).getName();
                        String vicinity = response.body().getResults().get(i).getVicinity();
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_place));
                        LatLng latLng = new LatLng(lat, lng);
                        markerOptions.position(latLng);
                        Log.d(TAG, "onResponse: size>0");
                        markerOptions.title(placeName + " : " + vicinity);
                        Marker m = googleMap.addMarker(markerOptions);
                    }
                } catch (Exception e) {
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                }
                CameraPosition target=CameraPosition.builder().target(new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude())).zoom(11).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(target));
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Log.d("onFailure", t.toString());
            }

        });
    }
}
