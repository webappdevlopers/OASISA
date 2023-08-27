package com.webapp.oasis.Admin.Stock;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.webapp.oasis.Admin.AddStockActivity;
import com.webapp.oasis.Admin.Add_Admin_credentials;
import com.webapp.oasis.Admin.AdminAddServiceActivity;
import com.webapp.oasis.Admin.AdminHomePage;
import com.webapp.oasis.LoginFirstScreen;
import com.webapp.oasis.R;
import com.webapp.oasis.Utilities.SessionManager;
import com.webapp.oasis.databinding.FragmentStockBinding;

import java.util.HashMap;

public class StockFragment extends Fragment {
    FragmentStockBinding binding;
    SessionManager session;
    String logincode;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentStockBinding inflate = FragmentStockBinding.inflate(getLayoutInflater());

        session = new SessionManager(getActivity());
        final HashMap<String, String> users = session.getUserDetails();
        this.logincode = session.getUserDetails().get(SessionManager.KEY_LoginCode);
        this.binding = inflate;

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
