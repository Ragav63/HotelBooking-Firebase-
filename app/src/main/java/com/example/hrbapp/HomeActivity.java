package com.example.hrbapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class HomeActivity extends AppCompatActivity implements PaymentFragment.OnPaymentCompleteListener{

    TabLayout tabLayout;
    TabItem sDates, sRooms, cDetails, confirmDetails, mPayment;
    FragmentManager fragmentManager;
    UserData userData; // Add this line to declare the UserData variable
    Bundle args;
    ImageButton menuIcon;

    private static final String PREFS_NAME = "UserDataPrefs";
    private static final String USER_DATA_KEY = "userData";

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save any necessary data to the outState bundle
        outState.putSerializable("userData", userData);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore the saved data from savedInstanceState bundle
        if (savedInstanceState != null) {
            userData = (UserData) savedInstanceState.getSerializable("userData");
            if (userData != null) {
                saveUserDataToPrefs(userData);
            }
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        userData = loadUserDataFromPrefs();

        // Receive UserData from the Intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("userData")) {
            userData = (UserData) intent.getSerializableExtra("userData");
        }

        if (userData != null) {

            args = new Bundle();
            args.putString("fname",userData.getFirstName());
            args.putString("lname",userData.getLastName());
            args.putString("mobile",userData.getMobile());
            args.putString("address",userData.getAddress());
            args.putString("email",userData.getEmail());
            args.putString("city",userData.getCity());
            args.putString("country",userData.getCountry());
            args.putString("docId",userData.getDocumentId());
            args.putString("gender",userData.getGender());
            args.putString("docType",userData.getDocType());
            args.putString("imageUrl",userData.getImageUrl());

            String fname=userData.getFirstName();
            String lname=userData.getLastName();
            String mobile=userData.getMobile();
            String address=userData.getAddress();
            String email=userData.getEmail();

            Log.d("HomeActivity", "Received userData: " + userData.toString());
            Log.d("HomeAct","Received all data"+fname+lname+mobile+address);
            // Use the userData object as needed
        }
        else {
            args = getIntent().getExtras();
            Log.d("HomeActivity", "No userData received");
            String fname =args.getString("fname");
            String lname=args.getString("lname");
            String email=args.getString("email");
            String mobile=args.getString("mobile");
            String address=args.getString("address");
            String city=args.getString("city");
            String country=args.getString("country");
            String docId=args.getString("docId");
            String gender=args.getString("gender");
            String docType=args.getString("docType");
            String imageUrl=args.getString("imageUrl");

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

            Log.d("HomeAct","Received all data"+fname+lname+mobile+address);

            Toast.makeText(this, "Received data from PaymentFragment or else"+fname+lname+mobile+address+email+city+country+docId+gender+docType+imageUrl, Toast.LENGTH_SHORT).show();
       }

        sDates=findViewById(R.id.selectDates);
        sRooms=findViewById(R.id.selectRooms);
        cDetails=findViewById(R.id.contactDetails);
        confirmDetails=findViewById(R.id.confirmDetails);
        mPayment=findViewById(R.id.makePayment);

        menuIcon=findViewById(R.id.settingsImgBtn);

        tabLayout = findViewById(R.id.include);

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });

//        setupTabs();

//        fragmentContainer = findViewById(R.id.fragmentcontainer);
        fragmentManager = getSupportFragmentManager();

        // Replace the initial fragment
        replaceFragment(new DateFragment(),args);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                Fragment changeFragment=null;
//                viewPager.setCurrentItem(tab.getPosition());

                int position=tab.getPosition();

                switch (position){
                    case 0:
                        replaceFragment(new DateFragment(),args);
                        tabLayout.getTabAt(0).select();
                        break;
                    case 1:
                        replaceFragment(new RoomFragment(),args);
                        tabLayout.getTabAt(1).select();
                        break;
                    case 2:
                        replaceFragment(new ContactDetailsFragment(),args);
                        tabLayout.getTabAt(2).select();
                        break;
                    case 3:
                        replaceFragment(new ConfirmDetailsFragment(),args);
                        tabLayout.getTabAt(3).select();
                        break;
                    default:
                        replaceFragment(new PaymentFragment(),args);
                        break;
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

//    private void setupTabs() {
//        String[] tabTitles = {"Select Dates", "Select Rooms", "Contact Details", "Confirm Details", "Make Payment"};
//
//        for (String title : tabTitles) {
//            TabLayout.Tab tab = tabLayout.newTab();
//            tab.setCustomView(createTabView(title));
//            tabLayout.addTab(tab);
//        }
//    }
//
//    private View createTabView(String title) {
//        View view = getLayoutInflater().inflate(R.layout.custom_tab, null);
//        TextView textView = view.findViewById(R.id.tabTitle);
//        textView.setText(title);
//        return view;
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.toolbar_settings_menu_items, menu);
//
//        // Set the username as the title for the profile menu item
//        MenuItem profileMenuItem = menu.findItem(R.id.menu_userProfile);
//        if (userData != null) {
//            profileMenuItem.setTitle(userData.getFirstName());
//        }
//
//        return true;
//    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);

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
                    Toast.makeText(HomeActivity.this, "My Bookings", Toast.LENGTH_SHORT).show();
                    Intent myBk=new Intent(getApplicationContext(), MyBookingsActivity.class);
                    String email=args.getString("email");
                    Log.e("homembmenuitem", email);
                    myBk.putExtra("email", email);
                    startActivity(myBk);
                    finish();//to finish the current activity

                } else if (item.getItemId() == R.id.menu_userLogout) {
                    Toast.makeText(HomeActivity.this, "User Logout Sucessfully", Toast.LENGTH_SHORT).show();
                    Intent logoutIntent=new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(logoutIntent);
                    finish();//to finish the current activity
                }

                return false;
            }
        });

        popupMenu.inflate(R.menu.toolbar_settings_menu_items);

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


    // Method to replace fragment in the FrameLayout
    private void replaceFragment(Fragment fragment, Bundle args) {

        if (args != null) {
            fragment.setArguments(args); // Set the bundle as fragment arguments
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentcontainer, fragment);
        transaction.commit();


        // Get the tab position based on the fragment
//        int tabPosition = getTabPosition(fragment);

        selectTabByFragment(fragment);
//        // Select the corresponding tab
//        if (tabPosition != -1) {
//            tabLayout.getTabAt(tabPosition).select();
//        }
    }

    public void selectTabByFragment(Fragment fragment) {
        int tabPosition = getTabPosition(fragment);
        if (tabPosition != -1) {
            tabLayout.getTabAt(tabPosition).select();
        }
    }

    // Method to get the tab position based on the fragment
    private int getTabPosition(Fragment fragment) {
        if (fragment instanceof DateFragment) {
            return 0;
        } else if (fragment instanceof RoomFragment) {
            return 1;
        } else if (fragment instanceof ContactDetailsFragment) {
            return 2;
        } else if (fragment instanceof ConfirmDetailsFragment) {
            return 3;
        } else if (fragment instanceof PaymentFragment) {
            return 4;
        }
        return -1;
    }

    @Override
    public void onPaymentComplete(Bundle args) {
        Intent intent = new Intent(this, DisplayActivity.class);
        intent.putExtras(args);
        startActivity(intent);
        finish();
    }

    private void saveUserDataToPrefs(UserData userData) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(USER_DATA_KEY, userData.toString());
        editor.apply();
    }

    private UserData loadUserDataFromPrefs() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String userDataString = prefs.getString(USER_DATA_KEY, null);
        if (userDataString != null) {
            return UserData.fromString(userDataString); // Assuming UserData has a fromString method
        }
        return null;
    }
}


