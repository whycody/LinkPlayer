package com.linkplayer.linkplayer.dialog.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import com.linkplayer.linkplayer.R;

public class AccessPermissionDialogFragment extends DialogFragment {

    private String OK, PERMISSION, NEED_PERMISSION;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        initializeStrings();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(PERMISSION)
                .setMessage(NEED_PERMISSION)
                .setPositiveButton(OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                        intent.setData(uri);
                        getActivity().startActivity(intent);
                        getActivity().finish();
                    }
                }).setNegativeButton(getActivity().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().finish();
                    }
                });

        return builder.create();
    }

    private void initializeStrings(){
        OK = getResources().getString(R.string.ok);
        PERMISSION = getString(R.string.permission);
        NEED_PERMISSION = getString(R.string.need_permission);
    }
}
