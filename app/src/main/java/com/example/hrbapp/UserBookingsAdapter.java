package com.example.hrbapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserBookingsAdapter extends RecyclerView.Adapter<UserBookingsAdapter.BookingViewHolder>{

    private List<Booking> bookingList;
    private OnBookingActionListener actionListener;

    public UserBookingsAdapter(List<Booking> bookingList, OnBookingActionListener actionListener) {
        this.bookingList = bookingList;
        this.actionListener = actionListener;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.mybookings_rec_items, parent, false);
        return new BookingViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookingList.get(position);
        holder.checkInDateBookingsVal.setText(booking.getCheckInDate());
        holder.roomTypeBookingsVal.setText(booking.getRoomType());

        holder.bookingViewBtn.setOnClickListener(v -> actionListener.onView(booking));
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public void updateList(List<Booking> newList) {
        bookingList = newList;
        notifyDataSetChanged();
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        public TextView checkInDateBookingsVal;
        public TextView roomTypeBookingsVal;
        public Button bookingViewBtn;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);

            checkInDateBookingsVal = itemView.findViewById(R.id.dateTvBookingsVal);
            roomTypeBookingsVal = itemView.findViewById(R.id.roomTypeTvBookingsVal);
            bookingViewBtn = itemView.findViewById(R.id.mybookingsViewbtn);
        }
    }

    public interface OnBookingActionListener {
        void onView(Booking booking);
    }
}
