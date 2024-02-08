package com.example.universe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InformationDetail extends AppCompatActivity implements View.OnClickListener{


    private InformationEntity info;
    private TextView titleText, detailText, timeText, label, nameText;
    private ImageView facilityImage;
    private LinearLayout mapLayout;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_detail);

        ((ImageButton)findViewById(R.id.mapIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.searchIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.snsIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.infoIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.mypageIcon)).setOnClickListener(this);

        ((ImageView) findViewById(R.id.backImage)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.map)).setOnClickListener(this);

        titleText = findViewById(R.id.title);
        detailText = findViewById(R.id.detail);
        timeText = findViewById(R.id.time);
        label = findViewById(R.id.label);
        nameText = findViewById(R.id.name);
        facilityImage = findViewById(R.id.image);
        mapLayout = findViewById(R.id.map);

        gestureDetector = new GestureDetector(this, new InformationDetail.SwipeGestureDetector());

        String inputTitle = getIntent().getStringExtra("inputText");
        loaddb(inputTitle);
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

    private void loaddb(String inputTitle) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                RoomFacilitiesDatabase db = Room.databaseBuilder(getApplicationContext(), RoomFacilitiesDatabase.class, "nishikan")
                        .fallbackToDestructiveMigration()
                        .build();
                InformationDAO dao = db.informationdao();

                InformationEntity entity = dao.findByTitle(inputTitle);
                if (entity != null) {
                    // UI更新のための情報を保存
                    info = entity;

                    // 既読フラグを1に更新
                    entity.setFlag(1);
                    dao.update(entity);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateUI();
                        }
                    });
                }
            }
        });
    }

    private void updateUI() {
        titleText.setText(info.getTitle());
        detailText.setText(info.getDetail());
        timeText.setText(info.getTime());
        label.setText(info.getLabel());

        label.setText(info.getLabel());
        switch (info.getLabel()) {
            case "重要":
                label.setBackgroundColor(Color.RED);
                break;
            case "イベント":
                label.setBackgroundColor(Color.GREEN);
                break;
            case "その他":
                label.setBackgroundColor(Color.GRAY);
                break;
        }

        if (info.getName() != null && !info.getName().isEmpty() && info.getImage() != null && !info.getImage().isEmpty()) {
            nameText.setText(info.getName());
            int resId = getResources().getIdentifier(info.getImage(), "drawable", getPackageName());
            facilityImage.setImageResource(resId);
            mapLayout.setVisibility(View.VISIBLE);
        } else {
            mapLayout.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View view){  //ボタンをクリックしたときの挙動
        if(view.getId() == R.id.mapIcon) {
            Intent intent = new Intent(getApplication(),MainActivity.class);
            startActivity(intent);
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
        } else if(view.getId() == R.id.map){
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(new Runnable(){
                @Override
                public void run(){
                    RoomFacilitiesDatabase db = Room.databaseBuilder(getApplicationContext(),RoomFacilitiesDatabase.class,"nishikan").build();
                    RoomDAO dao = db.roomdao();

                    String text = nameText.getText().toString();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(InformationDetail.this, Facilities.class);
                            intent.putExtra("inputText", text);
                            startActivity(intent);
                        }
                    });
                }
            });
        }
    }
}