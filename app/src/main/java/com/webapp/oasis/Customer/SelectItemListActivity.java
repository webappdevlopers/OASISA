package com.webapp.oasis.Customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.webapp.oasis.Admin.Adapter.SelectItemListAdapter;
import com.webapp.oasis.Model.AdminItemListModel;
import com.webapp.oasis.Model.CartModel;
import com.webapp.oasis.R;
import com.webapp.oasis.SqlliteDb.DatabaseHelper;
import com.webapp.oasis.Utilities.SessionManager;

public class SelectItemListActivity extends AppCompatActivity implements SelectItemListAdapter.CartListener{

    RecyclerView mRecyclerView;
    SelectItemListAdapter myOrderAdapter;
    String user_id, hash;
    SessionManager session;
    StringRequest stringRequest;
    RequestQueue mRequestQueue;
    ImageView back;
    ArrayList<AdminItemListModel> dm = new ArrayList<AdminItemListModel>();
    String did,dname,dmob,dplace,dvehno,ddest,dimage,drate,dmin,dmax;
    private SearchView searchView;
    private List<CartModel> productList = new ArrayList<>();
    SelectItemListAdapter.CartListener cartListener;
    public static TextView tltitem;
    DatabaseHelper mydb;
    LinearLayout llproceed;
    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_item_list);

        mydb = new DatabaseHelper(getApplicationContext());
        mydb.delete();

        cartListener = this;

        llproceed = findViewById(R.id.llproceed);
        tltitem = findViewById(R.id.tltitem);
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

        Intent i = getIntent();
        did=(i.getStringExtra("did"));
        dname=(i.getStringExtra("dname"));
        dmob=(i.getStringExtra("dmob"));
        dplace=(i.getStringExtra("dplace"));
        dvehno=(i.getStringExtra("dvehno"));
        ddest=(i.getStringExtra("ddest"));
        dimage=(i.getStringExtra("dimage"));
        drate=(i.getStringExtra("drate"));
        dmin=(i.getStringExtra("dmin"));
        dmax=(i.getStringExtra("dmax"));

        session = new SessionManager(getApplicationContext());
        final HashMap<String, String> users = session.getUserDetails();
        user_id = users.get(session.KEY_USERID);
        hash = users.get(session.KEY_HASH);
        Log.d("userid",user_id);
        Log.d("hash",hash);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.itemlistrecycler);
        mRecyclerView.setHasFixedSize(false);
        @SuppressLint("WrongConstant") RecyclerView.LayoutManager mLayoutManager12 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager12);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        myOrderAdapter = new SelectItemListAdapter(getApplicationContext(),dm,cartListener);
        mRecyclerView.setAdapter(myOrderAdapter);

//        item_list();

        llproceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tltitem.getText().toString().equals("0 Items Added"))
                {
                    Toast.makeText(SelectItemListActivity.this, "Please Select Item", Toast.LENGTH_SHORT).show();
                }
                else {
//                    place_order();
                }
            }
        });
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e(getClass().getSimpleName(), "refresh");
//                item_list();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void NetworkDialoguser_reviewd_details() {
        final Dialog dialogs = new Dialog(SelectItemListActivity.this);
        dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogs.setContentView(R.layout.networkdialog);
        dialogs.setCanceledOnTouchOutside(false);
        Button done = (Button) dialogs.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
//                item_list();
            }
        });
        dialogs.show();
    }
    private void NetworkDialoguser_place_order() {
        final Dialog dialogs = new Dialog(SelectItemListActivity.this);
        dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogs.setContentView(R.layout.networkdialog);
        dialogs.setCanceledOnTouchOutside(false);
        Button done = (Button) dialogs.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
//                place_order();
            }
        });
        dialogs.show();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SelectItemListActivity.this,CustomerPlaceOrderActivity.class));
        finish();
    }


    @Override
    public void onAdded(CartModel cartModel) {

    }

    @Override
    public void onRemoved(String s, String s1) {


        JsonArray itemArray = new JsonArray();
        for(int i=0;i<productList.size();i++){
            JsonObject product = new JsonObject();
            product.addProperty("item_id", productList.get(i).get_id());
            product.addProperty("qty", productList.get(i).getQty());
            itemArray.add(product);
        }
        tltitem.setText(""+itemArray.size());
        Log.d("data", itemArray.toString());

        //productList.clear();
        Toast.makeText(this, "item removed", Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, ""+productList.size()+" availables in cart", Toast.LENGTH_SHORT).show();
    }

}