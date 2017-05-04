package com.applozic.mobicomkit.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.applozic.mobicomkit.api.account.user.MobiComUserPreference;
import com.applozic.mobicomkit.uiwidgets.main.MainActivityK;
import com.applozic.mobicomkit.sample.LoginActivity;
import com.applozic.mobicomkit.welcome.PrefManager;
import com.applozic.mobicomkit.welcome.WelcomeActivity;

/**
 * Created by sunil on 21/12/2016.
 */

public class SplashScreenActivity extends Activity {
    private final int SPLASH_DISPLAY_LENGTH = 1000;
    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefManager = new PrefManager(this);
        setContentView(R.layout.splash_screen_layout);
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    if (prefManager.isFirstTimeLaunch()) {
                        Intent mainIntent = new Intent(SplashScreenActivity.this, WelcomeActivity.class);
                        SplashScreenActivity.this.startActivity(mainIntent);
                        SplashScreenActivity.this.finish();
                    } else {
                        if(MobiComUserPreference.getInstance(SplashScreenActivity.this).isLoggedIn()) {
                            Intent mainIntent = new Intent(SplashScreenActivity.this, MainActivityK.class);
                            SplashScreenActivity.this.startActivity(mainIntent);
                            SplashScreenActivity.this.finish();
                        }else {
                            Intent mainIntent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                            SplashScreenActivity.this.startActivity(mainIntent);
                            SplashScreenActivity.this.finish();
                        }
                    }
                }
            }, SPLASH_DISPLAY_LENGTH);
        }
}
