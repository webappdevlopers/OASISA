package com.webapp.oasis.Admin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.webapp.oasis.Admin.Complaintdetails;
import com.webapp.oasis.Customer.See_Full_Image_F;
import com.webapp.oasis.Model.AgentDriverOrderListModel;
import com.webapp.oasis.Model.CustomerAllListModel;
import com.webapp.oasis.R;
import com.webapp.oasis.Utilities.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomerAllListAdapter extends RecyclerView.Adapter<CustomerAllListAdapter.ViewHolder> implements Filterable {

    public Context context;
    private List<CustomerAllListModel> mUser;
    private List<CustomerAllListModel> mUser1;
    SessionManager session;

    public CustomerAllListAdapter(Context context2, List<CustomerAllListModel> mUser2) {
        this.context = context2;
        this.mUser = mUser2;
        this.mUser1 = mUser2;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public int getItemCount() {
        return this.mUser.size();
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_all_list_details, parent, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final CustomerAllListModel user_details = this.mUser.get(i);
        viewHolder.name.setText(user_details.getName());
        viewHolder.mobile.setText(user_details.getMobileNumber());
        viewHolder.tvemail.setText(user_details.getEmail());

        viewHolder.tvmachinemake.setText(user_details.getMachineMake());
        if (user_details.getMachineMake().isEmpty())
            viewHolder.tvmachinemake.setVisibility(View.GONE);

        viewHolder.tvmachinemodel.setText(user_details.getMachineModel());
        if (user_details.getMachineModel().isEmpty())
            viewHolder.tvmachinemake.setVisibility(View.GONE);
        viewHolder.tvaddress.setText(user_details.getAddress());

//        viewHolder.parcel_img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    if (!user_details.getImage().isEmpty()) {
//                        Log.d("user_details.getImage()", user_details.getImage());
//                        Intent intent = new Intent(context, See_Full_Image_F.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        intent.putExtra("imageurl", user_details.getImage());
//                        context.startActivity(intent);
//                    }
//                }catch (Exception e){
//
//                }
//            }
//        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,mobile,tvemail,tvmachinemake,tvmachinemodel,tvaddress;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            mobile = (TextView) itemView.findViewById(R.id.mobile);
            tvemail = (TextView) itemView.findViewById(R.id.tvemail);
            tvmachinemake = (TextView) itemView.findViewById(R.id.tvmachinemake);
            tvmachinemodel = (TextView) itemView.findViewById(R.id.tvmachinemodel);
            tvaddress = (TextView) itemView.findViewById(R.id.tvaddress);
        }
    }

    @Override
    public android.widget.Filter getFilter() {
        return new android.widget.Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String name = charSequence.toString().toLowerCase(); // Convert search query to lowercase
                String customerMobileNumber = charSequence.toString().toLowerCase(); // Convert search query to lowercase
                String customeraddress = charSequence.toString().toLowerCase(); // Convert search query to lowercase
                String customeremail = charSequence.toString().toLowerCase(); // Convert search query to lowercase

                if (name.isEmpty() || customerMobileNumber.isEmpty() || customeraddress.isEmpty() || customeremail.isEmpty()) {
                    mUser = mUser1;
                } else {
                    ArrayList<CustomerAllListModel> filteredList = new ArrayList<>();

                    for (CustomerAllListModel androidVersion : mUser1) {

                        if (androidVersion.getName().toLowerCase().contains(name)
                                || androidVersion.getMobileNumber().toLowerCase().contains(customerMobileNumber)
                                || androidVersion.getAddress().toLowerCase().contains(customeraddress)
                                || androidVersion.getEmail().toLowerCase().contains(customeremail)) {
                            filteredList.add(androidVersion);
                        }
                    }
                    mUser = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mUser;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mUser = (ArrayList<CustomerAllListModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
