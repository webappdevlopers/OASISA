package com.webapp.oasis.Technician;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.webapp.oasis.R;
import com.webapp.oasis.databinding.ActivityTechnicianHomepageBinding;

public class technicianHomeActivity extends AppCompatActivity {
    private ActivityTechnicianHomepageBinding binding;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTechnicianHomepageBinding inflate = ActivityTechnicianHomepageBinding.inflate(getLayoutInflater());
        this.binding = inflate;
        setContentView((View) inflate.getRoot());
        AppBarConfiguration build = new AppBarConfiguration.Builder(R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications).build();
        NavigationUI.setupWithNavController((BottomNavigationView) findViewById(R.id.nav_view), Navigation.findNavController(this, R.id.nav_host_fragment_activity_main2));
    }

    public void onBackPressed() {
        Intent a = new Intent("android.intent.action.MAIN");
        a.addCategory("android.intent.category.HOME");
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
        super.onBackPressed();
    }

}
