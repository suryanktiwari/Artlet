package com.example.artlet_v1;

        import androidx.appcompat.app.AppCompatActivity;
        import android.content.Intent;
        import android.os.Bundle;
        import android.os.Handler;
        import android.view.animation.Animation;
        import android.view.animation.AnimationUtils;
        import android.widget.LinearLayout;

public class SplashActivity extends AppCompatActivity {

    public static int timeout = 2000;
    LinearLayout l1, l2, l3;
    Animation a1, a2, a3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        l1 = (LinearLayout) findViewById(R.id.l1);
        l2 = (LinearLayout) findViewById(R.id.l2);
        l3 = (LinearLayout) findViewById(R.id.l3);
        a1 = AnimationUtils.loadAnimation(this, R.anim.temp);
        a2 = AnimationUtils.loadAnimation(this, R.anim.temp2);
        a3 = AnimationUtils.loadAnimation(this, R.anim.slide_out_up);
        l1.setAnimation(a1);
        l2.setAnimation(a2);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                l3.setAnimation(a3);
                startActivity(i);
                finish();
            }
        },timeout);
    }
}
