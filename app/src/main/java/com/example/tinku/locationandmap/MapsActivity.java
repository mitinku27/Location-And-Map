package com.example.tinku.locationandmap;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import Model.Earthquick;
import Ui.CustomWindowAdapter;
import Util.Constant;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback ,GoogleMap.OnInfoWindowClickListener
        ,GoogleMap.OnMarkerClickListener{
    LocationManager locationManager;
    LocationListener locationListener;
    AlertDialog dialog;
    AlertDialog.Builder builder;
    private GoogleMap mMap;
    RequestQueue queue;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        button = findViewById(R.id.mybutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsActivity.this, DetailListActivity.class));
            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        CustomWindowAdapter customWindowAdapter= new CustomWindowAdapter(getApplicationContext());
        mMap = googleMap;
        mMap.setInfoWindowAdapter(customWindowAdapter);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMarkerClickListener(this);
        queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constant.url, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("features");
                    for (int i =0;i<Constant.limit;i++)
                    {
                        Earthquick earthquick = new Earthquick();
                        JSONObject object = jsonArray.getJSONObject(i).getJSONObject("properties");
                        JSONObject geomat= jsonArray.getJSONObject(i).getJSONObject("geometry");
                        JSONArray array =geomat.getJSONArray("coordinates");
                        double lat=array.getDouble(0);
                        double lang=array.getDouble(1);
                       // Log.d("test",lat+" "+lang);
                        earthquick.setPlace(object.getString("place"));
                        earthquick.setType(object.getString("types"));
                        earthquick.setDetaillink(object.getString("detail"));
                        earthquick.setMagnitude(object.getDouble("mag"));
                        earthquick.setLat(lat);
                        earthquick.setLang(lang);
                        java.text.DateFormat dateFormat= java.text.DateFormat.getDateInstance();
                        String fomdat= dateFormat.format(new Date(object.getLong("time")).getTime());
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.title(earthquick.getPlace());
                        markerOptions.snippet("magnitude: "+String.valueOf(earthquick.getMagnitude())+"\n"+"date: "+fomdat);
                        markerOptions.position(new LatLng(lat,lang));
                        CircleOptions co = new CircleOptions();
                        co.center(new LatLng(lat,lang));
                        co.radius(30000);
                        co.strokeWidth(3.6f);



                        if(earthquick.getMagnitude()>=5)
                        {

                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                            co.fillColor(Color.RED);

                        }
                        else
                        {
                            co.fillColor(Color.CYAN);
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                        }
                        mMap.addCircle(co);
                        Marker marker= mMap.addMarker(markerOptions);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lang),1));
                        marker.setTag(earthquick.getDetaillink());

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonObjectRequest);
        // Add a marker in Sydney and move the camera


        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                /*Log.d("location",String.valueOf(location.getLatitude()));
                LatLng newLocation= new LatLng(location.getLatitude(),location.getLongitude());
                //LatLng sydney = new LatLng(-34, 151);
                mMap.addMarker(new MarkerOptions().position(newLocation).title("Marker"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(newLocation));

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    List<Address> addressList= geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                    if (addressList!= null && addressList.size() >0)
                    {
                        Address a = addressList.get(0);
                        String s= a.getAddressLine(0)+" "+a.getAdminArea();
                        Toast.makeText(MapsActivity.this,s,Toast.LENGTH_LONG).show();
                        Log.d("address",addressList.get(0).toString());
                    }
                    else
                    {
                        Log.d("address","not found");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

        }
        else
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            }
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
      // Toast.makeText(MapsActivity.this,marker.getTag().toString(),Toast.LENGTH_LONG).show();
        quickDetails(marker.getTag().toString());

    }

    private void quickDetails(String s) {
       // Log.d("test",s);
         JsonObjectRequest js = new JsonObjectRequest(Request.Method.GET, s, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                String detailsUrl="test";
               // Toast.makeText(getApplicationContext()," test"+detailsUrl,Toast.LENGTH_LONG).show();
                try {
                    JSONObject properties = response.getJSONObject("properties");
                    JSONObject products = properties.getJSONObject("products");
                    JSONArray geoserve = products.getJSONArray("geoserve");

                    for (int i = 0; i < geoserve.length(); i++) {
                        JSONObject geoserveObj = geoserve.getJSONObject(i);

                        JSONObject contentObj = geoserveObj.getJSONObject("contents");
                        JSONObject geoJsonObj = contentObj.getJSONObject("geoserve.json");

                        detailsUrl = geoJsonObj.getString("url");
                        getMoreDetails(detailsUrl);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(js);

    }

    private void getMoreDetails(String detailsUrl) {
        builder = new AlertDialog.Builder(MapsActivity.this);
        final View view = getLayoutInflater().inflate(R.layout.popup, null);
        final Button dismissButton = (Button) view.findViewById(R.id.dismissPop);
        final Button dismissButtonTop = (Button) view.findViewById(R.id.desmissPopTop);
        final TextView popList = (TextView) view.findViewById(R.id.popList);
        final WebView htmlPop = (WebView) view.findViewById(R.id.htmlWebview);
        final StringBuilder stringBuilder = new StringBuilder();

        final JsonObjectRequest jo = new JsonObjectRequest(Request.Method.GET, detailsUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    if (response.has("tectonicSummary") && response.getString("tectonicSummary") != null) {

                        JSONObject tectonic = response.getJSONObject("tectonicSummary");

                        if (tectonic.has("text") && tectonic.getString("text") != null) {

                            String text = tectonic.getString("text");

                            htmlPop.loadDataWithBaseURL(null, text, "text/html", "UTF-8", null);

                            Log.d("HTML", text);

                        }

                    }


                    JSONArray cities = response.getJSONArray("cities");

                    for (int i = 0; i < cities.length(); i++) {
                        JSONObject citiesObj = cities.getJSONObject(i);

                        stringBuilder.append("City: " + citiesObj.getString("name")
                                + "\n" + "Distance: " + citiesObj.getString("distance")
                                + "\n" + "Population: "
                                + citiesObj.getString("population"));

                        stringBuilder.append("\n\n");

                    }

                    popList.setText(stringBuilder);

                    dismissButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dismissButtonTop.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });




                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                
            }
        });
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
        queue.add(jo);

    }
            
    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}
