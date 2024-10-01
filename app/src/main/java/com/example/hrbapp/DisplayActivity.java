package com.example.hrbapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class DisplayActivity extends AppCompatActivity {

    // Retrieve the bundle data from the intent
    Bundle bundle;
    UserData userData;
    ImageButton menuIcon;
    private TextView chInValTv, chOutValTv, noOfRoomValTv, noOfPersonValTv, noOfDaysValTv,
            roomTitleValTv, roomDescValTv, roomPriceValTv, roomTaxValTv, roomOFPrice, roomTotalAmValTv,
            firstNameValTv, lastNameValTv, emailValTv, addressValTv, postCodeValTv, mobNoValTv,
            disCouponValTv, disPercentValTv, disAmountValTv, ccNameValTv, ccNumValTv, cvvValTv, expOnValTv,
            grandTotalValTv;
    private AppCompatButton bookAgainBtn, downloadReceiptBtn;
//    private DatabaseReference dbReferenceUser;
//    private String userId = "User LoginDetails"; // Replace with the actual user ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_display);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        menuIcon = findViewById(R.id.settingsImgBtn);

        chInValTv = findViewById(R.id.chInTvVal);
        chOutValTv = findViewById(R.id.chOutTvVal);
        noOfRoomValTv = findViewById(R.id.noOfRoomTvVal);
        noOfPersonValTv = findViewById(R.id.noOfPersonTvVal);
        noOfDaysValTv = findViewById(R.id.noOfDaysTvVal);
        roomTitleValTv = findViewById(R.id.roomTypeTvVal);
        roomDescValTv = findViewById(R.id.roomDescriptionTvVal);
        roomPriceValTv = findViewById(R.id.roomPriceTvVal);
        roomTaxValTv = findViewById(R.id.roomTaxTvVal);
        roomOFPrice = findViewById(R.id.roomOtherFacPriceTvVal);
        roomTotalAmValTv = findViewById(R.id.roomTotalAmTvVal);
        firstNameValTv = findViewById(R.id.firstNameTvVal);
        lastNameValTv = findViewById(R.id.lastNameTvVal);
        emailValTv = findViewById(R.id.emailTvVal);
        addressValTv = findViewById(R.id.addressTvVal);
        postCodeValTv = findViewById(R.id.postCodeTvVal);
        mobNoValTv = findViewById(R.id.mobNumTvVal);
        disCouponValTv = findViewById(R.id.disCouponTvVal);
        disPercentValTv = findViewById(R.id.disPercentTvVal);
        disAmountValTv = findViewById(R.id.disAmountTvVal);
        ccNameValTv = findViewById(R.id.ccNameTvVal);
        ccNumValTv = findViewById(R.id.ccNumberTvVal);
        cvvValTv = findViewById(R.id.cvvTvVal);
        expOnValTv = findViewById(R.id.expOnTvVal);

        grandTotalValTv = findViewById(R.id.grandTotalTvVal);

        bookAgainBtn = findViewById(R.id.bookAgainbtn);
        downloadReceiptBtn = findViewById(R.id.downloadRecbtn);

        bundle = getIntent().getExtras();


        if (bundle != null) {
            // Retrieve individual data items from the bundle
            String ChInDate = bundle.getString("chindate");
            String ChOutDate = bundle.getString("choutdate");

            String noroom = bundle.getString("noroom");
            String noperson = bundle.getString("rfnoperson");
            String duration = bundle.getString("rfduration");

            String roomfType = bundle.getString("roomfType");
            String roomfPrice = bundle.getString("roomfPrice");
            String roomfTax = bundle.getString("roomfTax");
            String otFacTitle = bundle.getString("roomfOtFacTitle");
            String roomfOtFacPrice = bundle.getString("roomfOtFacPrice");
            String total = bundle.getString("total");

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

            String selectedCoupon = bundle.getString("coupon");
            String discount = bundle.getString("discount");
            String discountAm = bundle.getString("discountAm");
            String grandTotal = bundle.getString("grandTotal");

            String ccNumber = bundle.getString("ccNumber");
            String ccName = bundle.getString("ccName");
            String cvvNum = bundle.getString("cvvNum");
            String expDate = bundle.getString("expDate");


            // Example: Display the retrieved data in TextViews

            chInValTv.setText(ChInDate);
            chOutValTv.setText(ChOutDate);
            noOfRoomValTv.setText(noroom);
            noOfPersonValTv.setText(noperson);
            noOfDaysValTv.setText(duration);
            roomTitleValTv.setText(roomfType);
            roomDescValTv.setText(roomfPrice);
            roomPriceValTv.setText(roomfPrice);
            roomTaxValTv.setText(roomfTax);
            roomOFPrice.setText(roomfOtFacPrice);
            roomTotalAmValTv.setText(total);
            firstNameValTv.setText(bookerFirstName);
            lastNameValTv.setText(bookerLastName);
            emailValTv.setText(email);
            addressValTv.setText(bookerAddress);
            postCodeValTv.setText(bookerPostCode);
            mobNoValTv.setText(bookerMobileNo);
            disCouponValTv.setText(selectedCoupon);
            disPercentValTv.setText(discount);
            disAmountValTv.setText(discountAm);
            ccNameValTv.setText(ccName);
            ccNumValTv.setText(ccNumber);
            cvvValTv.setText(cvvNum);
            expOnValTv.setText(expDate);

            grandTotalValTv.setText(grandTotal);

            menuIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopupMenu(v);
                }
            });

            downloadReceiptBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    generatePDF();
                }
            });


            bookAgainBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Create an intent to start DisplayActivity
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);

                    // Create a new bundle and put all the required data into it
                    Bundle newBundle = new Bundle();


