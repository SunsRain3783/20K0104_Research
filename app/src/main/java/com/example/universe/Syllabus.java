package com.example.universe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Syllabus extends AppCompatActivity implements View.OnClickListener{
    private GestureDetector gestureDetector;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syllabus);

        ((ImageButton)findViewById(R.id.mapIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.searchIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.snsIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.infoIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.mypageIcon)).setOnClickListener(this);
        ((ImageView) findViewById(R.id.backImage)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.select)).setOnClickListener(this);

        searchView = findViewById(R.id.search_view);
        searchView.setIconified(false); //最初から拡張させる(ヒントテキストを表示)
        AutoCompleteTextView autoCompleteTextView = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setTextColor(Color.BLACK);
        autoCompleteTextView.clearFocus(); //キーボードを閉じる
        searchView.setQueryHint("キーワードを入力してください");

        gestureDetector = new GestureDetector(this, new Syllabus.SwipeGestureDetector());
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

            // 左から右へのスワイプ
            if (Math.abs(diffX) > Math.abs(diffY) && diffX > 0) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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
        } else if(view.getId() == R.id.backImage){
            finish();
        } else if(view.getId() == R.id.select){
            Intent intent = new Intent(getApplication(),SyllabusDetail.class);
            startActivity(intent);
        }
    }
}