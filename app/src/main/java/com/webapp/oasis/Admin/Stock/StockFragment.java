package com.webapp.oasis.Admin.Stock;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.webapp.oasis.Admin.Adapter.AdminListAdapter;
import com.webapp.oasis.Admin.AddStockActivity;
import com.webapp.oasis.Admin.Add_Admin_credentials;
import com.webapp.oasis.Admin.AdminAddServiceActivity;
import com.webapp.oasis.Admin.AdminHomePage;
import com.webapp.oasis.LoginFirstScreen;
import com.webapp.oasis.Model.ItemsModel;
import com.webapp.oasis.R;
import com.webapp.oasis.Utilities.SessionManager;
import com.webapp.oasis.databinding.FragmentStockBinding;

import java.util.HashMap;

public class StockFragment extends Fragment {
    FragmentStockBinding binding;
    SessionManager session;
    String logincode, adminId;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentStockBinding inflate = FragmentStockBinding.inflate(getLayoutInflater());

        session = new SessionManager(getActivity());
        final HashMap<String, String> users = session.getUserDetails();
        adminId = users.get(SessionManager.KEY_AdminID);
        this.logincode = session.getUserDetails().get(SessionManager.KEY_LoginCode);
        this.binding = inflate;
        FirebaseDatabase.getInstance("https://oasis-a3b2c-default-rtdb.firebaseio.com/").getReference("ADMIN/ADMIN Credentials").addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                        try {
                            String AdminID = itemSnapshot.child("Admin ID").getValue(String.class);
                            String isDelete = itemSnapshot.child("isDelete").getValue(String.class);
                            if (AdminID.equals(adminId)) {
                                if (isDelete.equals("true")) {
                                    session.logoutUser();
                                }
                            }
                        }catch (Exception e){}
                    }
                }
            }

            public void onCancelled(DatabaseError databaseError) {
                Log.w("TAG", "Failed to read value.", databaseError.toException());
            }
        });

        inflate.mainContent.findViewById(R.id.rl_liststock).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                StockFragment.this.startActivity(new Intent(StockFragment.this.getActivity(), AdminAddServiceActivity.class));
            }
        });
        binding.mainContent.findViewById(R.id.rllogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure you want to logout?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                session.logoutUser();
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
        this.binding.mainContent.findViewById(R.id.rl_addstock).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                StockFragment.this.startActivity(new Intent(StockFragment.this.getActivity(), AddStockActivity.class));
            }
        });
        if (this.logincode.equals("5")) {
            binding.mainContent.findViewById(R.id.rl_ADD_ADMIN).setVisibility(View.GONE);
        }
        binding.mainContent.findViewById(R.id.rl_ADD_ADMIN).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StockFragment.this.getActivity(), Add_Admin_credentials.class));
            }
        });

        return this.binding.getRoot();
    }
}
