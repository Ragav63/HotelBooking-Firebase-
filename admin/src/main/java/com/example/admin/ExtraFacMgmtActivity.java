package com.example.admin;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ExtraFacMgmtActivity extends AppCompatActivity {

    TextView backTv;
    EditText extraFacNameEdt, extraFacPriceEdt;
    RecyclerView rvExtraFaciltiy;
    private List<ExtraFacility> extraFacilityList = new ArrayList<>();
    private ExtraFacilityAdapter extraFacilityAdapter;
    DatabaseReference databaseReference;
    ValueEventListener extraFacListener; // Declare the ValueEventListener globally
    ProgressBar progressBar;
    Button addBtn;

    boolean valid=true;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_extra_fac_mgmt);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        backTv=findViewById(R.id.backTv);

        extraFacNameEdt=findViewById(R.id.extraFacNameEdt);
        extraFacPriceEdt=findViewById(R.id.extraFacPriceEdt);

        databaseReference = FirebaseDatabase.getInstance().getReference("ExtraFacilities");

        rvExtraFaciltiy=findViewById(R.id.rvExtraFacilities);

        extraFacilityAdapter = new ExtraFacilityAdapter(ExtraFacMgmtActivity.this,extraFacilityList);
        rvExtraFaciltiy.setLayoutManager(new LinearLayoutManager(this));
        rvExtraFaciltiy.setAdapter(extraFacilityAdapter);

        progressBar=findViewById(R.id.progressBar);

        addBtn=findViewById(R.id.extraFacAddBtn);

        backTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ExtraFacMgmtActivity.this, AdminOpertationsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);

                if (!checkField(extraFacNameEdt) || !checkField(extraFacPriceEdt)){
                    progressBar.setVisibility(View.GONE);
                    return; // Return without proceeding further
                }

                // Create a new Room object with the entered values
                ExtraFacility extraFacility = new ExtraFacility();
                extraFacility.setExtraFacilityName(extraFacNameEdt.getText().toString());
                extraFacility.setExtraFacilityPrice(Integer.parseInt(extraFacPriceEdt.getText().toString()));

                // Add the new Room object to the list and notify the adapter
                extraFacilityList.add(extraFacility);
                extraFacilityAdapter.notifyDataSetChanged();

//                // Generate a unique key for the room
//                String exFacId= databaseReference.push().getKey();
                // Generate a unique integer id for the room
                int newId = generateUniqueId();
                extraFacility.setId(newId);

                // Add the new extrafacility to Firebase and update RecyclerView
                addExtraFacilityToFirebase(extraFacility);

            }
        });

        // Attach the listener to the database reference
        fetchExtraFacValueEventListener();


    }

    // Method to add a new room to Firebase and update the RecyclerView
    private void addExtraFacilityToFirebase(ExtraFacility extraFacility) {
        // Push the room data to Firebase Realtime Database under the generated key
        databaseReference.child(String.valueOf(extraFacility.getId())).setValue(extraFacility)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ExtraFacMgmtActivity.this, "Extra Facility Added", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ExtraFacMgmtActivity.this, "Failed to add extrafacility: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Clear EditText fields
        clearFields();

        progressBar.setVisibility(View.GONE);
    }

    private void fetchExtraFacValueEventListener() {

        // Attach a ValueEventListener to fetch data from Firebase
        extraFacListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Clear the existing roomList
                extraFacilityList.clear();

                // Loop through all the children in the dataSnapshot
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    // Deserialize the ExtraFac object from the snapshot
                    ExtraFacility extraFacility = postSnapshot.getValue(ExtraFacility.class);
                    // Add the extrafac to the extrafaclist
                    extraFacilityList.add(extraFacility);
                }
                // Notify the adapter that the data has changed
                extraFacilityAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
                Log.w(TAG, "loadExtrafacilities:onCancelled", databaseError.toException());
                Toast.makeText(ExtraFacMgmtActivity.this, "Failed to load ExtraFacility.", Toast.LENGTH_SHORT).show();
            }
        };

        // Attach the listener to the database reference
        databaseReference.addValueEventListener(extraFacListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove the ValueEventListener when the activity is destroyed
        if (databaseReference != null && extraFacListener != null) {
            databaseReference.removeEventListener(extraFacListener);
        }
    }

    public boolean checkField(EditText textField){
        String text = textField.getText().toString().trim();
        if (TextUtils.isEmpty(text)) {
            textField.setError("This field is required");
            return false;
        } else {
            textField.setError(null);
            return true;
        }
    }

    private void clearFields() {
        extraFacNameEdt.setText("");
        extraFacPriceEdt.setText("");
    }

    private int generateUniqueId() {
        int maxId = 0;
        for (ExtraFacility extraFacility : extraFacilityList) {
            if (extraFacility.getId() > maxId) {
                maxId = extraFacility.getId();
            }
        }
        return maxId + 1;
    }
}