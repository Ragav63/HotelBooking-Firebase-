package com.example.hrbapp;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ConfirmDetailsFragment extends Fragment {

    Bundle bundle;
    private EditText nameEdt, numberEdt, roomTypeEdt, roomNos, roomPriceEdt, taxEdt, exChEdt, discountEdt, disAmEdt, totalEdt, grandTotalEdt;
    private Spinner disCouponSpinner;
    private AppCompatButton makePaymentBtn;
    private DatabaseReference couponsRef;

    private List<Coupon> coupons = new ArrayList<>();
    private CustomSpinAdapterFragDiscounts adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_confirm_details, container, false);

        couponsRef = FirebaseDatabase.getInstance().getReference("PromocodeDiscounts");

        bundle = getArguments();

        if (bundle != null) {
            // Retrieve data from the bundle

            String fname =bundle.getString("fname");
            String lname=bundle.getString("lname");
            String email=bundle.getString("email");
            String mobile=bundle.getString("mobile");
            String address=bundle.getString("address");
            String city=bundle.getString("city");
            String country=bundle.getString("country");
            String docId=bundle.getString("docId");
            String gender=bundle.getString("gender");
            String docType=bundle.getString("docType");
            String imageUrl=bundle.getString("imageUrl");

            String bookerFirstName =bundle.getString("firstName");
            String bookerLastName =bundle.getString("lastName");
            String bookerAddress=bundle.getString("bookerAddress");
            String bookerPostCode=bundle.getString("postCode");
            String bookerMobileNo=bundle.getString("bookerMobileNo");

            String ChInDate = bundle.getString("chindate");
            String ChOutDate = bundle.getString("choutdate");

//            Log.d("condetailsCindate", ChInDate);
//            Log.d("condetailsCoutndate", ChOutDate);

            String noroom = bundle.getString("noroom");
            String noperson = bundle.getString("rfnoperson");
            String duration = bundle.getString("rfduration");

            String roomfType = bundle.getString("roomfType");
            String roomDesc = bundle.getString("roomDesc");
            String roomfPrice = bundle.getString("roomfPrice");
            String roomfTax = bundle.getString("roomfTax");
            String otFacTitle = bundle.getString("roomfOtFacTitle");
            String roomfOtFacPrice = bundle.getString("roomfOtFacPrice");
            String total = bundle.getString("total");


            // Initialize EditTexts
            nameEdt = v.findViewById(R.id.conNameEdt);
            numberEdt = v.findViewById(R.id.conPhnumEdt);
            roomTypeEdt = v.findViewById(R.id.conRoomTypeEdt);
            roomNos = v.findViewById(R.id.conRoomNoEdt);
            roomPriceEdt = v.findViewById(R.id.conRoomPriceEdt);
            taxEdt = v.findViewById(R.id.conTaxEdt);
            exChEdt = v.findViewById(R.id.conExchargeEdt);
            discountEdt = v.findViewById(R.id.conDiscountPerEdt);
            disAmEdt = v.findViewById(R.id.conDiscountAmEdt);
            totalEdt = v.findViewById(R.id.conTotalEdt);
            grandTotalEdt = v.findViewById(R.id.conGrandtotalEdt);

            disCouponSpinner = v.findViewById(R.id.spinner_discoupons);

            makePaymentBtn = v.findViewById(R.id.makePaymentBtn);

            nameEdt.setText(bookerFirstName);
            numberEdt.setText(bookerMobileNo);
            roomTypeEdt.setText(roomfType);
            roomNos.setText(noroom);
            roomPriceEdt.setText(roomfPrice);
            taxEdt.setText(roomfTax);
            exChEdt.setText(roomfOtFacPrice);
            totalEdt.setText(total);


            // Make emailEdt non-editable
            roomTypeEdt.setFocusable(false);
            roomTypeEdt.setClickable(false);
            roomTypeEdt.setCursorVisible(false);

            // Make emailEdt non-editable
            roomNos.setFocusable(false);
            roomNos.setClickable(false);
            roomNos.setCursorVisible(false);

            // Make emailEdt non-editable
            roomPriceEdt.setFocusable(false);
            roomPriceEdt.setClickable(false);
            roomPriceEdt.setCursorVisible(false);

            // Make emailEdt non-editable
            taxEdt.setFocusable(false);
            taxEdt.setClickable(false);
            taxEdt.setCursorVisible(false);

            // Make emailEdt non-editable
            exChEdt.setFocusable(false);
            exChEdt.setClickable(false);
            exChEdt.setCursorVisible(false);

            // Make emailEdt non-editable
            totalEdt.setFocusable(false);
            totalEdt.setClickable(false);
            totalEdt.setCursorVisible(false);

            // Make emailEdt non-editable
            discountEdt.setFocusable(false);
            discountEdt.setClickable(false);
            discountEdt.setCursorVisible(false);

            // Make emailEdt non-editable
            disAmEdt.setFocusable(false);
            disAmEdt.setClickable(false);
            disAmEdt.setCursorVisible(false);

            // Make emailEdt non-editable
            grandTotalEdt.setFocusable(false);
            grandTotalEdt.setClickable(false);
            grandTotalEdt.setCursorVisible(false);



            int textColor = getResources().getColor(R.color.fragbackground);


            // Retrieve data from Firebase
            retrieveCoupons();

            adapter = new CustomSpinAdapterFragDiscounts(requireContext(), android.R.layout.simple_spinner_item, coupons, textColor);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            disCouponSpinner.setAdapter(adapter);


            disCouponSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Coupon selectedCoupon = (Coupon) disCouponSpinner.getSelectedItem();
                    if (selectedCoupon != null) {
                        int discountPercentage = selectedCoupon.getPromoDiscountPercent();
                        discountEdt.setText(String.valueOf(discountPercentage));

                        // Calculate discount amount
                        Double discountAmount = (discountPercentage / 100.0) * Double.valueOf(totalEdt.getText().toString());

                        // Calculate discounted total amount
                        Double discountedTotal = Double.valueOf(totalEdt.getText().toString()) - discountAmount;

                        int disAm = (int) Math.round(discountAmount);
                        int disTot = (int) Math.round(discountedTotal);

                        // Set the calculated values to corresponding EditText fields
                        disAmEdt.setText(String.valueOf(disAm));
                        grandTotalEdt.setText(String.valueOf(disTot));
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            makePaymentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Fragment fragment = new PaymentFragment();
                    Bundle args = new Bundle();

                    args.putString("chindate", ChInDate);
                    args.putString("choutdate", ChOutDate);

                    int noofroom=(int)Double.parseDouble(noroom);
                    int noofperson=(int)Double.parseDouble(noperson);

                    args.putString("noroom", String.valueOf(noofroom));
                    args.putString("rfduration", String.valueOf(duration));
                    args.putString("rfnoperson", String.valueOf(noofperson));

                    args.putString("roomfType", String.valueOf(roomfType));
                    args.putString("roomDesc", roomDesc);
                    args.putString("roomfPrice", String.valueOf(roomfPrice));
                    args.putString("roomfTax", String.valueOf(roomfTax));
                    args.putString("roomfOtFacTitle", otFacTitle);
                    args.putString("roomfOtFacPrice", String.valueOf(roomfOtFacPrice));

                    args.putString("firstName", nameEdt.getText().toString());
                    args.putString("lastName", bookerLastName);
                    args.putString("bookerAddress", bookerAddress);
                    args.putString("postCode", bookerPostCode);
                    args.putString("bookerMobileNo", numberEdt.getText().toString());

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

                    args.putString("total", total);

                    Coupon selectedCoupon=(Coupon)disCouponSpinner.getSelectedItem();

                    args.putString("coupon", String.valueOf(selectedCoupon));
                    args.putString("discount", discountEdt.getText().toString());
                    args.putString("discountAm", disAmEdt.getText().toString());
                    args.putString("grandTotal", grandTotalEdt.getText().toString());

                    // Display the details in a default AlertDialog
                    displayDetailsDialog();

                    fragment.setArguments(args);

                    AppCompatActivity activity = (AppCompatActivity) getActivity();

                    if (activity != null) {
                        // Access the TabLayout from the activity reference
                        TabLayout tabLayout = activity.findViewById(R.id.include);

                        // Select the desired tab position
                        if (tabLayout != null) {
                            TabLayout.Tab tab = tabLayout.getTabAt(4);
                            if (tab != null) {
                                tab.select();
                            }
                        }
                    }

                    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragmentcontainer, fragment);
                    transaction.commit();
                }
            });

        } else {
            Log.e("ConfirmDetailsFragment", "Bundle is null");
        }

        return v;

    }

    private void retrieveCoupons() {
        couponsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                coupons.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Coupon coupon = snapshot.getValue(Coupon.class);
                    coupons.add(coupon);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ConfirmDetailsFragment", "Failed to read coupons", databaseError.toException());
            }
        });
    }

    private boolean validateFields() {
        // Check if any of the fields are empty
        return !TextUtils.isEmpty(nameEdt.getText())
                && !TextUtils.isEmpty(roomTypeEdt.getText())
                && !TextUtils.isEmpty(roomPriceEdt.getText())
                && !TextUtils.isEmpty(taxEdt.getText())
                && !TextUtils.isEmpty(exChEdt.getText())
                && !TextUtils.isEmpty(discountEdt.getText())
                && !TextUtils.isEmpty(disAmEdt.getText())
                && !TextUtils.isEmpty(totalEdt.getText())
                && !TextUtils.isEmpty(grandTotalEdt.getText());
    }


    private void displayDetailsDialog() {
        // Build the AlertDialog with the entered details
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Payment Details");

        // Construct the message string with the entered details
        StringBuilder message = new StringBuilder();

        bundle = getArguments();

        String ChInDate = bundle.getString("chindate");
        String ChOutDate = bundle.getString("choutdate");

        String noroom = bundle.getString("noroom");
        String noperson = bundle.getString("rfnoperson");
        String duration = bundle.getString("rfduration");

        int noofroom=(int)Double.parseDouble(noroom);
        int noofperson=(int)Double.parseDouble(noperson);

        String roomfType = bundle.getString("roomfType");
        String roomDesc = bundle.getString("roomDesc");
        String roomfPrice = bundle.getString("roomfPrice");
        String roomfTax = bundle.getString("roomfTax");
        String roomfOtFacPrice = bundle.getString("roomfOtFacPrice");
        String total = bundle.getString("total");

//        Log.d("getTotal",total);
        String firstName = bundle.getString("firstName");
        String lastName = bundle.getString("lastName");
        String email = bundle.getString("email");
        String address = bundle.getString("bookerAddress");
        String postCode = bundle.getString("bookerPostCode");
        String mobileNo = bundle.getString("bookerMobileNo");

        Coupon selectedCoupon = (Coupon) disCouponSpinner.getSelectedItem();
        String discount = discountEdt.getText().toString();
        String discountAm = disAmEdt.getText().toString();
        String grandTotal = grandTotalEdt.getText().toString();

        message.append("Check In Date: ").append(ChInDate).append("\n");
        message.append("Check Out Date: ").append(ChOutDate).append("\n");

        message.append("Selected Room: ").append(noofroom).append("\n");
        message.append("No of Person: ").append(noofperson).append("\n");
        message.append("Duration: ").append(duration).append("\n");

        message.append("Room Type: ").append(roomfType).append("\n");
        message.append("Room Description").append(roomDesc).append("\n");
        message.append("Room Price: ").append(roomfPrice).append("\n");
        message.append("Other Facility: ").append(roomfOtFacPrice).append("\n");
        message.append("Tax: ").append(roomfTax).append("\n");

        message.append("Total: ").append(total).append("\n");

        message.append("First Name: ").append(firstName).append("\n");
        message.append("Last Name: ").append(lastName).append("\n");
        message.append("Email: ").append(email).append("\n");
        message.append("Address: ").append(address).append("\n");
        message.append("Postal Code: ").append(postCode).append("\n");
        message.append("Mobile Number: ").append(mobileNo).append("\n");

        message.append("Discount Coupon: ").append(selectedCoupon).append("\n");
        message.append("Discount: ").append(discount).append("\n");
        message.append("Discount Amount: ").append(discountAm).append("\n");
        message.append("Grand Total: ").append(grandTotal).append("\n");


        // Set the message of the AlertDialog
        builder.setMessage(message.toString());

        // Add a button to close the dialog
        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Show the AlertDialog
        builder.create().show();
    }
}