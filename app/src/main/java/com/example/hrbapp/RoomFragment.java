package com.example.hrbapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class RoomFragment extends Fragment implements RecItemRoomAdapter.OnItemClickListener, RIOtFacRoomAdapter.OnItemClickListener{

//    String rPrice, rTax, rOFPrice, rTotal;
    Bundle bundle;
    private RecyclerView recVRoomType, roomRVOtFac;
    private List<RecItemRoom> recItemRoomList;
    private List<RecItOtFacRoom> recItOtFacRoomList;
    private RecItemRoomAdapter roomRecItemRoomAdapter;
    private RIOtFacRoomAdapter riOtFacRoomAdapter;
    private AppCompatButton selectDetailsBtn;
    private DatabaseReference dbReferenceRooms, dbReferenceExFac;
    private int selectedItemPosition;
    private double totalAmount = 0.0;
    String cbox="";
    String roomOFTitle;
    TextView roomType, roomDesc, roomPrice, roomTax, roomOtherFac, roomTotal;
    private DataViewModel dataViewModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataViewModel = new ViewModelProvider(requireActivity()).get(DataViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_room, container, false);

        // Initialize bundle here
        bundle = getArguments();

        roomType = v.findViewById(R.id.roomTypeVal);
        roomDesc = v.findViewById(R.id.roomDescVal);
        roomPrice = v.findViewById(R.id.roomPriceVal);
        roomTax = v.findViewById(R.id.roomTaxVal);
        roomOtherFac = v.findViewById(R.id.roomOFVal);
        roomTotal = v.findViewById(R.id.roomTotalVal);

        recVRoomType = v.findViewById(R.id.roomRecView);
        recVRoomType.setLayoutManager(new LinearLayoutManager(getContext()));

        roomRVOtFac = v.findViewById(R.id.roomRVFac);
        roomRVOtFac.setLayoutManager(new LinearLayoutManager(getContext()));

        selectDetailsBtn = v.findViewById(R.id.selectDetailsBtn);

        // Initialize data for RecyclerView
        recItemRoomList = new ArrayList<>();


        recItOtFacRoomList = new ArrayList<>();


//      getting reference from the firebase database for Rooms
        dbReferenceRooms = FirebaseDatabase.getInstance().getReference("Rooms"); // Adjust the path as needed

        dbReferenceRooms.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recItemRoomList.clear();
                for (DataSnapshot roomSnapshot : snapshot.getChildren()) {
                    RecItemRoom room = roomSnapshot.getValue(RecItemRoom.class);
                    if (room != null) {
                        recItemRoomList.add(room);
                    } else {
                        Log.e("RoomFragment", "Room data is null");
                    }
                }
                roomRecItemRoomAdapter.notifyDataSetChanged();
                Log.d("RoomFragment", "Room list updated: " + recItemRoomList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("RoomFragment", "Failed to read Room data", error.toException());
            }
        });

        // Set the item click listener to this fragment
        roomRecItemRoomAdapter = new RecItemRoomAdapter(getContext(), recItemRoomList, this);

        // Set adapter to RecyclerView
        recVRoomType.setAdapter(roomRecItemRoomAdapter);

        //getting reference from the firebase database for Extra Facilities
        dbReferenceExFac=FirebaseDatabase.getInstance().getReference("ExtraFacilities");

        dbReferenceExFac.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recItOtFacRoomList.clear();
                for (DataSnapshot exfacSnapshot:snapshot.getChildren()){
                    RecItOtFacRoom recItOtFacRoom=exfacSnapshot.getValue(RecItOtFacRoom.class);
                    if (recItOtFacRoom!=null){
                        recItOtFacRoomList.add(recItOtFacRoom);
                    } else {
                        Log.e("RoomFragment", "ExFac data is null");
                    }
                }
                riOtFacRoomAdapter.notifyDataSetChanged();
                Log.d("RoomFragment", "Extra Facility list updated: " + recItOtFacRoomList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("RoomFragment", "Failed to read Exfac data", error.toException());
            }
        });

        // Set the item click listener to this fragment
        riOtFacRoomAdapter = new RIOtFacRoomAdapter(getContext(), recItOtFacRoomList, this);

        // Set adapter to RecyclerView
        roomRVOtFac.setAdapter(riOtFacRoomAdapter);

        if (bundle != null) {
            String fname = bundle.getString("fname");
            String lname = bundle.getString("lname");
            String email = bundle.getString("email");
            String mobile = bundle.getString("mobile");
            String address = bundle.getString("address");
            String city = bundle.getString("city");
            String country = bundle.getString("country");
            String docId = bundle.getString("docId");
            String gender = bundle.getString("gender");
            String docType = bundle.getString("docType");
            String imageUrl = bundle.getString("imageUrl");

            String ChInDate = bundle.getString("ChInDate");
            String ChOutDate = bundle.getString("ChOutDate");

            String noroom = bundle.getString("SeRoom");
            String noperson =bundle.getString("SePerson");
            String duration = bundle.getString("duration");

            // Set data to ViewModel
            dataViewModel.setFname(fname);
            dataViewModel.setLname(lname);
            dataViewModel.setEmail(email);
            dataViewModel.setMobile(mobile);
            dataViewModel.setAddress(address);
            dataViewModel.setCity(city);
            dataViewModel.setCountry(country);
            dataViewModel.setDocId(docId);
            dataViewModel.setGender(gender);
            dataViewModel.setDocType(docType);
            dataViewModel.setImageUrl(imageUrl);

            dataViewModel.setCheckInDate(ChInDate);
            dataViewModel.setCheckOutDate(ChOutDate);

            dataViewModel.setSelectedRoom(noroom);
            dataViewModel.setSelectedPerson(noperson);
            dataViewModel.setDuration(duration);
        } else {
            restoreViewModelData();
        }



        selectDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dataViewModel.getSelectedItemPosition() >= 0 && dataViewModel.getSelectedItemPosition() < recItemRoomList.size()) {

                    String title = roomType.getText().toString();
                    String description = roomDesc.getText().toString();
                    String price = roomPrice.getText().toString();
                    String tax = roomTax.getText().toString();
                    String otFacTitle=roomOFTitle;
                    String otFacPrice = roomOtherFac.getText().toString();
                    String total = roomTotal.getText().toString();

                    // Store data in ViewModel
                    dataViewModel.setRoomType(title);
                    dataViewModel.setRoomDesc(description);
                    dataViewModel.setRoomPrice(price);
                    dataViewModel.setRoomTax(tax);
                    dataViewModel.setRoomOtherFac(otFacPrice);
                    dataViewModel.setRoomTotal(total);

                    Fragment fragment = new ContactDetailsFragment();
                    Bundle args = new Bundle();
                    args.putString("roomfType", title);
                    args.putString("roomDesc", description);
                    args.putString("roomfPrice", price);
                    args.putString("roomfTax", tax);
                    args.putString("roomfOtFacTitle",otFacTitle);
                    args.putString("roomfOtFacPrice", otFacPrice);
                    args.putString("total", total);
                    Log.d("roomFtotal", total);

                    // Retrieve data from ViewModel
                    String fname = dataViewModel.getFname();
                    String lname = dataViewModel.getLname();
                    String email = String.valueOf(dataViewModel.getEmail());
                    String mobile = dataViewModel.getMobile();
                    String address = dataViewModel.getAddress();
                    String city = dataViewModel.getCity();
                    String country = dataViewModel.getCountry();
                    String docId = dataViewModel.getDocId();
                    String gender = dataViewModel.getGender();
                    String docType = dataViewModel.getDocType();
                    String imageUrl = dataViewModel.getImageUrl();

                    String ChInDate = dataViewModel.getCheckInDate();
                    String ChOutDate = dataViewModel.getCheckOutDate();

                    Log.d("roomCindate", ChInDate);
                    Log.d("roomCoutndate", ChOutDate);
                    Log.d("RfgetusrDetails",fname+lname+email+mobile+city+address+docId+docType+gender+imageUrl);

                    String noroom = dataViewModel.getSelectedRoom();
                    String noperson = dataViewModel.getSelectedPerson();
                    String duration = dataViewModel.getDuration();


                    args.putString("fname",fname);
                    args.putString("lname",lname);
                    args.putString("mobile",mobile);
                    args.putString("address",address);
                    args.putString("email",email);
                    args.putString("city",city);
                    args.putString("country",country);
                    args.putString("docId",docId);
                    args.putString("gender",gender);
                    args.putString("docType",docType);
                    args.putString("imageUrl",imageUrl);

                    args.putString("chindate", ChInDate);
                    args.putString("choutdate", ChOutDate);

                    args.putString("noroom", String.valueOf(noroom));
                    args.putString("rfduration", String.valueOf(duration));
                    args.putString("rfnoperson", String.valueOf(noperson));

                    args.putAll(bundle);

                    fragment.setArguments(args);
                    AppCompatActivity activity = (AppCompatActivity) getActivity();

                    if (activity != null) {
                        // Access the TabLayout from the activity reference
                        TabLayout tabLayout = activity.findViewById(R.id.include);

                        // Select the desired tab position
                        if (tabLayout != null) {
                            TabLayout.Tab tab = tabLayout.getTabAt(2);
                            if (tab != null) {
                                tab.select();
                            }
                        }
                    }

                    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragmentcontainer, fragment);
                    transaction.commit();


                } else {
                    Log.e("RoomFragment", "Invalid selectedItemPosition: " + selectedItemPosition);
                }

            }
        });
        restoreViewModelData();

        return v;
    }


    @Override
    public void onItemClick(RecItemRoom item) {
        // Find the position of the clicked item in the list
//        selectedItemPosition = recItemRoomList.indexOf(item);

        dataViewModel.setSelectedItemPosition(recItemRoomList.indexOf(item));
        dataViewModel.setRoomType(item.getRoomType());
        dataViewModel.setRoomDesc(item.getRoomDescription());
        dataViewModel.setRoomPrice(String.valueOf(item.getRoomPrice()));
        dataViewModel.setRoomTax(String.valueOf(item.getRoomTax()));
        dataViewModel.setRoomOtherFac("0");

        updateViews();

//        String title = item.getRoomType();
//        String description = item.getRoomDescription();
//        String price = String.valueOf(item.getRoomPrice());
//        String tax = String.valueOf(item.getRoomTax());
//
//        roomType.setText(title);
//        roomDesc.setText(description);
//        roomPrice.setText(price);
//        roomTax.setText(tax);
//
//        rPrice = price;
//        rTax = tax;
//        roomOtherFac.setText("0");
        calculateTotalAmount();


        Log.d("RoomFragment", "Item clicked: " + item.getRoomType());


    }

    @Override
    public void onItemClick(RecItOtFacRoom item, boolean isChecked) {
//        selectedItemPosition = recItOtFacRoomList.indexOf(item);

        dataViewModel.setSelectedItemPosition(recItOtFacRoomList.indexOf(item));

        double itemPrice = Double.valueOf(item.getExtraFacilityPrice());

        if (item.isChecked()) {
//            totalAmount += Double.valueOf(item.getExtraFacilityPrice());
            cbox += item.getExtraFacilityName() + "\n"; // Add item name to the string
            roomOFTitle=cbox;
//            roomOtherFac.setText(String.valueOf(totalAmount));

            dataViewModel.setTotalAmount(dataViewModel.getTotalAmount() + itemPrice);
            dataViewModel.setRoomOtherFac(dataViewModel.getRoomOtherFac() + item.getExtraFacilityName() + "\n");

            calculateTotalAmount();
        } else {
//            totalAmount -= Double.valueOf(item.getExtraFacilityPrice());
            cbox = cbox.replace(item.getExtraFacilityName() + "\n", ""); // Remove item name from the string
            dataViewModel.setTotalAmount(dataViewModel.getTotalAmount() - itemPrice);
            dataViewModel.setRoomOtherFac(dataViewModel.getRoomOtherFac().replace(item.getExtraFacilityName() + "\n", ""));
            if (!TextUtils.isEmpty(cbox)) {
                roomOFTitle=cbox;
            } else {
                roomOFTitle=""; // Set empty text if cbox is empty
            }
            calculateTotalAmount();
        }
        updateViews();
        calculateTotalAmount();

    }

    private void updateViews() {
        roomType.setText(dataViewModel.getRoomType());
        roomDesc.setText(dataViewModel.getRoomDesc());
        roomPrice.setText(dataViewModel.getRoomPrice());
        roomTax.setText(dataViewModel.getRoomTax());
        roomOFTitle=dataViewModel.getRoomType();
        roomOtherFac.setText(String.valueOf(dataViewModel.getTotalAmount()));
    }

    private void restoreViewModelData() {
        roomType.setText(dataViewModel.getRoomType());
        roomDesc.setText(dataViewModel.getRoomDesc());
        roomPrice.setText(dataViewModel.getRoomPrice());
        roomTax.setText(dataViewModel.getRoomTax());
        roomOFTitle=dataViewModel.getRoomType();
        roomOtherFac.setText(String.valueOf(dataViewModel.getTotalAmount()));
        roomTotal.setText(dataViewModel.getRoomTotal());
    }



    public void calculateTotalAmount() {
        double roomPriceValue;
        int taxValue;
        double otherFacilityPrice;

        // Initialize bundle here
        bundle = getArguments();

        // Ensure bundle is not null
        if (bundle == null) {
            bundle = getArguments();
        }

        try {
            roomPriceValue = Double.parseDouble(roomPrice.getText().toString());
//            Toast.makeText(getContext(), String.valueOf(roomPriceValue), Toast.LENGTH_SHORT).show();
        } catch (NumberFormatException e) {
            roomPriceValue = 0.0;
        }

        try {
            taxValue = Integer.parseInt(roomTax.getText().toString());
            Toast.makeText(getContext(), String.valueOf(taxValue), Toast.LENGTH_SHORT).show();
        } catch (NumberFormatException e) {
            taxValue = 0;
        }

        try {
            otherFacilityPrice = Double.parseDouble(roomOtherFac.getText().toString());
//            Toast.makeText(getContext(), String.valueOf(otherFacilityPrice), Toast.LENGTH_SHORT).show();
        } catch (NumberFormatException e) {
            otherFacilityPrice = 0.0;
        }

        if (bundle!=null) {
            Double noroom = Double.valueOf(bundle.getString("SeRoom"));
            Double noperson = Double.valueOf(bundle.getString("SePerson"));
            String duration = bundle.getString("duration");

            // Remove all non-numeric characters from the string
            String numericDays = duration.replaceAll("[^\\d.]", "");

            // Convert the numeric string to an integer
            Double numberOfDays = Double.valueOf(Integer.parseInt(numericDays));

            Double Amount = roomPriceValue * noroom * numberOfDays;

            // Calculate the tax amount
            double taxAmount = Amount * ((double) taxValue / 100);

            Toast.makeText(getContext(), "Tax Amount : "+String.valueOf(taxAmount), Toast.LENGTH_SHORT).show();

            Amount+=otherFacilityPrice;

            Double totalAmount = Amount + taxAmount;

            roomTotal.setText(String.valueOf(totalAmount));

            dataViewModel.setRoomTotal(String.valueOf(totalAmount));

//            Double Amount = roomPriceValue + numberOfDays + noroom;
//
//            // Calculate the tax amount
//            double taxAmount = Amount * (taxValue / 100);
//
//            Amount+=otherFacilityPrice;
//
//            Double totalAmount = Amount + taxAmount;

        }

    }
}