package com.example.booking_app.User;

public class SlotModel {
    public String roomNo , slots;

    public SlotModel() {
        //for firebase
    }

    public SlotModel(String roomNo, String slots) {
        this.roomNo = roomNo;
        this.slots = slots;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public String getSlots() {
        return slots;
    }
}
