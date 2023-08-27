package com.webapp.oasis.Retailer;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;

import com.webapp.oasis.Admin.AddAgentActivity;
import com.webapp.oasis.Admin.AdminDriverListActivity;
import com.webapp.oasis.Admin.AminInvoiceListActivity;
import com.webapp.oasis.Admin.Map.MapsActivity;
import com.webapp.oasis.Admin.NotificationActivity;
import com.webapp.oasis.Donation.DonationActivity;
import com.webapp.oasis.R;
import com.webapp.oasis.Utilities.SessionManager;

public class RetailerActivity extends AppCompatActivity implements ActionBar.TabListener{
    RelativeLayout rl_driverdetails,rl_profile,rl_invoices,rl_tracking;
    RelativeLayout rlnotification,rllogout,rladdagent;
    SessionManager session;
    String role;
    TextView tvdemo;

    ActionBarDrawerToggle toggle;
    DrawerLayout Drawer;
    androidx.appcompat.widget.Toolbar toolbar;
    androidx.appcompat.app.ActionBar actionBar;
    ImageView profile_image;
    TextView name1;
    TextView email1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_retailer);


        session = new SessionManager(getApplicationContext());
        final HashMap<String, String> users = session.getUserDetails();
        role = users.get(session.KEY_ADMIN_ROLE);
        tvdemo = findViewById(R.id.tvdemo);
        tvdemo.setText("Hello, "+users.get(session.KEY_NAME));

        Log.d("role",role);

        rladdagent = findViewById(R.id.rladdagent);
        if (role.equals("1"))
        {
            rladdagent.setVisibility(View.VISIBLE);
        }
        else {
            rladdagent.setVisibility(View.GONE);
        }
        rlnotification = findViewById(R.id.rlnotification);
        rlnotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RetailerActivity.this, NotificationActivity.class);
                startActivity(intent);
            }
        });
        rl_tracking = findViewById(R.id.rl_tracking);
        rl_tracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Uiddd",users.get(session.KEY_USERID));
                Intent intent = new Intent(RetailerActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
        rladdagent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RetailerActivity.this, AddAgentActivity.class);
                startActivity(intent);
            }
        });
        rl_driverdetails = findViewById(R.id.rl_driverdetails);
        rl_driverdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RetailerActivity.this, AdminDriverListActivity.class);
                intent.putExtra("url","https://itkbusiness.online/rastasur/api/agent_order_detail");

                startActivity(intent);
            }
        });
        rl_profile = findViewById(R.id.rl_profile);
        rl_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RetailerActivity.this, RetailerProfileActivity.class);
                startActivity(intent);
            }
        });
        rl_invoices = findViewById(R.id.rl_invoices);
        rl_invoices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RetailerActivity.this, AminInvoiceListActivity.class);
                startActivity(intent);
            }
        });

        rllogout = findViewById(R.id.rllogout);
        rllogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RetailerActivity.this);
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

                    case R.id.tracking:
                        Log.d("Uiddd",users.get(session.KEY_USERID));
                        Intent intent = new Intent(RetailerActivity.this, MapsActivity.class);
                        startActivity(intent);

                        break;
                    case R.id.orderdetail:
                        Intent intent2 = new Intent(RetailerActivity.this, AdminDriverListActivity.class);
                        intent2.putExtra("url","https://itkbusiness.online/rastasur/api/agent_order_detail");

                        startActivity(intent2);

                        break;
                    case R.id.profile:
                        Intent intent3 = new Intent(RetailerActivity.this, RetailerProfileActivity.class);
                        startActivity(intent3);
                        break;
                    case R.id.invoice:
                        Intent intent4 = new Intent(RetailerActivity.this, AminInvoiceListActivity.class);
                        startActivity(intent4);

                        break;
                    case R.id.donation:
                        Intent intents = new Intent(RetailerActivity.this, DonationActivity.class);
                        startActivity(intents);

                        break;
                    case R.id.logout:
                        AlertDialog.Builder builder = new AlertDialog.Builder(RetailerActivity.this);
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
    }

    @Override
    public void onBackPressed() {

        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

        super.onBackPressed();
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