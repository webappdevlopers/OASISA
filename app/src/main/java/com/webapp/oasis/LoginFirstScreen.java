package com.webapp.oasis;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.utils.Oscillator;
import androidx.exifinterface.media.ExifInterface;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.auth.api.credentials.CredentialsApi;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.santalu.autoviewpager.AutoViewPager;
import com.webapp.oasis.Adapters.ImagePagerAdapter;
import com.webapp.oasis.Admin.Adapter.BannerAdapter;
import com.webapp.oasis.Admin.AdminHomePage;
import com.webapp.oasis.Customer.CustomerHomeActivity;
import com.webapp.oasis.LoginFirstScreen;
import com.webapp.oasis.Model.BannerPojo;
import com.webapp.oasis.SplashIntro.SplashActivity;
import com.webapp.oasis.Technician.technicianHomeActivity;
import com.webapp.oasis.Utilities.SessionManager;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.annotations.NonNull;
import me.relex.circleindicator.CircleIndicator;

public class LoginFirstScreen extends AppCompatActivity {
    private Timer autoScrollTimer;
    private TimerTask autoScrollTimerTask;

    public static final String GOOGLE_ACCOUNT = "google_account";
    RelativeLayout btnadmin;
    RelativeLayout btnretailer;
    RelativeLayout btntechnician;
    RelativeLayout btnuser;
    private SignInButton googleSignInButton;
    /* access modifiers changed from: private */
    public GoogleSignInClient googleSignInClient;
    String logincode;
    SessionManager session;
    AutoViewPager viewPager;

    //indicator for viewpager
    CircleIndicator indicator;
    //firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReferenceForBanner;
    BannerPojo bannerPojo;
    ArrayList<BannerPojo> bannerArraylist = new ArrayList<>();

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_login_first_screen);
        startAutoScrollTimer();
        Toast.makeText(LoginFirstScreen.this, "Welcome To OASIS GLOBE App", Toast.LENGTH_LONG).show();

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        this.session = sessionManager;
        this.logincode = sessionManager.getUserDetails().get(SessionManager.KEY_LoginCode);


        //viewpager implementation
        viewPager = findViewById(R.id.viewpager);
        indicator = findViewById(R.id.indicator);
        firebaseDatabase = FirebaseDatabase.getInstance("https://oasis-a3b2c-default-rtdb.firebaseio.com/");
        databaseReferenceForBanner = firebaseDatabase.getReference("ADMIN").child("Banner Images");
        showBannerOnViewPager();
