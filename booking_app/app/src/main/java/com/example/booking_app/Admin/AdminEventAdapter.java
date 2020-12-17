package com.example.booking_app.Admin;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booking_app.R;
import com.example.booking_app.RoomDetails;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;
import java.util.List;

public class AdminEventAdapter extends FirestoreRecyclerAdapter<AdminEventModel, AdminEventAdapter.EventHolder> {


    List<String> list = new ArrayList<>();

    private static onClickEvent listener;

    public AdminEventAdapter(@NonNull FirestoreRecyclerOptions<AdminEventModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull EventHolder holder, int position, @NonNull final AdminEventModel model) {
        holder.slotNAme.setText(model.getSeminarName());
        holder.date.setText(model.getDate());
        holder.slotNAme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.showDetails(model.getSeminarName());
            }
        });
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.deleteEvents(model.getSlot(),model.date,model.getSeminarName(),model.getRoomNo());
                RoomDetails.roomBlock = model.getBlock();
            }
        });
    }

    @NonNull
    @Override
    public EventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_slot_model , parent, false);
        return new EventHolder(view);
    }


    public static class EventHolder extends RecyclerView.ViewHolder{
        TextView slotNAme , date;
        Button deleteButton;

        public EventHolder(@NonNull View itemView) {
            super(itemView);
            slotNAme = itemView.findViewById(R.id.slotName);
            date = itemView.findViewById(R.id.date);
            deleteButton=itemView.findViewById(R.id.deleteButton);
        }
    }

    interface onClickEvent{
        void showDetails(String name);
        void deleteEvents(String slot,String date,String name,String roomNo);
    }

    public static void setOnClickEvent(onClickEvent interfaceListener){
        listener = interfaceListener;
    }
}
