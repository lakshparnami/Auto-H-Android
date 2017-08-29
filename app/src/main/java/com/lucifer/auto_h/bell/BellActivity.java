package com.lucifer.auto_h.bell;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.SeekBar;

import com.lucifer.auto_h.R;

public class BellActivity extends AppCompatActivity {
    float previousScale = 1f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bell);
        SeekBar seekBar = (SeekBar) findViewById(R.id.slider);
        final View open = findViewById(R.id.open);
        final View close = findViewById(R.id.close);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBar.getThumb().setTint(Color.parseColor(progress < 10 || progress > 90 ? "#00000000" : "#000000"));

                float nextScale = ((float) progress) / 100f;
                Animation anim = new ScaleAnimation(
                        previousScale, nextScale, // Start and end values for the X axis scaling
                        previousScale, nextScale, // Start and end values for the Y axis scaling
                        Animation.RELATIVE_TO_SELF, 1f, // Pivot point of X scaling
                        Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
                anim.setFillAfter(true); // Needed to keep the result of the animation
                anim.setDuration(1);
                close.startAnimation(anim);
                Animation anim2 = new ScaleAnimation(
                        1.0f - previousScale, 1.0f - nextScale, // Start and end values for the X axis scaling
                        1.0f - previousScale, 1.0f - nextScale, // Start and end values for the Y axis scaling
                        Animation.RELATIVE_TO_SELF, 0f, // Pivot point of X scaling
                        Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
                anim2.setFillAfter(true); // Needed to keep the result of the animation
                anim2.setDuration(1);
                open.startAnimation(anim2);
                previousScale = nextScale;
                if (progress < 10)
                    open.setBackground(getResources().getDrawable(R.drawable.circle, null));
                else if (progress > 90)
                    close.setBackground(getResources().getDrawable(R.drawable.circle, null));
                else {
                    open.setBackground(new ColorDrawable(Color.parseColor("#00ffffff")));
                    close.setBackground(new ColorDrawable(Color.parseColor("#00ffffff")));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (seekBar.getProgress() > 90) finish();
                else if (seekBar.getProgress() < 10) finish();
                else
                    seekBar.setProgress(50);
            }
        });
        seekBar.setProgress(50);
        Animation anim = new ScaleAnimation(
                0.5f, 0.5f, // Start and end values for the X axis scaling
                0.5f, 0.5f, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 1f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
        anim.setFillAfter(true); // Needed to keep the result of the animation
        close.startAnimation(anim);
        Animation anim2 = new ScaleAnimation(
                0.5f, 0.5f, // Start and end values for the X axis scaling
                0.5f, 0.5f, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
        anim2.setFillAfter(true); // Needed to keep the result of the animation
        open.startAnimation(anim2);
        seekBar.getThumb().setTint(Color.parseColor("#000000"));
    }
}
