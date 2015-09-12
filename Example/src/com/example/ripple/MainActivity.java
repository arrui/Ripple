package com.example.ripple;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.ripple.CustomRippleBackground.RippleAnimationListener;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final CustomRippleBackground rippleBackground = (CustomRippleBackground) findViewById(R.id.ripple);
        rippleBackground.setRippleAnimationListener(new RippleAnimationListener() {

            @Override
            public void onRippleAnimationEnd() {

            }

            @Override
            public void onRippleAnimationStart() {

            }
        });
        findViewById(R.id.btn_start).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                rippleBackground.startRippleAnimation();
            }
        });
        findViewById(R.id.btn_end).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                rippleBackground.stopRippleAnimation();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
