package com.example.smartgp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartgp.Adapter.SearchAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    LinearLayout linearLayout;
    Location curLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    FirebaseUser fuser;
    EditText search_Text;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    ArrayList<String> clinicIdList, clinicNameList, addressList, phoneNumberList, openingHourList;
    SearchAdapter searchAdapter;
    SupportMapFragment supportMapFragment;

    private static final int REQUEST_CODE = 101;

    private BottomSheetBehavior bottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        Log.d("MyTAG", "Current User ID " + fuser.getUid());

        //hooks
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navView);
        toolbar = findViewById(R.id.toolbar);

        //tool bar
        setSupportActionBar(toolbar);

        //nav drawer
        navigationView.bringToFront();
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLocation();

        //bottom sheet
        linearLayout = findViewById(R.id.design_bottom_sheet);
        bottomSheetBehavior = bottomSheetBehavior.from(linearLayout);

        //search clinic
        search_Text = (EditText) findViewById(R.id.search_Text);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        clinicIdList = new ArrayList<>();
        clinicNameList = new ArrayList<>();
        addressList = new ArrayList<>();
        phoneNumberList = new ArrayList<>();
        openingHourList = new ArrayList<>();


        search_Text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    clinicIdList.clear();
                    clinicNameList.clear();
                    addressList.clear();
                    phoneNumberList.clear();
                    openingHourList.clear();
                    recyclerView.setAlpha(0);
               /* } else if (s.toString().isEmpty()) {
                    recyclerView.setVisibility(View.INVISIBLE);*/
                } else {
                    recyclerView.setAlpha(1);
                    setAdapter(s.toString());

                }
            }
        });
    }

    private void setAdapter(final String searchString) {
        databaseReference.child("Clinic").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                clinicIdList.clear();
                clinicNameList.clear();
                addressList.clear();
                phoneNumberList.clear();
                openingHourList.clear();

                int count = 0;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String uid = snapshot.getKey();
                    String clinic_name = snapshot.child("clinicName").getValue(String.class);
                    String address = snapshot.child("address").getValue(String.class);
                    String clinicHP = snapshot.child("clinicPhoneNo").getValue(String.class);
                    String openingHrs = snapshot.child("openingHour").getValue(String.class);

                    if (clinic_name.toUpperCase().contains(searchString.toUpperCase())) {
                        clinicIdList.add(uid);
                        clinicNameList.add(clinic_name);
                        addressList.add(address);
                        phoneNumberList.add(clinicHP);
                        openingHourList.add(openingHrs);
                        count++;
                    } else if (address.toUpperCase().contains(searchString.toUpperCase())) {
                        clinicIdList.add(uid);
                        clinicNameList.add(clinic_name);
                        addressList.add(address);
                        phoneNumberList.add(clinicHP);
                        openingHourList.add(openingHrs);
                        count++;
                    } //else
                    if (count == 5)
                        break;
                }
                searchAdapter = new SearchAdapter(MainActivity.this, clinicIdList, clinicNameList, addressList, phoneNumberList, openingHourList);
                recyclerView.setAdapter(searchAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            recyclerView.setVisibility(View.GONE);
        } else
            super.onBackPressed();

    }

    private void setSupportActionBar(Toolbar toolbar) {
    }

    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    curLocation = location;
                    Toast.makeText(getApplicationContext(), curLocation.getLatitude()
                            + "" + curLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                    supportMapFragment = (SupportMapFragment)
                            getSupportFragmentManager().findFragmentById(R.id.google_map);
                    supportMapFragment.getMapAsync(MainActivity.this);
                }
            }
        });


    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        LatLng latLng = new LatLng(1.3463185, 103.6804502);
        //LatLng latLng = new LatLng(curLocation.getLatitude(),curLocation.getLongitude());
        final MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Current Location!");
        googleMap.addMarker(markerOptions);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));

        databaseReference.child("Location").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dsnapshot) {

                for (DataSnapshot snapshot : dsnapshot.getChildren()) {
                    String mid = snapshot.getKey();
                    String coor = snapshot.child("coordinates").getValue(String.class);
                    //split x y coordinate
                    String[] C = coor.split(" ");
                    LatLng latLng2 = new LatLng(Double.parseDouble(C[3]), Double.parseDouble(C[1]));
                    MarkerOptions markerOptions2 = new MarkerOptions().position(latLng2).title(mid);
                    googleMap.addMarker(markerOptions2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLocation();
                }
                break;
        }
    }

    //side menu link
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.chat:
                Intent intentChat = new Intent(MainActivity.this, NurseChatActivity.class);
                startActivity(intentChat);
                break;
            case R.id.user:
                Intent intentProfile = new Intent(MainActivity.this, EditProfileActivity.class);
                startActivity(intentProfile);
                break;
            case R.id.signout:
                FirebaseAuth.getInstance().signOut();
                finish();
                Intent intentSignOut = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(intentSignOut);
                break;
        }
        return true;
    }
}
