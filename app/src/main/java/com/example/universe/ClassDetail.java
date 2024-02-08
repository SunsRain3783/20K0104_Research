package com.example.universe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ClassDetail extends AppCompatActivity implements View.OnClickListener{
    private GestureDetector gestureDetector;
    private ImageView selectimage;
    private LinearLayout hiddenContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_detail);

        hiddenContent = findViewById(R.id.hidden_content);
        TextView textView1 = findViewById(R.id.text1);TextView textView2 = findViewById(R.id.text2);TextView textView3 = findViewById(R.id.text3);
        textView1.setPaintFlags(textView1.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);textView2.setPaintFlags(textView2.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);textView3.setPaintFlags(textView3.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        selectimage = findViewById(R.id.selectimage);

        ((ImageButton)findViewById(R.id.mapIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.searchIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.snsIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.infoIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.mypageIcon)).setOnClickListener(this);
        ((ImageView) findViewById(R.id.backImage)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.select)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.select2)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.select3)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.map)).setOnClickListener(this);

        gestureDetector = new GestureDetector(this, new ClassDetail.SwipeGestureDetector());
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
            if (hiddenContent.getVisibility() == View.GONE) {
                hiddenContent.setVisibility(View.VISIBLE);
                selectimage.setImageResource(R.drawable.up);
            } else {
                hiddenContent.setVisibility(View.GONE);
                selectimage.setImageResource(R.drawable.down);
            }
        } else if(view.getId() == R.id.select2){
            Intent intent = new Intent(getApplication(),QustionChat.class);
            startActivity(intent);
        } else if(view.getId() == R.id.select3){
            Intent intent = new Intent(getApplication(),SyllabusDetail.class);
            startActivity(intent);
        } else if(view.getId() == R.id.map){
            Intent intent = new Intent(ClassDetail.this, Facilities.class);
            intent.putExtra("inputText", "W103(西館1F)");
            startActivity(intent);
        }
    }
}