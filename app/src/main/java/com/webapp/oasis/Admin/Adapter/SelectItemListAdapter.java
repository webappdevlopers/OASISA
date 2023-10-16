package com.webapp.oasis.Admin.Adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
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

public class SelectItemListAdapter extends RecyclerView.Adapter<SelectItemListAdapter.ViewHolder> implements Filterable {
    CartListener cartListener;
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

    public interface CartListener {
        void onAdded(CartModel cartModel);

        void onRemoved(String str, String str2);
    }

    public SelectItemListAdapter(Context context2, List<AdminItemListModel> mUser2, CartListener cartListener) {
        this.context = context2;
        this.mUser = mUser2;
        this.mUser1 = mUser2;

        session = new SessionManager(context);
        this.cartListener = cartListener;
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
        holder.totalQty.setText(user_details.getQty());
        holder.sellingPrice.setText(user_details.getPrice());
        holder.totalAmount.setText("0");
//        Glide.with(this.context).load(user_details.getImage()).apply((BaseRequestOptions<?>) ((RequestOptions) new RequestOptions().placeholder((int) R.drawable.usericon)).fitCenter()).into(holder.image);

        holder.sellingPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() == 0){
                    ComplaintdetailsNew2.tltitemcost.setText("" + (Integer.parseInt(ComplaintdetailsNew2.tltitemcost.getText().toString())
                            - Integer.parseInt(holder.totalAmount.getText().toString()))+"");
                    holder.count = 0;
                    holder.totalAmount.setText("0");
                    holder.txt_val.setText("" + holder.count);
                    mydb.deleteDatas(user_details.getItemID());
                    holder.sellingPrice.setText("0");
                }
                else{
                }
            }
        });

        holder.imgplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    holder.count = holder.count + 1;
                    holder.txt_val.setText("" + holder.count);
                    holder.totalAmount.setText("" + (holder.count * Integer.parseInt(holder.sellingPrice.getText().toString()))+"");

                    boolean bol = mydb.hasObject(user_details.getItemID());

                    if (bol == true) {
                        boolean isInserted = mydb.updateDatas(user_details.getItemID(), user_details.getBrandName(), user_details.getItemName(),
                                holder.sellingPrice.getText().toString(), user_details.getQty(),holder.txt_val.getText().toString());

                        if (isInserted) {
                            //          Toast.makeText(context, "Data updated", Toast.LENGTH_SHORT).show();
                        } else {
                            //         Toast.makeText(context, "Data not updated", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (!holder.txt_val.getText().toString().equals("0")) {

                            boolean isInserted = mydb.inertData(user_details.getItemID(), user_details.getBrandName(), user_details.getItemName(),
                                    holder.sellingPrice.getText().toString(), user_details.getQty(), holder.txt_val.getText().toString());
                            if (isInserted) {
                                //              Toast.makeText(context, "Data inserted", Toast.LENGTH_SHORT).show();
                            } else {
                                //              Toast.makeText(context, "Data not inserted", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    Log.d("perfect", String.valueOf(mydb.getResults()));

                    ComplaintdetailsNew2.tltitemcost.setText((""+(Integer.parseInt(ComplaintdetailsNew2.tltitemcost.getText().toString())
                            +Integer.parseInt(holder.sellingPrice.getText().toString()))));
                    ComplaintdetailsNew2.tltitem.setText(holder.txt_val.getText().toString() + " item Added");
                    ComplaintdetailsNew2.tltitem.setText("" + mydb.getProfilesCount() + " item Added");
                }catch (Exception e){}
            }
        });

        holder.imgminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.txt_val.getText().toString().equals("0")) {
//                    Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                } else {
                    holder.count = holder.count - 1;
                    holder.txt_val.setText("" + holder.count);

                    holder.totalAmount.setText("" + (holder.count * Integer.parseInt(holder.sellingPrice.getText().toString()))+"");
                    String qty = holder.txt_val.getText().toString();
                    boolean bol = mydb.hasObject(user_details.getItemID());

                    if (bol == true) {
                        if (holder.txt_val.getText().toString().equals("0")) {
                            mydb.deleteDatas(user_details.getItemID());
//                    Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                        } else {
                            boolean isInserted = mydb.updateDatas(user_details.getItemID(), user_details.getBrandName(), user_details.getItemName(),
                                    holder.sellingPrice.getText().toString(), user_details.getQty(), holder.txt_val.getText().toString());
                        }
                    }
                    //mydb.deleteDatas(Service_id);
                    //        Toast.makeText(context, "Data Deleted", Toast.LENGTH_SHORT).show();
                }
                ComplaintdetailsNew2.tltitemcost.setText(""+((Integer.parseInt(ComplaintdetailsNew2.tltitemcost.getText().toString())
                        -Integer.parseInt(holder.sellingPrice.getText().toString()))));
                Log.d("perfect", String.valueOf(mydb.getResults()));
                ComplaintdetailsNew2.tltitem.setText("" + mydb.getProfilesCount() + " item Added");
            }
        });

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

        public ViewHolder(View itemView) {
            super(itemView);
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

    @Override
    public android.widget.Filter getFilter() {
        return new android.widget.Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String itemnamess = charSequence.toString().toLowerCase(); // Convert search query to lowercase
                String brandname = charSequence.toString().toLowerCase(); // Convert search query to lowercase
                String price = charSequence.toString().toLowerCase(); // Convert search query to lowercase

                if (charSequence.toString().isEmpty()) {
                    mUser = mUser1;
                } else {
                    ArrayList<AdminItemListModel> filteredList = new ArrayList<>();

                    for (AdminItemListModel androidVersion : mUser1) {
                        String itemname = androidVersion.getItemName().toLowerCase();
                        String bname = androidVersion.getBrandName().toLowerCase();
                        String pr = androidVersion.getPrice().toLowerCase();

                        if (itemname.contains(itemnamess) || bname.contains(brandname) || pr.contains(price)) {
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
                mUser = (ArrayList<AdminItemListModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
