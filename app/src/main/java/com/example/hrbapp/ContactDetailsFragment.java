package com.example.hrbapp;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;


public class ContactDetailsFragment extends Fragment {

    private EditText fNameEdt, lNameEdt, emailEdt, fulladdEdt, postcodeEdt, phnumEdt;
    private AppCompatButton confirmDetailsBtn;
    Bundle bundle;
    private static final String ARG_USER_DATA = "userData";
    UserData userData;
    private DataViewModel viewModel;

    public ContactDetailsFragment() {
        // Required empty public constructor
    }

    // Define newInstance method
    public static ContactDetailsFragment newInstance(UserData userData) {
        ContactDetailsFragment fragment = new ContactDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER_DATA, userData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ARG_USER_DATA, userData);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize the ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(DataViewModel.class);
        if (getArguments() != null) {
            userData = (UserData) getArguments().getSerializable(ARG_USER_DATA);
//            bundle = getArguments();
//            // Set data to ViewModel from arguments
//            viewModel.firstName.setValue(bundle.getString("fname"));
//            viewModel.lastName.setValue(bundle.getString("lname"));
//            viewModel.email.setValue(bundle.getString("email"));
//            viewModel.address.setValue(bundle.getString("address"));
//            viewModel.mobile.setValue(bundle.getString("mobile"));
            if (userData != null) {
                viewModel.setFirstName(viewModel.getFirstName().toString());
                viewModel.setLastName(viewModel.getLastName().toString());
                viewModel.setEmail(viewModel.getEmail().toString());
                viewModel.setFullAddress(viewModel.getFullAddress().toString());
                viewModel.setPostCode(viewModel.getPostCode().toString());
                viewModel.setMobileNumber(viewModel.getMobileNumber().toString());
                Log.d("ContactDetailsFragment", "onCreate: Received userData: " + userData.toString());
            } else {
                Log.d("ContactDetailsFragment", "onCreate: userData is null after getArguments");
            }
        } else {
            Log.d("ContactDetailsFragment", "onCreate: getArguments is null");
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_contact_details, container, false);

        // Logging the userData to check if values are received correctly
//        if (userData != null) {
//            Log.d("ContactDetailsFragment", "Received userData: " + userData.toString());
//        }else {
//            Log.d("ContactDetailsFragment", "No userData received first check");
//        }

        bundle=getArguments();
        // Initialize EditTexts
        fNameEdt = v.findViewById(R.id.fnameEdt);
        lNameEdt = v.findViewById(R.id.lnameEdt);
        emailEdt = v.findViewById(R.id.emailEdt);
        fulladdEdt = v.findViewById(R.id.fulladdEdt);
        postcodeEdt = v.findViewById(R.id.postcodeEdt);
        phnumEdt = v.findViewById(R.id.phnumEdt);
//        bundle = getArguments();

        confirmDetailsBtn=v.findViewById(R.id.confirmDetailsBtn);

//        // Retrieve UserData object passed to the fragment
//        UserData userData = null;
//        if (bundle != null) {
//            userData = (UserData) bundle.getSerializable("userData");
//        }

        // Set UserData to EditText fields if available
//        if (userData!= null) {
//
//            String name=userData.getFirstName();
//          //  Log.d("data", "onCreateView: Received userData: " + userData);
//
//            fNameEdt.requestFocus();
//            if (fNameEdt != null) {
//                fNameEdt.setText(name);
//            } else {
//                Log.e("ContactDetailsFragment", "fNameEdt is null");
//            }
//            Toast.makeText(requireContext(), name, Toast.LENGTH_SHORT).show();
//
//            lNameEdt.setText(userData.getLastName());
//            emailEdt.setText(userData.getEmail());
//            fulladdEdt.setText(userData.getAddress());
//            phnumEdt.setText(userData.getMobile());
//            // Set other fields as needed
//        }else {
//            Log.d("ContactDetailsFragment", "onCreateView: No userData received");
//        }

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

        fNameEdt.setText(fname);
        lNameEdt.setText(lname);
        emailEdt.setText(email);
        phnumEdt.setText(mobile);
        fulladdEdt.setText(address);

