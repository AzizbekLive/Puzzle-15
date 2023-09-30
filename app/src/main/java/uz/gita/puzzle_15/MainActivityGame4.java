package uz.gita.puzzle_15;

import static android.app.ProgressDialog.show;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.marsad.stylishdialogs.StylishAlertDialog;
import com.saadahmedsoft.popupdialog.PopupDialog;
import com.saadahmedsoft.popupdialog.Styles;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;
import com.thecode.aestheticdialogs.AestheticDialog;

import java.util.ArrayList;
import java.util.Collections;


public class MainActivityGame4 extends AppCompatActivity {


    TextView[][] views = new TextView[4][4];
    ArrayList<Integer> numbers = new ArrayList<>(16);
    int indexI = -1;
    int indexJ = -1;
    private static int movesCount;
    private Chronometer chronometer;
    private boolean isChronometerRunning;
    private long pauseOffset;
    private TextView textView_count;
    private MyPair myPair;
    private ImageView btn_finish;
//    private MediaPlayer mediaPlayer;

    //    SharedPreferences menaPref;
//    SharedPreferences.Editor editor;
    private ImageView pause;
    private MediaPlayer mediaPlayer;
    private int playbackPosition = 0;

    private ImageView volume;
    private boolean isVolumeOn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game4);
        textView_count = findViewById(R.id.txtMovesCounter);
        chronometer = findViewById(R.id.timer_textview);
        btn_finish = findViewById(R.id.btn_finish);
        movesCount = 0;
        mediaPlayer = MediaPlayer.create(this, R.raw.monkey);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        //click = MediaPlayer.create(this, R.raw.clc);

        volume = findViewById(R.id.valume4);
        volume.setOnClickListener(v -> toggleVolume());

        if (savedInstanceState != null) {
            playbackPosition = savedInstanceState.getInt("playbackPosition", 0);
        }

//        mediaPlayer = MediaPlayer.create(this, R.raw.monkey);
//        mediaPlayer.setLooping(true);
//        mediaPlayer.start();


//        menaPref = getApplicationContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
//        editor = menaPref.edit();

        pause = findViewById(R.id.pause4);
        pause.setOnClickListener(v -> {
            pauseChronometer();
            mediaPlayer.pause();
            PopupDialog.getInstance(this)
                    .setStyle(Styles.ALERT)
                    .setHeading("Warning")
                    .setDescription("To continue the game" +
                            " click the button below")
                    .setCancelable(false)
                    .showDialog(new OnDialogButtonClickListener() {
                        @Override
                        public void onDismissClicked(Dialog dialog) {
                            startChronometer();
                            mediaPlayer.start();
                            super.onDismissClicked(dialog);
                        }
                    });
        });
