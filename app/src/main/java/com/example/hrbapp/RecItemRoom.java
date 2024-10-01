package com.example.hrbapp;

public class RecItemRoom {

    //Recycler items for Room items recycler in Room fragment
    private int id;
    private String roomDescription;
    private double roomPrice;
    private int roomTax;
    private String roomType;

    public RecItemRoom() {
    }

    public RecItemRoom(int id, String roomDescription, double roomPrice, int roomTax, String roomType) {
        this.id=id;
        this.roomDescription = roomDescription;
        this.roomPrice = roomPrice;
        this.roomTax = roomTax;
        this.roomType = roomType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    @Override
    public String toString() {
        return "RecItemRoom{" +
                "roomDescription='" + roomDescription + '\'' +
                ", roomPrice='" + roomPrice + '\'' +
                ", roomTax='" + roomTax + '\'' +
                ", roomType='" + roomType + '\'' +
                '}';
    }
}
