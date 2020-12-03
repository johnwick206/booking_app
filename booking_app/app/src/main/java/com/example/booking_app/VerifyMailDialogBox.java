package com.example.booking_app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class VerifyMailDialogBox extends AppCompatDialogFragment {

    private static onCLickOk listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Verify");
        builder.setMessage("Check you mail to verify account");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //keep it blank
                listener.checkStatus();
            }
        });

        builder.setCancelable(false);
        return builder.create();
    }

    interface onCLickOk{
        void checkStatus();
    }

    public static void setOnCLickOk(onCLickOk intefaceListener){
        listener = intefaceListener;
    }


}
