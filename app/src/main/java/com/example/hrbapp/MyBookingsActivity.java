package com.example.hrbapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyBookingsActivity extends AppCompatActivity {
    
    TextView backTv;
    Bundle bundle;
    private RecyclerView mybookingsRecView;
    private UserBookingsAdapter bookingsAdapter;
    private List<Booking> bookingList = new ArrayList<>();
    private DatabaseReference bookingsRef;

    public static class Utils {
        public static String encodeEmail(String email) {
            return email.replace(".", ",");
        }
        public static String decodeEmail(String encodedEmail) {
            return encodedEmail.replace(",", ".");
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_bookings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bundle=getIntent().getExtras();

        String email=bundle.getString("email");
        Toast.makeText(this, "The shared email :"+email, Toast.LENGTH_SHORT).show();
        Log.d("MyBookingsActivity", "Encoded Email: " + Utils.encodeEmail(email)); // Log encoded email
        
        backTv=findViewById(R.id.backTv);
        
        mybookingsRecView=findViewById(R.id.mybookingsRecView);
        mybookingsRecView.setLayoutManager(new LinearLayoutManager(this));

        backTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MyBookingsActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        bookingsAdapter = new UserBookingsAdapter(bookingList, new UserBookingsAdapter.OnBookingActionListener() {
            @Override
            public void onView(Booking booking) {
                // Handle view action
//                Intent intent = new Intent(MyBookingsActivity.this, DisplayUserActivity.class);
//                intent.putExtra("bookingemailId", booking.getEmail());
//                startActivity(intent);
            }
        });

        mybookingsRecView.setAdapter(bookingsAdapter);

        // Encode the email to make it a valid Firebase path
        String encodedEmail = Utils.encodeEmail(email);

        bookingsRef = FirebaseDatabase.getInstance().getReference("Bookings").child(encodedEmail);

        fetchBookings();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchBookings(); // Ensure that bookings data is fetched whenever the activity resumes
    }

    private void fetchBookings() {
        bookingsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookingList.clear();
                Log.d("MyBookingsActivity", "DataSnapshot: " + snapshot.toString()); // Log snapshot data
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    // Retrieve the bookingId from the snapshot key
                    String bookingId = dataSnapshot.getKey();

                    Booking booking = dataSnapshot.getValue(Booking.class);
                    // Set the retrieved bookingId to the booking object
                    if (booking != null) {
//                        booking.setEmail(bookingId);
                        // Add the booking object to the list
                        bookingList.add(booking);
                    }
                }
                if (!bookingList.isEmpty()) {
                    bookingsAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MyBookingsActivity.this, "No bookings found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyBookingsActivity.this, "Failed to fetch bookings.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}