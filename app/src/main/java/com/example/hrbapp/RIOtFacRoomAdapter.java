package com.example.hrbapp;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RIOtFacRoomAdapter extends RecyclerView.Adapter<RIOtFacRoomAdapter.ItemViewHolder> {
    private Context context;
    private List<RecItOtFacRoom> recItOtFacRoomList;
    private int selectedItemPosition=-1; // Initialize with an invalid position
    private OnItemClickListener itemClickListener;



    public interface OnItemClickListener {
        void onItemClick(RecItOtFacRoom itemRoom, boolean isChecked);
    }

    public RIOtFacRoomAdapter(Context context, List<RecItOtFacRoom> recItOtFacRoomList, OnItemClickListener itemClickListener) {
        this.context = context;
        this.recItOtFacRoomList = recItOtFacRoomList;
        this.itemClickListener=itemClickListener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_fac_rec_items, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, @SuppressLint("RecyclerView") int position) {
        RecItOtFacRoom itemRoom = recItOtFacRoomList.get(position);
        holder.bind(itemRoom);
        holder.roomOtFacCheckbox.setText(itemRoom.getExtraFacilityName());
        holder.roomOtFacCheckbox.setChecked(itemRoom.isChecked());


        holder.roomOtFacCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            itemRoom.setChecked(isChecked);
            selectedItemPosition = position;

            itemClickListener.onItemClick(itemRoom, isChecked);


            // Set background and text color based on checkbox state
            if (isChecked) {
                holder.contentlayout.setBackgroundColor(ContextCompat.getColor(context, R.color.gold));
                holder.contentlayout.setBackground(ContextCompat.getDrawable(context, R.drawable.card_corner_curve_recitem_white));
                holder.roomOtFacCheckbox.setTextColor(ContextCompat.getColor(context,R.color.gold));
            } else {
                // Revert to default state
                holder.contentlayout.setBackgroundColor(ContextCompat.getColor(context, R.color.bl_card));
                holder.contentlayout.setBackground(ContextCompat.getDrawable(context, R.drawable.card_corner_curve_bl));
                holder.roomOtFacCheckbox.setTextColor(ContextCompat.getColor(context,R.color.white));
            }


            notifyDataSetChanged();
        });

    }

    private void displayItemDetails(RecItOtFacRoom item) {
        // Display details in a Toast, you can modify this to suit your needs
        String details = "Facility: " + item.getExtraFacilityName() + "\nPrice: Rs. " + item.getExtraFacilityPrice();
        Toast.makeText(context, details, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return recItOtFacRoomList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        CheckBox roomOtFacCheckbox;
        TextView roomOtFacPrice;
        TextView roomOtFacValue;
        LinearLayout contentlayout;


        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            roomOtFacCheckbox = itemView.findViewById(R.id.roomOFCBox);
            roomOtFacPrice = itemView.findViewById(R.id.roomOFPrice);
            roomOtFacValue=itemView.findViewById(R.id.roomOFVal);
            contentlayout=itemView.findViewById(R.id.hll);

        }

        public void bind(RecItOtFacRoom recItOccRoom) {
            roomOtFacCheckbox.setText(recItOccRoom.getExtraFacilityName());
            roomOtFacPrice.setText(String.valueOf(recItOccRoom.getExtraFacilityPrice()));
//            roomOtFacValue.setText(recItOccRoom.getRoomOFPrice());
        }
    }
}
