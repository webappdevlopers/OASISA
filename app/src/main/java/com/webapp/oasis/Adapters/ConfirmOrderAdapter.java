package com.webapp.oasis.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.webapp.oasis.R;

public class ConfirmOrderAdapter extends RecyclerView.Adapter<ConfirmOrderAdapter.ViewHolder> {

    int lastPosition = -1;
    private Context context;

    public ConfirmOrderAdapter(Context context) {
        this.context = context;

    }
    @Override
    public int getItemCount() {
        return 2;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.driver_order_details, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {


    }
    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView topimg;
        TextView AddCustomer;
        public ViewHolder(View itemView) {
            super(itemView);

        }
    }
}
