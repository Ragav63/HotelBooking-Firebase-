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

public class RoomMgmtActivity extends AppCompatActivity {

    TextView backTv;
    EditText roomTypeEdt, roomDescEdt, roomPriceEdt, roomTaxEdt;
    RecyclerView rvRoom;
    private List<Room> roomList = new ArrayList<>();
    RoomAdapter roomAdapter;
    DatabaseReference databaseReference;
    ValueEventListener roomListener; // Declare the ValueEventListener globally
    ProgressBar progressBar;
    Button addBtn;

    boolean valid=true;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_room_mgmt);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        backTv=findViewById(R.id.backTv);

        roomTypeEdt=findViewById(R.id.roomTypeEdt);
        roomDescEdt=findViewById(R.id.roomDesEdt);
        roomPriceEdt=findViewById(R.id.roomPriceEdt);
        roomTaxEdt=findViewById(R.id.roomTaxEdt);

        rvRoom=findViewById(R.id.rvRoom);

        databaseReference = FirebaseDatabase.getInstance().getReference("Rooms");

        roomAdapter = new RoomAdapter(RoomMgmtActivity.this,roomList);
        rvRoom.setLayoutManager(new LinearLayoutManager(this));
        rvRoom.setAdapter(roomAdapter);

        progressBar=findViewById(R.id.progressBar);

        addBtn=findViewById(R.id.roomAddBtn);

        backTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RoomMgmtActivity.this, AdminOpertationsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);

                if (!checkField(roomTypeEdt) || !checkField(roomDescEdt) || !checkField(roomPriceEdt) || !checkField(roomTaxEdt)){
                    progressBar.setVisibility(View.GONE);
                    return; // Return without proceeding further
                }

                // Create a new Room object with the entered values
                Room room = new Room();
//                RecItemRoom recItemRoom;
                room.setRoomType(roomTypeEdt.getText().toString());
                room.setRoomDescription(roomDescEdt.getText().toString());
                room.setRoomPrice(Double.parseDouble(roomPriceEdt.getText().toString()));
                room.setRoomTax(Integer.parseInt(roomTaxEdt.getText().toString()));

                // Generate a unique integer id for the room
                int newId = generateUniqueId();
                room.setId(newId);

                // Add the new room to Firebase and update RecyclerView
                addRoomToFirebase(room);

            }
        });

        // Attach the listener to the database reference
        fetchRoomValueEventListener();

    }

    // Method to add a new room to Firebase and update the RecyclerView
    private void addRoomToFirebase(Room room) {
        progressBar.setVisibility(View.VISIBLE);

        // Push the room data to Firebase Realtime Database under the generated key
        databaseReference.child(String.valueOf(room.getId())).setValue(room)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(RoomMgmtActivity.this, "Room Added", Toast.LENGTH_SHORT).show();

                // Clear EditText fields
                clearFields();
            }
        })
            .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(RoomMgmtActivity.this, "Failed to add room: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchRoomValueEventListener() {
        // Attach a ValueEventListener to fetch data from Firebase
        roomListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Clear the existing roomList
                roomList.clear();

                // Loop through all the children in the dataSnapshot
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    // Deserialize the Room object from the snapshot
                    Room room = postSnapshot.getValue(Room.class);
                    // Add the room to the roomList
                    roomList.add(room);
                }
                // Notify the adapter that the data has changed
                roomAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
                Log.w(TAG, "loadRooms:onCancelled", databaseError.toException());
                Toast.makeText(RoomMgmtActivity.this, "Failed to load rooms.", Toast.LENGTH_SHORT).show();
            }
        };

        // Attach the listener to the database reference
        databaseReference.addValueEventListener(roomListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove the ValueEventListener when the activity is destroyed
        if (databaseReference != null && roomListener != null) {
            databaseReference.removeEventListener(roomListener);
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
        roomTypeEdt.setText("");
        roomDescEdt.setText("");
        roomPriceEdt.setText("");
        roomTaxEdt.setText("");
    }

    private int generateUniqueId() {
        int maxId = 0;
        for (Room room : roomList) {
            if (room.getId() > maxId) {
                maxId = room.getId();
            }
        }
        return maxId + 1;
    }
}