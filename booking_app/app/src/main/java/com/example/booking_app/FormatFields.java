package com.example.booking_app;

public class FormatFields {
    String date , block;


    public FormatFields(String date, String block) {
        setBlock(block);
        setDate(date);
    }


    private void setDate(String date) {
        String[] dateSortArray = date.split("/");
        RoomDetails.date = dateSortArray[2] + "-" + dateSortArray[1]  + "-" + dateSortArray[0];
    }


    private void setBlock(String block) {
        String[] blockSortArray = block.split("-");
        RoomDetails.roomBlock = blockSortArray[0];
    }
}
