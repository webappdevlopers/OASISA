package com.webapp.oasis.Admin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.common.internal.ImagesContract;
import com.webapp.oasis.Model.InvoiceListModel;
import com.webapp.oasis.R;
import com.webapp.oasis.Utilities.SessionManager;
import com.webapp.oasis.WebviewActivity;
import java.util.List;

public class AdminInvoiceListAdapter extends RecyclerView.Adapter<AdminInvoiceListAdapter.ViewHolder> {
    /* access modifiers changed from: private */
    public Context context;
    String hash;
    int lastPosition = -1;
    private List<InvoiceListModel> mUser;
    SessionManager session;
    String user_id;

    public AdminInvoiceListAdapter(Context context2, List<InvoiceListModel> mUser2) {
        this.context = context2;
        this.mUser = mUser2;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    public int getItemCount() {
        return this.mUser.size();
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_invoice_list_details, parent, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final InvoiceListModel user_details = this.mUser.get(i);
        viewHolder.invoice_no.setText(user_details.getInvoice_no());
        viewHolder.driver_name.setText(user_details.getDriver_name());
        viewHolder.destination.setText(user_details.getDestination());
        viewHolder.source.setText(user_details.getSource());
        viewHolder.order_status.setText(user_details.getOrder_status());
        viewHolder.final_cost.setText(user_details.getFinal_cost());
        viewHolder.receiver_name.setText(user_details.getReceiver_name());
        viewHolder.date.setText(user_details.getOrder_date());
        viewHolder.imginvoice.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(AdminInvoiceListAdapter.this.context, WebviewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra(ImagesContract.URL, user_details.getInvoice());
                AdminInvoiceListAdapter.this.context.startActivity(intent);
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView date;
        TextView destination;
        TextView driver_name;
        TextView final_cost;
        ImageView imginvoice;
        TextView invoice_no;
        TextView order_status;
        TextView receiver_name;
        TextView source;

        public ViewHolder(View itemView) {
            super(itemView);
            this.receiver_name = (TextView) itemView.findViewById(R.id.service);
            this.order_status = (TextView) itemView.findViewById(R.id.order_status);
            this.source = (TextView) itemView.findViewById(R.id.timing);
            this.driver_name = (TextView) itemView.findViewById(R.id.driver_name);
            this.invoice_no = (TextView) itemView.findViewById(R.id.invoice_no);
            this.destination = (TextView) itemView.findViewById(R.id.destination);
            this.final_cost = (TextView) itemView.findViewById(R.id.final_cost);
            this.date = (TextView) itemView.findViewById(R.id.date);
            this.imginvoice = (ImageView) itemView.findViewById(R.id.imginvoice);
        }
    }
}
