package com.example.admin;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {
    private List<Room> roomList;
    private Context context;
    private DatabaseReference reference; // Firebase Database reference
    public RoomAdapter(Context context,List<Room> roomList) {
        this.context=context;
        this.roomList = roomList;
        this.reference = FirebaseDatabase.getInstance().getReference("Rooms"); // Initialize the Firebase Database reference

        // Listen for changes in the Rooms data
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                roomList.clear(); // Clear the list
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Room room = dataSnapshot.getValue(Room.class);
                    if (room != null) {
                        roomList.add(room);
                    }
                }
                notifyDataSetChanged(); // Notify the adapter that the data has changed
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors.
                Toast.makeText(context, "Failed to load data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ViewHolder class
    static class RoomViewHolder extends RecyclerView.ViewHolder {
        TextView roomTypeTv, roomDescriptionTv, roomPriceTv, roomTaxTv;
        Button deleteBtn, updateBtn;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            roomTypeTv = itemView.findViewById(R.id.roomTypeTvVal);
            roomDescriptionTv = itemView.findViewById(R.id.roomDesTvVal);
            roomPriceTv = itemView.findViewById(R.id.roomPriceTvVal);
            roomTaxTv = itemView.findViewById(R.id.roomtaxTvVal);

            deleteBtn=itemView.findViewById(R.id.roomrecDeleteBtn);
            updateBtn=itemView.findViewById(R.id.roomrecUpdateBtn);
        }
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.roommgmt_recycler_items, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = roomList.get(position);
        holder.roomTypeTv.setText(room.getRoomType());
        holder.roomDescriptionTv.setText(room.getRoomDescription());
        holder.roomPriceTv.setText(String.valueOf(room.getRoomPrice()));
        holder.roomTaxTv.setText(String.valueOf(room.getRoomTax()));


        // Set click listeners for update and delete buttons
        holder.updateBtn.setOnClickListener(view -> {
            // Implement update operation
            openUpdateDialog(holder.itemView.getContext(), position);
        });

        holder.deleteBtn.setOnClickListener(view -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete Room")
                    .setMessage("Are you sure you want to delete this room?")
                    .setPositiveButton("Yes", (dialog, which) -> deleteRoom(room.getId(), position))
                    .setNegativeButton("No", null)
                    .show();
            // Remove the item from Firebase and the list
//            deleteRoom(holder.itemView.getContext(), position);
//            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    private void deleteRoom(int roomId, int position) {
//        Room room = roomList.get(position);
//        String roomId = String.valueOf(room.getId());
        reference.child(String.valueOf(roomId)).removeValue().addOnSuccessListener(aVoid -> {
            if (position >= 0 && position < roomList.size()){
                roomList.remove(position);
                notifyItemRemoved(position);

//                notifyItemRangeChanged(position, roomList.size());
                if (roomList.isEmpty()) {
                    notifyDataSetChanged(); // Refresh adapter if list is empty
                }
                Toast.makeText(context, "Room Deleted", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(context, "Failed to delete room: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void openUpdateDialog(Context context, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_update_room, null);
        builder.setView(dialogView);

        EditText roomTypeEdt = dialogView.findViewById(R.id.editRoomTypeEdt);
        EditText roomDescEdt = dialogView.findViewById(R.id.editRoomDescEdt);
        EditText roomPriceEdt = dialogView.findViewById(R.id.editRoomPriceEdt);
        EditText roomTaxEdt = dialogView.findViewById(R.id.editRoomTaxEdt);

        Room room = roomList.get(position);
        roomTypeEdt.setText(room.getRoomType());
        roomDescEdt.setText(room.getRoomDescription());
        roomPriceEdt.setText(String.valueOf(room.getRoomPrice()));
        roomTaxEdt.setText(String.valueOf(room.getRoomTax()));

        builder.setMessage("Room Updation")
                .setPositiveButton("Update", (dialog, which) -> {
            // Update room details
            room.setRoomType(roomTypeEdt.getText().toString());
            room.setRoomDescription(roomDescEdt.getText().toString());
            room.setRoomPrice(Double.parseDouble(roomPriceEdt.getText().toString()));
            room.setRoomTax(Integer.parseInt(roomTaxEdt.getText().toString()));

            // Update the room in Firebase
            String roomId = String.valueOf(room.getId());
            reference.child(roomId).setValue(room)
            .addOnSuccessListener(aVoid -> {
                // Notify adapter after editing
                notifyItemChanged(position);
                Toast.makeText(context, "Room Updated", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> Toast.makeText(context, "Failed to update room: " + e.getMessage(), Toast.LENGTH_SHORT).show());

        }).setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss()).create();

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(dialogInterface -> {
            // Set message text color to white
            int messageTextColor = ContextCompat.getColor(context, android.R.color.white);
            TextView messageTextView = dialog.getWindow().findViewById(android.R.id.message);

            if (messageTextView != null) {
                messageTextView.setTextColor(messageTextColor);
            }
            Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            positiveButton.setTextColor(ContextCompat.getColor(context, R.color.white));
            negativeButton.setTextColor(ContextCompat.getColor(context, R.color.white));
        });

        dialog.show();
    }
}
