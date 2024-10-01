package com.example.admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;

public class SignUpActivity extends AppCompatActivity {

    EditText firstNameEdt, lastNameEdt, emailAddressEdt, mobileNumberEdt, documentNameEdt, documentIdEdt, passwordEdt, confirmPasswordEdt;
    RadioGroup genderRGroup;
    RadioButton maleRb, femaleRb;
    CheckBox showPassword;
    Button btnRegister;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView loginTextView;

    boolean valid=true;

//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser != null){
//            Intent intent=new Intent(getApplicationContext(), AdminOpertationsActivity.class);
//            startActivity(intent);
//            finish();
//        }
//    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        mAuth=FirebaseAuth.getInstance();

        firstNameEdt=findViewById(R.id.signupFirstname);
        lastNameEdt=findViewById(R.id.signupLastname);
        emailAddressEdt=findViewById(R.id.signupEmail);
        mobileNumberEdt=findViewById(R.id.signupMobileno);
        documentNameEdt=findViewById(R.id.signupDocName);
        documentIdEdt=findViewById(R.id.signupDocId);
        passwordEdt=findViewById(R.id.signupPassword);
        showPassword=findViewById(R.id.showPasswordCheckBox);
        confirmPasswordEdt=findViewById(R.id.signupConfirmPassword);

        genderRGroup=findViewById(R.id.genderRg);

        maleRb=findViewById(R.id.radio_male);
        femaleRb=findViewById(R.id.radio_female);

        mAuth = FirebaseAuth.getInstance();

        progressBar=findViewById(R.id.signupPBar);

        btnRegister=findViewById(R.id.signupBtn);

        loginTextView=findViewById(R.id.loginTv);

        emailAddressEdt.addTextChangedListener(new TextWatcher() {
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
                    emailAddressEdt.setError("Invalid email format");
                } else {
                    emailAddressEdt.setError(null);
                }
            }
        });

        mobileNumberEdt.addTextChangedListener(new TextWatcher() {
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
                String mobileNumber = s.toString();

                // Check if the mobile number starts with a digit between 6 and 9 and has a length of 10 digits
                if (!mobileNumber.matches("^[6-9]\\d{9}$")) {
                    mobileNumberEdt.setError("Mobile number must start with 6-9 and be 10 digits long");
                } else {
                    mobileNumberEdt.setError(null);
                }
            }
        });

        passwordEdt.addTextChangedListener(new TextWatcher() {
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
                    passwordEdt.setError("Minimum length is 8 characters");
                } else {
                    passwordEdt.setError(null);
                }
            }
        });

        // Set OnClickListener for the showPassword checkbox
        showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the checkbox is checked
                if (showPassword.isChecked()) {
                    // If checked, set the input type of the password EditText to "text"
                    passwordEdt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    // If unchecked, set the input type back to "password" to hide the password
                    passwordEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

        confirmPasswordEdt.addTextChangedListener(new TextWatcher() {
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
                    confirmPasswordEdt.setError("Minimum length is 8 characters");
                } else {
                    confirmPasswordEdt.setError(null);
                }
            }
        });

        // Set default gender to "Male"
        genderRGroup.check(R.id.radio_male);

        // Add OnClickListener to the male radio button
        maleRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (maleRb.isChecked()) {
                    // Male radio button is checked, you can get its details here
                    String gender = maleRb.getText().toString();
                    // Do something with the gender, such as storing it in a variable or displaying it
                    Toast.makeText(SignUpActivity.this, "Gender: " + gender, Toast.LENGTH_SHORT).show();
                    // Uncheck the female radio button
                    femaleRb.setChecked(false);
                }
            }
        });

        // Add OnClickListener to the female radio button
        femaleRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (femaleRb.isChecked()) {
                    // Female radio button is checked, you can get its details here
                    String gender = femaleRb.getText().toString();
                    // Do something with the gender, such as storing it in a variable or displaying it
                    Toast.makeText(SignUpActivity.this, "Gender: " + gender, Toast.LENGTH_SHORT).show();
                    // Uncheck the female radio button
                    maleRb.setChecked(false);
                }
            }
        });




        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();//to finish the current activity
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);

                String FirstName, LastName, Email, MobileNo, DocumentName, DocumentId, Password, ConfirmPassword;

                FirstName = String.valueOf(firstNameEdt.getText());
                LastName = String.valueOf(lastNameEdt.getText());
                Email=String.valueOf(emailAddressEdt.getText());
                MobileNo=String.valueOf(mobileNumberEdt.getText());
                DocumentName=String.valueOf(documentNameEdt.getText());
                DocumentId=String.valueOf(documentIdEdt.getText());
                Password=String.valueOf(passwordEdt.getText());
                ConfirmPassword=String.valueOf(confirmPasswordEdt.getText());

                //Checking mail addresss
                 if ( !android.util.Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
                    emailAddressEdt.setError("Invalid email format");
                    Toast.makeText(SignUpActivity.this, "Invalid email format", Toast.LENGTH_SHORT).show();
                     progressBar.setVisibility(View.GONE);
                     return;
                }

                // Check mobile number length
                if (MobileNo.length() != 10) {
                    mobileNumberEdt.setError("Mobile number must be 10 digits");
                    progressBar.setVisibility(View.GONE);
                    return; // Return without proceeding further
                }

                // Check which radio button is checked and get its text
