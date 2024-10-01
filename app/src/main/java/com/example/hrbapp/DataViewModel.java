package com.example.hrbapp;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class DataViewModel extends ViewModel {

    String fname;
    String lname;
    String logemail ;
    String mobile;
    String address;
    String city;
    String country;
    String docId;
    String gender;
    String docType;
    String imageUrl;
    private String checkInDate;
    private String checkOutDate;
    private String selectedRoom;
    private String selectedPerson;
    private String duration;

    // Single selected room
    private RecItemRoom selectedRoomItem;

    // Multiple selected extra facilities
    private List<RecItOtFacRoom> selectedFacItems = new ArrayList<>();
    private String roomType;
    private String roomDesc;
    private String roomPrice;
    private String roomTax;
    private String roomOtherFac;
    private String roomTotal;
    private int selectedItemPosition;
    private double totalAmount;

    private final MutableLiveData<String> firstName = new MutableLiveData<>();
    private final MutableLiveData<String> lastName = new MutableLiveData<>();
    private final MutableLiveData<String> email = new MutableLiveData<>();
    private final MutableLiveData<String> fullAddress = new MutableLiveData<>();
    private final MutableLiveData<String> postCode = new MutableLiveData<>();
    private final MutableLiveData<String> mobileNumber = new MutableLiveData<>();

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getLogemail() {
        return logemail;
    }

    public void setLogemail(String logemail) {
        this.logemail = logemail;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public RecItemRoom getSelectedRoomItem() {
        return selectedRoomItem;
    }

    public void setSelectedRoomItem(RecItemRoom selectedRoomItem) {
        this.selectedRoomItem = selectedRoomItem;
    }

    public List<RecItOtFacRoom> getSelectedFacItems() {
        return selectedFacItems;
    }

    public void setSelectedFacItems(List<RecItOtFacRoom> selectedFacItems) {
        this.selectedFacItems = selectedFacItems;
    }

    public String getSelectedRoom() {
        return selectedRoom;
    }

    public void setSelectedRoom(String selectedRoom) {
        this.selectedRoom = selectedRoom;
    }

    public String getSelectedPerson() {
        return selectedPerson;
    }

    public void setSelectedPerson(String selectedPerson) {
        this.selectedPerson = selectedPerson;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getRoomDesc() {
        return roomDesc;
    }

    public void setRoomDesc(String roomDesc) {
        this.roomDesc = roomDesc;
    }

    public String getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(String roomPrice) {
        this.roomPrice = roomPrice;
    }

    public String getRoomTax() {
        return roomTax;
    }

    public void setRoomTax(String roomTax) {
        this.roomTax = roomTax;
    }

    public String getRoomOtherFac() {
        return roomOtherFac;
    }

    public void setRoomOtherFac(String roomOtherFac) {
        this.roomOtherFac = roomOtherFac;
    }

    public String getRoomTotal() {
        return roomTotal;
    }

    public void setRoomTotal(String roomTotal) {
        this.roomTotal = roomTotal;
    }

    public int getSelectedItemPosition() {
        return selectedItemPosition;
    }

    public void setSelectedItemPosition(int selectedItemPosition) {
        this.selectedItemPosition = selectedItemPosition;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public MutableLiveData<String> getFirstName() {
        return firstName;
    }

    public void setFirstName(String fName) {
        firstName.setValue(fName);
    }

    public MutableLiveData<String> getLastName() {
        return lastName;
    }
    public void setLastName(String lName) {
        lastName.setValue(lName);
    }


    public MutableLiveData<String> getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email.setValue(email);
    }


    public MutableLiveData<String> getFullAddress() {
        return fullAddress;
    }
    public void setFullAddress(String address) {
        fullAddress.setValue(address);
    }

    public MutableLiveData<String> getPostCode() {
        return postCode;
    }
    public void setPostCode(String postCode) {
        this.postCode.setValue(postCode);
    }

    public MutableLiveData<String> getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber.setValue(mobileNumber);
    }


}
