package com.webapp.oasis.Admin;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.webapp.oasis.BuildConfig;
import com.webapp.oasis.LoginFirstScreen;
import com.webapp.oasis.R;
import com.webapp.oasis.SweetAlertExample;
import com.webapp.oasis.Utilities.SessionManager;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.annotations.NonNull;

public class AdminHomePage extends AppCompatActivity {
    DrawerLayout Drawer;

    SessionManager session;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_admin_home_page);
        this.Drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        AppBarConfiguration build = new AppBarConfiguration.Builder(R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_report).setDrawerLayout(this.Drawer).build();
        NavigationUI.setupWithNavController((BottomNavigationView) findViewById(R.id.nav_view), Navigation.findNavController(this, R.id.nav_host_fragment_activity_admin_home_page));


        SessionManager sessionManager = new SessionManager(getApplicationContext());
        this.session = sessionManager;
        handlerr = new Handler() {
            public void handleMessage(android.os.Message msg) {
                FirebaseDatabase mDatabase = FirebaseDatabase.getInstance("https://oasis-a3b2c-default-rtdb.firebaseio.com/");
                DatabaseReference mDbRef = mDatabase.getReference("Update");

                mDbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                        String value = (String) dataSnapshot.getValue();

                        if (!value.equals(String.valueOf(BuildConfig.VERSION_CODE))) {
                            SweetAlertExample.showSweetAlert(AdminHomePage.this, "Update Available", "A new version is available. Update now ?", SweetAlertDialog.WARNING_TYPE, true);


                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        };


        handlerr.sendEmptyMessage(0);
        ConnectivityManager ConnectionManagerr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfoo = ConnectionManagerr.getActiveNetworkInfo();
        if (networkInfoo != null && networkInfoo.isConnected() == true) {

        } else {
            NetworkDialog();
        }


        handler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                FirebaseDatabase mDatabase = FirebaseDatabase.getInstance("https://oasis-a3b2c-default-rtdb.firebaseio.com/");
                DatabaseReference mDbRef = mDatabase.getReference("Enable");
                mDbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                        String value = (String) dataSnapshot.getValue();
                        if (value.equals("yes") || value == "Yes") {
                        } else if (value.equals("maintenance") || value == "Maintenance") {
                            Toast.makeText(AdminHomePage.this, "Server is in Maintenance\nKindly contact your developer", Toast.LENGTH_LONG).show();
                        } else if (value.equals("developercharges") || value == "Developercharges") {
                            getdata();
                        } else if (value.equals("server") || value == "Server") {
                            session.logoutUser();
                            Toast.makeText(AdminHomePage.this, "Server Plan is Expired\nKindly renew your server", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        };

        handler.sendEmptyMessage(0);
        ConnectivityManager ConnectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected() == true) {

        } else {
            NetworkDialog();
        }
    }

    private Handler handler = new Handler();
    private Handler handlerr = new Handler();

    private void NetworkDialog() {
        final Dialog dialogs = new Dialog(AdminHomePage.this);
        dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogs.setContentView(R.layout.networkdialog);
        dialogs.setCanceledOnTouchOutside(false);
        Button done = (Button) dialogs.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHomePage.this, AdminHomePage.class);
                startActivity(intent);
                finish();
                dialogs.dismiss();
            }
        });
        dialogs.show();
    }

    private void getdata() {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance("https://oasis-a3b2c-default-rtdb.firebaseio.com/");
        DatabaseReference mDbRef = mDatabase.getReference("msg");

        mDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                Toast.makeText(AdminHomePage.this, "" + value, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminHomePage.this, "Fail to get data.", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onBackPressed() {
        Intent a = new Intent("android.intent.action.MAIN");
        a.addCategory("android.intent.category.HOME");
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
        super.onBackPressed();
    }
}
