package com.example.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdminBookingsAdapter extends RecyclerView.Adapter<AdminBookingsAdapter.BookingViewHolder>{

    private List<Bookings> bookingList;
    private OnBookingActionListener actionListener;

    public AdminBookingsAdapter(List<Bookings> bookingList, OnBookingActionListener actionListener) {
        this.bookingList = bookingList;
        this.actionListener = actionListener;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adminbookings_rec_items, parent, false);
        return new BookingViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Bookings booking = bookingList.get(position);
        holder.bookingMailIdTvVal.setText(booking.getEmail());
        holder.customerBookingNameVal.setText(booking.getFirstName());
        holder.customerBookingcindateVal.setText(booking.getCheckInDate());
        holder.customerBookingRoomVal.setText(booking.getRoomType());
        holder.customerBookingPriceVal.setText(booking.getGrandTotal());
        holder.customerPaymentVal.setText("Completed");

        holder.bookingApproveBtn.setOnClickListener(v -> actionListener.onApprove(booking));
        holder.bookingRejectBtn.setOnClickListener(v -> actionListener.onReject(booking));
        holder.bookingViewBtn.setOnClickListener(v -> actionListener.onView(booking));
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public void updateList(List<Bookings> newList) {
        bookingList = newList;
        notifyDataSetChanged();
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        public TextView bookingMailIdTvVal;
        public TextView customerBookingNameVal;
        public TextView customerBookingcindateVal;
        public TextView customerBookingRoomVal;
        public TextView customerBookingPriceVal;
        public TextView customerPaymentVal;
        public Button bookingRejectBtn;
        public Button bookingApproveBtn;
        public Button bookingViewBtn;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            bookingMailIdTvVal = itemView.findViewById(R.id.bookingMailIdTvVal);
            customerBookingNameVal = itemView.findViewById(R.id.customerBookingNameVal);
            customerBookingcindateVal=itemView.findViewById(R.id.customerBookingcindateVal);
            customerBookingRoomVal=itemView.findViewById(R.id.customerBookingRoomVal);
            customerBookingPriceVal=itemView.findViewById(R.id.customerBookingPriceVal);
            customerPaymentVal = itemView.findViewById(R.id.customerPaymentVal);

            bookingRejectBtn = itemView.findViewById(R.id.bookingRejectBtn);
            bookingApproveBtn = itemView.findViewById(R.id.bookingApproveBtn);
            bookingViewBtn = itemView.findViewById(R.id.bookingViewBtn);
        }
    }

    public interface OnBookingActionListener {
        void onApprove(Bookings booking);
        void onReject(Bookings booking);
        void onView(Bookings booking);
    }
}
