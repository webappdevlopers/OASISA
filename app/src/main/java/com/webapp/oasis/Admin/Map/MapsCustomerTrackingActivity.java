package com.webapp.oasis.Admin.Map;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.webapp.oasis.Model.AgentListModel;
import com.webapp.oasis.R;
import com.webapp.oasis.Utilities.Config;
import com.webapp.oasis.Utilities.SessionManager;

public class MapsCustomerTrackingActivity extends FragmentActivity implements OnMapReadyCallback {


    private ViewGroup infoWindow;
    private TextView infoTitle;
    private TextView infoSnippet;
    private Button infoButton;
    private OnInfoWindowElemTouchListener infoButtonListener;

    SessionManager session;
    StringRequest stringRequest;
    RequestQueue mRequestQueue;
    String user_id, hash;
    private GoogleMap mMap;
    ArrayList<LatLng> arrayList = new ArrayList<LatLng>();
    LatLng sydney;
    ArrayList<String> arrayList1 = new ArrayList<String>();
    String sydney11;
    MapWrapperLayout mapWrapperLayout;
    String lat="",lon="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        session = new SessionManager(getApplicationContext());
        final HashMap<String, String> users = session.getUserDetails();
        user_id = users.get(session.KEY_USERID);
        hash = users.get(session.KEY_HASH);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapWrapperLayout = (MapWrapperLayout)findViewById(R.id.map_relative_layout);


        try {
            lat = getIntent().getStringExtra("lat");
            lon = getIntent().getStringExtra("lon");
        }
        catch (Exception e){}
        get_distance();

        this.infoWindow = (ViewGroup)getLayoutInflater().inflate(R.layout.info_window, null);
        this.infoTitle = (TextView)infoWindow.findViewById(R.id.title);
        this.infoSnippet = (TextView)infoWindow.findViewById(R.id.snippet);
        this.infoButton = (Button)infoWindow.findViewById(R.id.button);

        // Setting custom OnTouchListener which deals with the pressed state
        // so it shows up
        this.infoButtonListener = new OnInfoWindowElemTouchListener(infoButton,
                getResources().getDrawable(R.drawable.round_but_green_sel), //btn_default_normal_holo_light
                getResources().getDrawable(R.drawable.round_but_red_sel)) //btn_default_pressed_holo_light
        {
            @Override
            protected void onClickConfirmed(View v, Marker marker) {
                // Here we can perform some action triggered after clicking the button
                Toast.makeText(MapsCustomerTrackingActivity.this, marker.getTitle() + "'s button clicked!", Toast.LENGTH_SHORT).show();
            }
        };
        this.infoButton.setOnTouchListener(infoButtonListener);

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        int secondsDelayed = 1;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                for (int i=0; i<arrayList.size();i++)
                {
                    mMap.addMarker(new MarkerOptions().position(arrayList.get(i)).title(arrayList1.get(i)));
                    mMap.addMarker(new MarkerOptions()
                            .title("Maharashtra")
                            .snippet("")/*
                            .icon(BitmapDescriptorFactory
                                    .fromResource(R.drawable.ic_car))*/
                            .position(arrayList.get(i)));

                    float zoomLevel = 15.0f; //This goes up to 21
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomLevel));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(arrayList.get(i)));

                    mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                        @Override
                        public View getInfoWindow(Marker marker) {
                            return null;
                        }

                        @Override
                        public View getInfoContents(Marker marker) {
                            // Setting up the infoWindow with current's marker info
                            infoTitle.setText(marker.getTitle());
                            infoSnippet.setText(marker.getSnippet());
                            infoButtonListener.setMarker(marker);

                            // We must call this to set the current marker and infoWindow references
                            // to the MapWrapperLayout
                            //    mapWrapperLayout.setMarkerWithInfoWindow(marker, infoWindow);
                            mapWrapperLayout.setMarkerWithInfoWindow(marker, infoWindow);
                            return infoWindow;
                        }
                    });
                }
            }
        }, secondsDelayed * 4000);
    }
    private void get_distance() {
        final ProgressDialog showMe = new ProgressDialog(MapsCustomerTrackingActivity.this, AlertDialog.THEME_HOLO_LIGHT);
        showMe.setMessage("Please wait");
        showMe.setCancelable(true);
        showMe.setCanceledOnTouchOutside(false);
        showMe.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.get_distance,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {
                        showMe.dismiss();

                        try {
                            JSONObject j = new JSONObject(ServerResponse);

                            String status = j.getString("status");
                            String msg = j.getString("msg");


                            arrayList.clear();
                            if (status.equals("200")) {

                                arrayList.clear();

                                JSONArray applist = j.getJSONArray("data");
                                if (applist != null && applist.length() > 0) {

//                                    for (int i = 0; i < applist.length(); i++) {
//
//                                        JSONObject jsonObject = applist.getJSONObject(i);
//                                        final AgentListModel rm = new AgentListModel();
//
//                                        try {
//                                            String s=(jsonObject.getString("lat"));
//                                            String s1=(jsonObject.getString("lon"));
//                                            float a= Float.parseFloat(s);
//                                            float a1= Float.parseFloat(s1);
//                                            sydney = new LatLng(a,a1);
//                                            Geocoder geocoder = new Geocoder(MapsCustomerTrackingActivity.this, Locale.getDefault());
//                                            List<Address> addresses = null;
//                                            try {
//                                                addresses = geocoder.getFromLocation(a, a1, 1);
//                                            } catch (IOException e) {
//                                                e.printStackTrace();
//                                            }
//                                            String cityName = addresses.get(0).getAddressLine(0);
//                                            String stateName = addresses.get(0).getAddressLine(1);
//                                            String countryName = addresses.get(0).getAddressLine(2);
//
//                                            String all = cityName+" "+stateName+" "+countryName;
//                                            int len = all.length();
//                                            String all1 = all.substring(0,len-10);
//                                            sydney11 = new String(all1);
//
//                                            arrayList1.add(sydney11);
//                                            arrayList.add(sydney);
//                                        }
//
//                                        catch (Exception e)
//                                        {
//                                            Log.d("Exception", String.valueOf(e));
//                                        }
//
//                                    }
                                }
                            } else {

                                // Showing Echo Response Message Coming From Server.
                                Toast.makeText(MapsCustomerTrackingActivity.this, msg, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            showMe.dismiss();
                            //      Toast.makeText(CelebrityListActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        Log.d("eerror", String.valueOf(volleyError));
                        NetworkDialoguser_reviewd_details();

                        showMe.dismiss();
                        // Toast.makeText(StampOfferActivity.this, "No Connection", Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("api_key", "53rihkffdirofmbglolfhbcmfvlfjgkgnmmf");
                params.put("hash", hash);
                params.put("lon", lon);
                params.put("lat", lat);
                return params;
            }
        };
        // Creating RequestQueue.
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(150000, 1, 1.0f));

        RequestQueue requestQueue = Volley.newRequestQueue(MapsCustomerTrackingActivity.this);
        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);
    }

    private void NetworkDialoguser_reviewd_details() {
        final Dialog dialogs = new Dialog(MapsCustomerTrackingActivity.this);
        dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogs.setContentView(R.layout.networkdialog);
        dialogs.setCanceledOnTouchOutside(false);
        Button done = (Button) dialogs.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
                get_distance();
            }
        });
        dialogs.show();
    }

}