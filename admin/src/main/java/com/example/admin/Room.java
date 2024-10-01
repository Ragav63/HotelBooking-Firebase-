package com.example.admin;

public class Room {
    private int id;
    private String roomType;
    private String roomDescription;
    private double roomPrice;
    private int roomTax;

    public Room() {
        this.id=id;
        this.roomType = roomType;
        this.roomDescription = roomDescription;
        this.roomPrice = roomPrice;
        this.roomTax = roomTax;
    }

    public Room(String roomType, String roomDescription, double roomPrice, int roomTax) {
        this.roomType = roomType;
        this.roomDescription = roomDescription;
        this.roomPrice = roomPrice;
        this.roomTax = roomTax;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public double getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(double roomPrice) {
        this.roomPrice = roomPrice;
    }

    public int getRoomTax() {
        return roomTax;
    }

    public void setRoomTax(int roomTax) {
        this.roomTax = roomTax;
    }
}
