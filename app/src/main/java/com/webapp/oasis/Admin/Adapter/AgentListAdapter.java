package com.webapp.oasis.Admin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

import com.webapp.oasis.Model.AgentListModel;
import com.webapp.oasis.R;
import com.webapp.oasis.Utilities.SessionManager;

public class AgentListAdapter extends RecyclerView.Adapter<AgentListAdapter.ViewHolder> {

    int lastPosition = -1;
    private Context mContext;
    private List<AgentListModel> mUser;
    SessionManager session;

    public AgentListAdapter(Context context, List<AgentListModel> mUser) {
        this.mContext = context;
        this.mUser = mUser;

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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.agent_list_details, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int i) {
        final AgentListModel user_details = mUser.get(i);

        holder.name.setText(user_details.getName());
        holder.mobile.setText(user_details.getMobile());
//        holder.place.setText(user_details.getPlace());
//
//        Glide
//                .with(mContext)
//                .load(user_details.getImage())
//                .apply(new RequestOptions()
//                        .placeholder(R.drawable.usericon)
//                        .fitCenter())
//                .into(holder.ProfilePicture);

        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse("tel:" + user_details.getMobile()));
                mContext.startActivity(intent);
            }
        });

    }
    public static class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout call;
        TextView name,mobile,place;
        CircularImageView ProfilePicture;

        public ViewHolder(View itemView) {
            super(itemView);

            call=itemView.findViewById(R.id.call);
            name=itemView.findViewById(R.id.name);
            mobile=itemView.findViewById(R.id.mobile);
            place=itemView.findViewById(R.id.place);
            ProfilePicture=itemView.findViewById(R.id.ProfilePicture);

        }
    }
}
