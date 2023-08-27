package com.webapp.oasis.Wallet;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;

import com.webapp.oasis.Model.WalletTransDriverModel;
import com.webapp.oasis.R;
import com.webapp.oasis.Utilities.SessionManager;

public class WalletTransactionDriverAdapter extends RecyclerView.Adapter<WalletTransactionDriverAdapter.ViewHolder> {

    int lastPosition = -1;
    private Context context;

    private List<WalletTransDriverModel> mUser;
    SessionManager session;
    String user_id, hash;
    public WalletTransactionDriverAdapter(Context context, List<WalletTransDriverModel> mUser) {

        this.mUser = mUser;
        this.context = context;
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.walletdetails, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        final WalletTransDriverModel user_details = mUser.get(i);

        viewHolder.date.setText(user_details.getDate());
        viewHolder.time.setText(user_details.getTime());
        viewHolder.balance.setText(user_details.getAmount());

        if (user_details.getTran_status().equals("1"))
        {
            viewHolder.walletdesc.setText("Wallet Credited");
            viewHolder.walletdesc.setTextColor(Color.parseColor("#00A859"));
        }
        else
        {
            viewHolder.walletdesc.setText("Wallet Debited");
            viewHolder.walletdesc.setTextColor(Color.parseColor("#F44336"));
        }

    }
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView date,time,walletdesc,balance;
        public ViewHolder(View itemView) {
            super(itemView);

            date=itemView.findViewById(R.id.date);
            time=itemView.findViewById(R.id.time);
            walletdesc=itemView.findViewById(R.id.walletdesc);
            balance=itemView.findViewById(R.id.balance);

        }
    }
}
