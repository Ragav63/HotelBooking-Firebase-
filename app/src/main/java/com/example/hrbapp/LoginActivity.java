package com.example.hrbapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText emailEdt, passwordEdt;
    Button btnLogin;
    FirebaseAuth lauth;
    ProgressBar progressBar;
    TextView signUpTv;

    private boolean isPasswordVisible = false;

    // Declare a global variable to store user data
    private UserData userData;
    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String KEY_EMAIL = "loginemail";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        emailEdt = findViewById(R.id.loginEmail);
        passwordEdt = findViewById(R.id.loginPassword);
        btnLogin = findViewById(R.id.loginBtn);

        lauth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progressBar);

        signUpTv = findViewById(R.id.signUpTv);

        emailEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Not needed
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Check email validity after the user finishes typing
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                    emailEdt.setError("Invalid email format");
                } else {
                    emailEdt.setError(null);
                }
            }
        });

        passwordEdt.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (passwordEdt.getRight() - passwordEdt.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if (isPasswordVisible) {
                            // Hide password
                            passwordEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            passwordEdt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eyevisibility24px, 0); // Original eye icon
                            isPasswordVisible = false;
                        } else {
                            // Show password
                            passwordEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            passwordEdt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eyeunvisible24px, 0); // Change to closed eye icon
                            isPasswordVisible = true;
                        }
                        // Move cursor to the end of the text
                        passwordEdt.setSelection(passwordEdt.getText().length());
                        return true;
                    }
                }
                return false;
            }
        });


        signUpTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
                finish();//to finish the current activity
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String email, password;
                email=emailEdt.getText().toString().trim();
                password=passwordEdt.getText().toString().trim();

                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
                    lauth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Toast.makeText(getApplicationContext(), "Login Sucessfull", Toast.LENGTH_SHORT).show();

                                        // Fetch user data from Firebase using the email
                                        fetchUserData(email);

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "Enter All Fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Check if userData is passed from SignUpActivity
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("userData")) {
            UserData userData = (UserData) intent.getSerializableExtra("userData");
            if (userData != null) {
                Log.d("LoginActivity", "Received userData from SignUpActivity: " + userData.toString());
                // You can use this userData object as needed
            }
        }
    }

    private void fetchUserData(String email) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("User LoginDetails");
        userRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        userData = dataSnapshot.getValue(UserData.class);
                        if (userData != null) {
                            // User data found, proceed to HomeActivity
                            Log.d("LoginActivity", "Fetched userData: " + userData.toString());
                            Toast.makeText(LoginActivity.this, "Fetched userData: "+userData.toString(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            intent.putExtra("userData", userData);
                            startActivity(intent);
                            finish();
                            return;
                        }
                    }
                }
                // No user data found
                Toast.makeText(LoginActivity.this, "Failed to fetch user data.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this, "Failed to fetch user data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}