package com.example.admin;

public class Bookings {
    private String bookingId;
    private String paymentStatus;
    private String userId;
    public String checkInDate;
    public String checkOutDate;
    public String noOfRooms;
    public String duration;
    public String noOfPersons;
    public String roomType;
    public String roomDescription;
    public String roomPrice;
    public String roomTax;
    public String otherFacilitiesTitle;
    public String otherFacilitiesPrice;
    public String totalPrice;
    public String firstName;
    public String lastName;
    public String email;
    public String address;
    public String postalCode;
    public String mobileNumber;
    public String coupon;
    public String discount;
    public String discountAmount;
    public String grandTotal;
    public String creditCardNumber;
    public String creditCardName;
    public String cvv;
    public String expiryDate;

    public Bookings(){

    }

    public Bookings(String bookingId, String paymentStatus, String userId, String checkInDate, String checkOutDate, String noOfRooms, String duration, String noOfPersons, String roomType, String roomDescription, String roomPrice, String roomTax, String otherFacilitiesTitle, String otherFacilitiesPrice, String totalPrice, String firstName, String lastName, String email, String address, String postalCode, String mobileNumber, String coupon, String discount, String discountAmount, String grandTotal, String creditCardNumber, String creditCardName, String cvv, String expiryDate) {
        this.bookingId = bookingId;
        this.paymentStatus = paymentStatus;
        this.userId = userId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.noOfRooms = noOfRooms;
        this.duration = duration;
        this.noOfPersons = noOfPersons;
        this.roomType = roomType;
        this.roomDescription = roomDescription;
        this.roomPrice = roomPrice;
        this.roomTax = roomTax;
        this.otherFacilitiesTitle = otherFacilitiesTitle;
        this.otherFacilitiesPrice = otherFacilitiesPrice;
        this.totalPrice = totalPrice;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.postalCode = postalCode;
        this.mobileNumber = mobileNumber;
        this.coupon = coupon;
        this.discount = discount;
        this.discountAmount = discountAmount;
        this.grandTotal = grandTotal;
        this.creditCardNumber = creditCardNumber;
        this.creditCardName = creditCardName;
        this.cvv = cvv;
        this.expiryDate = expiryDate;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getNoOfRooms() {
        return noOfRooms;
    }

    public void setNoOfRooms(String noOfRooms) {
        this.noOfRooms = noOfRooms;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getNoOfPersons() {
        return noOfPersons;
    }

    public void setNoOfPersons(String noOfPersons) {
        this.noOfPersons = noOfPersons;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getRoomDescription() {
        return roomDescription;
    }

    public void setRoomDescription(String roomDescription) {
        this.roomDescription = roomDescription;
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

    public String getOtherFacilitiesTitle() {
        return otherFacilitiesTitle;
    }

    public void setOtherFacilitiesTitle(String otherFacilitiesTitle) {
        this.otherFacilitiesTitle = otherFacilitiesTitle;
    }

    public String getOtherFacilitiesPrice() {
        return otherFacilitiesPrice;
    }

    public void setOtherFacilitiesPrice(String otherFacilitiesPrice) {
        this.otherFacilitiesPrice = otherFacilitiesPrice;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(String grandTotal) {
        this.grandTotal = grandTotal;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public String getCreditCardName() {
        return creditCardName;
    }

    public void setCreditCardName(String creditCardName) {
        this.creditCardName = creditCardName;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    // Add this method if grandTotal is a String
    public double getGrandTotalAsDouble() {
        try {
            return Double.parseDouble(grandTotal);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}
