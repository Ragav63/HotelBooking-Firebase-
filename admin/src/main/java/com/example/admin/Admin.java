package com.example.admin;

public class Admin {
    private String firstName;
    private String lastName;
    private String email;
    private String mobileNumber;
    private String documentName;
    private String documentId;
    private String gender; // To store the selected gender

    // Constructor
    public Admin(String firstName, String lastName, String email, String mobileNumber, String documentName, String documentId, String gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.documentName=documentName;
        this.documentId = documentId;
        this.gender = gender;
    }

    // Getters
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getDocumentName() {
        return documentName;
    }

    public String getDocumentId() {
        return documentId;
    }

    public String getGender() {
        return gender;
    }
}
