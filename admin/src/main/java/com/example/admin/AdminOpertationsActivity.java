package com.example.admin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AdminOpertationsActivity extends AppCompatActivity {

//    FirebaseAuth auth;
    TextView adminEmailTv, logoutTv;
    ProgressBar progressBar;
    Button roomMgmtBtn, extraFacMgmtBtn, promoCodeDisMgmtBtn, bookingsMgmtBtn;
    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String KEY_EMAIL = "loginemail";
    private String adminEmail;
//    FirebaseUser admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_opertations);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



//        auth=FirebaseAuth.getInstance();

        adminEmailTv=findViewById(R.id.adminemailTv);
        logoutTv=findViewById(R.id.logoutTv);

        progressBar=findViewById(R.id.progressBar);

        roomMgmtBtn=findViewById(R.id.roomMgmtBtn);
        extraFacMgmtBtn=findViewById(R.id.extraFacMgmtBtn);
        promoCodeDisMgmtBtn=findViewById(R.id.promoDiscountMgmtBtn);
        bookingsMgmtBtn=findViewById(R.id.bookingsMgmtBtn);

//        String AdminEmail=getIntent().getStringExtra("loginemail");
//        adminEmailTv.setText(AdminEmail);

//        admin=auth.getCurrentUser();//to get the current user
//        if(admin==null){//if the user is null it will finish the current activity and move to login activity
//            Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
//            startActivity(intent);
//            finish();
//        } else {
//            adminEmailTv.setText(admin.getEmail());
//        }

        // Check if savedInstanceState is not null and restore the email
        // Check if there is an extra with the key "adminEmail"
        if (getIntent().hasExtra("loginemail")) {
            // Retrieve the email from the intent extras
            adminEmail = getIntent().getStringExtra("loginemail");
            // Set the email to adminEmailTv
            adminEmailTv.setText(adminEmail);
            saveAdminEmail(adminEmail); // Save admin email to SharedPreferences
        }  else {
            // If the email is not passed in the intent, retrieve it from SharedPreferences
            adminEmail = getAdminEmailFromPrefs();
            adminEmailTv.setText(adminEmail);
        }

        logoutTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   progressBar.setVisibility(View.VISIBLE);
                // Clear the saved admin email from SharedPreferences
                clearAdminEmail() ;
                // Start LoginActivity
                Intent intent = new Intent(AdminOpertationsActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        roomMgmtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                Intent intent=new Intent(getApplicationContext(), RoomMgmtActivity.class);
                startActivity(intent);
                progressBar.setVisibility(View.GONE);
                finish();
            }
        });

        extraFacMgmtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                Intent intent=new Intent(getApplicationContext(), ExtraFacMgmtActivity.class);
                startActivity(intent);
                progressBar.setVisibility(View.GONE);
                finish();
            }
        });

        promoCodeDisMgmtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                Intent intent=new Intent(getApplicationContext(), PromoCodeDisMgmtActivity.class);
                startActivity(intent);
                progressBar.setVisibility(View.GONE);
                finish();
            }
        });

        bookingsMgmtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                Intent intent=new Intent(getApplicationContext(), BookingsMgmtActivity.class);
                startActivity(intent);
                progressBar.setVisibility(View.GONE);
                finish();
            }
        });
    }

    private void clearAdminEmail() {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.remove(KEY_EMAIL);
        editor.apply();
    }
//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        // Save the email in the savedInstanceState bundle
//        outState.putString("loginemail", adminEmail);
//    }

    @Override
    protected void onResume() {
        super.onResume();
        // Set the email to adminEmailTv again when the activity resumes
        if (adminEmail != null) {
            adminEmailTv.setText(adminEmail);
        }
    }

    // Save admin email to SharedPreferences
    private void saveAdminEmail(String email) {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }

    // Retrieve admin email from SharedPreferences
    private String getAdminEmailFromPrefs() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_EMAIL, "");
    }
}