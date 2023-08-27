package com.webapp.oasis.Customer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;

import com.webapp.oasis.Model.After_Place_Order_Successs_Model;
import com.webapp.oasis.R;
import com.webapp.oasis.Utilities.SessionManager;

public class After_Place_Order_Successs_Adapter extends RecyclerView.Adapter<After_Place_Order_Successs_Adapter.ViewHolder> {

    int lastPosition = -1;
    private Context context;
    private List<After_Place_Order_Successs_Model> list;
    SessionManager sessionManager;
    String username,uhash,uemail,uid;
    StringRequest stringRequest;
    RequestQueue mRequestQueue;
    String cityid;

    public After_Place_Order_Successs_Adapter(Context context, List<After_Place_Order_Successs_Model> list) {
        this.context = context;
        this.list=list;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.after_place_order_successs_details, parent, false);
        ViewHolder vh = new ViewHolder(v);

        sessionManager=new SessionManager(context);
        HashMap<String, String> users = sessionManager.getUserDetails();
        uid = users.get(SessionManager.KEY_USERID);
        uhash = users.get(SessionManager.KEY_HASH);

        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {


        final After_Place_Order_Successs_Model model=list.get(i);
        viewHolder.name.setText(model.getItem_name());
        viewHolder.weight.setText(model.getItem_weight());
        viewHolder.size.setText(model.getItem_size());
        viewHolder.qty.setText(model.getQty());
        Glide.with(context).load(model.getItem_image()).into(viewHolder.image);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;
        public TextView name,weight,size, qty;

        public ViewHolder(View itemView) {
            super(itemView);


            image=itemView.findViewById(R.id.image);
            name=itemView.findViewById(R.id.name);
            weight=itemView.findViewById(R.id.weight);
            qty=itemView.findViewById(R.id.qty);
            size=itemView.findViewById(R.id.size);

        }
    }

}
