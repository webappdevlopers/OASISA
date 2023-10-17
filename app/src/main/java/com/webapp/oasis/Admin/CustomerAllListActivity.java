package com.webapp.oasis.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.webapp.oasis.Admin.Adapter.AdminListAdapter;
import com.webapp.oasis.Admin.Adapter.BillSelectedItemListAdapter;
import com.webapp.oasis.Admin.Adapter.CustomerAllListAdapter;
import com.webapp.oasis.Model.AgentListModel;
import com.webapp.oasis.Model.CustomerAllListModel;
import com.webapp.oasis.Model.ItemsModel;
import com.webapp.oasis.R;
import com.webapp.oasis.Utilities.SessionManager;

import java.util.ArrayList;

public class CustomerAllListActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    CustomerAllListAdapter myOrderAdapter;
    SessionManager session;
    ArrayList<CustomerAllListModel> customerAllListModels = new ArrayList<>();
    ImageView back;
    private SearchView searchView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_all_list);

        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        searchView = findViewById(R.id.searchview);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (myOrderAdapter == null) {
                    return false;
                } else {
                    myOrderAdapter.getFilter().filter(newText);
                    return true;
                }
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.itemlistrecycler);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        final ProgressDialog showMe = new ProgressDialog(this);
        showMe.setMessage("Please wait");
        showMe.setCancelable(true);
        showMe.setCanceledOnTouchOutside(false);
        showMe.show();
        FirebaseDatabase.getInstance("https://oasis-a3b2c-default-rtdb.firebaseio.com/").getReference("Customer/Registration").addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    customerAllListModels.clear();
                    for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                        customerAllListModels.add(new CustomerAllListModel((String) itemSnapshot.child("Name").getValue(String.class),
                                (String) itemSnapshot.child("MobileNumber").getValue(String.class),
                                (String) itemSnapshot.child("Email").getValue(String.class),
                                (String) itemSnapshot.child("Area").getValue(String.class) + (String) itemSnapshot.child("Address").getValue(String.class) + (String) itemSnapshot.child("Pincode").getValue(String.class),
                                (String) itemSnapshot.child("Machine Model").getValue(String.class),
                                (String) itemSnapshot.child("MachineMake").getValue(String.class)));

                        myOrderAdapter = new CustomerAllListAdapter(getApplicationContext(), customerAllListModels);
                        recyclerView.setAdapter(myOrderAdapter);
                        myOrderAdapter.notifyDataSetChanged();
                        showMe.dismiss();
                    }
                    return;
                }
                showMe.dismiss();
            }

            public void onCancelled(DatabaseError databaseError) {
                Log.w("TAG", "Failed to read value.", databaseError.toException());
            }
        });
    }
}