//        ((ViewPager) findViewById(R.id.viewPager)).setAdapter(new ImagePagerAdapter(new int[]{R.drawable.roimageslidertwo, R.drawable.roimagessliderone, R.drawable.ro1}));
        if (this.session.isLoggedIn()) {
            if (this.logincode.equals(ExifInterface.GPS_MEASUREMENT_3D)) {
                Intent intent = new Intent(this, CustomerHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else if (this.logincode.equals(ExifInterface.GPS_MEASUREMENT_2D)) {
                startActivity(new Intent(this, technicianHomeActivity.class));
                finish();
            } else if (this.logincode.equals("5")) {
                startActivity(new Intent(LoginFirstScreen.this, AdminHomePage.class));
                finish();
            } else if (this.logincode.equals("6")) {
                startActivity(new Intent(LoginFirstScreen.this, AdminHomePage.class));
                finish();
            }
        }
        new Timer().scheduleAtFixedRate(new TimerTask() {
            public /* synthetic */ void lambda$run$0$LoginFirstScreen$1() {
                LoginFirstScreen.this.viewPager.setCurrentItem(LoginFirstScreen.this.viewPager.getCurrentItem() + 1, true);
            }

            public void run() {
                LoginFirstScreen.this.runOnUiThread(new Runnable() {
                    public final void run() {

                        lambda$run$0$LoginFirstScreen$1();
                        //  throw new RuntimeException("Test Crash"); // Force a crash

                    }
                });
            }
        }, (long) CredentialsApi.CREDENTIAL_PICKER_REQUEST_CODE, (long) CredentialsApi.CREDENTIAL_PICKER_REQUEST_CODE);
        this.btnadmin = (RelativeLayout) findViewById(R.id.btnadmin);
        this.btnuser = (RelativeLayout) findViewById(R.id.btnuser);
        this.btntechnician = (RelativeLayout) findViewById(R.id.btntechnician);
        this.btnretailer = (RelativeLayout) findViewById(R.id.btnretailer);
        this.btnadmin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(LoginFirstScreen.this, LoginActivity.class);
                intent.putExtra("Login_From", "Admin");
                LoginFirstScreen.this.startActivity(intent);
            }
        });
        this.googleSignInClient = GoogleSignIn.getClient((Activity) this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestProfile().requestEmail().build());
        this.btnuser.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                LoginFirstScreen.this.checkConnection();
                LoginFirstScreen.this.startActivityForResult(LoginFirstScreen.this.googleSignInClient.getSignInIntent(), 101);
            }
        });
        this.btntechnician.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(LoginFirstScreen.this, LoginActivity.class);
                intent.putExtra("Login_From", "Technician");
                LoginFirstScreen.this.startActivity(intent);
            }
        });
        this.btnretailer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(LoginFirstScreen.this, LoginActivity.class);
                intent.putExtra("Login_From", "Super Admin");
                LoginFirstScreen.this.startActivity(intent);
            }
        });

        handler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                FirebaseDatabase mDatabase = FirebaseDatabase.getInstance("https://oasis-a3b2c-default-rtdb.firebaseio.com/");
                DatabaseReference mDbRef = mDatabase.getReference("Enable");

                mDbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {

                        String value = (String) dataSnapshot.getValue();

                        if (value.equals("yes") || value == "Yes") {

                        } else if (value.equals("maintenance") || value == "Maintenance") {
                            Toast.makeText(LoginFirstScreen.this, "Server is in Maintenance\nKindly contact your developer", Toast.LENGTH_LONG).show();
                        } else if (value.equals("developercharges") || value == "Developercharges") {
                            getdata();
                        } else if (value.equals("server") || value == "Server") {

                            Toast.makeText(LoginFirstScreen.this, "Server Plan is Expired\nKindly renew your server", Toast.LENGTH_LONG).show();

                            Intent a = new Intent("android.intent.action.MAIN");
                            a.addCategory("android.intent.category.HOME");
                            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(a);
                        } else if (!value.equalsIgnoreCase(String.valueOf(BuildConfig.VERSION_CODE))) {
                            redirectToPlayStore();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        };

        handler.sendEmptyMessage(0);
        ConnectivityManager ConnectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected() == true) {

        } else {
            NetworkDialog();
        }

    }

    private void NetworkDialog() {
        final Dialog dialogs = new Dialog(LoginFirstScreen.this);
        dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogs.setContentView(R.layout.networkdialog);
        dialogs.setCanceledOnTouchOutside(false);
        Button done = (Button) dialogs.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginFirstScreen.this, LoginFirstScreen.class);
                startActivity(intent);
                finish();
                dialogs.dismiss();
            }
        });
        dialogs.show();
    }

    private void getdata() {

        // calling add value event listener method
        // for getting the values from database.
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance("https://oasis-a3b2c-default-rtdb.firebaseio.com/");
        DatabaseReference mDbRef = mDatabase.getReference("msg");

        mDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);

                Toast.makeText(LoginFirstScreen.this, "" + value, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginFirstScreen.this, "Fail to get data.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private Handler handler = new Handler();
    BannerAdapter bannerAdapter;

    private void showBannerOnViewPager() {
        databaseReferenceForBanner.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    bannerPojo = new BannerPojo();
                    if (data.child("bannerImage").getValue(String.class) != null) {
                        bannerPojo.setBannerImage(data.child("bannerImage").getValue(String.class));
                        bannerArraylist.add(bannerPojo);
                    }
                }
                bannerAdapter = new BannerAdapter(LoginFirstScreen.this, bannerArraylist);

                viewPager.setAdapter(bannerAdapter);
                viewPager.setDuration(99999999);
                indicator.setViewPager(viewPager);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void onBackPressed() {
        Intent a = new Intent("android.intent.action.MAIN");
        a.addCategory("android.intent.category.HOME");
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(a);
        super.onBackPressed();
    }

    public void checkConnection() {
        if (!isOnline()) {
            networkmain();
        }
    }

    /* access modifiers changed from: protected */
    public boolean isOnline() {
        @SuppressLint("WrongConstant") NetworkInfo netInfo = ((ConnectivityManager) getSystemService("connectivity")).getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void networkmain() {
        final Dialog dialogs = new Dialog(this);
        dialogs.requestWindowFeature(1);
        dialogs.setContentView(R.layout.networkdialog);
        dialogs.setCanceledOnTouchOutside(false);
        ((Button) dialogs.findViewById(R.id.done)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialogs.dismiss();
                LoginFirstScreen.this.checkConnection();
            }
        });
        dialogs.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && requestCode == 101) {
            try {
                onLoggedIn(GoogleSignIn.getSignedInAccountFromIntent(data).getResult(ApiException.class));
            } catch (ApiException e) {
                String str = Oscillator.TAG;
                Log.w(str, "signInResult:failed code=" + e.getStatusCode());
            }
        }
    }

    private void onLoggedIn(GoogleSignInAccount googleSignInAccount) {
        FirebaseDatabase.getInstance("https://oasis-a3b2c-default-rtdb.firebaseio.com/").getReference("Customer/Registration").addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot contains all direct children (user IDs)
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String userId = userSnapshot.getKey();

                        if (userId.equalsIgnoreCase(googleSignInAccount.getId())) {
                            Log.d(Oscillator.TAG, "Data uploaded successfully");
                            String mobileNumber = userSnapshot.child("MobileNumber").getValue(String.class);

                            Log.d("mobileNumber", mobileNumber + "");
                            session.LoginCode(ExifInterface.GPS_MEASUREMENT_3D);
                            session.useridsession(googleSignInAccount.getId());
                            session.createLoginSession((String) null, googleSignInAccount.getDisplayName(), mobileNumber, "", "");
                            Intent intent = new Intent(LoginFirstScreen.this, CustomerHomeActivity.class);
                            intent.putExtra(LoginFirstScreen.GOOGLE_ACCOUNT, googleSignInAccount);
                            startActivity(intent);
                            finish();
                            break;
                        } else {
                            Intent intent = new Intent(LoginFirstScreen.this, LoginActivity.class);
                            intent.putExtra(GOOGLE_ACCOUNT, googleSignInAccount);
                            intent.putExtra("Login_From", "Customer");
                            startActivity(intent);
                            finish();
                        }
                        // Now you have access to each user ID
                        // You can log it or use it as needed
                        Log.d(Oscillator.TAG, "User ID: " + userId);
                    }
                    // Continue with your code or perform any other operations with the user IDs
                } else {
                    // Data for the reference node does not exist in the database
                    // Handle this case as needed
                    Intent intent = new Intent(LoginFirstScreen.this, LoginActivity.class);
                    intent.putExtra(GOOGLE_ACCOUNT, googleSignInAccount);
                    intent.putExtra("Login_From", "Customer");
                    startActivity(intent);
                    finish();

                    Log.d(Oscillator.TAG, "No data found in the database");
                }
            }

            public void onCancelled(DatabaseError databaseError) {
                Log.w("TAG", "Failed to read value.", databaseError.toException());
            }
        });
    }

    private void startAutoScrollTimer() {
        autoScrollTimer = new Timer();
        autoScrollTimerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                    }
                });
            }
        };

        // Change the delay to 5000 milliseconds (5 seconds) and the period to 5000 milliseconds (5 seconds)
        autoScrollTimer.schedule(autoScrollTimerTask, 10000000, 10000000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopAutoScrollTimer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopAutoScrollTimer();
    }

    private void stopAutoScrollTimer() {
        if (autoScrollTimer != null) {
            autoScrollTimer.cancel();
            autoScrollTimer = null;
        }

        if (autoScrollTimerTask != null) {
            autoScrollTimerTask.cancel();
            autoScrollTimerTask = null;
        }
    }

    private void redirectToPlayStore() {
        try {
            // Open the app's page on the Play Store for updates
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=" + getPackageName()));
            startActivity(intent);
        } catch (android.content.ActivityNotFoundException e) {
            // If the Play Store app is not available, open the Play Store website
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
            startActivity(intent);
        }
    }
}
