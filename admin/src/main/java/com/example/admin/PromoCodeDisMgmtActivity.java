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

public class PromoCodeDisMgmtActivity extends AppCompatActivity {

    TextView backTv;
    EditText promoCodeEdt, discountEdt;
    RecyclerView rvPrDiscountMgmt;
    private List<PromoCodeDiscount> promoCodeDiscountList = new ArrayList<>();
    PromoDiscountMgmtAdapter promoDiscountMgmtAdapter;
    DatabaseReference databaseReference;
    ValueEventListener promoDisListener; // Declare the ValueEventListener globally
    ProgressBar progressBar;
    Button addBtn;
    boolean valid=true;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_promo_code_dis_mgmt);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        backTv=findViewById(R.id.backTv);

        promoCodeEdt=findViewById(R.id.prcodeEdt);
        discountEdt=findViewById(R.id.promodiscountEdt);

        rvPrDiscountMgmt=findViewById(R.id.rvPromoDiscounts);
        promoDiscountMgmtAdapter = new PromoDiscountMgmtAdapter(PromoCodeDisMgmtActivity.this, promoCodeDiscountList);
        rvPrDiscountMgmt.setLayoutManager(new LinearLayoutManager(this));
        rvPrDiscountMgmt.setAdapter(promoDiscountMgmtAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("PromocodeDiscounts");

        progressBar=findViewById(R.id.progressBar);

        addBtn=findViewById(R.id.prDisAddBtn);

        backTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PromoCodeDisMgmtActivity.this, AdminOpertationsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);

                if (!checkField(promoCodeEdt) || !checkField(discountEdt)){
                    progressBar.setVisibility(View.GONE);
                    return; // Return without proceeding further
                }

                // Create a new Room object with the entered values
                PromoCodeDiscount promoCodeDiscount = new PromoCodeDiscount();
                promoCodeDiscount.setPromoDiscountName(promoCodeEdt.getText().toString());
                promoCodeDiscount.setPromoDiscountPercent(Integer.parseInt(discountEdt.getText().toString()));

                // Add the new Room object to the list and notify the adapter
                promoCodeDiscountList.add(promoCodeDiscount);
                promoDiscountMgmtAdapter.notifyDataSetChanged();

                // Generate a unique key for the room
//                String proDisId = databaseReference.push().getKey();

                // Generate a unique integer id for the room
                int newId = generateUniqueId();
                promoCodeDiscount.setId(newId);

                // Add the new extrafacility to Firebase and update RecyclerView
                addPromocodeDiscountToFirebase(promoCodeDiscount);



//                Toast.makeText(PromoCodeDisMgmtActivity.this, "Promo/Discount Added", Toast.LENGTH_SHORT).show();
            }
        });

        // Attach the listener to the database reference
        fetchPromocodeDiscountValueEventListener();


    }

    // Method to add a new room to Firebase and update the RecyclerView
    private void addPromocodeDiscountToFirebase(PromoCodeDiscount promoCodeDiscount) {
        // Push the room data to Firebase Realtime Database under the generated key
        databaseReference.child(String.valueOf(promoCodeDiscount.getId())).setValue(promoCodeDiscount)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(PromoCodeDisMgmtActivity.this, "Promo/Discount Added", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(PromoCodeDisMgmtActivity.this, "Failed to add Promo/Discount: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Clear EditText fields
        clearFields();

        progressBar.setVisibility(View.GONE);
    }

    private void fetchPromocodeDiscountValueEventListener() {

        // Attach a ValueEventListener to fetch data from Firebase
        promoDisListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Clear the existing roomList
                promoCodeDiscountList.clear();

                // Loop through all the children in the dataSnapshot
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    // Deserialize the ExtraFac object from the snapshot
                    PromoCodeDiscount promoCodeDiscount = postSnapshot.getValue(PromoCodeDiscount.class);
                    // Add the extrafac to the extrafaclist
                    promoCodeDiscountList.add(promoCodeDiscount);
                }
                // Notify the adapter that the data has changed
                promoDiscountMgmtAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
                Log.w(TAG, "loadPromocodeDis:onCancelled", databaseError.toException());
                Toast.makeText(PromoCodeDisMgmtActivity.this, "Failed to load PromocodeDiscount.", Toast.LENGTH_SHORT).show();
            }
        };

        // Attach the listener to the database reference
        databaseReference.addValueEventListener(promoDisListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove the ValueEventListener when the activity is destroyed
        if (databaseReference != null && promoDisListener != null) {
            databaseReference.removeEventListener(promoDisListener);
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
        promoCodeEdt.setText("");
        discountEdt.setText("");
    }

    private int generateUniqueId() {
        int maxId = 0;
        for (PromoCodeDiscount promoCodeDiscount : promoCodeDiscountList) {
            if (promoCodeDiscount.getId() > maxId) {
                maxId = promoCodeDiscount.getId();
            }
        }
        return maxId + 1;
    }
}