package com.example.admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BookingsMgmtActivity extends AppCompatActivity {

    TextView backTv;
    ImageButton filterIcon;
    private RecyclerView bookingsRecView;
    private AdminBookingsAdapter bookingsAdapter;
    private List<Bookings> bookingList = new ArrayList<>();
    private DatabaseReference bookingsRef;

    private static final String FILTER_STATE_KEY = "filterState";
    private static final int FILTER_BY_DATE = 1;
    private static final int FILTER_BY_ROOM = 2;
    private static final int FILTER_BY_PRICE = 3;
    private static final int FILTER_NONE = 0;
    private int currentFilterState = FILTER_BY_DATE;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bookings_mgmt);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        backTv=findViewById(R.id.backTv);

        filterIcon=findViewById(R.id.filtericon);

        backTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(BookingsMgmtActivity.this, AdminOpertationsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        filterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });

        bookingsRecView = findViewById(R.id.adminBookingsrecV);
        bookingsRecView.setLayoutManager(new LinearLayoutManager(this));

        bookingsAdapter = new AdminBookingsAdapter(bookingList, new AdminBookingsAdapter.OnBookingActionListener() {
            @Override
            public void onApprove(Bookings booking) {
                // Handle approve action
            }

            @Override
            public void onReject(Bookings booking) {
                // Handle reject action
            }

            @Override
            public void onView(Bookings booking) {
                // Handle view action
                Intent intent = new Intent(BookingsMgmtActivity.this, DisplayUserActivity.class);
                intent.putExtra("bookingId", booking.getBookingId());
                startActivity(intent);
            }
        });

        bookingsRecView.setAdapter(bookingsAdapter);

        bookingsRef = FirebaseDatabase.getInstance().getReference("Bookings");

        fetchBookings();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(FILTER_STATE_KEY, currentFilterState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentFilterState = savedInstanceState.getInt(FILTER_STATE_KEY, FILTER_BY_DATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchBookings(); // Ensure that bookings data is fetched whenever the activity resumes
        applyCurrentFilter();
    }

    private void applyCurrentFilter() {
        switch (currentFilterState) {
            case FILTER_BY_DATE:
                filterListByDate();
                break;
            case FILTER_BY_ROOM:
                filterListByRoom();
                break;
            case FILTER_BY_PRICE:
                filterListByPrice();
                break;
            default:
                bookingsAdapter.updateList(new ArrayList<>(bookingList));
                break;
        }
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);


        // Assuming you have a menu resource named toolbar_menu.xml
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getItemId() == R.id.menu_datewise) {
                    // Handle menu item click for datewise
                    filterListByDate();
                    Toast.makeText(BookingsMgmtActivity.this, "Datewise", Toast.LENGTH_SHORT).show();
                } else if (item.getItemId() == R.id.menu_roomwise) {
                    // Handle menu item click for roomwise
                    filterListByRoom();
                    Toast.makeText(BookingsMgmtActivity.this, "Roomwise", Toast.LENGTH_SHORT).show();
                } else if (item.getItemId() == R.id.menu_pricewise) {
                    // Handle menu item click for namewise
                    filterListByPrice();
                    Toast.makeText(BookingsMgmtActivity.this, "Pricewise", Toast.LENGTH_SHORT).show();
                }
//                // Handle menu item clicks
//                switch (item.getItemId()) {
//                    case R.id.menu_datewise:
//                        // Handle profile menu item click
//                        Toast.makeText(BookingsMgmtActivity.this, "Datewise", Toast.LENGTH_SHORT).show();
//                        return true;
//
//                    case R.id.menu_roomwise:
//                        Toast.makeText(BookingsMgmtActivity.this, "Roomwise", Toast.LENGTH_SHORT).show();
//                        return true;
//
//                    case R.id.menu_namewise:
//                        Toast.makeText(BookingsMgmtActivity.this, "Namewise", Toast.LENGTH_SHORT).show();
//                        return true;
//
//
//                    default:
//                        return false;
//                }
                return false;
            }
        });

        popupMenu.inflate(R.menu.toolbar_bookingsmgmt_menu_items);

        try {
            Field fieldMPopup=PopupMenu.class.getDeclaredField("mPopup");
            fieldMPopup.setAccessible(true);
            Object mPopup=fieldMPopup.get(popupMenu);
            Class<?>popupClass=mPopup.getClass();
            Method setForceShowIcon=popupClass.getDeclaredMethod("setForceShowIcon",boolean.class);
            setForceShowIcon.invoke(mPopup,true);

        }catch (Exception e){

            Log.e("popup","error",e);

        }finally {
            popupMenu.show();
        }
    }

    private void fetchBookings() {
        bookingsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookingList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    // Retrieve the bookingId from the snapshot key
                    String bookingId = dataSnapshot.getKey();

                    Bookings booking = dataSnapshot.getValue(Bookings.class);
                    // Set the retrieved bookingId to the booking object
                    if (booking != null) {
                        booking.setBookingId(bookingId);
                        // Add the booking object to the list
                        bookingList.add(booking);
                    }
                }
                if (!bookingList.isEmpty()) {
                    applyCurrentFilter(); // Apply default filter when data is fetched
                    bookingsAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(BookingsMgmtActivity.this, "No bookings found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BookingsMgmtActivity.this, "Failed to fetch bookings.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterListByDate() {
        currentFilterState = FILTER_BY_DATE;
        List<Bookings> filteredList = new ArrayList<>(bookingList);
        Collections.sort(filteredList, Comparator.comparing(Bookings::getCheckInDate));
        bookingsAdapter.updateList(filteredList);
    }

    private void filterListByRoom() {
        currentFilterState = FILTER_BY_ROOM;
        List<Bookings> sortedList = new ArrayList<>(bookingList);
        Collections.sort(sortedList, Comparator.comparing(Bookings::getRoomType));
        bookingsAdapter.updateList(sortedList);
    }

    private void filterListByPrice() {
        currentFilterState = FILTER_BY_PRICE;
        List<Bookings> sortedList = new ArrayList<>(bookingList);
        Collections.sort(sortedList, new Comparator<Bookings>() {
            @Override
            public int compare(Bookings b1, Bookings b2) {
                return Double.compare(b1.getGrandTotalAsDouble(), b2.getGrandTotalAsDouble());
            }
        });
//        Collections.sort(sortedList, Comparator.comparing(Bookings::getGrandTotal));

        bookingsAdapter.updateList(sortedList);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        //the process of the below code denotes the the three dot menu icon button in the toolbar or the screen
//        //by removing it we can act the image button as drop down menu list of items
//        //getMenuInflater().inflate(R.menu.toolbar_icon_menu_items, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        // Handle other menu item clicks here if needed
//        return super.onOptionsItemSelected(item);
//    }
}