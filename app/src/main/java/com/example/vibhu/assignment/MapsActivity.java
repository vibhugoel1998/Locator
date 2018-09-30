package com.example.vibhu.assignment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Interpolator;
import android.graphics.Point;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Double lat;
    Double lon;
    String name;
    Double finalLat;
    Double finalLon;
    LinearLayout linearLayout;
    EditText editText;
    Intent intent;
    String type;
    Button addButton;
    Double cLat;
    Double cLon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        addButton=findViewById(R.id.addbutton);
        intent = getIntent();
        type=intent.getStringExtra("type");
        if(type.equals("1")){
            lat = Double.parseDouble(intent.getStringExtra("lat"));
            lon = Double.parseDouble(intent.getStringExtra("lon"));
            Log.d("help", lat + "");
            Log.d("help", lon + "");
            name = intent.getStringExtra("name");
            addButton.setVisibility(View.INVISIBLE);
        }
        else
        {
            cLat=intent.getDoubleExtra("clat",0.0);
            cLon=intent.getDoubleExtra("clon",0.0);
            finalLon=cLon;
            finalLat=cLat;
        }
        linearLayout=findViewById(R.id.newLinear);
        linearLayout.setVisibility(View.INVISIBLE);
        editText=findViewById(R.id.newname);




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
          initCamera();
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();

                // Setting the position for the marker
                markerOptions.position(latLng);

                // Setting the title for the marker.
                // This will be displayed on taping the marker
                markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                finalLat=latLng.latitude;
                finalLon=latLng.longitude;



                // Clears the previously touched position
                mMap.clear();

                // Animating to the touched position
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                // Placing a marker on the touched position
                mMap.addMarker(markerOptions);
            }
        });




    }

    public void initCamera() {
        LatLng place;
        if(type.equals("1")){
            place=new LatLng(lat,lon);
        }
        else {
            place=new LatLng(cLat,cLon);
            name="Central Delhi";
        }
        CameraPosition position = CameraPosition.builder()
                .target( place )
                .zoom( 15f )
                .bearing( 0.0f )
                .tilt( 0.0f )
                .build();

        mMap.animateCamera(
                CameraUpdateFactory.newCameraPosition( position ), null );
        mMap.addMarker(new MarkerOptions().position(place).title(name));
        mMap.setMapType( GoogleMap.MAP_TYPE_HYBRID );
    }
    public void Back(View view){
        finish();
    }
    public void Add(View view){
        if(finalLat!=null && finalLon!=null){
            linearLayout.setVisibility(View.VISIBLE);
        }
        else{
            finish();
        }
    }
    public void Submit1234(View view){
        if(editText!=null && editText.getText()!=null){
            Log.d("hello4",editText.getText().toString());
            intent.putExtra("name",editText.getText().toString());
            intent.putExtra("lat",finalLat+"");
            intent.putExtra("lon",finalLon+"");
            setResult(1,intent);
            finish();
        }
    }

}
