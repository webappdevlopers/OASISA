package com.webapp.oasis.Technician;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.webapp.oasis.Customer.Adapter.ComplaintStatusListAdapter;
import com.webapp.oasis.Customer.See_Full_Image_F;
import com.webapp.oasis.Model.AgentListModel;
import com.webapp.oasis.R;
import com.webapp.oasis.Utilities.SessionManager;
import com.webapp.oasis.databinding.FragmentDashboardBinding;

import java.util.HashMap;
import java.util.Iterator;

/* renamed from: com.webapp.oasis.Technician.ui.dashboard.DashboardFragment */
public class TechnicianProfileFragment extends Fragment {
    private FragmentDashboardBinding binding;
    SessionManager session;
    String technicianId_session;
    String url ="";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);

        SessionManager sessionManager = new SessionManager(getActivity());
        this.session = sessionManager;
        HashMap<String, String> users = sessionManager.getUserDetails();
        technicianId_session = users.get(SessionManager.KEY_TecnicianID);


        final ProgressDialog showMe1 = new ProgressDialog(getActivity());
        showMe1.setMessage("Please wait");
        showMe1.setCancelable(true);
        showMe1.setCanceledOnTouchOutside(false);
        showMe1.show();
        FirebaseDatabase.getInstance("https://oasis-a3b2c-default-rtdb.firebaseio.com/").getReference("Techinician/TechinicianDetails").addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                    while (it.hasNext()) {
                        DataSnapshot itemSnapshot = it.next();
                        AgentListModel agentListModel2 = new AgentListModel((String) itemSnapshot.child("Name").getValue(String.class), (String) itemSnapshot.child("Mobile").getValue(String.class), (String) itemSnapshot.child("Technician Password").getValue(String.class), (String) itemSnapshot.child("email").getValue(String.class), (String) itemSnapshot.child("AdhaarCard").getValue(String.class), (String) itemSnapshot.child("License").getValue(String.class), (String) itemSnapshot.child("Technician ID").getValue(String.class),(String) itemSnapshot.child("isDelete").getValue(String.class));
                        if(agentListModel2.getTechnician_id().equals(technicianId_session)){

                            TechnicianProfileFragment.this.binding.name.setText(agentListModel2.getName());
                            TechnicianProfileFragment.this.binding.mobile.setText(agentListModel2.getMobile());
                            TechnicianProfileFragment.this.binding.email.setText(agentListModel2.getEmail());
                            url = agentListModel2.getLicense();
                            Glide.with(TechnicianProfileFragment.this).load(agentListModel2.getLicense()).
                                    placeholder((int) R.drawable.noimage).into(TechnicianProfileFragment.this.binding.rluploadAdhaarcard);
                        }
                        showMe1.dismiss();
                    }
                    return;
                }
                showMe1.dismiss();
            }

            public void onCancelled(DatabaseError databaseError) {
                Log.w("TAG", "Failed to read value.", databaseError.toException());
            }
        });
        TechnicianProfileFragment.this.binding.rluploadAdhaarcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!url.isEmpty()) {
                        Log.d("user_details.getImage()", url);
                        Intent intent = new Intent(getActivity(), See_Full_Image_F.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("imageurl", url);
                        startActivity(intent);
                    }
                }catch (Exception e){

                }
            }
        });
        return this.binding.getRoot();
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.binding = null;
    }
}
