package com.example.booking_app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DialogBoxRB extends DialogFragment {
    private static onClickBlocks blockInterfaceRef;
   private int ref = 0;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final String[] list = getActivity().getResources().getStringArray(R.array.Blocks);
        builder.setTitle("Block")
                .setSingleChoiceItems(list, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        blockInterfaceRef.setBlockName(list[i]);
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //keep blank
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //keep blank
                    }
                });
        return builder.create();
    }

    public interface onClickBlocks{
        void setBlockName(String blockName);
    }

    public static void setOnCLickBlockOptn(onClickBlocks onClickBlocks){
        blockInterfaceRef = onClickBlocks;
    }
}
