package com.webapp.oasis.Donation;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import com.webapp.oasis.Admin.AdmiinHomeScreen;
import com.webapp.oasis.Customer.CustomerHomeActivity;
import com.webapp.oasis.Driver.DriverHomeActivity;
import com.webapp.oasis.R;
import com.webapp.oasis.Retailer.RetailerActivity;
import com.webapp.oasis.Utilities.SessionManager;

public class DialogPaymentSuccessFragment extends DialogFragment {

    private View root_view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.dialog_payment_success, container, false);

        TextView time,date,amount;
        TextView tvthanku,tvyourtrans,tvdate,tvtime,tvadmount,tvcompleted;
        String logincode;

        Calendar cc = Calendar.getInstance();
        int year = cc.get(Calendar.YEAR);
        int month = cc.get(Calendar.MONTH);
        int mDay = cc.get(Calendar.DAY_OF_MONTH);
        Log.d("Date", mDay + "/" + month + "/" + year);

        String currentTime = new SimpleDateFormat("h:mm:a", java.util.Locale.getDefault()).format(new Date());
        Log.d("time_format", currentTime);

        time = root_view.findViewById(R.id.time);
        time.setText(currentTime);

        date = root_view.findViewById(R.id.date);
        date.setText(mDay + "/" + month + "/" + year);

        SessionManager session;
        session = new SessionManager(getActivity());
        HashMap<String, String> users = session.getUserDetails();

        amount = root_view.findViewById(R.id.amount);
        amount.setText(users.get(session.AMOUNT));
        logincode = users.get(session.KEY_LoginCode);

        tvthanku = root_view.findViewById(R.id.tvthanku);
        tvyourtrans = root_view.findViewById(R.id.tvyourtrans);
        tvdate = root_view.findViewById(R.id.tvdate);
        tvtime = root_view.findViewById(R.id.tvtime);
        tvadmount = root_view.findViewById(R.id.tvadmount);
        tvcompleted = root_view.findViewById(R.id.tvcompleted);

        ((FloatingActionButton) root_view.findViewById(R.id.fab)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (session.isLoggedIn() == true) {
                    if (logincode.equals("1")) {
                        Intent intent = new Intent(getActivity(), AdmiinHomeScreen.class);
                        startActivity(intent);
                    } else if (logincode.equals("3")) {
                        Intent intent = new Intent(getActivity(), CustomerHomeActivity.class);
                        startActivity(intent);
                    } else if (logincode.equals("2")) {
                        Intent intent = new Intent(getActivity(), DriverHomeActivity.class);
                        startActivity(intent);
                    } else if (logincode.equals("4")) {
                        Intent intent = new Intent(getActivity(), RetailerActivity.class);
                        startActivity(intent);
                    }
                }
                dismiss();
            }
        });

        return root_view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}