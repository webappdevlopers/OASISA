package com.webapp.oasis.Admin;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.webapp.oasis.R;

public class AdminHomePage extends AppCompatActivity {
    DrawerLayout Drawer;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_admin_home_page);
        this.Drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        AppBarConfiguration build = new AppBarConfiguration.Builder(R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_report).setDrawerLayout(this.Drawer).build();
        NavigationUI.setupWithNavController((BottomNavigationView) findViewById(R.id.nav_view), Navigation.findNavController(this, R.id.nav_host_fragment_activity_admin_home_page));

    }

    public void onBackPressed() {
        Intent a = new Intent("android.intent.action.MAIN");
        a.addCategory("android.intent.category.HOME");
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
        super.onBackPressed();
    }
}
