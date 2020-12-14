package com.example.booking_app.User;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booking_app.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;

public class UserSlotAdapter extends FirestoreRecyclerAdapter<SlotModel , UserSlotAdapter.SlotHolder> {

    private static onClickRoom listener;
    public static ArrayList<String> roomList = new ArrayList<>();

    public UserSlotAdapter(@NonNull FirestoreRecyclerOptions<SlotModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull SlotHolder holder, int position, @NonNull final SlotModel model) {
        holder.roomNo.setText(model.getRoomNo());
        holder.roomNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.openForm(model.getSlots() , model.getRoomNo());
            }
        });

        roomList.add(model.getRoomNo());
    }

    @NonNull
    @Override
    public SlotHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_room_model , parent, false);
        return new SlotHolder(view);
    }

    public static class SlotHolder extends RecyclerView.ViewHolder {

        TextView roomNo;
        public SlotHolder(@NonNull View itemView) {
            super(itemView);
            roomNo = itemView.findViewById(R.id.RoomNo);
        }
    }

    interface onClickRoom{
        void openForm(String slots , String roomNo);
    }

    public static void setOnCLickRoom(onClickRoom intefaceListener){
        listener = intefaceListener;
    }
}
