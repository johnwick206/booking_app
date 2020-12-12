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

public class UserSlotAdapter extends FirestoreRecyclerAdapter<SlotModel , UserSlotAdapter.SlotHolder> {


    public UserSlotAdapter(@NonNull FirestoreRecyclerOptions<SlotModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull SlotHolder holder, int position, @NonNull SlotModel model) {
        holder.roomNo.setText(model.getRoomNo());
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
}
