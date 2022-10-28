package com.tunc.xlocal;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tunc.xlocal.databinding.ActivityMapsBinding;
import com.tunc.xlocal.fragments.PostFragment;
import com.tunc.xlocal.model.Post;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Button btnCamera;
    private Uri postPhoto;
    private PostFragment postFragment;
    private LatLng konum;
    private ActivityResultLauncher<String> permissionLauncher;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;
    private ArrayList<Post> postArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        postArray = new ArrayList<Post>();
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        reqisterLauncher();

        getPosts();




        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);






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



        //LocationManager --> konum yöneticisi android konumu izni alır.
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        //locakasyon dinleyici
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                //  System.out.println("location => "+ location.toString());
                SharedPreferences sharedPreferences = MapsActivity.this.getSharedPreferences("com.tunc.xlocal",MODE_PRIVATE);
                boolean info = sharedPreferences.getBoolean("info",false);

                //gelen postların harita üzerinde görüntülenmesi
                for(Post post : postArray){
                    final LatLng postLocationLatLng = new LatLng(post.latitute,post.longitute);
                    Marker marker = mMap.addMarker(new MarkerOptions().position(postLocationLatLng).title(post.userUudi));
                }





                //markerClick
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(@NonNull Marker marker) {
                        // System.out.println("bastım");
                        // mMap.setInfoWindowAdapter(new MarkerInfoWindowAdapter(MapsActivity.this));




                        // showCustomAlert();
                        return false;
                    }
                });

                if(info == false){


                    LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());

                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,16));
                    sharedPreferences.edit().putBoolean("info",true).apply();


                }
            }
        };

        //konum izni
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
                Snackbar.make(binding.getRoot(),"permission needed for maps",Snackbar.LENGTH_INDEFINITE).setAction("give permission", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //izin isteği
                        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
                    }
                }).show();
            }else{
                //izin isteği
                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);

            }
        }else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

            //ilk açıldığında en son konumu gösterir
            Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(lastLocation != null){
                LatLng lastUserLocation = new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation,16));
            }
        }
    }

    public void removePostFragmentOnMapsActivity(){
        if( postFragment != null){
            getSupportFragmentManager().beginTransaction().remove(postFragment).commit();
        }
    }

    public void goToProfile(View view){
        Intent goToProfileDetailsActivity = new Intent(this,Profile.class);
        startActivity(goToProfileDetailsActivity);

    }

    public void openCamera(View view){

        Intent goToPostActivity = new Intent(this,PostActivity.class);
        goToPostActivity.putExtra("konum",konum);
        startActivity(goToPostActivity);

    }

    private void reqisterLauncher(){
        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if(result){
                    //izin verildi
                    if( ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

                        //ilk açıkdığında en son konumu göstersin
                        Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if(lastLocation != null){
                            LatLng lastUserLocation = new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());
                            Marker deneme = mMap.addMarker(new MarkerOptions().title("deneme").position(lastUserLocation));
                            deneme.showInfoWindow();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation,16));

                        }



                    }

                }else{
                    //izin red edildi
                    Toast.makeText(MapsActivity.this, "Permission needed",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public  void getPosts(){

        firebaseFirestore.collection("Post").addSnapshotListener((value, error) -> {

            if (value.isEmpty()){
                Toast.makeText(this, " There is an error in the getPost Method", Toast.LENGTH_SHORT).show();
            }else{
                // Eğer post sorgusu boş değilse postları bir post tipindeki array'e atıyoruz.

                for(DocumentSnapshot document : value.getDocuments()){
                    Post getPost = new Post();
                    getPost.countOfComment = (long) document.get("countOfComment");
                    getPost.countOfConfirm = (long) document.get("countOfConfirm");
                    getPost.countOfJoin = (long) document.get("countOfJoin");
                    getPost.countOfLike = (long) document.get("countOfLike");
                    getPost.date = document.getDate("date");
                    getPost.description = document.get("aciklama").toString();
                    getPost.postImageDownloadUrl = document.get("post_image_download_url").toString();
                    getPost.userUudi = document.get("user_uuid").toString();
                    getPost.latitute =(double) document.get("latitude");
                    getPost.longitute = (double) document.get("longitude");
                    postArray.add(getPost);

                }




            }



        });

    }









}