        // Observe LiveData from ViewModel and update UI accordingly
        viewModel.getFirstName().observe(getViewLifecycleOwner(), s -> fNameEdt.setText(s));
        viewModel.getLastName().observe(getViewLifecycleOwner(), s -> lNameEdt.setText(s));
//        viewModel.getEmail().subSequence(getViewLifecycleOwner(), s -> emailEdt.setText(s));
        viewModel.getFullAddress().observe(getViewLifecycleOwner(), s -> fulladdEdt.setText(s));
        viewModel.getMobileNumber().observe(getViewLifecycleOwner(), s -> phnumEdt.setText(s));

        // Observe postcode LiveData and update UI
        viewModel.getPostCode().observe(getViewLifecycleOwner(), postcode -> {
            if (postcode != null) {
                postcodeEdt.setText(postcode);
            }
        });

//        emailEdt.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                // Not needed
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                // Not needed
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                // Check email validity after the user finishes typing
//                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
//                    emailEdt.setError("Invalid email format");
//                } else {
//                    emailEdt.setError(null);
//                }
//            }
//        });


        // Make emailEdt non-editable
        emailEdt.setFocusable(false);
        emailEdt.setClickable(false);
        emailEdt.setCursorVisible(false);

        confirmDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if all fields are not empty
                if (validateFields()) {
                    String bookerFirstName =  fNameEdt.getText().toString();
                    String bookerLastName = lNameEdt.getText().toString();
                    String email =  emailEdt.getText().toString();
                    String bookerAddress=fulladdEdt.getText().toString();
                    String bookerPostCode=postcodeEdt.getText().toString();
                    String bookerMobileNo=phnumEdt.getText().toString();

//                    viewModel.setPostCode(bookerPostCode);
                    viewModel.setFirstName(fNameEdt.getText().toString());
                    viewModel.setLastName(lNameEdt.getText().toString());
                    viewModel.setEmail(emailEdt.getText().toString());
                    viewModel.setFullAddress(fulladdEdt.getText().toString());
                    viewModel.setPostCode(postcodeEdt.getText().toString());
                    viewModel.setMobileNumber(phnumEdt.getText().toString());

                    Fragment fragment = new ConfirmDetailsFragment();
                    Bundle args = new Bundle();

                    if (bundle!=null) {
                        String ChInDate = bundle.getString("chindate");
                        String ChOutDate = bundle.getString("choutdate");

                        Log.e("cdetailsCindate", ChInDate);
                        Log.e("cdetailsCoutndate", ChOutDate);

                        Double noroom = Double.valueOf(bundle.getString("noroom"));
                        Double noperson = Double.valueOf(bundle.getString("rfnoperson"));
                        String duration = bundle.getString("rfduration");
                        String title = bundle.getString("roomfType");
                        String roomDesc = bundle.getString("roomDesc");
                        String price = bundle.getString("roomfPrice");
                        String tax = bundle.getString("roomfTax");
                        String otFacTitle = bundle.getString("roomfOtFacTitle");
                        String otFacPrice = bundle.getString("roomfOtFacPrice");
                        String total = bundle.getString("total");

                        args.putString("chindate", ChInDate);
                        args.putString("choutdate", ChOutDate);

                        args.putString("noroom", String.valueOf(noroom));
                        args.putString("rfduration", String.valueOf(duration));
                        args.putString("rfnoperson", String.valueOf(noperson));

                        args.putString("roomfType", String.valueOf(title));
                        args.putString("roomDesc", roomDesc);
                        args.putString("roomfPrice", String.valueOf(price));
                        args.putString("roomfTax", String.valueOf(tax));
                        args.putString("roomfOtFacTitle", otFacTitle);
                        args.putString("roomfOtFacPrice", String.valueOf(otFacPrice));

                        args.putString("firstName", bookerFirstName);
                        args.putString("lastName", bookerLastName);
                        args.putString("bookerAddress", bookerAddress);
                        args.putString("postCode", bookerPostCode);
                        args.putString("bookerMobileNo", bookerMobileNo);

                        args.putString("fname", fname);
                        args.putString("lname", lname);
                        args.putString("mobile", mobile);
                        args.putString("address", address);
                        args.putString("email", email);
                        args.putString("city", city);
                        args.putString("country", country);
                        args.putString("docId", docId);
                        args.putString("gender", gender);
                        args.putString("docType", docType);
                        args.putString("imageUrl", imageUrl);

                        args.putString("total", total);

                    } else {
                        // Retrieve data from ViewModel
                        String fname = viewModel.getFname();
                        String lname = viewModel.getLname();
                        email = String.valueOf(viewModel.getEmail());
                        String mobile = viewModel.getMobile();
                        String address = viewModel.getAddress();
                        String city = viewModel.getCity();
                        String country = viewModel.getCountry();
                        String docId = viewModel.getDocId();
                        String gender = viewModel.getGender();
                        String docType = viewModel.getDocType();
                        String imageUrl = viewModel.getImageUrl();

                        String ChInDate = viewModel.getCheckInDate();
                        String ChOutDate = viewModel.getCheckOutDate();

                        Log.d("roomCindate", ChInDate);
                        Log.d("roomCoutndate", ChOutDate);
                        Log.d("RfgetusrDetails",fname+lname+email+mobile+city+address+docId+docType+gender+imageUrl);

                        String noroom = viewModel.getSelectedRoom();
                        String noperson = viewModel.getSelectedPerson();
                        String duration = viewModel.getDuration();


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
                    }

                    fragment.setArguments(args);
                    AppCompatActivity activity = (AppCompatActivity) getActivity();

                    if (activity != null) {
                        // Access the TabLayout from the activity reference
                        TabLayout tabLayout = activity.findViewById(R.id.include);

                        // Select the desired tab position
                        if (tabLayout != null) {
                            TabLayout.Tab tab = tabLayout.getTabAt(3);
                            if (tab != null) {
                                tab.select();
                            }
                        }
                    }

                    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragmentcontainer, fragment);
                    transaction.commit();

                } else {
                    // Show an error message if any field is empty
                    Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }

    private boolean validateFields() {
        // Check if any of the fields are empty and validate specific fields
        if (TextUtils.isEmpty(fNameEdt.getText())) {
            fNameEdt.setError("First name is required");
            return false;
        }
        if (TextUtils.isEmpty(lNameEdt.getText())) {
            lNameEdt.setError("Last name is required");
            return false;
        }
        if (TextUtils.isEmpty(emailEdt.getText())) {
            emailEdt.setError("Email is required");
            return false;
        }
        if (TextUtils.isEmpty(fulladdEdt.getText())) {
            fulladdEdt.setError("Address is required");
            return false;
        }
        if (TextUtils.isEmpty(postcodeEdt.getText())) {
            postcodeEdt.setError("Post code is required");
            return false;
        }
        if (postcodeEdt.getText().length() != 6) {
            postcodeEdt.setError("Post code must be 6 digits");
            return false;
        }
        if (TextUtils.isEmpty(phnumEdt.getText())) {
            phnumEdt.setError("Mobile number is required");
            return false;
        }
        if (phnumEdt.getText().length() != 10) {
            phnumEdt.setError("Mobile number must be 10 digits");
            return false;
        }
        return true;
    }

    private void displayDetailsDialog() {
        // Build the AlertDialog with the entered details
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Contact Details");

        // Construct the message string with the entered details
        StringBuilder message = new StringBuilder();
        message.append("First Name: ").append(fNameEdt.getText().toString()).append("\n");
        message.append("Last Name: ").append(lNameEdt.getText().toString()).append("\n");
        message.append("Email Address: ").append(emailEdt.getText().toString()).append("\n");
        message.append("Full Address: ").append(fulladdEdt.getText().toString()).append("\n");
        message.append("Post Code: ").append(postcodeEdt.getText().toString()).append("\n");
        message.append("Mobile Number: ").append(phnumEdt.getText().toString()).append("\n");

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