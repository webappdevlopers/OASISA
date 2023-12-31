package com.webapp.oasis.Customer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.motion.utils.Oscillator;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.webapp.oasis.Donation.DonationActivity;
import com.webapp.oasis.LoginActivity;
import com.webapp.oasis.LoginFirstScreen;
import com.webapp.oasis.R;
import com.webapp.oasis.SplashIntro.SplashActivity;
import com.webapp.oasis.Utilities.SessionManager;

import java.util.HashMap;

public class CustomerHomeActivity extends AppCompatActivity implements LocationListener, ActionBar.TabListener {
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    DrawerLayout Drawer;
    ActionBar actionBar;
    String customer_id;
    TextView email1;
    GoogleSignInAccount googleSignInAccount;
    String hash;
    String lat = "";
    LocationManager locationManager;
    String lon = "";
    RequestQueue mRequestQueue;
    TextView name1;
    ImageView profile_image;
    String provider;
    RelativeLayout rl_driverdetails;
    RelativeLayout rl_invoices;
    RelativeLayout rl_placeorder;
    RelativeLayout rl_treac_order;
    RelativeLayout rllogout;
    RelativeLayout rlcall;
    RelativeLayout rlnotification;
    RelativeLayout rlprofile;
    SessionManager session;
    StringRequest stringRequest;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    TextView tvdemo;
    GoogleApiClient mGoogleApiClient;
    int flag1 = 0;


    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.drawer);
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        this.session = sessionManager;
        HashMap<String, String> users = sessionManager.getUserDetails();
        this.customer_id = users.get(SessionManager.CustomerId);
        this.hash = users.get(SessionManager.KEY_HASH);
        TextView textView = (TextView) findViewById(R.id.tvdemo);
        this.tvdemo = textView;
        textView.setText("Hello, " + users.get("name"));
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.rlprofile);
        this.rlprofile = relativeLayout;
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(CustomerHomeActivity.this, CustomerProfileActivity.class);
                intent.putExtra(LoginFirstScreen.GOOGLE_ACCOUNT, CustomerHomeActivity.this.googleSignInAccount);
                CustomerHomeActivity.this.startActivity(intent);
            }
        });
        RelativeLayout relativeLayout2 = (RelativeLayout) findViewById(R.id.rl_treac_order);
        this.rl_treac_order = relativeLayout2;
        relativeLayout2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CustomerHomeActivity.this.startActivity(new Intent(CustomerHomeActivity.this, ComplaintStatusActivity.class));
            }
        });
        RelativeLayout relativeLayout3 = (RelativeLayout) findViewById(R.id.rl_placeorder);
        this.rl_placeorder = relativeLayout3;
        relativeLayout3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CustomerHomeActivity.this.startActivity(new Intent(CustomerHomeActivity.this, AddComplaintActivity.class));
            }
        });
        RelativeLayout relativeLayout4 = (RelativeLayout) findViewById(R.id.rl_driverdetails);
        this.rl_driverdetails = relativeLayout4;
        relativeLayout4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(CustomerHomeActivity.this, CustomerProfileActivity.class);
                intent.putExtra(LoginFirstScreen.GOOGLE_ACCOUNT, CustomerHomeActivity.this.googleSignInAccount);
                CustomerHomeActivity.this.startActivity(intent);
            }
        });
        RelativeLayout relativeLayout5 = (RelativeLayout) findViewById(R.id.rl_invoices);
        this.rl_invoices = relativeLayout5;
        relativeLayout5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "OASIS App");
                    String shareMessage = "Download OASIS GLOBE App Now\n\nhttps://play.google.com/store/apps/details?id="+getPackageName();
                    //     shareMessage = shareMessage + "https://zene.page.link/Zi7X" + BuildConfig.APPLICATION_ID +"\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
                    //e.toString();
                }
            }
        });
        RelativeLayout relativeLayout6 = (RelativeLayout) findViewById(R.id.rllogout);
        this.rllogout = relativeLayout6;
        relativeLayout6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CustomerHomeActivity.this);
                builder.setMessage("Are you sure you want to logout?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        CustomerHomeActivity.this.session.logoutUser();
                        CustomerHomeActivity.this.finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                builder.create().show();
            }
        });
        rllogout = findViewById(R.id.rllogout);
        rllogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CustomerHomeActivity.this);
                builder.setMessage("Are you sure you want to logout?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                        new ResultCallback<Status>() {
                                            @Override
                                            public void onResult(Status status) {
                                                flag1 = 1;
                                                Toast.makeText(getApplicationContext(), "Logged Out Successfully", Toast.LENGTH_LONG).show();
                                                session.logoutUser();
                                                Toast.makeText(getApplicationContext(), "Logged Out Successfully", Toast.LENGTH_LONG).show();
                                            }
                                        });
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
        final String[] supportnumber = new String[1];
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance("https://oasis-a3b2c-default-rtdb.firebaseio.com/");
        DatabaseReference mDbRef = mDatabase.getReference("supportnumber");

        mDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                supportnumber[0] = snapshot.getValue(String.class);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CustomerHomeActivity.this, "Fail to get data.", Toast.LENGTH_LONG).show();
            }
        });
        rlcall = findViewById(R.id.rlcall);
        rlcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+supportnumber[0]));
                startActivity(intent);
            }
        });

        Toolbar toolbar2 = (Toolbar) findViewById(R.id.toolbar);
        this.toolbar = toolbar2;
        setSupportActionBar(toolbar2);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        this.actionBar = getSupportActionBar();
        NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);
        this.Drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        View headerView = nav_view.getHeaderView(0);
        this.profile_image = (ImageView) headerView.findViewById(R.id.profile_image);
        TextView textView2 = (TextView) headerView.findViewById(R.id.profile_text);
        this.name1 = textView2;
        textView2.setText(users.get("name"));


        FirebaseDatabase.getInstance("https://oasis-a3b2c-default-rtdb.firebaseio.com/").getReference("Customer/Registration").child(this.customer_id).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    textView2.setText((String) dataSnapshot.child("Name").getValue(String.class));
                    tvdemo.setText("Hello, "+(String) dataSnapshot.child("Name").getValue(String.class));
                    Glide.with((FragmentActivity) CustomerHomeActivity.this).load((String) dataSnapshot.child("ProfileImage").getValue(String.class)).into(CustomerHomeActivity.this.profile_image);
                }
            }
            public void onCancelled(DatabaseError error) {
                Log.w(Oscillator.TAG, "Failed to read value.", error.toException());
            }
        });

        ActionBarDrawerToggle r5 = new ActionBarDrawerToggle(this, this.Drawer, this.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        this.toggle = r5;
        this.Drawer.addDrawerListener(r5);
        this.toggle.syncState();
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            public boolean onNavigationItemSelected(MenuItem item) {
                CustomerHomeActivity.this.actionBar.setTitle(item.getTitle());
                CustomerHomeActivity.this.Drawer.closeDrawers();
                switch (item.getItemId()) {
                    case R.id.addcomplaint /*2131361942*/:
                        CustomerHomeActivity.this.startActivity(new Intent(CustomerHomeActivity.this, AddComplaintActivity.class));
                        return true;
                    case R.id.complaintstatus /*2131362067*/:
                        CustomerHomeActivity.this.startActivity(new Intent(CustomerHomeActivity.this, ComplaintStatusActivity.class));
                        return true;
                    case R.id.donation /*2131362115*/:
                        CustomerHomeActivity.this.startActivity(new Intent(CustomerHomeActivity.this, DonationActivity.class));
                        return true;
                    case R.id.logout /*2131362306*/:
                        AlertDialog.Builder builder = new AlertDialog.Builder(CustomerHomeActivity.this);
                        builder.setMessage("Are you sure you want to logout?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                CustomerHomeActivity.this.session.logoutUser();
                                CustomerHomeActivity.this.finish();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                        builder.create().show();
                        return true;
                    case R.id.profile /*2131362469*/:
                        CustomerHomeActivity.this.startActivity(new Intent(CustomerHomeActivity.this, CustomerProfileActivity.class));
                        return true;
                    case R.id.ShareApp /*2131362469*/:
                    case R.id.Rate /*2131362469*/:
                        try {
                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "OASIS App");
                            String shareMessage = "Download OASIS GLOBE App Now\n\nhttps://play.google.com/store/apps/details?id="+getPackageName();
                            //     shareMessage = shareMessage + "https://zene.page.link/Zi7X" + BuildConfig.APPLICATION_ID +"\n\n";
                            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                            startActivity(Intent.createChooser(shareIntent, "choose one"));
                        } catch (Exception e) {
                            //e.toString();
                        }
                        return true;
                    default:
                        return true;
                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void onLocationChanged(Location location) {
        this.lat = String.valueOf(location.getLatitude());
        this.lon = String.valueOf(location.getLongitude());
    }

    public void onProviderDisabled(String provider2) {
        if (ActivityCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") != 0) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.ACCESS_FINE_LOCATION"}, 0);
        }
    }

    public void onProviderEnabled(String provider2) {
    }

    public void onStatusChanged(String provider2, int status, Bundle extras) {
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    @Override
    protected void onStart() {

        // getAllData();

        if (flag1 > 0) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {

                            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(i);
                        }
                    });
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        super.onStart();
    }
}
