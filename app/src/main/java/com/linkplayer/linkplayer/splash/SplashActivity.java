package com.linkplayer.linkplayer.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.linkplayer.linkplayer.R;
import com.linkplayer.linkplayer.main.MainActivity;

public class SplashActivity extends AppCompatActivity implements SplashView{

    private SplashPresenter splashPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.background_splash);
        splashPresenter = new SplashPresenterImpl(this, this);
        splashPresenter.checkPermissionsAndStartActivity();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        splashPresenter.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }

    @Override
    public void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
