package com.webapp.oasis;

import android.app.Activity;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SweetAlertExample {

    public static void showSweetAlert(Activity activity, String title, String message, int alertType, boolean showUpdateButton) {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(activity, alertType)
                .setTitleText(title)
                .setContentText(message);
        sweetAlertDialog.setCancelable(false);
        if (showUpdateButton) {
            sweetAlertDialog.setConfirmText("Update")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                            AppUpdateUtils.redirectToPlayStore(activity.getApplicationContext());

                            sweetAlertDialog.dismissWithAnimation();
                        }
                    });
        }

        sweetAlertDialog.show();
    }
}
