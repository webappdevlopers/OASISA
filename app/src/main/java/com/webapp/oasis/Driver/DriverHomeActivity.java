package com.webapp.oasis.Driver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.location.LocationManagerCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
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
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.webapp.oasis.Admin.AminInvoiceListActivity;
import com.webapp.oasis.Donation.DonationActivity;
import com.webapp.oasis.R;
import com.webapp.oasis.Utilities.Config;
import com.webapp.oasis.Utilities.SessionManager;
import com.webapp.oasis.Wallet.DriverApprovalPendingActivity;
import com.webapp.oasis.Wallet.WalletDriverActivity;
import com.webapp.oasis.Wallet.WalletEmptyActivity;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class DriverHomeActivity extends AppCompatActivity implements LocationListener, ActionBar.TabListener {

    RelativeLayout rl_addvehicle, rl_invoices, rl_order, rl_orderdetails;
    RelativeLayout rlnotification, rllogout, rl_profile;
    SessionManager session;

    LocationManager locationManager;
    String provider;
    TextView latlong;
    String lat = "", lon = "";
    StringRequest stringRequest;
    RequestQueue mRequestQueue;
    String user_id, hash;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    TextView tvdemo;
    ActionBarDrawerToggle toggle;
    DrawerLayout Drawer;
    androidx.appcompat.widget.Toolbar toolbar;
    androidx.appcompat.app.ActionBar actionBar;
    ImageView profile_image;
    TextView name1;
    TextView wallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_driver);


        session = new SessionManager(getApplicationContext());
        final HashMap<String, String> users = session.getUserDetails();
        user_id = users.get(session.KEY_USERID);
        hash = users.get(session.KEY_HASH);
        Log.d("User id", user_id);

        tvdemo = findViewById(R.id.tvdemo);
        tvdemo.setText("Hello, " + users.get(session.KEY_NAME));

        wallet = findViewById(R.id.wallet);
        rl_profile = findViewById(R.id.rl_profile);
        rl_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DriverHomeActivity.this, DriverProfileActivity.class);
                startActivity(intent);
            }
        });
        latlong = findViewById(R.id.latlong);
        rl_invoices = findViewById(R.id.rl_invoices);
        rl_invoices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DriverHomeActivity.this, AminInvoiceListActivity.class);
                intent.putExtra("url", "https://itkbusiness.online/rastasur/api/driver_order_details");
                startActivity(intent);
            }
        });

        rl_orderdetails = findViewById(R.id.rl_orderdetails);
        rl_orderdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DriverHomeActivity.this, DriverOrderActivity.class);
                startActivity(intent);
            }
        });

        rl_addvehicle = findViewById(R.id.rl_addvehicle);
        rl_addvehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DriverHomeActivity.this, WalletDriverActivity.class);
                startActivity(intent);
            }
        });
        rllogout = findViewById(R.id.rllogout);
        rllogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DriverHomeActivity.this);
                builder.setMessage("Are you sure you want to logout?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                session.logoutUser();
                                finish();

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        actionBar = getSupportActionBar();

        NavigationView nav_view = findViewById(R.id.nav_view);
        Drawer = findViewById(R.id.drawer_layout);
        View headerView = nav_view.getHeaderView(0);
        profile_image = headerView.findViewById(R.id.profile_image);
        name1 = headerView.findViewById(R.id.profile_text);

        name1.setText(users.get(session.KEY_NAME));


        toggle = new ActionBarDrawerToggle(this, Drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }
        };
        Drawer.addDrawerListener(toggle);
        toggle.syncState();
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(final MenuItem item) {
                actionBar.setTitle(item.getTitle());
                Drawer.closeDrawers();
                //  drawer.closeDrawers();


                switch (item.getItemId()) {

                /*    case R.id.addvehicle:
                        Intent intent = new Intent(DriverHomeActivity.this, AddVehicleActivity.class);
                        startActivity(intent);

                        break;*/
                    case R.id.orderdetail:

                        Intent intent2 = new Intent(DriverHomeActivity.this, DriverOrderActivity.class);
                        startActivity(intent2);

                        break;
                    case R.id.profile:
                        Intent intent3 = new Intent(DriverHomeActivity.this, DriverProfileActivity.class);
                        startActivity(intent3);
                        break;
                    case R.id.invoice:

                        Intent intent4 = new Intent(DriverHomeActivity.this, AminInvoiceListActivity.class);
                        intent4.putExtra("url", "https://itkbusiness.online/rastasur/api/driver_order_detail");
                        startActivity(intent4);

                        break;
                    case R.id.donation:
                        Intent intents = new Intent(DriverHomeActivity.this, DonationActivity.class);
                        startActivity(intents);

                        break;
                    case R.id.wallet:
                        Intent intent = new Intent(DriverHomeActivity.this, WalletDriverActivity.class);
                        startActivity(intent);

                        break;
                    case R.id.ShareApp:
                        try {
                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "OASIS GLOBE App");
                            String shareMessage = "Download OASIS GLOBE App Now\n\nhttps://play.google.com/store/apps/details?id="+getPackageName();
                            //     shareMessage = shareMessage + "https://zene.page.link/Zi7X" + BuildConfig.APPLICATION_ID +"\n\n";
                            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                            startActivity(Intent.createChooser(shareIntent, "choose one"));
                        } catch (Exception e) {
                            //e.toString();
                        }
                        break;
                    case R.id.Rate: {
                        Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName() + "");
                        Intent goMarket = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(goMarket);
                    }
                    break;
                    case R.id.logout:
                        AlertDialog.Builder builder = new AlertDialog.Builder(DriverHomeActivity.this);
                        builder.setMessage("Are you sure you want to logout?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        session.logoutUser();
                                        finish();

                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert = builder.create();
                        alert.show();

                        break;
                }


                return true;
            }
        });

        Runnable r2 = new Runnable() {
            @Override
            public void run() {
                if (ActivityCompat.checkSelfPermission(DriverHomeActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // Check Permissions Now
                    ActivityCompat.requestPermissions(DriverHomeActivity.this,
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

                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10, 0, DriverHomeActivity.this);

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
                        LocationServices.getFusedLocationProviderClient(DriverHomeActivity.this)
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
        driver_cordinate();

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

    @Override
    public void onBackPressed() {

        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

        super.onBackPressed();
    }

    int TW = 0;

    private void driver_cordinate() {

        final ProgressDialog showMe = new ProgressDialog(DriverHomeActivity.this);
        showMe.setMessage("Updating Coordinates");
        showMe.setCancelable(true);
        showMe.setCanceledOnTouchOutside(false);
//        showMe.show();
        String url = Config.driver_cordinate;
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
                                //success
                                Log.d("location", lat + " | " + lon);
                                wallet.setText(j.getString("balance"));
                                //     Toast.makeText(DriverHomeActivity.this, ""+msg, Toast.LENGTH_SHORT).show();

                            } else if (status.equals("300")) {
                                //Driver Not Approve
                                if (TW == 0) {
                                    Toast.makeText(DriverHomeActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(DriverHomeActivity.this, DriverApprovalPendingActivity.class);
                                    startActivity(intent);
                                    finish();
                                    TW = 1;
                                }
                            } else if (status.equals("400")) {
                                //"Wallet Balance is 0
                                if (TW == 0) {
                                    Intent intent = new Intent(DriverHomeActivity.this, WalletEmptyActivity.class);
                                    startActivity(intent);
                                    finish();
                                    Toast.makeText(DriverHomeActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                                    Log.d("location", lat + " | " + lon);
                                    TW = 1;
                                }
                            } else {

                                // Showing Echo Response Message Coming From Server.
               //                 Toast.makeText(DriverHomeActivity.this, msg, Toast.LENGTH_LONG).show();
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
              //          Toast.makeText(DriverHomeActivity.this, "Not connected to internet", Toast.LENGTH_SHORT).show();
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
                params.put("driver_id", user_id);
                params.put("hash", hash);
                params.put("lat", lat);
                params.put("lon", lon);

                return params;
            }
        };
        stringRequest.setTag("TAG");
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);

    }

    private void NetworkDialog() {
        final Dialog dialogs = new Dialog(DriverHomeActivity.this);
        dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogs.setContentView(R.layout.networkdialog);
        dialogs.setCanceledOnTouchOutside(false);
        Button done = (Button) dialogs.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
                driver_cordinate();
            }
        });
        dialogs.show();
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }
}