package com.webapp.oasis.Admin.Adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;
import androidx.exifinterface.media.ExifInterface;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.webapp.oasis.Model.NotificationModel;
import com.webapp.oasis.R;
import com.webapp.oasis.Utilities.Config;
import com.webapp.oasis.Utilities.SessionManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
    /* access modifiers changed from: private */
    public Context context;
    protected List<NotificationModel> data = new ArrayList();
    String hash;
    String role;
    SessionManager session;
    String user_id;

    public NotificationAdapter(Context context2, List<NotificationModel> data2) {
        this.context = context2;
        this.data = data2;
        SessionManager sessionManager = new SessionManager(context2);
        this.session = sessionManager;
        HashMap<String, String> users = sessionManager.getUserDetails();
//        this.user_id = users.get(SessionManager.KEY_USERID);
        this.hash = users.get(SessionManager.KEY_HASH);
        this.role = users.get(SessionManager.KEY_ADMIN_ROLE);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        Button btnstatus;
        TextView date = ((TextView) this.itemView.findViewById(R.id.date));
        ImageView img;
        TextView name = ((TextView) this.itemView.findViewById(R.id.name));
        TextView place = ((TextView) this.itemView.findViewById(R.id.etadress));
        TextView vehical_np = ((TextView) this.itemView.findViewById(R.id.vehical_np));

        public MyViewHolder(View view) {
            super(view);
            this.btnstatus = (Button) view.findViewById(R.id.btnstatus);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.notificationdetails, parent, false));
    }

    public void onBindViewHolder(final MyViewHolder viewHolder, int i) {
        viewHolder.itemView.setTag(this.data.get(i));
        final NotificationModel d = this.data.get(i);
        viewHolder.name.setText(d.getName());
        viewHolder.date.setText(d.getDate());
        viewHolder.place.setText(d.getPlace());
        if (d.getSadmin_approve_status() == 1) {
            viewHolder.btnstatus.setText("Reject");
        } else {
            viewHolder.btnstatus.setText("Approve");
        }
        viewHolder.btnstatus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (d.getSadmin_approve_status() == 1) {
                    if (viewHolder.btnstatus.getText().toString().equals("Approve")) {
                        viewHolder.btnstatus.setText("Reject");
                    } else {
                        viewHolder.btnstatus.setText("Approve");
                    }
                    NotificationAdapter.this.change_driver_status(ExifInterface.GPS_MEASUREMENT_2D, d.getDriver_id());
                    return;
                }
                if (viewHolder.btnstatus.getText().toString().equals("Approve")) {
                    viewHolder.btnstatus.setText("Reject");
                } else {
                    viewHolder.btnstatus.setText("Approve");
                }
                NotificationAdapter.this.change_driver_status("1", d.getDriver_id());
            }
        });
    }

    public int getItemCount() {
        return this.data.size();
    }

    /* access modifiers changed from: private */
    public void change_driver_status(String status, String driver_id) {
        final ProgressDialog showMe = new ProgressDialog(this.context, 3);
        showMe.setMessage("Please wait");
        showMe.setCancelable(true);
        showMe.setCanceledOnTouchOutside(false);
        final String str = status;
        final String str2 = driver_id;
        StringRequest r3 = new StringRequest(1, Config.change_driver_status, new Response.Listener<String>() {
            @SuppressLint("LongLogTag")
            public void onResponse(String ServerResponse) {
                showMe.dismiss();
                Log.d("server response for otp : ", ServerResponse);
                try {
                    JSONObject j = new JSONObject(ServerResponse);
                    String status = j.getString(NotificationCompat.CATEGORY_STATUS);
                    String msg = j.getString(NotificationCompat.CATEGORY_MESSAGE);
                    if (status.equals("200")) {
                        Toast.makeText(NotificationAdapter.this.context, "Status Change", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(NotificationAdapter.this.context, msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    showMe.dismiss();
                    Toast.makeText(NotificationAdapter.this.context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                showMe.dismiss();
                Log.d("volley error", String.valueOf(volleyError));
            }
        }) {
            public Map<String, String> getHeaders() {
                return new HashMap<>();
            }

            /* access modifiers changed from: protected */
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("api_key", "53rihkffdirofmbglolfhbcmfvlfjgkgnmmf");
                params.put("user_id", NotificationAdapter.this.user_id);
                params.put(SessionManager.KEY_HASH, NotificationAdapter.this.hash);
                params.put(NotificationCompat.CATEGORY_STATUS, str);
                params.put("driver_id", str2);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this.context);
        r3.setRetryPolicy(new DefaultRetryPolicy(20000, 1, 1.0f));
        requestQueue.add(r3);
    }
}
