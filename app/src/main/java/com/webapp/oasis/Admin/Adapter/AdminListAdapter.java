package com.webapp.oasis.Admin.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.webapp.oasis.Admin.AdminUpdateItemDetailsActivity;
import com.webapp.oasis.Model.ItemsModel;
import com.webapp.oasis.R;
import com.webapp.oasis.SqlliteDb.DatabaseHelper;
import com.webapp.oasis.Utilities.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminListAdapter extends RecyclerView.Adapter<AdminListAdapter.ViewHolder> {
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
    ProgressDialog showMe;

    public AdminListAdapter(Context context2, List<ItemsModel> mUser2) {
        this.context = context2;
        this.mUser = mUser2;
        SessionManager sessionManager = new SessionManager(context2);
        this.session = sessionManager;
        HashMap<String, String> users = sessionManager.getUserDetails();
        this.user_id = users.get(SessionManager.KEY_TecnicianID);
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
        ViewHolder vh = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_service_list_details, parent, false));
        this.requestQueue = Volley.newRequestQueue(this.context);
        return vh;
    }

    public void onBindViewHolder(ViewHolder holder, int i) {
        final ItemsModel user_details = this.mUser.get(i);
        holder.name.setText(user_details.getItemName());
        holder.price.setText(user_details.getPrice());

        holder.update.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AdminListAdapter.this.context, AdminUpdateItemDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("name", user_details.getItemName());
                intent.putExtra("brandname", user_details.getBrandName());
                intent.putExtra(DatabaseHelper.COL_3, user_details.getQty());
                intent.putExtra("size", user_details.getPrice());
                intent.putExtra("ItemID", user_details.getItemID());
                AdminListAdapter.this.context.startActivity(intent);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showMe = new ProgressDialog(v.getContext());
                showMe.setMessage("Please wait");
                showMe.setCancelable(true);
                showMe.setCanceledOnTouchOutside(false);
                showMe.show();
                edit_technician(user_details.getQty());


            }
        });
    }

    public void edit_technician(String adminId) {

        DatabaseReference myRef = FirebaseDatabase.getInstance("https://oasis-a3b2c-default-rtdb.firebaseio.com/")
                .getReference("ADMIN/ADMIN Credentials");

        Map<String, Object> data = new HashMap<>();
        data.put("isDelete", "true"); // Update only the "isDelete" field

        myRef.child(adminId).updateChildren(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            public void onComplete(Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context.getApplicationContext(), "Deleted Successfully", Toast.LENGTH_LONG).show();
                      showMe.dismiss();
                } else {
                    // Handle the error
                    Toast.makeText(context.getApplicationContext(), "Failed to Delete", Toast.LENGTH_LONG).show();
                     showMe.dismiss();
                }
            }
        });
    }
    void deleteFunctionality(ItemsModel user_details) {
        DatabaseReference myRef = FirebaseDatabase.getInstance("https://oasis-a3b2c-default-rtdb.firebaseio.com/").getReference("ADMIN/ADMIN Credentials");
        Query query = myRef.orderByChild("Admin ID").equalTo(user_details.getQty());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    dataSnapshot.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Data successfully deleted

                                showMe.dismiss();
                                notifyItemRemoved(0);
                                mUser.clear();

                                Log.d("TAG", "Item deleted successfully.");


                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
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
                        Toast.makeText(AdminListAdapter.this.context, "Item Deleted Successfully", Toast.LENGTH_SHORT).show();
                        AdminListAdapter.this.mUser.remove(i);
                        AdminListAdapter.this.notifyItemRemoved(i);
                        AdminListAdapter.this.notifyItemRangeChanged(i, AdminListAdapter.this.mUser.size());
                        return;
                    }
                    Toast.makeText(AdminListAdapter.this.context, "Something Has Happened. Please Try Again!", Toast.LENGTH_SHORT).show();
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
            Log.d("data", AdminListAdapter.this.user_id + "\n" + AdminListAdapter.this.hash + "\n" + PostId);
            String PostId2 = PostId.substring(1, PostId.length());
            this.parameters.put("api_key", "53rihkffdirofmbglolfhbcmfvlfjgkgnmmf");
            this.parameters.put("user_id", AdminListAdapter.this.user_id);
            this.parameters.put(SessionManager.KEY_HASH, AdminListAdapter.this.hash);
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