//            ImageView imageView = findViewById(R.id.resume);
//            imageView.setOnClickListener(w -> {
//                //alertDialogBuilder.dismiss();
//            });

        findViewById(R.id.reload).setOnClickListener(v -> {
//            startActivity(new Intent(this, MainActivity_levels.class));
//            finish();

            movesCount = 0;
            Collections.shuffle(numbers);
            views[indexI][indexJ].setVisibility(View.VISIBLE);
            describeNumbers();
            loadViews();
            chronometer.setFormat(null);
            chronometer.start();
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.setFormat("%s");
//            AestheticDialog.showEmoji(this, "Muvaffaqiyatli", "Siz o'yinni qayta boshladingiz", AestheticDialog.SUCCESS);
        });

        //AestheticDialog.showToaster(this, "Mufaffaqiyatli", "O'yin boshlandi, OMAD!", AestheticDialog.SUCCESS);
        //AestheticDialog.showToaster(this,"","",AestheticDialog.);

        btn_finish.setOnClickListener(v -> {
//            startActivity(new Intent(this, MainActivity2.class));
//            finish();
            new StylishAlertDialog(this, StylishAlertDialog.WARNING)
                    .setTitleText("Warning")
                    .setContentText("Do you really want to exit")
                    .setConfirmText("Stay")
                    .setCancelButton("Exit", null)
                    .setCancelClickListener(sDialog -> {
                        finish();
                    })
                    .show();

        });

        loadViews();
        loadRandomNumbers();
        describeNumbers();
        startChronometer();


        if (savedInstanceState != null) {
            //AestheticDialog.showToaster(this, "Mufaffaqiyatli", "O'yin boshlandi, OMAD!", AestheticDialog.SUCCESS);
            movesCount = savedInstanceState.getInt("count");
            textView_count.setText(String.valueOf(movesCount));
        }
    }
    private void toggleVolume() {
        if (isVolumeOn) {
            mediaPlayer.setVolume(0, 0); // Mute volume
            volume.setImageResource(R.drawable.mute);
        } else {
            mediaPlayer.setVolume(1, 1); // Set volume to max
            volume.setImageResource(R.drawable.volume);
        }
        isVolumeOn = !isVolumeOn;
    }


    @Override
    protected void onStart() {
        super.onStart();
        // Start or continue playing the music
        mediaPlayer.seekTo(playbackPosition);
        mediaPlayer.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Pause the music and save the playback position
        mediaPlayer.pause();
        playbackPosition = mediaPlayer.getCurrentPosition();
    }

    private void loadViews() {
        RelativeLayout relativeLayout = findViewById(R.id.relative);

        for (int i = 0; i < relativeLayout.getChildCount(); i++) {
            views[i / 4][i % 4] = (TextView) relativeLayout.getChildAt(i);

            views[i / 4][i % 4].setTag(new MyPair(i / 4, i % 4));

            views[i / 4][i % 4].setOnClickListener(v -> {
                MyPair data = (MyPair) v.getTag();
                canPermit(data.i, data.j);
            });
        }
    }

    private void loadRandomNumbers() {
        for (int i = 0; i < 16; i++) {
            numbers.add(i);
        }

        do {
            Collections.shuffle(numbers);
            findXposition();
            Log.d("TTT", "shuffle again");
        } while (!isSolved());
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("count", movesCount);
        long delTime = chronometer.getBase() - SystemClock.elapsedRealtime();
        outState.putLong("time", delTime);
        ArrayList<String> list = new ArrayList<>();
        outState.putBoolean("isChronometerRunning", isChronometerRunning);
        outState.putLong("pauseOffset", pauseOffset);
        super.onSaveInstanceState(outState);
        RelativeLayout relativeLayout = findViewById(R.id.relative);
        for (int i = 0; i < relativeLayout.getChildCount(); i++) {
            TextView textView = (TextView) relativeLayout.getChildAt(i);
            list.add(textView.getText().toString());
        }
        outState.putStringArrayList("buttons", list);
        outState.putInt("playbackPosition", playbackPosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Nothing specific to handle here for configuration changes
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ArrayList<String> list = savedInstanceState.getStringArrayList("buttons");
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals("")) {
                views[i / 4][i % 4].setVisibility(View.INVISIBLE);
                views[i / 4][i % 4].setText("");

                indexI = i / 4;
                indexJ = i % 4;

            } else {
                views[i / 4][i % 4].setVisibility(View.VISIBLE);
                views[i / 4][i % 4].setText(list.get(i));
            }
        }
        long delTime = savedInstanceState.getLong("time", 0);
        chronometer.setBase(SystemClock.elapsedRealtime() + delTime);
        ArrayList<String> list1 = savedInstanceState.getStringArrayList("buttons");
        loadSavedNumbers(list1);
    }

    private void loadSavedNumbers(ArrayList<String> numbers) {

        textView_count.setText(String.valueOf(movesCount));
        for (int i = 0; i < numbers.size(); i++) {
            if (numbers.get(i).equals("")) {
                myPair = new MyPair(i % 4, i / 4);
            }
            views[i / 4][i % 4].setText(numbers.get(i));
        }
    }

    private void startChronometer() {
        if (!isChronometerRunning) {
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            isChronometerRunning = true;
        }
    }

    private void pauseChronometer() {
        if (isChronometerRunning) {
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            isChronometerRunning = false;
        }
    }

    private void findXposition() {
        for (int i = 0; i < numbers.size(); i++) {
            if (numbers.get(i) == 0) {
                indexI = i / 4;
                indexJ = i % 4;
            }
        }
    }

    private boolean isSolved() {
        int inversion_count = 0;

        for (int i = 0; i < numbers.size(); i++) {
            if (numbers.get(i) == 0) continue;
            for (int j = i + 1; j < numbers.size(); j++) {
                if (Integer.parseInt(numbers.get(j).toString()) > Integer.parseInt(numbers.get(i).toString())) {
                    inversion_count++;
                }
            }
        }

        return (inversion_count % 2 == 1) || ((4 - indexI) % 2 == 1);
    }

    private void describeNumbers() {
        Collections.shuffle(numbers);
        int count = 0;

        for (int i = 0; i < numbers.size(); i++) {
            for (int j = i + 1; j < numbers.size() - 1; j++) {
                if (numbers.get(i) > numbers.get(j)) count++;
            }
        }
        if (!((count % 2 == 0 && (4 - indexI) % 2 == 1) || (count % 2 == 1 && (4 - indexI) % 2 == 0)))
            describeNumbers();

        for (int i = 0; i < numbers.size(); i++) {
            if (numbers.get(i) == 0) {
                views[i / 4][i % 4].setVisibility(View.INVISIBLE);
                indexJ = i % 4;
                indexI = i / 4;
            } else views[i / 4][i % 4].setText(String.valueOf(numbers.get(i)));
        }
        textView_count.setText(String.valueOf(movesCount));
    }


    private void canPermit(int i, int j) {
        movesCount++;


        TextView movesCounterTextView = findViewById(R.id.txtMovesCounter);
        movesCounterTextView.setText(String.valueOf(movesCount));

        //click.start();
        boolean bool = (Math.abs(indexI - i) == 1 && indexJ == j) || (Math.abs(indexJ - j) == 1 && indexI == i);
        //click.setLooping(true);
        if (bool) {
            //userCoins += 1;
            views[indexI][indexJ].setVisibility(View.VISIBLE);
            views[indexI][indexJ].setText(views[i][j].getText());
            views[i][j].setVisibility(View.INVISIBLE);
            views[i][j].setText("");
            indexI = i;
            indexJ = j;
            isWin();
        }
    }

