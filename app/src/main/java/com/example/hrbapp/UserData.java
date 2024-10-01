package com.example.hrbapp;

import java.io.Serializable;
import java.util.Objects;

public class UserData implements Serializable {
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private String address;
    private String city;
    private String country;
    private String documentId;
//    private String password;
//    private String confirmPassword;
    private String gender; // Add field for spinner selection
    private String docType; // Add field for spinner selection
    private String imageUrl; // Add field for image URL

    // Default constructor
    public UserData() {
        // Default constructor required for Firebase
    }

    public UserData(String firstName, String lastName, String email, String mobile, String address, String city, String country, String documentId, String gender, String docType, String imageUrl) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobile = mobile;
        this.address = address;
        this.city = city;
        this.country = country;
        this.documentId = documentId;
        this.gender = gender;
        this.docType = docType;
        this.imageUrl = imageUrl;
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

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public String getConfirmPassword() {
//        return confirmPassword;
//    }
//
//    public void setConfirmPassword(String confirmPassword) {
//        this.confirmPassword = confirmPassword;
//    }

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

    @Override
    public String toString() {
        return "UserData{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", documentId='" + documentId + '\'' +
                ", gender='" + gender + '\'' +
                ", docType='" + docType + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    // Method to convert a String to a UserData object
    public static UserData fromString(String userDataString) {
        String[] parts = userDataString.split(",");
        if (parts.length != 11) {
            throw new IllegalArgumentException("Invalid userDataString: " + userDataString);
        }
        UserData userData = new UserData();
        userData.setFirstName(parts[0]);
        userData.setLastName(parts[1]);
        userData.setMobile(parts[2]);
        userData.setAddress(parts[3]);
        userData.setEmail(parts[4]);
        userData.setCity(parts[5]);
        userData.setCountry(parts[6]);
        userData.setDocumentId(parts[7]);
        userData.setGender(parts[8]);
        userData.setDocType(parts[9]);
        userData.setImageUrl(parts[10]);
        return userData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserData userData = (UserData) o;
        return Objects.equals(firstName, userData.firstName) && Objects.equals(lastName, userData.lastName) && Objects.equals(mobile, userData.mobile) && Objects.equals(address, userData.address) && Objects.equals(email, userData.email) && Objects.equals(city, userData.city) && Objects.equals(country, userData.country) && Objects.equals(documentId, userData.documentId) && Objects.equals(gender, userData.gender) && Objects.equals(docType, userData.docType) && Objects.equals(imageUrl, userData.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, mobile, address, email, city, country, documentId, gender, docType, imageUrl);
    }
}
