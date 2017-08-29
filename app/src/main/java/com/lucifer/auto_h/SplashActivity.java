package com.lucifer.auto_h;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                beginAnimation();
            }
        }, 200);
      /*  new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        },1000);*/
//                    itemsData.get(position).getActivity().startActivity(i, options.toBundle());


    }

    @Override
    public void onBackPressed() {
        beginAnimation();
    }

    private void beginAnimation() {
        View logo = findViewById(R.id.logo);
        View bottom = findViewById(R.id.bottom);
        Pair<View, String> p1 = Pair.create(logo, "logo");
        Pair<View, String> p2 = Pair.create(bottom, "bottom");
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(SplashActivity.this, p1, p2);
        startActivity(new Intent(SplashActivity.this, LoginActivity.class), options.toBundle());
    }
}
