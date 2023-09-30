package uz.gita.puzzle_15;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class MainActivity extends AppCompatActivity {

    private Handler handler = new Handler();
    ImageView imageView;
    private Animation shrinkAnimation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        shrinkAnimation = AnimationUtils.loadAnimation(this, R.anim.shrink_animation);
        imageView = findViewById(R.id.btn_start);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                imageView.setVisibility(View.VISIBLE);
            }
        }, 1100);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                LottieAnimationView lottieAnimationView = findViewById(R.id.lottie);
                lottieAnimationView.setVisibility(View.INVISIBLE);
                startAnimationLoop();
            }
        }, 1100);

        imageView.setOnClickListener(v -> {
            imageView.startAnimation(shrinkAnimation);
            startActivity(new Intent(this, MainActivity2.class));
            finish();
        });

    }

    private void startAnimationLoop() {
        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation fadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out);

        imageView.startAnimation(fadeInAnimation);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                imageView.startAnimation(fadeOutAnimation);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imageView.startAnimation(fadeInAnimation);
                        startAnimationLoop();
                    }
                }, fadeOutAnimation.getDuration());
            }
        }, fadeInAnimation.getDuration());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onBackPressed() {
//        new StylishAlertDialog(this, StylishAlertDialog.WARNING)
//                .setTitleText("Eslatma")
//                .setContentText("Dasturdan chiqishga qatiymisiz?")
//                .setConfirmText("Ha")
//                .setCancelButton("Yoq", null)
//                .setConfirmClickListener(new StylishAlertDialog.OnStylishClickListener() {
//                    @Override
//                    public void onClick(StylishAlertDialog sDialog) {
//                        System.exit(0);
//                        finish();
//                    }
//                })
//                .show();
    }
}