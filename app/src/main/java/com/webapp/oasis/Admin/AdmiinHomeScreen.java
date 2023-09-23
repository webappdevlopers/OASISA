package com.webapp.oasis.Admin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.gms.common.internal.ImagesContract;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.webapp.oasis.Admin.Map.MapsActivity;
import com.webapp.oasis.Donation.DonationActivity;
import com.webapp.oasis.R;
import com.webapp.oasis.SplashIntro.SplashActivity;
import com.webapp.oasis.Utilities.SessionManager;
import java.util.HashMap;

import io.reactivex.annotations.NonNull;

public class AdmiinHomeScreen extends AppCompatActivity implements ActionBar.TabListener {
    DrawerLayout Drawer;
    ActionBar actionBar;
    TextView email1;
    TextView name1;
    ImageView profile_image;
    RelativeLayout rl_additem;
    RelativeLayout rl_driverdetails;
    RelativeLayout rl_invoices;
    RelativeLayout rl_tracking;
    RelativeLayout rladdagent;
    RelativeLayout rllogout;
    RelativeLayout rlnotification;
    String role;
    SessionManager session;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    TextView tvdemo;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.drawer_admin);
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        this.session = sessionManager;
        HashMap<String, String> users = sessionManager.getUserDetails();
        this.role = users.get(SessionManager.KEY_ADMIN_ROLE);
        TextView textView = (TextView) findViewById(R.id.tvdemo);
        this.tvdemo = textView;
        rllogout = findViewById(R.id.rllogout);
        textView.setText("Hello, " + users.get("name"));
        Log.d("role", this.role);
        this.rlnotification = (RelativeLayout) findViewById(R.id.rlnotification);
        this.rladdagent = (RelativeLayout) findViewById(R.id.rladdagent);
        if (this.role.equals("1")) {
            this.rlnotification.setVisibility(8);
        } else {
            this.rladdagent.setVisibility(8);
            this.rlnotification.setVisibility(0);
        }
        rllogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                session.logoutUser();
            }
        });
        this.rlnotification.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (AdmiinHomeScreen.this.role.equals("1")) {
                    Intent intent = new Intent(AdmiinHomeScreen.this, NotificationActivity.class);
                    intent.putExtra(ImagesContract.URL, "https://itkbusiness.online/rastasur/api/super_admin_notification_list");
                    AdmiinHomeScreen.this.startActivity(intent);
                    return;
                }
                Intent intent2 = new Intent(AdmiinHomeScreen.this, NotificationActivity.class);
                intent2.putExtra(ImagesContract.URL, "http://itkbusiness.online/rastasur/api/admin_notification_list");
                AdmiinHomeScreen.this.startActivity(intent2);
            }
        });
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.rl_tracking);
        this.rl_tracking = relativeLayout;
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AdmiinHomeScreen.this.startActivity(new Intent(AdmiinHomeScreen.this, MapsActivity.class));
            }
        });
        this.rladdagent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AdmiinHomeScreen.this.startActivity(new Intent(AdmiinHomeScreen.this, AddTechnicianActivity.class));
            }
        });
        RelativeLayout relativeLayout2 = (RelativeLayout) findViewById(R.id.rl_driverdetails);
        this.rl_driverdetails = relativeLayout2;
        relativeLayout2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(AdmiinHomeScreen.this, AdminDriverListActivity.class);
                intent.putExtra(ImagesContract.URL, "http://itkbusiness.online/rastasur/api/order_detail_for_agent");
                AdmiinHomeScreen.this.startActivity(intent);
            }
        });
        RelativeLayout relativeLayout3 = (RelativeLayout) findViewById(R.id.rl_additem);
        this.rl_additem = relativeLayout3;
        relativeLayout3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (AdmiinHomeScreen.this.role.equals("1")) {
                    Intent intent = new Intent(AdmiinHomeScreen.this, NotificationActivity.class);
                    intent.putExtra(ImagesContract.URL, "https://itkbusiness.online/rastasur/api/super_admin_notification_list");
                    AdmiinHomeScreen.this.startActivity(intent);
                    return;
                }
                Intent intent2 = new Intent(AdmiinHomeScreen.this, NotificationActivity.class);
                intent2.putExtra(ImagesContract.URL, "http://itkbusiness.online/rastasur/api/admin_notification_list");
                AdmiinHomeScreen.this.startActivity(intent2);
            }
        });
        RelativeLayout relativeLayout4 = (RelativeLayout) findViewById(R.id.rl_invoices);
        this.rl_invoices = relativeLayout4;
        relativeLayout4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (AdmiinHomeScreen.this.role.equals("1")) {
                    Intent intent = new Intent(AdmiinHomeScreen.this, AminInvoiceListActivity.class);
                    intent.putExtra(ImagesContract.URL, "https://itkbusiness.online/rastasur/api/super_admin_order_detail");
                    AdmiinHomeScreen.this.startActivity(intent);
                    return;
                }
                Intent intent2 = new Intent(AdmiinHomeScreen.this, AminInvoiceListActivity.class);
                intent2.putExtra(ImagesContract.URL, "https://itkbusiness.online/rastasur/api/agent_order_detail");
                AdmiinHomeScreen.this.startActivity(intent2);
            }
        });
        RelativeLayout relativeLayout5 = (RelativeLayout) findViewById(R.id.rllogout);
        this.rllogout = relativeLayout5;
        relativeLayout5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdmiinHomeScreen.this);
                builder.setMessage("Are you sure you want to logout?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AdmiinHomeScreen.this.session.logoutUser();
                        AdmiinHomeScreen.this.finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                builder.create().show();
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
                AdmiinHomeScreen.this.actionBar.setTitle(item.getTitle());
                AdmiinHomeScreen.this.Drawer.closeDrawers();
                switch (item.getItemId()) {
                    case R.id.Rate /*2131361854*/:
                        AdmiinHomeScreen.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + AdmiinHomeScreen.this.getPackageName() + "")));
                        return true;
                    case R.id.ShareApp /*2131361870*/:
                        try {
                            Intent shareIntent = new Intent("android.intent.action.SEND");
                            shareIntent.setType("text/plain");
                            shareIntent.putExtra("android.intent.extra.SUBJECT", "OASIS GLOBE App");
                            shareIntent.putExtra("android.intent.extra.TEXT", "Download OASIS GLOBE App Now\n\nhttps://play.google.com/store/apps/details?id=" + AdmiinHomeScreen.this.getPackageName());
                            AdmiinHomeScreen.this.startActivity(Intent.createChooser(shareIntent, "choose one"));
                            return true;
                        } catch (Exception e) {
                            return true;
                        }
                    case R.id.donation /*2131362115*/:
                        AdmiinHomeScreen.this.startActivity(new Intent(AdmiinHomeScreen.this, DonationActivity.class));
                        return true;
                    case R.id.invoice /*2131362231*/:
                        if (AdmiinHomeScreen.this.role.equals("1")) {
                            Intent intent = new Intent(AdmiinHomeScreen.this, AminInvoiceListActivity.class);
                            intent.putExtra(ImagesContract.URL, "https://itkbusiness.online/rastasur/api/super_admin_order_detail");
                            AdmiinHomeScreen.this.startActivity(intent);
                            return true;
                        }
                        Intent intent2 = new Intent(AdmiinHomeScreen.this, AminInvoiceListActivity.class);
                        intent2.putExtra(ImagesContract.URL, "https://itkbusiness.online/rastasur/api/agent_order_detail");
                        AdmiinHomeScreen.this.startActivity(intent2);
                        return true;
                    case R.id.logout /*2131362306*/:
                        AlertDialog.Builder builder = new AlertDialog.Builder(AdmiinHomeScreen.this);
                        builder.setMessage("Are you sure you want to logout?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                AdmiinHomeScreen.this.session.logoutUser();
                                AdmiinHomeScreen.this.finish();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                        builder.create().show();
                        return true;
                    case R.id.menunotification /*2131362343*/:
                        if (AdmiinHomeScreen.this.role.equals("1")) {
                            Intent intent3 = new Intent(AdmiinHomeScreen.this, NotificationActivity.class);
                            intent3.putExtra(ImagesContract.URL, "https://itkbusiness.online/rastasur/api/super_admin_notification_list");
                            AdmiinHomeScreen.this.startActivity(intent3);
                            return true;
                        }
                        Intent intent4 = new Intent(AdmiinHomeScreen.this, NotificationActivity.class);
                        intent4.putExtra(ImagesContract.URL, "http://itkbusiness.online/rastasur/api/admin_notification_list");
                        AdmiinHomeScreen.this.startActivity(intent4);
                        return true;
                    case R.id.orderdetail /*2131362416*/:
                        Intent intent22 = new Intent(AdmiinHomeScreen.this, AdminDriverListActivity.class);
                        intent22.putExtra(ImagesContract.URL, "http://itkbusiness.online/rastasur/api/order_detail_for_agent");
                        AdmiinHomeScreen.this.startActivity(intent22);
                        return true;
                    case R.id.tracking /*2131362688*/:
                        AdmiinHomeScreen.this.startActivity(new Intent(AdmiinHomeScreen.this, MapsActivity.class));
                        return true;
                    default:
                        return true;
                }
            }
        });
    }
    public void onBackPressed() {
        Intent a = new Intent("android.intent.action.MAIN");
        a.addCategory("android.intent.category.HOME");
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(a);
        super.onBackPressed();
    }

    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }
}
