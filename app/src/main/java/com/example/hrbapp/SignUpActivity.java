package com.example.hrbapp;

import static android.content.ContentValues.TAG;

import static androidx.core.content.ContentProviderCompat.requireContext;

import static org.bouncycastle.cms.RecipientId.password;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hrbapp.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {

    EditText firstNameEdt, lastNameEdt, emailEdt, addressEdt, mobilenoEdt, cityEdt, countryEdt, documentIdEdt, passwordEdt, confPassEdt;
    Button btnRegister, btnChooseFile;
    FirebaseAuth auth;
    ProgressBar progressBar;
    TextView uploadDocTV, loginTV;
    Uri uri;
    private String imageUrl = "";
    Spinner genderSpinner, docTypeSpinner;



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

        auth = FirebaseAuth.getInstance();
//        DatabaseReference genderRef = FirebaseDatabase.getInstance().getReference("gender");
//        DatabaseReference documentRef = FirebaseDatabase.getInstance().getReference("document");


        firstNameEdt = findViewById(R.id.signupFirstname);
        lastNameEdt = findViewById(R.id.signupLastname);
        emailEdt = findViewById(R.id.signupEmail);
        addressEdt = findViewById(R.id.signupAddress);
        mobilenoEdt = findViewById(R.id.signupMobileno);
        cityEdt = findViewById(R.id.signupCity);
        countryEdt = findViewById(R.id.signupCountry);
        documentIdEdt=findViewById(R.id.signupDocId);
        passwordEdt = findViewById(R.id.signupPassword);
        confPassEdt = findViewById(R.id.signupConfirmPassword);

        CheckBox showPasswordCheckBox = findViewById(R.id.showPasswordCheckBox);

        genderSpinner = findViewById(R.id.spinner_gender);
        docTypeSpinner = findViewById(R.id.spinner_docType);

        progressBar = findViewById(R.id.progressBar);

        uploadDocTV=findViewById(R.id.upDocTv);

        btnChooseFile = findViewById(R.id.signupchooseFileBtn);
        btnRegister = findViewById(R.id.signupBtn);

        loginTV = findViewById(R.id.loginTv);

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

        mobilenoEdt.addTextChangedListener(new TextWatcher() {
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
                    mobilenoEdt.setError("Mobile number must start with 6-9 and be 10 digits long");
                } else {
                    mobilenoEdt.setError(null);
                }
            }
        });


        List<String> genderNames = Arrays.asList("Male", "Female", "Others");

//        for (String genderName : genderNames) {
//            genderRef.push().setValue(genderName)
//                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Gender added successfully"))
//                    .addOnFailureListener(e -> Log.w(TAG, "Error adding gender", e));
//        }

//        genderRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                List<String> genderNames = new ArrayList<>();
//                for (DataSnapshot citySnapshot : dataSnapshot.getChildren()) {
//                    String genderName = citySnapshot.getValue(String.class);
//                    genderNames.add(genderName);
//                }

                int textColor = getResources().getColor(R.color.fragbackground);

                // Create a custom adapter and set it to the Spinner
                CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, genderNames, textColor);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                genderSpinner.setAdapter(adapter);

//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.w(TAG, "Failed to read value.", databaseError.toException());
//            }
//        });


        List<String> docTypeNames = Arrays.asList("JPG", "Image", "Others");

//        for (String docName : docTypeNames) {
//            documentRef.push().setValue(docName)
//                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Document added successfully"))
//                    .addOnFailureListener(e -> Log.w(TAG, "Error adding Document", e));
//        }

//        documentRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                List<String> docTypeNames = new ArrayList<>();
//                for (DataSnapshot citySnapshot : dataSnapshot.getChildren()) {
//                    String docName = citySnapshot.getValue(String.class);
//                    docTypeNames.add(docName);
//                }

                int textColor1 = getResources().getColor(R.color.fragbackground);

                // Create a custom adapter and set it to the Spinner
                CustomSpinnerAdapter adapter1 = new CustomSpinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, docTypeNames, textColor1);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                docTypeSpinner.setAdapter(adapter1);

