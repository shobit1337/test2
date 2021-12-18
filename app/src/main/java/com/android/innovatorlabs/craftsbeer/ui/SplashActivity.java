package com.android.innovatorlabs.craftsbeer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.android.innovatorlabs.craftsbeer.R;

public class SplashActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        ImageView splashImage = findViewById(R.id.splash_image);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.splash_anim);

        splashImage.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // Do Nothing
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Intent intent = new Intent(SplashActivity.this, ProductListActivity.class);

                startActivity(intent);

                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // Do Nothing
            }
        });

    }


}
