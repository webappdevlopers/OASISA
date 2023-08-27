package com.webapp.oasis.Utilities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.webapp.oasis.LoginFirstScreen;
import java.util.HashMap;

public class SessionManager {
    public static final String AMOUNT = "amount";
    public static final String CustomerId = "CustomerId";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String IS_LOGIN_FIRST = "firstlogin";
    private static final String IS_STEPPER = "isstepper";
    public static final String KEY_ADMIN_ROLE = "adminrole";
    public static final String KEY_EMAIL = "KEY_EMAIL";
    public static final String KEY_GSTNO = "KEY_GSTNO";
    public static final String KEY_HASH = "hash";
    public static final String KEY_USERID = "KEY_USERID";
    public static final String KEY_LoginCode = "admin";
    public static final String KEY_MOB = "mob";
    public static final String KEY_NAME = "name";
    public static final String KEY_PLACE = "place";
    public static final String KEY_RET_STATUS = "KEY_RET_STATUS";
    public static final String KEY_RET_TYPE = "KEY_RET_TYPE";
    public static final String KEY_SHOP_IMAGE = "KEY_SHOP_IMAGE";
    public static final String KEY_AdminID = "KEY_AdminID";
    public static final String KEY_TecnicianID = "TechnicianID";
    private static final String Pref_Name = "Login";
    Context _context;
    Context context;
    SharedPreferences.Editor editor;
    SharedPreferences pref;

    public SessionManager(Context context1) {
        this.context = context1;
        SharedPreferences sharedPreferences = context1.getSharedPreferences(Pref_Name, 0);
        this.pref = sharedPreferences;
        this.editor = sharedPreferences.edit();
    }

    public void useridsession(String customerId) {
        this.editor.putString(CustomerId, customerId);
        this.editor.commit();
    }

    public void createamoutsession(String amount) {
        this.editor.putString(AMOUNT, amount);
        this.editor.commit();
    }

    public void LoginCode(String logincode) {
        this.editor.putBoolean(IS_LOGIN_FIRST, true);
        this.editor.putString(KEY_LoginCode, logincode);
        this.editor.commit();
    }

    public void createStepperSession() {
        this.editor.putBoolean(IS_STEPPER, true);
        this.editor.commit();
    }

    public void logoutUser() {
        this.editor.clear();
        this.editor.commit();
        Intent i = new Intent(this.context, LoginFirstScreen.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.context.startActivity(i);
    }

    public void createAdminLoginRole(String role) {
        this.editor.putString(KEY_ADMIN_ROLE, role);
        this.editor.commit();
    }

    public void createLoginSessionTecnician(String technicianid) {
        this.editor.putBoolean(IS_LOGIN, true);
        this.editor.putString(KEY_TecnicianID, technicianid);
        this.editor.commit();
    }
    public void createLoginSessionAdmin(String  adminID) {
        this.editor.putBoolean(IS_LOGIN, true);
        this.editor.putString(KEY_AdminID, adminID);
        this.editor.commit();
    }

    public void createLoginSession(String technicianid, String name, String mobile, String place, String hash) {
        this.editor.putBoolean(IS_LOGIN, true);
        this.editor.putString(KEY_TecnicianID, technicianid);
        this.editor.putString("name", name);
        this.editor.putString(KEY_MOB, mobile);
        this.editor.putString(KEY_PLACE, place);
        this.editor.putString(KEY_HASH, hash);
        this.editor.commit();
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();
        user.put(AMOUNT, this.pref.getString(AMOUNT, ""));
        user.put(KEY_LoginCode, this.pref.getString(KEY_LoginCode, ""));
        user.put(KEY_TecnicianID, this.pref.getString(KEY_TecnicianID, ""));
        user.put("name", this.pref.getString("name", ""));
        user.put(KEY_MOB, this.pref.getString(KEY_MOB, ""));
        user.put(KEY_PLACE, this.pref.getString(KEY_PLACE, ""));
        user.put(KEY_HASH, this.pref.getString(KEY_HASH, ""));
        user.put(KEY_ADMIN_ROLE, this.pref.getString(KEY_ADMIN_ROLE, ""));
        user.put(CustomerId, this.pref.getString(CustomerId, ""));
        return user;
    }

    public boolean setLoginFirst() {
        return this.pref.getBoolean(IS_LOGIN_FIRST, false);
    }

    public boolean isLoggedIn() {
        return this.pref.getBoolean(IS_LOGIN_FIRST, false);
    }

    public boolean isSteeperWizard() {
        return this.pref.getBoolean(IS_STEPPER, false);
    }
}