//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.w(TAG, "Failed to read value.", databaseError.toException());
//            }
//        });

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


        showPasswordCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Toggle password visibility
                if (isChecked) {
                    // Show password
                    passwordEdt.setTransformationMethod(null);
                } else {
                    // Hide password
                    passwordEdt.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });

        confPassEdt.addTextChangedListener(new TextWatcher() {
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
                    confPassEdt.setError("Minimum length is 8 characters");
                } else {
                    confPassEdt.setError(null);
                }
            }
        });


        // This launcher is responsible for launching the activity and receiving the result.
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            //Intent data = result.getData();
                            uri = result.getData().getData();

                            String fileName = getFileName(uri);
                            if (fileName != null) {
                                uploadDocTV.setText(fileName);
//                                uploadImageToFirebase(uri);
                            }
                        } else {
                            Toast.makeText(SignUpActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        // Listener for upload button
        btnChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });


        loginTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();//to finish the current activity
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                String firstname, lastname, email, mobileno, address, city, country, docId, password, confirmPass;

                firstname = String.valueOf(firstNameEdt.getText());
                lastname = String.valueOf(lastNameEdt.getText());
                email = String.valueOf(emailEdt.getText());
                mobileno = String.valueOf(mobilenoEdt.getText());
                address = String.valueOf(addressEdt.getText());
                city = String.valueOf(cityEdt.getText());
                country = String.valueOf(countryEdt.getText());
                docId = String.valueOf(documentIdEdt.getText());
                password = String.valueOf(passwordEdt.getText());
                confirmPass = String.valueOf(confPassEdt.getText());

                //Checking mail addresss
                if ( !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailEdt.setError("Invalid email format");
                    Toast.makeText(SignUpActivity.this, "Invalid email format", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                // Perform field validation
                if (!checkField(firstNameEdt) || !checkField(lastNameEdt) || !checkField(emailEdt) ||
                        !checkField(mobilenoEdt) || !checkField(addressEdt) || !checkField(cityEdt) ||
                        !checkField(countryEdt) || !checkField(documentIdEdt) || !checkField(passwordEdt) || !checkField(passwordEdt) || !checkField(confPassEdt)  || !checkTextVField(uploadDocTV)) {
                    progressBar.setVisibility(View.GONE);
                    return; // Return without proceeding further
                }

                // Check mobile number length
                if (mobileno.length() != 10) {
                    mobilenoEdt.setError("Mobile number must be 10 digits");
                    progressBar.setVisibility(View.GONE);
                    return; // Return without proceeding further
                }

                if (password.length() < 8) {
                    // If password length is less than 8 characters, show error and return
                    progressBar.setVisibility(View.GONE);
                    passwordEdt.setError("Minimum length is 8 characters");
                    Toast.makeText(SignUpActivity.this, "Minimum length is 8 characters", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(confirmPass)){
                    Toast.makeText(SignUpActivity.this, "Password and confirm password doesn't match", Toast.LENGTH_SHORT).show();
                    return;
                }

                String gender = genderSpinner.getSelectedItem().toString();
                String doctype=docTypeSpinner.getSelectedItem().toString();

                //imageUrl is obtained after selecting an image, so it depends on your image selection logic
//                String imageUrl = ""; // Initialize to empty string for now, will be set later when image is selected

                UserData userData = new UserData(firstname, lastname, email, mobileno, address, city, country, docId, gender, doctype, imageUrl);

                if (uri != null) {
                    uploadImageToFirebase(uri, userData); // Upload image and then create user
                } else {
                    createUserAndSaveData(userData); // Create user directly if no image selected
                }


            }
        });

    }

    private void uploadImageToFirebase(Uri imageUri, UserData userData) {
        if (imageUri != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            StorageReference fileReference = storageReference.child("uploads/" + System.currentTimeMillis() + "." + getFileExtension(imageUri));

            fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imageUrl = uri.toString();
                            userData.setImageUrl(imageUrl); // Set the imageUrl in UserData
                            Toast.makeText(SignUpActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                            createUserAndSaveData(userData); // Proceed with user creation and data saving
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SignUpActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE); // Hide progress bar if image upload fails
                }
            });
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @SuppressLint("Range")
    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public boolean checkField(EditText textField){
        String text = textField.getText().toString().trim();
        if (TextUtils.isEmpty(text)) {
            textField.setError("This field is required");
            return false;
        } else if (textField == emailEdt && !android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
            textField.setError("Invalid email format");
            Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            textField.setError(null);
            return true;
        }
    }

    public boolean checkTextVField(TextView textField){
        String text = textField.getText().toString().trim();
        if (TextUtils.isEmpty(text)) {
            textField.setError("This field is required");
            return false;
        } else {
            textField.setError(null);
            return true;
        }
    }

    private void saveUserDataToFirebase(UserData userData) {
        String currentDate= DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

        // Push user data to Firebase Realtime Database//here document id is the key for the reference and admin are the values
        FirebaseDatabase.getInstance().getReference("User LoginDetails")
                .child(currentDate).setValue(userData)
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
    }

    private void createUserAndSaveData(UserData userData) {

//        String password = String.valueOf(passwordEdt.getText());
        auth.createUserWithEmailAndPassword(userData.getEmail(), passwordEdt.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(SignUpActivity.this, "Account Created.",Toast.LENGTH_SHORT).show();
                            saveUserDataToFirebase(userData);
                            // Log the userData to check its contents
                            Log.d("SignUpActivity", "userData: " + userData.toString());
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            intent.putExtra("userData", userData);
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
}