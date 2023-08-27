package com.webapp.oasis.Admin.Adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.webapp.oasis.Admin.AdminUpdateItemDetailsActivity;
import com.webapp.oasis.Admin.Complaint.ComplaintsFragment;
import com.webapp.oasis.LoginActivity;
import com.webapp.oasis.Model.ItemsModel;
import com.webapp.oasis.R;
import com.webapp.oasis.SqlliteDb.DatabaseHelper;
import com.webapp.oasis.Utilities.SessionManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class AdminStockListAdapter extends RecyclerView.Adapter<AdminStockListAdapter.ViewHolder> {
    /* access modifiers changed from: private */
    public Context context;
    String hash;
    int lastPosition = -1;
    /* access modifiers changed from: private */
    public List<ItemsModel> mUser;
    RequestQueue requestQueue;
    SessionManager session;
    StringRequest stringRequest;
    String user_id;

    public AdminStockListAdapter(Context context2, List<ItemsModel> mUser2) {
        this.context = context2;
        this.mUser = mUser2;
        SessionManager sessionManager = new SessionManager(context2);
        this.session = sessionManager;
        HashMap<String, String> users = sessionManager.getUserDetails();
//        this.user_id = users.get(SessionManager.KEY_USERID);
        this.hash = users.get(SessionManager.KEY_HASH);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public int getItemCount() {
        return this.mUser.size();
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vh = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_item_list_details, parent, false));
        this.requestQueue = Volley.newRequestQueue(this.context);
        return vh;
    }

    public void onBindViewHolder(ViewHolder holder, int i) {
        final ItemsModel user_details = this.mUser.get(i);
        holder.name.setText(user_details.getItemName());
        holder.brandName.setText(user_details.getBrandName());
        holder.qty.setText(user_details.getQty());
        holder.price.setText(user_details.getPrice());
        try {
            if (LoginActivity.userType.equalsIgnoreCase("Admin")) {
                holder.update.setVisibility(View.GONE);
                holder.delete.setVisibility(View.GONE);

            } else {
                holder.update.setVisibility(View.VISIBLE);
                holder.delete.setVisibility(View.VISIBLE);

            }
        }catch (Exception e){
            holder.update.setVisibility(View.VISIBLE);
            holder.delete.setVisibility(View.VISIBLE);
        }
        holder.update.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AdminStockListAdapter.this.context, AdminUpdateItemDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("name", user_details.getItemName());
                intent.putExtra("brandname", user_details.getBrandName());
                intent.putExtra(DatabaseHelper.COL_3, user_details.getQty());
                intent.putExtra("size", user_details.getPrice());
                intent.putExtra("ItemID", user_details.getItemID());
                AdminStockListAdapter.this.context.startActivity(intent);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Are you sure you want to Delete?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        final ProgressDialog showMe = new ProgressDialog(v.getContext());
                        showMe.setMessage("Please wait");
                        showMe.setCancelable(true);
                        showMe.setCanceledOnTouchOutside(false);
                        showMe.show();
                        FirebaseDatabase.getInstance("https://oasis-a3b2c-default-rtdb.firebaseio.com/").getReference("ADMIN/ITEMS").child(user_details.getItemID()).removeValue(new DatabaseReference.CompletionListener() {
                            public void onComplete(DatabaseError error, DatabaseReference ref) {
                                showMe.dismiss();
                                if (error != null) {
                                    Log.e("TAG", "Failed to delete item.", error.toException());
                                    return;
                                }
                                notifyItemRemoved(0);
                                mUser.clear();
                                Log.d("TAG", "Item deleted successfully.");
                            }
                        });
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                builder.create().show();


            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView brandName;
        TextView color;
        ImageView delete;
        ImageView image;
        TextView name;
        TextView price;
        TextView qty;
        ImageView update;
        TextView weight;

        public ViewHolder(View itemView) {
            super(itemView);
            this.name = (TextView) itemView.findViewById(R.id.name);
            this.brandName = (TextView) itemView.findViewById(R.id.brandname);
            this.qty = (TextView) itemView.findViewById(R.id.qty);
            this.image = (ImageView) itemView.findViewById(R.id.rluploadAdhaarcard);
//            this.color = (TextView) itemView.findViewById(R.id.color);
            this.weight = (TextView) itemView.findViewById(R.id.weight);
            this.price = (TextView) itemView.findViewById(R.id.price);
            this.update = (ImageView) itemView.findViewById(R.id.update);
            this.delete = (ImageView) itemView.findViewById(R.id.delete);

        }
    }

    private void deletePost(String post_id, final int i) {
        this.requestQueue.add(new getDetailsRequest(post_id, new Response.Listener<String>() {
            public void onResponse(String response) {
                Log.i("Response", response);
                try {
                    if (new JSONObject(response).getString(NotificationCompat.CATEGORY_STATUS).equals("200")) {
                        Toast.makeText(AdminStockListAdapter.this.context, "Item Deleted Successfully", Toast.LENGTH_LONG).show();
                        AdminStockListAdapter.this.mUser.remove(i);
                        AdminStockListAdapter.this.notifyItemRemoved(i);
                        AdminStockListAdapter.this.notifyItemRangeChanged(i, AdminStockListAdapter.this.mUser.size());
                        return;
                    }
                    Toast.makeText(AdminStockListAdapter.this.context, "Something Has Happened. Please Try Again!", Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }));
    }

    public class getDetailsRequest extends StringRequest {
        private static final String REGISTER_URL = "https://itkbusiness.online/rastasur/api/delete_item";
        private Map<String, String> parameters = new HashMap();

        public getDetailsRequest(String PostId, Response.Listener<String> listener) {
            super(1, "https://itkbusiness.online/rastasur/api/delete_item", listener, (Response.ErrorListener) null);
            Log.d("data", AdminStockListAdapter.this.user_id + "\n" + AdminStockListAdapter.this.hash + "\n" + PostId);
            String PostId2 = PostId.substring(1, PostId.length());
            this.parameters.put("api_key", "53rihkffdirofmbglolfhbcmfvlfjgkgnmmf");
            this.parameters.put("user_id", AdminStockListAdapter.this.user_id);
            this.parameters.put(SessionManager.KEY_HASH, AdminStockListAdapter.this.hash);
            this.parameters.put("item_id", PostId2);
        }

        public Map<String, String> getHeaders() throws AuthFailureError {
            return new HashMap<>();
        }

        /* access modifiers changed from: protected */
        public Map<String, String> getParams() throws AuthFailureError {
            return this.parameters;
        }
    }
}
