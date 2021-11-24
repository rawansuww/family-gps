package com.example.myapplication;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.myapplication.databinding.ActivityMapsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    Person[] persons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //WRITING TO FIREBASE
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Locations");
        double latitude = 75.85;
        double longitude = 76.899;
        double speed = 240;
        Location x = new Location(latitude, longitude, speed); //subject to change as for RDB as key identifier is the PERSON name
        Person personX = new Person("Dina AlAshakr", 25, x);
        personX.setLoc(x);
        myRef.child("Dina's iPhone").setValue(personX);

        // Read from the database ---------------------------

        persons = new Person[2];
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               int i=0;
               for (DataSnapshot ds: dataSnapshot.getChildren())
               {
                   persons[i++] = ds.getValue(Person.class);
                   //Log.d("halo", String.valueOf(persons[i++].getName()));
                   /*

*/
               }


               for (int j=0; j<persons.length; j++){
                   Log.d("halo", String.valueOf(persons[j]));
                   /*mMap.addMarker(new MarkerOptions().position(new LatLng(
                           persons[j].getLoc().getLatitude(), persons[j].getLoc().getLongitude()))).setTitle(
                           persons[j].getName()+
                                   ": \nLongi="+persons[j].getLoc().getLongitude()+
                                   "\nLatitude="+persons[j].getLoc().getLatitude()+
                                   "\n Speed:"+ persons[j].getLoc());

                   mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(persons[j].getLoc().getLatitude(), persons[j].getLoc().getLongitude())));
              */ }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("halooo", "Failed to read value.", error.toException());

            }
        });
    }

        /* Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney)); ****/

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;





            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                @Override
                public View getInfoWindow(Marker marker){
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    View v = getLayoutInflater().inflate(R.layout.marker_layout, null);
                    v.setLayoutParams(new RelativeLayout.LayoutParams(800, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    TextView Longi = v.findViewById(R.id.longitudeTextView);
                    Longi.setText(marker.getTitle());
                    return v;
                }
            });

        }

       /* @Override
        public void onMapLongClick(LatLng latLng) {
            Location NewLocationMarker = new Location("new");
            NewLocationMarker.setLatitude(latLng.latitude);
            NewLocationMarker.setLongitude(latLng.longitude);
            MainActivity.locationList.add(NewLocationMarker);

            mMap.addMarker(new MarkerOptions().position(latLng)).setTitle("Longi="+latLng.longitude + "\nLatitude="+latLng.latitude);
        }*/
    }


