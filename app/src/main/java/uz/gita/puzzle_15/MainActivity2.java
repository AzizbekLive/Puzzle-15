package uz.gita.puzzle_15;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.marsad.stylishdialogs.StylishAlertDialog;

public class MainActivity2 extends AppCompatActivity {

    private ImageView imageView;
    private ImageView imageView1;
    private Handler handler = new Handler();
    private ImageView infoView;
    private ImageView info_icon;
    private MediaPlayer mediaPlayer;
    private Animation shrinkAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        imageView = findViewById(R.id.button);
        imageView1 = findViewById(R.id.imgPuzzle);
        info_icon = findViewById(R.id.info_icon);
        infoView = findViewById(R.id.infoButton);
        shrinkAnimation = AnimationUtils.loadAnimation(this, R.anim.shrink_animation);

        mediaPlayer = MediaPlayer.create(this, R.raw.mclick);

        infoView.setOnClickListener(v -> {
            mediaPlayer.start();
            infoView.startAnimation(shrinkAnimation);
            info_icon.startAnimation(shrinkAnimation);
            startActivity(new Intent(this, MainActivityHelp.class));
        });

        imageView.setOnClickListener(v -> {
            mediaPlayer.start();
            imageView.startAnimation(shrinkAnimation);
            Intent intent = new Intent(MainActivity2.this, MainActivity_levels.class);
            startActivity(intent);
        });

        startAnimationLoop();
    }

    private void startAnimationLoop() {
        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation fadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out);

        imageView1.startAnimation(fadeInAnimation);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                imageView1.startAnimation(fadeOutAnimation);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imageView1.startAnimation(fadeInAnimation);
                        startAnimationLoop();
                    }
                }, fadeOutAnimation.getDuration());
            }
        }, fadeInAnimation.getDuration());
    }

    @Override
    public void onBackPressed() {
//        new StylishAlertDialog(this, StylishAlertDialog.WARNING)
//                .setTitleText("Warning")
//                .setContentText("Do you really want to exit?")
//                .setConfirmText("Stay")
//                .setCancelButton("Exit", sDialog -> {
//                    finish();
//                    System.exit(0);
//                })
//                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