//                String gender = maleRb.isChecked() ? maleRb.getText().toString() : femaleRb.getText().toString();

                // Get the checked radio button id
                int checkedRadioButtonId = genderRGroup.getCheckedRadioButtonId();
                // Find the checked radio button
                RadioButton checkedRadioButton = findViewById(checkedRadioButtonId);
                // Get the gender from the checked radio button
                String Gender = checkedRadioButton.getText().toString();


                // Perform field validation
                if (!checkField(firstNameEdt) || !checkField(lastNameEdt) || !checkField(emailAddressEdt) ||
                        !checkField(mobileNumberEdt) || !checkField(documentNameEdt) || !checkField(documentIdEdt) ||
                        !checkField(passwordEdt) || !checkField(confirmPasswordEdt)) {
                    progressBar.setVisibility(View.GONE);
                    return; // Return without proceeding further
                }

                if (Password.length() < 8) {
                    // If password length is less than 8 characters, show error and return
                    progressBar.setVisibility(View.GONE);
                    passwordEdt.setError("Minimum length is 8 characters");
                    Toast.makeText(SignUpActivity.this, "Minimum length is 8 characters", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!Password.equals(ConfirmPassword)) {
                    Toast.makeText(SignUpActivity.this, "Password and Confirm Password should be same.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return; // Return without proceeding further
                }


                // Create an instance of User
                Admin admin = new Admin(FirstName, LastName, Email, MobileNo, DocumentName, DocumentId, Gender);

                // Sign in success, update UI with the signed-in user's information
//                                        Toast.makeText(SignUpActivity.this, "Account Created.", Toast.LENGTH_SHORT).show();
//                String currentDate= DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
//                String emailKey = Email.replace('.', ',');
                // Use push().getKey() to generate a unique key
                String adminId = FirebaseDatabase.getInstance().getReference("AdminLoginDetails").push().getKey();

                // Push user data to Firebase Realtime Database//here document id is the key for the reference and admin are the values
                FirebaseDatabase.getInstance().getReference("Admin LoginDetails")
                        .child(adminId).setValue(admin)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // Data saved successfully
                                    Toast.makeText(SignUpActivity.this, "Data Saved to Firebase.", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    // Data save failed
                                    Toast.makeText(SignUpActivity.this, "Failed to save data to Firebase.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SignUpActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                            }
                        });



                mAuth.createUserWithEmailAndPassword(Email, Password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(getApplicationContext(), AdminOpertationsActivity.class);
                                        intent.putExtra("loginEmail",Email);
                                        startActivity(intent);
                                        finish();//to finish the current activity

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
            }
        });
    }

    public boolean checkField(EditText textField){
        String text = textField.getText().toString().trim();
        if (TextUtils.isEmpty(text)) {
            textField.setError("This field is required");
            return false;
        } else if (textField == emailAddressEdt && !android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
            textField.setError("Invalid email format");
            Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            textField.setError(null);
            return true;
        }
    }
}