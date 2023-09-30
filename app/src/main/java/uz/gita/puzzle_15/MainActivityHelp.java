package uz.gita.puzzle_15;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivityHelp extends AppCompatActivity {
    private Animation shrinkAnimation;
    private ImageView imageView;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_help);

        mediaPlayer = MediaPlayer.create(this, R.raw.mclick);

        imageView = findViewById(R.id.button_contact);
        shrinkAnimation = AnimationUtils.loadAnimation(this, R.anim.shrink_animation);
        imageView.setOnClickListener(view -> {
            openTelegramProfile();
            mediaPlayer.start();
            imageView.startAnimation(shrinkAnimation);

        });

    }

    private void openTelegramProfile() {
        String telegramUsername = "azizlive";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/" + telegramUsername));
        startActivity(intent);
    }
}