//                    newBundle.putString("fname", bundle.getString("fname"));
//                    newBundle.putString("lname", bundle.getString("lname"));
//                    newBundle.putString("email", bundle.getString("email"));
//                    newBundle.putString("mobile", bundle.getString("mobile"));
//                    newBundle.putString("address", bundle.getString("address"));
//                    newBundle.putString("city", bundle.getString("city"));
//                    newBundle.putString("country", bundle.getString("country"));
//                    newBundle.putString("docId", bundle.getString("docId"));
//                    newBundle.putString("gender", bundle.getString("gender"));
//                    newBundle.putString("docType", bundle.getString("docType"));
//                    newBundle.putString("imageUrl", bundle.getString("imageUrl"));

                    Toast.makeText(DisplayActivity.this, "Thank You For Visiting!", Toast.LENGTH_SHORT).show();

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

                    newBundle.putString("fname",fname);
                    newBundle.putString("lname",lname);
                    newBundle.putString("mobile",mobile);
                    newBundle.putString("address",address);
                    newBundle.putString("email",email);
                    newBundle.putString("city",city);
                    newBundle.putString("country",country);
                    newBundle.putString("docId",docId);
                    newBundle.putString("gender",gender);
                    newBundle.putString("docType",docType);
                    newBundle.putString("imageUrl",imageUrl);

                    Toast.makeText(DisplayActivity.this, "Data Passing to Home Act from Dis Act"+fname+lname+mobile+address+email+city+country+docId+gender+docType+imageUrl, Toast.LENGTH_SHORT).show();

                    // Add the bundle to the intent
                    intent.putExtras(newBundle);

                    // Start the DisplayActivity using the intent
                    startActivity(intent);
                }
            });


        } else {
            Log.e("ConfirmDetailsFragment", "Bundle is null");
        }
    }

//    private void retrieveUserData() {
//
//    }

    private void showPopupMenu(View view) {

        // Retrieve user data from intent
//        Intent intent = getIntent();
//        if (intent != null && intent.hasExtra("userData")) {
//            userData = (UserData) intent.getSerializableExtra("userData");
//            if (userData != null) {
//                // Use the user data as needed
//                Log.d("DisplayActivity", "Received userData: " + userData.toString());
//                // Example: Set user's name to a TextView
//            }
//        }

        bundle = getIntent().getExtras();

        String email = bundle.getString("email");


        PopupMenu popupMenu = new PopupMenu(this, view);

        popupMenu.inflate(R.menu.toolbar_settings_menu_items);

        // Retrieve user data when the activity starts
//        retrieveUserData();

//        dbReferenceUser = FirebaseDatabase.getInstance().getReference(userId); // Adjust the path as needed
//
//
//
//        dbReferenceUser.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    UserData user = snapshot.getValue(UserData.class);
//                    if (user != null) {
//                        displayName=user.getFirstName();
//                    } else {
//                        Log.d("DisplayActivity", "User data is null");
//                    }
//                } else {
//                    Log.d("DisplayActivity", "User snapshot does not exist");
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.e("DisplayActivity", "Failed to read user data", error.toException());
//            }
//        });
//        // Inflate the popup menu
//        popupMenu.getMenuInflater().inflate(R.menu.toolbar_settings_menu_items, popupMenu.getMenu());
//
//        // Set the username as the title for the profile menu item in the popup menu
//        MenuItem profileMenuItem = popupMenu.getMenu().findItem(R.id.menu_userProfile);
//        if (userData != null) {
//            profileMenuItem.setTitle(userData.getFirstName());
//        }

        // Assuming you have a menu resource named toolbar_menu.xml
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getItemId() == R.id.menu_userBookingDetails) {
                    Toast.makeText(DisplayActivity.this, "My Bookings", Toast.LENGTH_SHORT).show();
                    Intent myBk = new Intent(getApplicationContext(), MyBookingsActivity.class);
                    Log.e("displaymbmenuitem", email);
                    myBk.putExtra("email", email);
                    startActivity(myBk);
                    finish();//to finish the current activity

                } else if (item.getItemId() == R.id.menu_userLogout) {
                    Toast.makeText(DisplayActivity.this, "User Logout Sucessfully", Toast.LENGTH_SHORT).show();
                    Intent logoutIntent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(logoutIntent);
                    finish();//to finish the current activity
                }

                return false;
            }
        });

