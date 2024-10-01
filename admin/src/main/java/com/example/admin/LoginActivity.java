package com.example.admin;



import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText loginEmailEdt, loginPasswordEdt;
    Button loginBtn;
    TextView loginSignupTv;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    boolean valid=true;
    private boolean isPasswordVisible = false;
    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String KEY_EMAIL = "loginemail";

//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser != null){
//            Intent intent=new Intent(getApplicationContext(), AdminOpertationsActivity.class);
//            String email=currentUser.getEmail();
//            intent.putExtra("loginemail", email);
//            startActivity(intent);
//            finish();
//        }
//    }

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

        loginEmailEdt=findViewById(R.id.loginEmail);
        loginPasswordEdt=findViewById(R.id.loginPassword);
        loginBtn=findViewById(R.id.loginBtn);

        mAuth=FirebaseAuth.getInstance();

        progressBar=findViewById(R.id.progressBar);

        loginSignupTv=findViewById(R.id.signUpTv);

        loginEmailEdt.addTextChangedListener(new TextWatcher() {
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
                    loginEmailEdt.setError("Invalid email format");
                } else {
                    loginEmailEdt.setError(null);
                }
            }
        });

        loginPasswordEdt.addTextChangedListener(new TextWatcher() {
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
                if (s.length() < 8) {
                    loginPasswordEdt.setError("Minimum length is 8 characters");
                } else {
                    loginPasswordEdt.setError(null);
                }
            }
        });

        loginPasswordEdt.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (loginPasswordEdt.getRight() - loginPasswordEdt.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if (isPasswordVisible) {
                            // Hide password
                            loginPasswordEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            loginPasswordEdt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eyevisibility24px, 0); // Original eye icon
                            isPasswordVisible = false;
                        } else {
                            // Show password
                            loginPasswordEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            loginPasswordEdt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eyeunvisible24px, 0); // Change to closed eye icon
                            isPasswordVisible = true;
                        }
                        // Move cursor to the end of the text
                        loginPasswordEdt.setSelection(loginPasswordEdt.getText().length());
                        return true;
                    }
                }
                return false;
            }
        });

        loginSignupTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
                finish();//to finish the current activity
            }
        });


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String email, password;
                email=String.valueOf(loginEmailEdt.getText());
                password=String.valueOf(loginPasswordEdt.getText());


//                checkField(loginEmailEdt);
//
//                checkField(loginPasswordEdt);

                if (!checkField(loginEmailEdt) || !checkField(loginPasswordEdt)){
                    progressBar.setVisibility(View.GONE);
                    return; // Return without proceeding further
                }

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    // If email or password is empty, show error and return
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "Email and password are required", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 8) {
                    // If password length is less than 8 characters, show error and return
                    progressBar.setVisibility(View.GONE);
                    loginPasswordEdt.setError("Minimum length is 8 characters");
                    Toast.makeText(LoginActivity.this, "Minimum length is 8 characters", Toast.LENGTH_SHORT).show();
                    return;
                }

//                Toast.makeText(LoginActivity.this, "Admin Operation Activity", Toast.LENGTH_SHORT).show();
//                Intent intent=new Intent(LoginActivity.this, AdminOpertationsActivity.class);
//                intent.putExtra("loginemail", email);
//                startActivity(intent);
//                finish();

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        String userEmail = user.getEmail();
                                        // Save the login email in SharedPreferences
                                        saveLoginEmail(userEmail);
                                        // Start AdminOpertationsActivity
                                        Intent intent = new Intent(getApplicationContext(), AdminOpertationsActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }

    private void saveLoginEmail(String email) {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }

    public boolean checkField(EditText textField){
        String text = textField.getText().toString().trim();
        if (TextUtils.isEmpty(text)) {
            textField.setError("This field is required");
            return false;
        } else if (textField == loginEmailEdt && !android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
            textField.setError("Invalid email format");
            Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            textField.setError(null);
            return true;
        }
    }
}