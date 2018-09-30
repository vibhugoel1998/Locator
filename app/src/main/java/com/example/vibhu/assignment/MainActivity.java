package com.example.vibhu.assignment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.Context;
import android.widget.Toast;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    CustomAdapter adapter;
    ArrayList<Details> arrayList;
    private DatabaseReference mDatabase;
    TextView textView;
    protected Context context;
    Double currentLat;
    Double currentLon;
    LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.noitems);
        textView.setVisibility(View.INVISIBLE);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        listView = findViewById(R.id.mainlist);
        arrayList = new ArrayList<>();
        adapter = new CustomAdapter(arrayList, this);
        listView.setAdapter(adapter);
        GPSTracker mGPS = new GPSTracker(this);
        if(mGPS.canGetLocation ){
            mGPS.getLocation();
            currentLat=mGPS.getLatitude();
            currentLon=mGPS.getLongitude();
        }
        else{
            Toast.makeText(MainActivity.this, "No", Toast.LENGTH_SHORT).show();
        }
        if(currentLon==null || currentLat==null){
            currentLat=28.6330;
            currentLon=77.2194;
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,MapsActivity.class);
                intent.putExtra("type","2");
                intent.putExtra("clat",currentLat);
                intent.putExtra("clon",currentLon);
                startActivityForResult(intent,1);
            }
        });
        mDatabase.child("Details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("check","yes1");
                String check=dataSnapshot.getValue(String.class);
                if(check.equals("no")){
                    textView.setVisibility(View.VISIBLE);
                }
                else if(check.equals("yes")){
                    Log.d("check","yes");
                    mDatabase.child("Values").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot snapshot:dataSnapshot.getChildren())
                            {
                                Log.d("check","yes");
                                if(snapshot.child("area")!=null && snapshot.child("lat")!=null && snapshot.child("lon")!=null)
                                {
                                    String name=snapshot.getKey();
                                    Log.d("check",name);
                                    String area=snapshot.child("area").getValue(String.class);
                                    String lat=snapshot.child("lat").getValue(Double.class)+"";
                                    String lon=snapshot.child("lon").getValue(Double.class)+"";
                                    Details newObject=new Details(name,area,lat,lon);
                                    arrayList.add(newObject);
                                    adapter.notifyDataSetChanged();
                                }
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("hello3","yes");
        if(requestCode==1 && resultCode==1){
            Log.d("hello1","yes");
            if(data!=null){
                Log.d("hello2","yes");
                Double lati=Double.parseDouble(data.getStringExtra("lat"));
                Double longi=Double.parseDouble(data.getStringExtra("lon"));
                String hello1=data.getStringExtra("lat");
                String hello2=data.getStringExtra("lon");
                String name=data.getStringExtra("name");
                String area="not specified";
                Details newObj=new Details(name,area,hello1,hello2);
                arrayList.add(newObj);
                adapter.notifyDataSetChanged();
                mDatabase.child("Values").child(name).child("lat").setValue(lati);
                mDatabase.child("Values").child(name).child("lon").setValue(longi);
                mDatabase.child("Values").child(name).child("area").setValue(area);
                mDatabase.child("Values").child(name).child("name").setValue(name);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