//                try {
//                    Field fieldMPopup = PopupMenu.class.getDeclaredField("mPopup");
//                    fieldMPopup.setAccessible(true);
//                    Object mPopup = fieldMPopup.get(popupMenu);
//                    Class<?> popupClass = mPopup.getClass();
//                    Method setForceShowIcon = popupClass.getDeclaredMethod("setForceShowIcon", boolean.class);
//                    setForceShowIcon.invoke(mPopup, true);
//
//                } catch (Exception e) {
//
//                    Log.e("popup", "error", e);
//
//                } finally {
//                    popupMenu.show();
//                }
        popupMenu.show();
    }

    private void generatePDF() {
        // Create a new PDF document
        try {
            PdfWriter writer = new PdfWriter(new File(getExternalFilesDir(null), "receipt.pdf"));
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Add content to the PDF document
            document.add(new Paragraph("Check In Date: " + chInValTv.getText().toString()));
            document.add(new Paragraph("Check Out Date: " + chOutValTv.getText().toString()));


            document.add(new Paragraph("Selected Room: " + noOfRoomValTv.getText().toString()));
            document.add(new Paragraph("No of Person: " + noOfPersonValTv.getText().toString()));
            document.add(new Paragraph("Duration: " + noOfDaysValTv.getText().toString()));

            document.add(new Paragraph("Room Type: " + roomTitleValTv.getText().toString()));
            document.add(new Paragraph("Room Description" + roomDescValTv.getText().toString()));
            document.add(new Paragraph("Room Price: " + roomPriceValTv.getText().toString()));
            document.add(new Paragraph("Other Facility Price: " + roomOFPrice.getText().toString()));
            document.add(new Paragraph("Tax: " + roomTaxValTv.getText().toString()));

            document.add(new Paragraph("Total: " + roomTotalAmValTv.getText().toString()));

            document.add(new Paragraph("First Name: " + firstNameValTv.getText().toString()));
            document.add(new Paragraph("Last Name: " + lastNameValTv.getText().toString()));
            document.add(new Paragraph("Email: " + emailValTv.getText().toString()));
            document.add(new Paragraph("Address: " + addressValTv.getText().toString()));
            document.add(new Paragraph("Postal Code: " + postCodeValTv.getText().toString()));
            document.add(new Paragraph("Mobile Number: " + mobNoValTv.getText().toString()));

            document.add(new Paragraph("Discount Coupon" + disCouponValTv.getText().toString()));
            document.add(new Paragraph("Discount Percent: " + disPercentValTv.getText().toString()));
            document.add(new Paragraph("Discount Amount: " + disAmountValTv.getText().toString()));

            document.add(new Paragraph("Credit Card Number: " + ccNumValTv.getText().toString()));
            document.add(new Paragraph("Credit Card Name: " + ccNameValTv.getText().toString()));
            document.add(new Paragraph("CVV Number: " + cvvValTv.getText().toString()));
            document.add(new Paragraph("Expiry On: " + expOnValTv.getText().toString()));

            document.add(new Paragraph("Grand Total: " + grandTotalValTv.getText().toString()));

            Log.d("FilePath", "PDF File Path: " + new File(getExternalFilesDir(null), "receipt.pdf").getAbsolutePath());

            // Close the document
            document.close();

            Toast.makeText(DisplayActivity.this, "Receipt downloaded successfully!", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}