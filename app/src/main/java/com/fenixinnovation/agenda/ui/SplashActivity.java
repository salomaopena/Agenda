package com.fenixinnovation.agenda.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.fenixinnovation.agenda.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        Intent i = MainActivity.getStartIntent(SplashActivity.this);

        new Handler().postDelayed(() -> {
            startActivity(i);
            finish();
        }, 5 * 1000);
    }
}
