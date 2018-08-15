package com.example.ciheng.shoppingmap.View;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.ciheng.shoppingmap.Adapter.urlAdapter;
import com.example.ciheng.shoppingmap.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final String TAG = "MapsActivity";
    private GoogleMap mMap;
    private int mUserId;
    public LocationManager mLocationManager;
    private urlAdapter mUrlAdapter = new urlAdapter();
    private Set<PoiTarget> poiTargets = new HashSet<PoiTarget>();
    private static final float DEFAULT_ZOOM = 17;

    //private Boolean mLocationPermissionsGranted = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mUserId = intent.getIntExtra("user_id", -1);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        showProducts();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the mUserData grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (mLocationManager.isProviderEnabled(mLocationManager.NETWORK_PROVIDER)) {
            mLocationManager.requestLocationUpdates(mLocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    //instatiate the class, latlng
                    LatLng latLng = new LatLng(latitude, longitude);
                    //instatiate the class, geocoder
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    try {
                        List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                        //String str = addressList.get(0).getLocality() + ',';
                        //str += addressList.get(0).getCountryName();
                        //mMap.addMarker(new MarkerOptions().position(latLng).title(str));
                        //String message = "default marker: longitude "+longitude+" latitude "+latitude;
                        //Log.v(TAG,message);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            });
        } else if (mLocationManager.isProviderEnabled(mLocationManager.GPS_PROVIDER)) {
            mLocationManager.requestLocationUpdates(mLocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    //instantiate the class, latlng
                    LatLng latLng = new LatLng(latitude, longitude);
                    //instantiate the class, geocoder
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    try {
                        List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
//                        String str = addressList.get(0).getLocality() + ',';
//                        str += addressList.get(0).getCountryName();
                        //mMap.addMarker(new MarkerOptions().position(latLng).title(str));
                        //String message = "default marker: longitude "+longitude+" latitude "+latitude;
                        //Log.v(TAG,message);
                        // showProducts();
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            });

        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the mUserData will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the mUserData has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String title = marker.getTitle();

                int productId = Integer.parseInt(title.substring(title.lastIndexOf("@") + 1));
                String message = "marker id " + productId;
                Log.v(TAG, message);
                gotoDetail(productId);
                return false;
            }
        });
    }

    public void showProducts() {
        RequestQueue data = Volley.newRequestQueue(this);
        String url = mUrlAdapter.getMapLocation();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                String message = "response length " + response.length();
                Log.v(TAG, message);
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject Event = response.getJSONObject(i);
                        double latitude = Event.getDouble("latitude");
                        double longitude = Event.getDouble("longitude");
                        String thumbnail = Event.getString("thumbnail").replaceAll("\\/", "/");
                        String productName = Event.getString("name");
                        int productId = Event.getInt("id_product");
                        message = "thumbnail of " + productName + " is: " + thumbnail;
                        Log.v(TAG, message);
                        LatLng latLng = new LatLng(latitude, longitude);
                        message = "marker " + i + ": longitude " + longitude + " latitude " + latitude;
                        String title = productName + "@" + productId;
                        Log.v(TAG, message);
                        Marker m = mMap.addMarker(new MarkerOptions().position(latLng).title(title));
                        PoiTarget pt;
                        pt = new PoiTarget(m);
                        poiTargets.add(pt);
                        Picasso.get()
                                .load(thumbnail)
                                .into(pt);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        data.add(request);
    }

    private void gotoDetail(int productId) {
        Intent intent = new Intent(this, ProductDetail.class);
        Bundle extras = new Bundle();
        extras.putInt("user_id",mUserId);
        extras.putInt("product_id",productId);
        intent.putExtras(extras);
        startActivity(intent);
    }

    class PoiTarget implements Target { //for loading marker icon
        private Marker m;

        public PoiTarget(Marker m) {
            this.m = m;
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            m.setIcon(BitmapDescriptorFactory.fromBitmap(bitmap));
            poiTargets.remove(this);
            Log.v(TAG, " @+ Set bitmap for " + m.getTitle() + " PT size: #" + poiTargets.size());
        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
            Log.v(TAG, " @+ [ERROR] Don't set bitmap for " + m.getTitle());
            poiTargets.remove(this);
        }


        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    }

}