package com.webapp.oasis.Admin.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.webapp.oasis.Admin.ComplaintdetailsNew2;
import com.webapp.oasis.Model.AdminItemListModel;
import com.webapp.oasis.Model.CartModel;
import com.webapp.oasis.R;
import com.webapp.oasis.SqlliteDb.DatabaseHelper;
import com.webapp.oasis.Utilities.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BillSelectedItemListAdapter extends RecyclerView.Adapter<BillSelectedItemListAdapter.ViewHolder> implements Filterable {
    private Context context;
    String hash;
    int lastPosition = -1;
    /* access modifiers changed from: private */
    public List<AdminItemListModel> mUser;
    /* access modifiers changed from: private */
    public List<AdminItemListModel> mUser1;
    DatabaseHelper mydb;
    RequestQueue requestQueue;
    SessionManager session;
    StringRequest stringRequest;
    String user_id;


    public BillSelectedItemListAdapter(Context context2, List<AdminItemListModel> mUser2) {
        this.context = context2;
        this.mUser = mUser2;
        this.mUser1 = mUser2;

        session = new SessionManager(context);
        SessionManager sessionManager = new SessionManager(context2);
        this.session = sessionManager;
        HashMap<String, String> users = sessionManager.getUserDetails();
        this.user_id = users.get(SessionManager.KEY_TecnicianID);
        this.hash = users.get(SessionManager.KEY_HASH);
        this.mydb = new DatabaseHelper(context2);
    }

    public int getItemCount() {
        return this.mUser.size();
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.select_item_list_details, parent, false));
    }

    public int getItemViewType(int position) {
        return position;
    }

    public void onBindViewHolder(ViewHolder holder, int i) {
        final AdminItemListModel user_details = this.mUser.get(i);
        holder.item_id.setText(user_details.getItemID());
        holder.itemName.setText(user_details.getItemName());
        holder.brandName.setText(user_details.getBrandName());
        holder.mrpPrice.setText(user_details.getPrice());
        holder.totalQty.setText(user_details.getQty()+" * "+user_details.getPrice()+" Rs");
//        holder.totalQty.setVisibility(View.GONE);
//        holder.sellingPrice.setText(user_details.getPrice());
        holder.sellingPrice.setVisibility(View.GONE);
        holder.totalAmount.setVisibility(View.GONE);
        holder.txt_val.setText((Integer.parseInt(user_details.getQty())*Integer.parseInt(user_details.getPrice()))+" Rs");
//        Glide.with(this.context).load(user_details.getImage()).apply((BaseRequestOptions<?>) ((RequestOptions) new RequestOptions().placeholder((int) R.drawable.usericon)).fitCenter()).into(holder.image);

        holder.imgplus.setVisibility(View.GONE);
        holder.imgminus.setVisibility(View.GONE);
        holder.mrplayout.setVisibility(View.GONE);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public Button btnitemselect;
        public TextView txt_val;
        public ImageView imgminus;
        public ImageView imgplus;
        public TextView item_id;
        TextView itemName;
        TextView brandName;
        TextView totalQty;
        TextView mrpPrice;
        TextView totalAmount;
        public EditText sellingPrice;
        public int count = 0;
        public LinearLayout thirdlayout;
        public LinearLayout mrplayout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.mrplayout=  itemView.findViewById(R.id.mrplayout);
            this.thirdlayout=  itemView.findViewById(R.id.thirdlayout);
            this.sellingPrice = (EditText) itemView.findViewById(R.id.sellingPrice);
            this.totalAmount = (TextView) itemView.findViewById(R.id.totalAmount);
            this.itemName = (TextView) itemView.findViewById(R.id.itemName);
            this.imgminus = (ImageView) itemView.findViewById(R.id.imgminus);
            this.imgplus = (ImageView) itemView.findViewById(R.id.imgplus);
            this.mrpPrice = (TextView) itemView.findViewById(R.id.mrpPrice);
            this.item_id = (TextView) itemView.findViewById(R.id.item_id);
            this.totalQty = (TextView) itemView.findViewById(R.id.totalQty);
            this.txt_val = (TextView) itemView.findViewById(R.id.txt_val);
            this.brandName = (TextView) itemView.findViewById(R.id.brandName);
            this.btnitemselect = (Button) itemView.findViewById(R.id.btnitemselect);
        }
    }

    public Filter getFilter() {
        return new Filter() {
            /* access modifiers changed from: protected */
            public FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    BillSelectedItemListAdapter selectItemListAdapter = BillSelectedItemListAdapter.this;
                    List unused = selectItemListAdapter.mUser = selectItemListAdapter.mUser1;
                } else {
//                    ArrayList<AdminItemListModel> filteredList = new ArrayList<>();
//                    for (AdminItemListModel androidVersion : SelectItemListAdapter.this.mUser) {
//                        if (androidVersion.getName().toLowerCase().contains(charString) || androidVersion.getName().contains(charString) || androidVersion.getName().contains(charString)) {
//                            filteredList.add(androidVersion);
//                        }
//                    }
//                    List unused2 = SelectItemListAdapter.this.mUser = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = BillSelectedItemListAdapter.this.mUser;
                return filterResults;
            }

            /* access modifiers changed from: protected */
            public void publishResults(CharSequence charSequence, FilterResults filterResults) {
                List unused = BillSelectedItemListAdapter.this.mUser = (ArrayList) filterResults.values;
                BillSelectedItemListAdapter.this.notifyDataSetChanged();
            }
        };
    }
}
