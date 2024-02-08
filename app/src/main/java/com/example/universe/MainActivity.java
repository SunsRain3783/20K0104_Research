package com.example.universe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{ //リスナーをインプリメント

    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((ImageButton)findViewById(R.id.mapIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.searchIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.snsIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.infoIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.mypageIcon)).setOnClickListener(this);

        ((Button)findViewById(R.id.kanriButton)).setOnClickListener(this);
        ((Button)findViewById(R.id.nishiButton)).setOnClickListener(this);
        ((Button)findViewById(R.id.higashiButton)).setOnClickListener(this);
        ((Button)findViewById(R.id.minamiButton)).setOnClickListener(this);
        ((Button)findViewById(R.id.kitaButton)).setOnClickListener(this);

        gestureDetector = new GestureDetector(this, new MainActivity.SwipeGestureDetector());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    private class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float diffX = e2.getX() - e1.getX();
            float diffY = e2.getY() - e1.getY();

            // 右から左へのスワイプ（Searchへ遷移)
            if (Math.abs(diffX) > Math.abs(diffY) && diffX < 0) {
                Intent intent = new Intent(MainActivity.this, Search.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            }

            return false;
        }
    }

    @Override
    public void onClick(View view){  //ボタンをクリックしたときの挙動
        if(view.getId() == R.id.mapIcon) {  //switch使うとエラー(if elseを使う)
            Intent intentlevel = new Intent(getApplication(),MainActivity.class); //画面遷移(MainActivity→GameLevel)
            startActivity(intentlevel);
        } else if(view.getId() == R.id.searchIcon){
            Intent intent = new Intent(getApplication(),Search.class);
            startActivity(intent);
        } else if(view.getId() == R.id.snsIcon){
            Intent intent = new Intent(getApplication(),FeedTop.class);
            startActivity(intent);
        } else if(view.getId() == R.id.infoIcon){
            Intent intent = new Intent(getApplication(),Information.class);
            startActivity(intent);
        } else if(view.getId() == R.id.mypageIcon){
            Intent intent = new Intent(getApplication(),MypageTop.class);
            startActivity(intent);
        } else if(view.getId() == R.id.nishiButton){
            Intent intent = new Intent(getApplication(),nishi_1f.class);
            startActivity(intent);
        } else if(view.getId() == R.id.higashiButton){
            Intent intent = new Intent(getApplication(),higashi_1f.class);
            startActivity(intent);
        } else if(view.getId() == R.id.kanriButton){
            Intent intent = new Intent(getApplication(),kanri_2f.class);
            startActivity(intent);
        } else if(view.getId() == R.id.minamiButton){
            Intent intent = new Intent(getApplication(),minami_1f.class);
            startActivity(intent);
        }
    }
}