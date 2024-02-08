package com.example.universe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class minami_1f extends AppCompatActivity implements View.OnClickListener {

    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minami1f);

        ((ImageButton)findViewById(R.id.mapIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.searchIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.snsIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.infoIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.mypageIcon)).setOnClickListener(this);
        ((Button)findViewById(R.id.button1)).setOnClickListener(this);
        ((Button)findViewById(R.id.button2)).setOnClickListener(this);

        gestureDetector = new GestureDetector(this, new minami_1f.SwipeGestureDetector());
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

            // 下から上へのスワイプ
            if (Math.abs(diffY) > Math.abs(diffX) && diffY < 0) {
                Intent intent = new Intent(minami_1f.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down);
                return true;
            }

            return false;
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Intent intent;

        if(id == R.id.mapIcon) {
            intent = new Intent(getApplication(),MainActivity.class);
        } else if(view.getId() == R.id.searchIcon){
            intent = new Intent(getApplication(),Search.class);
        } else if(id == R.id.snsIcon){
            intent = new Intent(getApplication(),FeedTop.class);
        } else if(id == R.id.infoIcon){
            intent = new Intent(getApplication(),Information.class);
        } else if(id == R.id.mypageIcon){
            intent = new Intent(getApplication(),MypageTop.class);
        } else if (id == R.id.button1) {
            intent = new Intent(this, Facilities.class);
            intent.putExtra("inputText", "小金井図書館(南館1F)");
        } else if (id == R.id.button2) {
            intent = new Intent(this, Facilities.class);
            intent.putExtra("inputText", "ラーニングコモンズ(南館1F)");
        } else {
            return;
        }

        startActivity(intent);
    }

}
