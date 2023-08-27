package com.webapp.oasis.Admin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.webapp.oasis.Customer.See_Full_Image_F;
import com.webapp.oasis.Model.AgentListModel;
import com.webapp.oasis.R;
import com.webapp.oasis.Utilities.SessionManager;
import java.util.List;

public class technicianListAdapter extends RecyclerView.Adapter<technicianListAdapter.ViewHolder> {
    int lastPosition = -1;
    /* access modifiers changed from: private */
    public Context mContext;
    private List<AgentListModel> mUser;
    SessionManager session;

    public technicianListAdapter(Context context, List<AgentListModel> mUser2) {
        this.mContext = context;
        this.mUser = mUser2;
    }

    public int getItemCount() {
        return this.mUser.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.agent_list_details, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int i) {
        final AgentListModel user_details = this.mUser.get(i);
        holder.name.setText(user_details.getName());
        holder.mobile.setText(user_details.getMobile());
        holder.email.setText(user_details.getEmail());
        Glide.with(this.mContext).load(user_details.getAdhaarCard()).apply((BaseRequestOptions<?>) ((RequestOptions) new RequestOptions().placeholder((int) R.drawable.noimage)).fitCenter()).into((ImageView) holder.ProfilePicture);
        Glide.with(this.mContext).load(user_details.getLicense()).apply((BaseRequestOptions<?>) ((RequestOptions) new RequestOptions().placeholder((int) R.drawable.noimage)).fitCenter()).into((ImageView) holder.licence);
        holder.call.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.DIAL");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setData(Uri.parse("tel:" + user_details.getMobile()));
                technicianListAdapter.this.mContext.startActivity(intent);
            }
        });
        holder.licence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!user_details.getAdhaarCard().isEmpty()) {
                        Log.d("user_details.getImage()", user_details.getLicense());
                        Intent intent = new Intent(mContext, See_Full_Image_F.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("imageurl", user_details.getLicense());
                        mContext.startActivity(intent);
                    }
                }catch (Exception e){

                }
            }
        });
        holder.ProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!user_details.getAdhaarCard().isEmpty()) {
                        Log.d("user_details.getImage()", user_details.getAdhaarCard());
                        Intent intent = new Intent(mContext, See_Full_Image_F.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("imageurl", user_details.getAdhaarCard());
                        mContext.startActivity(intent);
                    }
                }catch (Exception e){

                }
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CircularImageView ProfilePicture;
        RelativeLayout call;
        TextView email;
        CircularImageView licence;
        TextView mobile;
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            this.call = (RelativeLayout) itemView.findViewById(R.id.call);
            this.name = (TextView) itemView.findViewById(R.id.name);
            this.mobile = (TextView) itemView.findViewById(R.id.mobile);
            this.email = (TextView) itemView.findViewById(R.id.email);
            this.ProfilePicture = (CircularImageView) itemView.findViewById(R.id.ProfilePicture);
            this.licence = (CircularImageView) itemView.findViewById(R.id.licence);
        }
    }
}
