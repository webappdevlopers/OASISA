package com.webapp.oasis.Driver.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.HashMap;
import java.util.List;

import com.webapp.oasis.Driver.VehiclaUpdateDetailsActivity;
import com.webapp.oasis.Model.VehicleListModel;
import com.webapp.oasis.R;
import com.webapp.oasis.Utilities.SessionManager;

public class VehicleListAdapter extends RecyclerView.Adapter<VehicleListAdapter.ViewHolder> {

    int lastPosition = -1;
    private Context mContext;
    private List<VehicleListModel> mUser;
    SessionManager session;
    String user_id, hash;
    RequestQueue requestQueue;


    public VehicleListAdapter(Context context, List<VehicleListModel> mUser) {
        this.mContext = context;
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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.vehicle_list_details, parent, false);
        ViewHolder vh = new ViewHolder(v);
        requestQueue = Volley.newRequestQueue(mContext);
        return vh;
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int i) {
        final VehicleListModel user_details = mUser.get(i);

        holder.vehical_no.setText(user_details.getVehical_no());
        holder.licence_no.setText(user_details.getLicence_no());

        holder.source.setText(user_details.getSource());
        holder.destination.setText(user_details.getDestination());
        holder.costpkg.setText(user_details.getCostpkg());
        holder.minodrpkg.setText(user_details.getMinodrpkg());
        holder.maxodrpkg.setText(user_details.getMaxodrpkg());

        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, VehiclaUpdateDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("licence_no", user_details.getLicence_no());
                intent.putExtra("vehical_no", user_details.getVehical_no());
                intent.putExtra("source", user_details.getSource());
                intent.putExtra("destination", user_details.getDestination());
                intent.putExtra("costpkg", user_details.getCostpkg());
                intent.putExtra("image", user_details.getImage());
                intent.putExtra("minodrpkg", "1");
                intent.putExtra("maxodrpkg", "1");
                mContext.startActivity(intent);
            }

        });
        Glide
                .with(mContext)
                .load(user_details.getImage())
                .apply(new RequestOptions()
                        .fitCenter())
                .into(holder.image);

    }
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView vehical_no,licence_no,source,destination,costpkg,minodrpkg,maxodrpkg;

        ImageView image, update, delete;
        public ViewHolder(View itemView) {
            super(itemView);

            vehical_no=itemView.findViewById(R.id.vehical_no);
            licence_no=itemView.findViewById(R.id.licence_no);
            update = itemView.findViewById(R.id.update);
            source=itemView.findViewById(R.id.source);
            destination=itemView.findViewById(R.id.destination);
            costpkg = itemView.findViewById(R.id.costpkg);
            minodrpkg=itemView.findViewById(R.id.minodrpkg);
            maxodrpkg=itemView.findViewById(R.id.maxodrpkg);
            image = itemView.findViewById(R.id.image);

        }
    }
}
