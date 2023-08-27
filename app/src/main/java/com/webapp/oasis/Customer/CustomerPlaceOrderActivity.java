package com.webapp.oasis.Customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.location.LocationManagerCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.webapp.oasis.Admin.Map.MapsCustomerTrackingActivity;
import com.webapp.oasis.Customer.Adapter.SDDriverListAdapter;
import com.webapp.oasis.Model.SDDriverListModel;
import com.webapp.oasis.R;
import com.webapp.oasis.Utilities.Config;
import com.webapp.oasis.Utilities.SessionManager;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class CustomerPlaceOrderActivity extends AppCompatActivity implements LocationListener {


    RecyclerView mRecyclerView;
    SDDriverListAdapter myOrderAdapter;
    Button btnlist;
    StringRequest stringRequest;
    RequestQueue mRequestQueue;
    SessionManager session;
    private Spinner mySpinnerS, mySpinnerD;
    private ArrayList<String> flats1 = new ArrayList<String>();
    private ArrayList<String> flatids1 = new ArrayList<String>();
    private ArrayList<String> flats = new ArrayList<String>();
    private ArrayList<String> flatids = new ArrayList<String>();
    String spin_val1 = "null", spin_val2 = "null";
    String getpostid, user_id, hash_id;
    ArrayList<SDDriverListModel> dm = new ArrayList<SDDriverListModel>();
    ImageView back;
    Button btnsearch;
    TextView availableno;
    ImageView imgmap;
    SwipeRefreshLayout swipeRefreshLayout;

    String lat="",lon="";
    String source="",destination="";

    LocationManager locationManager;
    String provider;
    String hash;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_place_order);

        session = new SessionManager(CustomerPlaceOrderActivity.this);
        HashMap<String, String> users = session.getUserDetails();
        user_id = users.get(session.KEY_USERID);
        hash_id = users.get(session.KEY_HASH);
        hash = users.get(session.KEY_HASH);

        availableno = findViewById(R.id.availableno);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();

                startActivity(new Intent(CustomerPlaceOrderActivity.this, CustomerHomeActivity.class));
                finish();
            }
        });

        try {
            source = getIntent().getStringExtra("source");
            destination = getIntent().getStringExtra("destination");
            lat = getIntent().getStringExtra("lat");
            lon = getIntent().getStringExtra("lon");
            Log.d("latlon",lat+" "+lon);
        }
        catch (Exception e){}
        imgmap = findViewById(R.id.imgmap);
        imgmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(CustomerPlaceOrderActivity.this, MapsCustomerTrackingActivity.class);
                intent.putExtra("lat",lat);
                intent.putExtra("lon",lon);
                startActivity(intent);
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.itemlistrecycler);
        mRecyclerView.setHasFixedSize(false);
        @SuppressLint("WrongConstant") RecyclerView.LayoutManager mLayoutManager12 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager12);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        myOrderAdapter = new SDDriverListAdapter(getApplicationContext(), dm);
        mRecyclerView.setAdapter(myOrderAdapter);

        mySpinnerS = (Spinner) findViewById(R.id.spngender);
        flats.add("Select Source");
        flatids.add("0");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CustomerPlaceOrderActivity.this, android.R.layout.simple_list_item_1, flats);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mySpinnerS.setAdapter(adapter);

        mySpinnerS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!flats1.get(position).equals("Select Source")) {
                    spin_val1 = flatids.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mySpinnerD = (Spinner) findViewById(R.id.spngenderD);
        flats1.add("Select Destination");
        flatids1.add("0");
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(CustomerPlaceOrderActivity.this, android.R.layout.simple_list_item_1, flats1);
        adapter1.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mySpinnerD.setAdapter(adapter1);

        mySpinnerD.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!flats1.get(position).equals("Select Destination")) {
                    spin_val2 = flatids1.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
       /* Source();
        Destination();*/

        btnsearch = findViewById(R.id.btnsearch);
        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spin_val1.equals("null")) {
                    Toast.makeText(CustomerPlaceOrderActivity.this, "Select Source", Toast.LENGTH_SHORT).show();
                } else if (spin_val2.equals("null")) {
                    Toast.makeText(CustomerPlaceOrderActivity.this, "Select Destination", Toast.LENGTH_SHORT).show();
                } else {
                    search_driver_by_sd();
                }
            }
        });
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e(getClass().getSimpleName(), "refresh");

                get_distance();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        Runnable r2 = new Runnable() {
            @Override
            public void run() {
                if (ActivityCompat.checkSelfPermission(CustomerPlaceOrderActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // Check Permissions Now
                    ActivityCompat.requestPermissions(CustomerPlaceOrderActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            0);
                }
                // Getting LocationManager object
                statusCheck();

                locationManager = (LocationManager) getSystemService(
                        Context.LOCATION_SERVICE);

                // Creating an empty criteria object
                Criteria criteria = new Criteria();

                // Getting the name of the provider that meets the criteria
                provider = locationManager.getBestProvider(criteria, false);

                if (provider != null && !provider.equals("")) {
                    if (!provider.contains("gps")) { // if gps is disabled
                        final Intent poke = new Intent();
                        poke.setClassName("itkida.rastasuraksha.app",
                                "itkida.rastasuraksha.app.widget.SettingsAppWidgetProvider");
                        poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
                        poke.setData(Uri.parse("3"));
                        sendBroadcast(poke);
                    }
                    // Get the location from the given provider
                    Location location = locationManager
                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10, 0, CustomerPlaceOrderActivity.this);

                    if (location != null)
                        onLocationChanged(location);
                    else
                        location = locationManager.getLastKnownLocation(provider);
                    if (location != null)
                        onLocationChanged(location);
                    else {
                        //Toast.makeText(getBaseContext(), "Location can't be retrieved", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    //Toast.makeText(getBaseContext(), "No Provider Found",Toast.LENGTH_SHORT).show();
                }
            }
        };

        new Handler().postDelayed(r2, 1000);
        requestPermission();
        locationManager = (LocationManager) getApplication().getSystemService(Context.LOCATION_SERVICE);
        if (LocationManagerCompat.isLocationEnabled(locationManager)) {

        } else {

        }
        get_distance();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                get_distance();
            }
        }, 1000);

    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(
                Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getCurrentLocation() {


        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        LocationServices.getFusedLocationProviderClient(this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(CustomerPlaceOrderActivity.this)
                                .removeLocationUpdates(this);

                    }
                }, Looper.getMainLooper());
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(
                "Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false).setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog,
                                        final int id) {
                        startActivity(new Intent(
                                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog,
                                        final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* getMenuInflater().inflate(R.menu.activity_main, menu); */
        return true;
    }

    @Override
    public void onLocationChanged(Location location) {

        lat = String.valueOf(location.getLatitude());
        lon = String.valueOf(location.getLongitude());
        users_cordinate();

    }

    @Override
    public void onProviderDisabled(String provider) {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    0);
        }
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

    private void users_cordinate() {

        final ProgressDialog showMe = new ProgressDialog(CustomerPlaceOrderActivity.this);
        showMe.setMessage("Updating Coordinates");
        showMe.setCancelable(true);
        showMe.setCanceledOnTouchOutside(false);
//        showMe.show();
        String url = Config.users_cordinate;
        mRequestQueue = Volley.newRequestQueue(this);
        stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {
                        showMe.dismiss();
                        Log.d("Login Server Response", ServerResponse);

                        try {
                            JSONObject j = new JSONObject(ServerResponse);

                            String status = j.getString("status");
                            String msg = j.getString("msg");

                            if (status.equals("200")) {

                                Log.d("location",lat + " | " + lon);
                                //     latlong.setText(lat + " | " + lon);

                            } else {

                                // Showing Echo Response Message Coming From Server.
                                Toast.makeText(CustomerPlaceOrderActivity.this, msg, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();
                        showMe.dismiss();
//                        NetworkDialog();
                        Log.d("Error", String.valueOf(error));
          //              Toast.makeText(CustomerPlaceOrderActivity.this, "Not connected to internet", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();

                return headers;
            }

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();


                params.put("api_key", "53rihkffdirofmbglolfhbcmfvlfjgkgnmmf");
                params.put("id", user_id);
                params.put("hash", hash);
                params.put("lat", lat);
                params.put("lon", lon);

                return params;
            }
        };
        stringRequest.setTag("TAG");
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);

    }

    private void NetworkDialog() {
        final Dialog dialogs = new Dialog(CustomerPlaceOrderActivity.this);
        dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogs.setContentView(R.layout.networkdialog);
        dialogs.setCanceledOnTouchOutside(false);
        Button done = (Button) dialogs.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
                users_cordinate();
            }
        });
        dialogs.show();
    }

    public void Source() {

        final ProgressDialog showMe = new ProgressDialog(CustomerPlaceOrderActivity.this);
        showMe.setMessage("Please wait");
        showMe.setCancelable(true);
        showMe.setCanceledOnTouchOutside(false);

        String url = Config.city_list;
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());

        stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        flats.clear();
                        flatids.clear();
                        flats.add("Select Source");
                        flatids.add("0");
                        showMe.dismiss();
                        JSONObject j = null;
                        try {
                            j = new JSONObject(response);

                            Log.d("Response city S list", response);

                            String status = j.getString("status");
                            if (status.equals("200")) {

                                JSONArray applist = j.getJSONArray("data");
                                if (applist != null && applist.length() > 0) {
                                    for (int i = 0; i < applist.length(); i++) {
                                        JSONObject getOne = applist.getJSONObject(i);
                                        flats.add(getOne.getString("name"));
                                        flatids.add(getOne.getString("id"));
                                    }

                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(CustomerPlaceOrderActivity.this, android.R.layout.simple_list_item_1, flats);
                                    adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                                    mySpinnerS.setAdapter(adapter);
                                }
                                showMe.dismiss();

                            }
                        } catch (JSONException e) {
                            Log.e("TAG", "Something Went Wrong");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        showMe.dismiss();
                        Toast.makeText(CustomerPlaceOrderActivity.this, "Internet not connected", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                return headers;
            }

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_key", "53rihkffdirofmbglolfhbcmfvlfjgkgnmmf");
                return params;
            }
        };
        stringRequest.setTag("TAG");
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);
    }

    public void Destination() {

        final ProgressDialog showMe = new ProgressDialog(CustomerPlaceOrderActivity.this);
        showMe.setMessage("Please wait");
        showMe.setCancelable(true);
        showMe.setCanceledOnTouchOutside(false);

        String url = Config.city_list;
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());

        stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        flats1.clear();
                        flatids1.clear();
                        flats1.add("Select Destination");
                        flatids1.add("0");
                        showMe.dismiss();
                        JSONObject j = null;
                        try {
                            j = new JSONObject(response);

                            Log.d("Response city D list", response);

                            String status = j.getString("status");
                            if (status.equals("200")) {

                                JSONArray applist = j.getJSONArray("data");
                                if (applist != null && applist.length() > 0) {
                                    for (int i = 0; i < applist.length(); i++) {
                                        JSONObject getOne = applist.getJSONObject(i);
                                        flats1.add(getOne.getString("name"));
                                        flatids1.add(getOne.getString("id"));
                                    }

                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(CustomerPlaceOrderActivity.this, android.R.layout.simple_list_item_1, flats1);
                                    adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                                    mySpinnerD.setAdapter(adapter);
                                }
                                showMe.dismiss();

                            }
                        } catch (JSONException e) {
                            Log.e("TAG", "Something Went Wrong");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        showMe.dismiss();
                        Toast.makeText(CustomerPlaceOrderActivity.this, "Internet not connected", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                return headers;
            }

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_key", "53rihkffdirofmbglolfhbcmfvlfjgkgnmmf");
                return params;
            }
        };
        stringRequest.setTag("TAG");
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);
    }

    private void search_driver_by_sd()  {
        final ProgressDialog showMe = new ProgressDialog(CustomerPlaceOrderActivity.this, AlertDialog.THEME_HOLO_LIGHT);
        showMe.setMessage("Please wait");
        showMe.setCancelable(true);
        showMe.setCanceledOnTouchOutside(false);
        showMe.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.search_driver_by_sd,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {
                        showMe.dismiss();

                        try {
                            JSONObject j = new JSONObject(ServerResponse);

                            String status = j.getString("status");
                            String msg = j.getString("msg");
                            Log.d("agent list response", ServerResponse);


                            dm.clear();
                            myOrderAdapter = new SDDriverListAdapter(getApplicationContext(), dm);
                            mRecyclerView.setAdapter(myOrderAdapter);//ds=model       d=Model
                            availableno.setText("Available Vehicles : 0");
                            dm.clear();
                            if (status.equals("200")) {

                                int vehiclecount = 0;
                                dm.clear();

                                JSONArray applist = j.getJSONArray("data");

                                if (applist != null && applist.length() > 0) {

                                    for (int i = 0; i < applist.length(); i++) {

                                        JSONObject jsonObject = applist.getJSONObject(i);
                                        final SDDriverListModel rm = new SDDriverListModel();

                                        rm.setId(jsonObject.getString("id"));
                                        rm.setName(jsonObject.getString("name"));
                                        rm.setMobile(jsonObject.getString("mobile"));
                                        rm.setPlace(jsonObject.getString("destination"));
                                        rm.setVehical_no(jsonObject.getString("vehical_no"));
                                        rm.setDestination(jsonObject.getString("destination"));
                                        rm.setCostpkg(jsonObject.getString("costpkg"));
                                        rm.setMinodrpkg(jsonObject.getString("minodrpkg"));
                                        rm.setMaxodrpkg(jsonObject.getString("maxodrpkg"));
                                        rm.setImage(jsonObject.getString("image"));

                                        vehiclecount = i + 1;
                                        availableno.setText("Available Vehicles : " + vehiclecount);
                                        dm.add(rm);
                                        myOrderAdapter = new SDDriverListAdapter(getApplicationContext(), dm);
                                        mRecyclerView.setAdapter(myOrderAdapter);//ds=model       d=Model

                                    }
                                } else {
                                    Toast.makeText(CustomerPlaceOrderActivity.this, "No Vehicle Available For This Route", Toast.LENGTH_LONG).show();
                                }

                            } else {

                                // Showing Echo Response Message Coming From Server.
                                Toast.makeText(CustomerPlaceOrderActivity.this, msg, Toast.LENGTH_LONG).show();
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
                params.put("user_id", user_id);
                params.put("hash", hash_id);
                params.put("source", spin_val1);
                params.put("destination", spin_val2);
                return params;
            }
        };
        // Creating RequestQueue.
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(150000, 1, 1.0f));

        RequestQueue requestQueue = Volley.newRequestQueue(CustomerPlaceOrderActivity.this);
        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);
    }

    private void NetworkDialoguser_reviewd_details() {
        final Dialog dialogs = new Dialog(CustomerPlaceOrderActivity.this);
        dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogs.setContentView(R.layout.networkdialog);
        dialogs.setCanceledOnTouchOutside(false);
        Button done = (Button) dialogs.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
                search_driver_by_sd();
            }
        });
        dialogs.show();
    }

    private void get_distance() {
        final ProgressDialog showMe = new ProgressDialog(CustomerPlaceOrderActivity.this, AlertDialog.THEME_HOLO_LIGHT);
        showMe.setMessage("Please wait");
        showMe.setCancelable(true);
        showMe.setCanceledOnTouchOutside(false);
     //   showMe.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.get_distance,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {
                        showMe.dismiss();

                        try {
                            JSONObject j = new JSONObject(ServerResponse);

                            String status = j.getString("status");
                            String msg = j.getString("msg");
                            Log.d("agent list response", ServerResponse);

                            dm.clear();
                            myOrderAdapter = new SDDriverListAdapter(getApplicationContext(), dm);
                            mRecyclerView.setAdapter(myOrderAdapter);//ds=model       d=Model

                            if (status.equals("200")) {

                                int vehiclecount = 0;
                                dm.clear();

                                JSONArray applist = j.getJSONArray("data");

                                if (applist != null && applist.length() > 0) {

                                    for (int i = 0; i < applist.length(); i++) {

                                        JSONObject jsonObject = applist.getJSONObject(i);
                                        final SDDriverListModel rm = new SDDriverListModel();

                                        rm.setId(jsonObject.getString("id"));
                                        rm.setName(jsonObject.getString("name"));
                                        rm.setPlace(jsonObject.getString("source"));
                                        //rm.setVehical_no(jsonObject.getString("vehical_no"));
                                        rm.setDestination(jsonObject.getString("destination"));
                                        rm.setVehicle_type(jsonObject.getString("vehicle_type"));
                                        /*
                                        rm.setCostpkg(jsonObject.getString("costpkg"));
                                        rm.setMinodrpkg(jsonObject.getString("minodrpkg"));
                                        rm.setMaxodrpkg(jsonObject.getString("maxodrpkg"));*/
                                        rm.setImage(jsonObject.getString("vehicle_photo"));
                                        rm.setSourceuser(source);
                                        rm.setDestinationuser(destination);

                                        vehiclecount = i + 1;
                                        availableno.setText("Available Vehicles : " + vehiclecount);
                                           dm.add(rm);
                                        myOrderAdapter = new SDDriverListAdapter(getApplicationContext(), dm);
                                        mRecyclerView.setAdapter(myOrderAdapter);//ds=model       d=Model

                                    }
                                } else {
                                    Toast.makeText(CustomerPlaceOrderActivity.this, "No Vehicle Available For This Route", Toast.LENGTH_LONG).show();
                                }

                            } else {

                                // Showing Echo Response Message Coming From Server.
                                Toast.makeText(CustomerPlaceOrderActivity.this, msg, Toast.LENGTH_LONG).show();
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
              //          NetworkDialoguser_reviewd_details1();

                        showMe.dismiss();
                        Toast.makeText(CustomerPlaceOrderActivity.this, "No Vehicle Available for this Route", Toast.LENGTH_LONG).show();
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
                params.put("hash", hash_id);
                params.put("lon", lon);
                params.put("lat", lat);
                return params;
            }
        };

        // Creating RequestQueue.
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(150000, 1, 1.0f));

        RequestQueue requestQueue = Volley.newRequestQueue(CustomerPlaceOrderActivity.this);
        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);
    }
    private void NetworkDialoguser_reviewd_details1() {
        final Dialog dialogs = new Dialog(CustomerPlaceOrderActivity.this);
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(CustomerPlaceOrderActivity.this, CustomerHomeActivity.class));
        finish();
    }
}