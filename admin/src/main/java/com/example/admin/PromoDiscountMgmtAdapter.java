package com.example.admin;

import android.annotation.SuppressLint;
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

public class PromoDiscountMgmtAdapter extends RecyclerView.Adapter<PromoDiscountMgmtAdapter.PromoDiscountMgmtViewHolder> {
    private Context context;
    private List<PromoCodeDiscount> promoCodeDiscountList;
    private DatabaseReference reference; // Firebase Database reference


    public PromoDiscountMgmtAdapter(Context context, List<PromoCodeDiscount> promoCodeDiscountList) {
        this.context=context;
        this.promoCodeDiscountList = promoCodeDiscountList;
        this.reference=FirebaseDatabase.getInstance().getReference("PromocodeDiscounts");// Initialize the Firebase Database reference

        // Listen for changes in the Rooms data
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                promoCodeDiscountList.clear(); // Clear the list
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    PromoCodeDiscount promoCodeDiscount = dataSnapshot.getValue(PromoCodeDiscount.class);
                    if (promoCodeDiscount != null) {
                        promoCodeDiscountList.add(promoCodeDiscount);
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
    static class PromoDiscountMgmtViewHolder extends RecyclerView.ViewHolder {
        TextView promoDiscountName, promoDiscountPrice;
        Button deleteBtn, updateBtn;

        public PromoDiscountMgmtViewHolder(@NonNull View itemView) {
            super(itemView);
            promoDiscountName = itemView.findViewById(R.id.item_disNameTvVal);
            promoDiscountPrice = itemView.findViewById(R.id.item_DisPerTvVal);

            deleteBtn=itemView.findViewById(R.id.prcoderecDeleteBtn);
            updateBtn=itemView.findViewById(R.id.prcoderecUpdateBtn);
        }
    }

    @NonNull
    @Override
    public PromoDiscountMgmtViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.prdismgmt_recycler_items, parent, false);
        return new PromoDiscountMgmtViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PromoDiscountMgmtViewHolder holder, int position) {
        PromoCodeDiscount promoCodeDiscount = promoCodeDiscountList.get(position);
        holder.promoDiscountName.setText(promoCodeDiscount.getPromoDiscountName());
        holder.promoDiscountPrice.setText(String.valueOf(promoCodeDiscount.getPromoDiscountPercent()));

        // Set click listeners for update and delete buttons
        holder.updateBtn.setOnClickListener(view -> {
            // Implement update operation
            openUpdateDialog(holder.itemView.getContext(), position);
        });

        holder.deleteBtn.setOnClickListener(view -> {
            // Implement delete operation
            // Remove the item from the list and notify the adapter
            new AlertDialog.Builder(context)
                    .setTitle("Delete Promocode/Discount")
                    .setMessage("Are you sure you want to delete this extra facility?")
                    .setPositiveButton("Yes", (dialog, which) -> deletePromocodeDiscount(promoCodeDiscount.getId(), position))
                    .setNegativeButton("No", null)
                    .show();

//            promoCodeDiscountList.remove(position);
//            Toast.makeText(view.getContext(), "Promo/Discount Deleted", Toast.LENGTH_SHORT).show();
//            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return promoCodeDiscountList.size();
    }

    private void deletePromocodeDiscount(int promoDisId, int position){
        reference.child(String.valueOf(promoDisId)).removeValue().addOnSuccessListener(aVoid -> {
            if (position >= 0 && position < promoCodeDiscountList.size()) {
                promoCodeDiscountList.remove(position);
                notifyItemRemoved(position);
//                notifyItemRangeChanged(position, promoCodeDiscountList.size());
                if (promoCodeDiscountList.isEmpty()) {
                    notifyDataSetChanged(); // Refresh adapter if list is empty
                }
                Toast.makeText(context, "Promocode/Discount Deleted", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(context, "Failed to delete promo/discount: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }


    private void openUpdateDialog(Context context, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_update_prdiscount, null);
        builder.setView(dialogView);

        EditText prDisName = dialogView.findViewById(R.id.editPrDisNameEdt);
        EditText prDisPrice = dialogView.findViewById(R.id.editPrDisPriceEdt);

        PromoCodeDiscount promoCodeDiscount = promoCodeDiscountList.get(position);
        prDisName.setText(promoCodeDiscount.getPromoDiscountName());
        prDisPrice.setText(String.valueOf(promoCodeDiscount.getPromoDiscountPercent()));

        builder.setMessage("Promo/Discount Updation")
                .setPositiveButton("Update", (dialog, which) -> {
            // Update room details
            promoCodeDiscount.setPromoDiscountName(prDisName.getText().toString());
            promoCodeDiscount.setPromoDiscountPercent(Integer.parseInt(prDisPrice.getText().toString()));

                    // Update the room in Firebase
                    String prDisid = String.valueOf(promoCodeDiscount.getId());
                    reference.child(prDisid).setValue(promoCodeDiscount)
                            .addOnSuccessListener(aVoid -> {
                                // Notify adapter after editing
                                notifyItemChanged(position);
                                Toast.makeText(context, "Promo/Discount Updated", Toast.LENGTH_SHORT).show();
                            }).addOnFailureListener(e -> Toast.makeText(context, "Failed to update promo/discount: " + e.getMessage(), Toast.LENGTH_SHORT).show());

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
