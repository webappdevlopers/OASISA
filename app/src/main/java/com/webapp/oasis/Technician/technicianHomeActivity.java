package com.webapp.oasis.Technician;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.webapp.oasis.R;
import com.webapp.oasis.Utilities.SessionManager;
import com.webapp.oasis.databinding.ActivityTechnicianHomepageBinding;

import java.util.HashMap;

public class technicianHomeActivity extends AppCompatActivity {
    private ActivityTechnicianHomepageBinding binding;
    SessionManager session;
    String technicianId;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTechnicianHomepageBinding inflate = ActivityTechnicianHomepageBinding.inflate(getLayoutInflater());
        this.binding = inflate;
        setContentView((View) inflate.getRoot());
        session = new SessionManager(technicianHomeActivity.this);
        final HashMap<String, String> users = session.getUserDetails();
        technicianId = users.get(SessionManager.KEY_TecnicianID);
        AppBarConfiguration build = new AppBarConfiguration.Builder(R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications).build();
        NavigationUI.setupWithNavController((BottomNavigationView) findViewById(R.id.nav_view), Navigation.findNavController(this, R.id.nav_host_fragment_activity_main2));

        FirebaseDatabase.getInstance("https://oasis-a3b2c-default-rtdb.firebaseio.com/").getReference("Techinician/TechinicianDetails").addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                        String technician_id = itemSnapshot.child("Technician ID").getValue(String.class);
                        String isDelete = itemSnapshot.child("isDelete").getValue(String.class);
                        if (technician_id.equals(technicianId)) {
                            if (isDelete.equals("true")) {
                                session.logoutUser();
                            }
                        }
                    }
                }
            }

            public void onCancelled(DatabaseError databaseError) {
                Log.w("TAG", "Failed to read value.", databaseError.toException());
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
