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

public class ExtraFacilityAdapter extends RecyclerView.Adapter<ExtraFacilityAdapter.ExtraFacilityViewHolder> {
    private Context context;
    private List<ExtraFacility> extraFacilityList;

    private DatabaseReference reference; // Firebase Database reference

    public ExtraFacilityAdapter(Context context,List<ExtraFacility> extraFacilityList) {
        this.context=context;
        this.extraFacilityList = extraFacilityList;
        this.reference = FirebaseDatabase.getInstance().getReference("ExtraFacilities"); // Initialize the Firebase Database reference

        // Listen for changes in the Rooms data
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                extraFacilityList.clear(); // Clear the list
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ExtraFacility extraFacility = dataSnapshot.getValue(ExtraFacility.class);
                    if (extraFacility != null) {
                        extraFacilityList.add(extraFacility);
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
    static class ExtraFacilityViewHolder extends RecyclerView.ViewHolder {
        TextView extraFacilityName, extraFacilityPrice;
        Button deleteBtn, updateBtn;

        public ExtraFacilityViewHolder(@NonNull View itemView) {
            super(itemView);
            extraFacilityName = itemView.findViewById(R.id.exFacNameTvVal);
            extraFacilityPrice = itemView.findViewById(R.id.exFacPriceTvVal);

            deleteBtn=itemView.findViewById(R.id.exFacrecDeleteBtn);
            updateBtn=itemView.findViewById(R.id.exFacrecUpdateBtn);
        }
    }

    @NonNull
    @Override
    public ExtraFacilityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.extrafacmgmt_recycler_items, parent, false);
        return new ExtraFacilityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExtraFacilityViewHolder holder, int position) {
        ExtraFacility extraFacility = extraFacilityList.get(position);
        holder.extraFacilityName.setText(extraFacility.getExtraFacilityName());
        holder.extraFacilityPrice.setText(String.valueOf(extraFacility.getExtraFacilityPrice()));

        // Set click listeners for update and delete buttons
        holder.updateBtn.setOnClickListener(view -> {
            // Implement update operation
            openUpdateDialog(holder.itemView.getContext(), position);
        });

        holder.deleteBtn.setOnClickListener(view -> {
            // Implement delete operation
            // Remove the item from the list and notify the adapter
            new AlertDialog.Builder(context)
                    .setTitle("Delete ExtraFacility")
                    .setMessage("Are you sure you want to delete this extra facility?")
                    .setPositiveButton("Yes", (dialog, which) -> deleteExtraFacility(extraFacility.getId(), position))
                    .setNegativeButton("No", null)
                    .show();

//            extraFacilityList.remove(position);
//            Toast.makeText(view.getContext(), "Extra Facility Deleted", Toast.LENGTH_SHORT).show();
//            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return extraFacilityList.size();
    }

    private void deleteExtraFacility(int extraFacId, int position){
        reference.child(String.valueOf(extraFacId)).removeValue().addOnSuccessListener(aVoid -> {
            if (position >= 0 && position < extraFacilityList.size()) {
                extraFacilityList.remove(position);
                notifyItemRemoved(position);
//                notifyItemRangeChanged(position, extraFacilityList.size());
                if (extraFacilityList.isEmpty()) {
                    notifyDataSetChanged(); // Refresh adapter if list is empty
                }
                Toast.makeText(context, "Extra Facility Deleted", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(context, "Failed to delete extrafacility: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void openUpdateDialog(Context context, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_update_extrafac, null);
        builder.setView(dialogView);

        EditText exFacName = dialogView.findViewById(R.id.editExFacNameEdt);
        EditText exFacPrice = dialogView.findViewById(R.id.editExFacPriceEdt);

        ExtraFacility extraFacility = extraFacilityList.get(position);
        exFacName.setText(extraFacility.getExtraFacilityName());
        exFacPrice.setText(String.valueOf(extraFacility.getExtraFacilityPrice()));


        builder.setMessage("Extra Facility Updation")
                .setPositiveButton("Update", (dialog, which) -> {
            // Update room details
            extraFacility.setExtraFacilityName(exFacName.getText().toString());
            extraFacility.setExtraFacilityPrice(Integer.parseInt(exFacPrice.getText().toString()));

                    // Update the room in Firebase
                    String exFacid = String.valueOf(extraFacility.getId());
                    reference.child(exFacid).setValue(extraFacility)
                            .addOnSuccessListener(aVoid -> {
                                // Notify adapter after editing
                                notifyItemChanged(position);
                                Toast.makeText(context, "Extra Facility Updated", Toast.LENGTH_SHORT).show();
                            }).addOnFailureListener(e -> Toast.makeText(context, "Failed to update extrafacility: " + e.getMessage(), Toast.LENGTH_SHORT).show());

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
