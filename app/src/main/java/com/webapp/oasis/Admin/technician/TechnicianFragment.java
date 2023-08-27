package com.webapp.oasis.Admin.technician;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import com.webapp.oasis.Admin.AddTechnicianActivity;
import com.webapp.oasis.Admin.TechnicianListActivity;
import com.webapp.oasis.R;
import com.webapp.oasis.Utilities.SessionManager;
import com.webapp.oasis.databinding.FragmentTechnicianBinding;

import java.util.HashMap;

/* renamed from: com.webapp.oasis.Admin.ui.technician.TechnicianFragment */
public class TechnicianFragment extends Fragment {
    FragmentTechnicianBinding binding;

    SessionManager session;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentTechnicianBinding inflate = FragmentTechnicianBinding.inflate(getLayoutInflater());
        this.binding = inflate;


        session = new SessionManager(getActivity());
        final HashMap<String, String> users = session.getUserDetails();

        inflate.rlTechnicialList.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                TechnicianFragment.this.lambda$onCreateView$0$TechnicianFragment(view);
            }
        });
        this.binding.rlAddtechnician.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                TechnicianFragment.this.lambda$onCreateView$1$TechnicianFragment(view);
            }
        });


        inflate.rllogout.setOnClickListener(new View.OnClickListener() {
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
        return this.binding.getRoot();
    }

    public /* synthetic */ void lambda$onCreateView$0$TechnicianFragment(View view) {
        startActivity(new Intent(getActivity(), TechnicianListActivity.class));
    }

    public /* synthetic */ void lambda$onCreateView$1$TechnicianFragment(View view) {
        startActivity(new Intent(getActivity(), AddTechnicianActivity.class));
    }
}
