package com.linkplayer.linkplayer.splash;

public interface SplashPresenter {

    void checkPermissionsAndStartActivity();

    void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults);

}
