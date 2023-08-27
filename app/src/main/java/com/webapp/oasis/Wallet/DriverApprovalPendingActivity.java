package com.webapp.oasis.Wallet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.webapp.oasis.R;

public class DriverApprovalPendingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_approval_pending);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
