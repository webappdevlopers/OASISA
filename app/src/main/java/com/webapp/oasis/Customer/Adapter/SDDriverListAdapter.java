package com.webapp.oasis.Customer.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.HashMap;
import java.util.List;

import com.webapp.oasis.Customer.PlaceOrderActivity1;
import com.webapp.oasis.Model.SDDriverListModel;
import com.webapp.oasis.R;
import com.webapp.oasis.Utilities.SessionManager;

public class SDDriverListAdapter extends RecyclerView.Adapter<SDDriverListAdapter.ViewHolder> {

    int lastPosition = -1;
    private Context context;

    StringRequest stringRequest;
    RequestQueue requestQueue;
    private List<SDDriverListModel> mUser;
    SessionManager session;
    String user_id, hash;


    public SDDriverListAdapter(Context context, List<SDDriverListModel> mUser) {
        this.context = context;
        this.mUser = mUser;

        session = new SessionManager(context);
        HashMap<String, String> users = session.getUserDetails();
        user_id = users.get(session.KEY_USERID);
        hash = users.get(session.KEY_HASH);

    }
    @Override
    public int getItemCount() {
        return mUser.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_vehicle_list_details, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int i) {
        final SDDriverListModel user_details = mUser.get(i);

        holder.name.setText(user_details.getName());
        holder.mobile.setText(user_details.getMobile());
//        holder.place.setText(user_details.getPlace());
//        holder.vehical_no.setText(user_details.getVehical_no());
        holder.destination.setText(user_details.getDestination()+" to "+user_details.getDestination());
        holder.vehicle_type.setText(user_details.getVehicle_type());

        Glide
                .with(context)
                .load(user_details.getImage())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_delivery_truck)
                        .fitCenter())
                .into(holder.image);

        holder.btnplaceorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlaceOrderActivity1.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("did", user_details.getId());
                intent.putExtra("dname", user_details.getName());
                intent.putExtra("ddest", user_details.getDestination()+" to "+user_details.getDestination());
                intent.putExtra("dimage", user_details.getImage());
                intent.putExtra("vtype", user_details.getVehicle_type());
                intent.putExtra("source", user_details.getSourceuser());
                intent.putExtra("destination", user_details.getDestinationuser());
                context.startActivity(intent);
            }
        });

    }
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name,mobile,place,vehical_no,destination,maxodrpkg,minodrpkg,vehicle_type;
        ImageView image;
        Button btnplaceorder;

        public ViewHolder(View itemView) {
            super(itemView);

            vehicle_type = itemView.findViewById(R.id.vehicle_type);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            mobile = itemView.findViewById(R.id.mobile);
            place = itemView.findViewById(R.id.place);
            vehical_no = itemView.findViewById(R.id.vehical_no);
            destination = itemView.findViewById(R.id.destination);
            btnplaceorder = itemView.findViewById(R.id.btnplaceorder);
            maxodrpkg = itemView.findViewById(R.id.maxodrpkg);
            minodrpkg = itemView.findViewById(R.id.minodrpkg);
        }
    }
}
