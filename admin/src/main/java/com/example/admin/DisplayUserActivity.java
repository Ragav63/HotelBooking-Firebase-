package com.example.admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DisplayUserActivity extends AppCompatActivity {

    ImageButton backIcon;
    private TextView chInValTv, chOutValTv, noOfRoomValTv, noOfPersonValTv, noOfDaysValTv,
            roomTitleValTv, roomDescValTv, roomPriceValTv, roomTaxValTv, roomOFPrice, roomTotalAmValTv,
            firstNameValTv, lastNameValTv, emailValTv, addressValTv, postCodeValTv, mobNoValTv,
            disCouponValTv, disPercentValTv, disAmountValTv, ccNameValTv, ccNumValTv, cvvValTv, expOnValTv,
            grandTotalValTv;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_display_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        backIcon=findViewById(R.id.backImgBtn);

        chInValTv = findViewById(R.id.chInTvVal);
        chOutValTv = findViewById(R.id.chOutTvVal);
        noOfRoomValTv = findViewById(R.id.noOfRoomTvVal);
        noOfPersonValTv = findViewById(R.id.noOfPersonTvVal);
        noOfDaysValTv = findViewById(R.id.noOfDaysTvVal);
        roomTitleValTv = findViewById(R.id.roomTypeTvVal);
        roomDescValTv = findViewById(R.id.roomDescriptionTvVal);
        roomPriceValTv = findViewById(R.id.roomPriceTvVal);
        roomTaxValTv = findViewById(R.id.roomTaxTvVal);
        roomOFPrice = findViewById(R.id.roomOtherFacPriceTvVal);
        roomTotalAmValTv = findViewById(R.id.roomTotalAmTvVal);
        firstNameValTv = findViewById(R.id.firstNameTvVal);
        lastNameValTv = findViewById(R.id.lastNameTvVal);
        emailValTv = findViewById(R.id.emailTvVal);
        addressValTv = findViewById(R.id.addressTvVal);
        postCodeValTv = findViewById(R.id.postCodeTvVal);
        mobNoValTv = findViewById(R.id.mobNumTvVal);
        disCouponValTv = findViewById(R.id.disCouponTvVal);
        disPercentValTv = findViewById(R.id.disPercentTvVal);
        disAmountValTv = findViewById(R.id.disAmountTvVal);
        ccNameValTv = findViewById(R.id.ccNameTvVal);
        ccNumValTv = findViewById(R.id.ccNumberTvVal);
        cvvValTv = findViewById(R.id.cvvTvVal);
        expOnValTv = findViewById(R.id.expOnTvVal);

        grandTotalValTv = findViewById(R.id.grandTotalTvVal);

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DisplayUserActivity.this, BookingsMgmtActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Retrieve the bookingId value from the Intent
        String bookingId = getIntent().getStringExtra("bookingId");

        // Get a reference to the specific booking node in the database
        DatabaseReference bookingRef = FirebaseDatabase.getInstance().getReference("Bookings").child(bookingId);

        // Read the data from the specific booking node
        bookingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Booking node exists, retrieve its details
                    Bookings booking = snapshot.getValue(Bookings.class);
                    if (booking != null) {
                        // Populate the UI with the retrieved booking details
                        chInValTv.setText(booking.getCheckInDate());
                        chOutValTv.setText(booking.getCheckOutDate());
                        noOfRoomValTv.setText(booking.getNoOfRooms());
                        noOfPersonValTv.setText(booking.getNoOfPersons());
                        noOfDaysValTv.setText(booking.getDuration());
                        roomTitleValTv.setText(booking.getRoomType());
                        roomDescValTv.setText(booking.getRoomDescription());
                        roomPriceValTv.setText(booking.getRoomPrice());
                        roomTaxValTv.setText(booking.getRoomTax());
                        roomOFPrice.setText(booking.getOtherFacilitiesPrice());
                        roomTotalAmValTv.setText(booking.getTotalPrice());
                        firstNameValTv.setText(booking.getFirstName());
                        lastNameValTv.setText(booking.getLastName());
                        emailValTv.setText(booking.getEmail());
                        addressValTv.setText(booking.getAddress());
                        postCodeValTv.setText(booking.getPostalCode());
                        mobNoValTv.setText(booking.getMobileNumber());

                        disCouponValTv.setText(booking.getCoupon());
                        disPercentValTv.setText(booking.getDiscount());
                        disAmountValTv.setText(booking.getDiscountAmount());
                        ccNameValTv.setText(booking.getCreditCardName());
                        ccNumValTv.setText(booking.getCreditCardNumber());
                        cvvValTv.setText(booking.getCvv());
                        expOnValTv.setText(booking.getExpiryDate());
                        grandTotalValTv.setText(booking.getGrandTotal());
                    }
                } else {
                    // Booking node does not exist
                    // Handle this case, e.g., display an error message
                    Toast.makeText(DisplayUserActivity.this, "No User Data Received", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors that may occur while retrieving data
                Toast.makeText(DisplayUserActivity.this, "Failed to retrieve booking details.", Toast.LENGTH_SHORT).show();
                Log.e("DisplayUserActivity", "Failed to retrieve booking details: " + error.getMessage());
            }
        });

    }
}