//    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

    private void isWin() {
        int count = 0;

        for (int i = 0; i < 15; i++) {
            if (views[i / 4][i % 4].getText().equals(String.valueOf(i + 1))) {
                count++;
            }
        }
        if (count == 15) {
            PopupDialog.getInstance(this)
                    .setStyle(Styles.SUCCESS)
                    .setHeading("Victory Achieved")
                    .setDescription("Congratulations, your skillful strategy and quick thinking have led you to a well-deserved victory!")
                    .setCancelable(false)
                    .setDismissButtonText("Next")
                    .showDialog(new OnDialogButtonClickListener() {
                        @Override
                        public void onDismissClicked(Dialog dialog) {
                            startActivity(new Intent(MainActivityGame4.this, MainActivity_levels.class));
                            finish();
                            super.onDismissClicked(dialog);
                        }
                    });

        }
    }

    @Override
    public void onBackPressed() {
        new StylishAlertDialog(this, StylishAlertDialog.WARNING)
                .setTitleText("Warning")
                .setContentText("Do you really want to exit")
                .setConfirmText("Stay")
                .setCancelButton("Exit", null)
                .setCancelClickListener(new StylishAlertDialog.OnStylishClickListener() {
                    @Override
                    public void onClick(StylishAlertDialog sDialog) {
                        finish();
                    }
                })
                .show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!isVolumeOn) {
            mediaPlayer.setVolume(0, 0);
        }
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!isVolumeOn) {
            mediaPlayer.setVolume(1, 1);
        }
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
    }

//    @Override
//    protected void onResume() {
//        mediaPlayer.start();
//        super.onResume();
//    }
}