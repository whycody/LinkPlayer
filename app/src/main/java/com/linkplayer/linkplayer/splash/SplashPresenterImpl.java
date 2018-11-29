package com.linkplayer.linkplayer.splash;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.linkplayer.linkplayer.dialog.fragments.AccessPermissionDialogFragment;

public class SplashPresenterImpl implements SplashPresenter{

    private Activity activity;
    private SplashView splashView;

    public SplashPresenterImpl(Activity activity, SplashView splashView){
        this.activity = activity;
        this.splashView = splashView;
    }

    private String permission =  Manifest.permission.READ_EXTERNAL_STORAGE;

    @Override
    public void checkPermissionsAndStartActivity(){
        boolean readPermission = readPermissionsGranted();
        if(readPermission)
            splashView.startMainActivity();
        else
            checkDontAskAgainOptionAndRequestPermissions();
    }

    private boolean readPermissionsGranted(){
        return ContextCompat.checkSelfPermission
                (activity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean canShowRequestPermission(){
        return activity.shouldShowRequestPermissionRationale(permission);
    }

    private void checkDontAskAgainOptionAndRequestPermissions(){
        boolean canShowRequestPermission = true;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
            canShowRequestPermission = canShowRequestPermission();
        if(canShowRequestPermission)
            requestPermission();
        else
            showInformationAboutPermission();
    }

    private void showInformationAboutPermission() {
        AccessPermissionDialogFragment dialogFragment = new AccessPermissionDialogFragment();
        dialogFragment.show(activity.getFragmentManager(), "AccessPermissionDialogFragment");
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(activity, new String[]{permission}, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        checkPermissionsAndStartActivity();
    }

}
