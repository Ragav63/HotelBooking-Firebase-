package com.example.hrbapp;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecItemRoomAdapter extends RecyclerView.Adapter<RecItemRoomAdapter.ItemViewHolder> {
    private Context context;
    private List<RecItemRoom> itemRoomList;
    private OnItemClickListener itemClickListener;
    private int selectedItemPosition = 0; // Initialize with an invalid position

    public interface OnItemClickListener {
        void onItemClick(RecItemRoom item);

    }

    public RecItemRoomAdapter(Context context, List<RecItemRoom> itemRoomList, OnItemClickListener itemClickListener) {
        this.context = context;
        this.itemRoomList = itemRoomList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_rec_items, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, @SuppressLint("RecyclerView") int position) {
        RecItemRoom itemRoom = itemRoomList.get(position);
        holder.bind(itemRoom);

        // Set background color based on selected item position
        if (position == selectedItemPosition) {
            holder.contentlayout.setBackgroundColor(ContextCompat.getColor(context, R.color.gold));
            holder.contentlayout.setBackground(ContextCompat.getDrawable(context, R.drawable.card_corner_curve_recitem_white));
            holder.roomTitleTv.setTextColor(ContextCompat.getColor(context, R.color.gold));
            holder.descriptionTv.setTextColor(ContextCompat.getColor(context, R.color.gold));
            holder.priceTv.setTextColor(ContextCompat.getColor(context, R.color.gold));
            holder.taxTv.setTextColor(ContextCompat.getColor(context,R.color.gold));


        } else {
            holder.contentlayout.setBackgroundColor(ContextCompat.getColor(context, R.color.bl_card));
            holder.roomTitleTv.setTextColor(ContextCompat.getColor(context, R.color.back));
            holder.descriptionTv.setTextColor(ContextCompat.getColor(context, R.color.back));
            holder.priceTv.setTextColor(ContextCompat.getColor(context, R.color.gold));
            holder.taxTv.setTextColor(ContextCompat.getColor(context,R.color.gold));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update selected item position and notify data set changed
                int previousPosition = selectedItemPosition;

                selectedItemPosition = position;

                // Notify previous and new selected item positions to update their UI
                if (previousPosition != RecyclerView.NO_POSITION) {
                    notifyItemChanged(previousPosition);
                }
                notifyItemChanged(position);
                notifyDataSetChanged();
                itemClickListener.onItemClick(itemRoom);
            }
        });


    }

    @Override
    public int getItemCount() {
        return itemRoomList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView roomTitleTv, descriptionTv, priceTv, taxTv;
        TextView roomType, roomPrice, roomTax;
        LinearLayout contentlayout;


        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            roomTitleTv = itemView.findViewById(R.id.roomTV);
            descriptionTv = itemView.findViewById(R.id.descriptionTV);
            priceTv = itemView.findViewById(R.id.priceTV);
            taxTv=itemView.findViewById(R.id.taxTV);
            contentlayout=itemView.findViewById(R.id.hll);

            roomType=itemView.findViewById(R.id.roomTypeVal);
            roomPrice=itemView.findViewById(R.id.roomPriceVal);
            roomTax=itemView.findViewById(R.id.roomTaxVal);

        }

        public void bind(RecItemRoom recItemRoom) {
            roomTitleTv.setText(recItemRoom.getRoomType());
            descriptionTv.setText(recItemRoom.getRoomDescription());
            priceTv.setText(String.valueOf(recItemRoom.getRoomPrice()));
            taxTv.setText(String.valueOf(recItemRoom.getRoomTax()));


        }
    }
}
