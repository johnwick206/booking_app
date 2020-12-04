package com.example.booking_app.Admin;

public class AdminEventModel {
    String block ,name, category,date,description,email,organizer,roomNo ,slot,splRequirement;

    public AdminEventModel() {
        //for firebase //keep it empty
    }

    public AdminEventModel(
             String block
            , String name
            , String category
            , String date
            , String description
            , String email
            , String organizer
            , String roomNo
            , String slot
            , String splRequirement) {
        this.block = block;
        this.name = name;
        this.category = category;
        this.date = date;
        this.description = description;
        this.email = email;
        this.organizer = organizer;
        this.roomNo = roomNo;
        this.slot = slot;
        this.splRequirement = splRequirement;
    }


    public String getBlock() {
        return block;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getEmail() {
        return email;
    }

    public String getOrganizer() {
        return organizer;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public String getSlot() {
        return slot;
    }

    public String getSplRequirement() {
        return splRequirement;
    }
}
