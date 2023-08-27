package com.webapp.oasis.Admin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.webapp.oasis.Admin.AdminUpdateItemDetailsActivity;
import com.webapp.oasis.Model.AdminItemListModel;
import com.webapp.oasis.R;
import com.webapp.oasis.Utilities.Config;
import com.webapp.oasis.Utilities.SessionManager;

public class AdminItemListAdapter extends RecyclerView.Adapter<AdminItemListAdapter.ViewHolder> {

    int lastPosition = -1;
    private Context context;

    StringRequest stringRequest;
    RequestQueue requestQueue;
    private List<AdminItemListModel> mUser;
    SessionManager session;
    String user_id, hash;

        public AdminItemListAdapter(Context context, List<AdminItemListModel> mUser) {
            this.context = context;
            this.mUser = mUser;

            session = new SessionManager(context);
            HashMap<String, String> users = session.getUserDetails();
//            user_id = users.get(session.KEY_USERID);
            hash = users.get(session.KEY_HASH);

        }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public int getItemCount() {
        return mUser.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_item_list_details, parent, false);
        ViewHolder vh = new ViewHolder(v);
        requestQueue = Volley.newRequestQueue(context);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int i) {

        final AdminItemListModel user_details = mUser.get(i);

//        holder.name.setText(user_details.getName());
//        holder.item_id.setText(user_details.getItem_id());
////        holder.price.setText(user_details.getPrice());
////        holder.color.setText(user_details.getColor());
//        holder.weight.setText(user_details.getWeight());
//        holder.size.setText(user_details.getSize());

//        Glide
//                .with(context)
//                .load(user_details.getImage())
//                .apply(new RequestOptions()
//                        .placeholder(R.drawable.usericon)
//                        .fitCenter())
//                .into(holder.image);

//        holder.update.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, AdminUpdateItemDetailsActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("name", user_details.getName());
////                intent.putExtra("price", user_details.getPrice());
////                intent.putExtra("color", user_details.getColor());
//                intent.putExtra("weight", user_details.getWeight());
//                intent.putExtra("size", user_details.getSize());
//                intent.putExtra("image", user_details.getImage());
//                intent.putExtra("item_id", user_details.getItem_id());
//                context.startActivity(intent);
//            }
//        });
//        holder.delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                deletePost(user_details.getItem_id(), i);
//            }
//
//        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, item_id, price, color, weight, size;
        ImageView image, update, delete;

        public ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            item_id = itemView.findViewById(R.id.item_id);
            price = itemView.findViewById(R.id.price);
            image = itemView.findViewById(R.id.image);
//            color = itemView.findViewById(R.id.color);
            weight = itemView.findViewById(R.id.weight);
            size = itemView.findViewById(R.id.size);
            update = itemView.findViewById(R.id.update);
            delete = itemView.findViewById(R.id.delete);
        }
    }

    private void deletePost(String post_id, int i) {

        getDetailsRequest getdetailrequest = new getDetailsRequest(post_id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("Response", response);

                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getString("status").equals("200")) {
                        Toast.makeText(context, "Item Deleted Successfully", Toast.LENGTH_SHORT).show();
                        mUser.remove(i);
                        notifyItemRemoved(i);
                        notifyItemRangeChanged(i, mUser.size());

                    } else
                        Toast.makeText(context, "Something Has Happened. Please Try Again!", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        requestQueue.add(getdetailrequest);
    }

    public class getDetailsRequest extends StringRequest {

        private static final String REGISTER_URL = Config.delete_item;
        private Map<String, String> parameters;

        public getDetailsRequest(String PostId, Response.Listener<String> listener) {
            super(Method.POST, REGISTER_URL, listener, null);
            parameters = new HashMap<>();
            Log.d("data", user_id + "\n" + hash + "\n" + PostId);

            PostId = PostId.substring(1, PostId.length());
            parameters.put("api_key", "53rihkffdirofmbglolfhbcmfvlfjgkgnmmf");
            parameters.put("user_id", user_id);
            parameters.put("hash", hash);
            ;
            parameters.put("item_id", PostId);
        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String> headers = new HashMap<String, String>();

            return headers;
        }

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            return parameters;
        }
    }
}
