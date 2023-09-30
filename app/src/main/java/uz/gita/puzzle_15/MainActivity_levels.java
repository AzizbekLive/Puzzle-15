package uz.gita.puzzle_15;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity_levels extends AppCompatActivity {

    private Animation shrinkAnimation;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_levels);
        shrinkAnimation = AnimationUtils.loadAnimation(this, R.anim.shrink_animation);
        mediaPlayer = MediaPlayer.create(this, R.raw.mclick);


        ImageView easy = findViewById(R.id.easy);
        ImageView medium = findViewById(R.id.medium);
        ImageView hard = findViewById(R.id.hard);

        easy.setOnClickListener(v -> {
            mediaPlayer.start();
            easy.startAnimation(shrinkAnimation);
            startActivity(new Intent(this, MainActivityGame3.class));
            finish();
        });

        medium.setOnClickListener(v -> {
            mediaPlayer.start();
            medium.startAnimation(shrinkAnimation);
            startActivity(new Intent(this, MainActivityGame4.class));
            finish();
        });

        hard.setOnClickListener(v -> {
            mediaPlayer.start();
            hard.startAnimation(shrinkAnimation);
            startActivity(new Intent(this, MainActivityGame5.class));
        });
    }
}