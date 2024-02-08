package com.example.universe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

public class Information extends AppCompatActivity implements View.OnClickListener{
    private List<InformationEntity> list;
    private TextView text1;
    private TextView text2;
    private TextView text3;
    private TextView text4;
    private TextView text5;
    private TextView text6;
    private TextView text7;
    private TextView text8;
    private TextView text9;
    private TextView text10;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        ((ImageButton)findViewById(R.id.mapIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.searchIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.snsIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.infoIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.mypageIcon)).setOnClickListener(this);

        ((LinearLayout)findViewById(R.id.info1)).setOnClickListener(this);((LinearLayout)findViewById(R.id.info2)).setOnClickListener(this);((LinearLayout)findViewById(R.id.info3)).setOnClickListener(this);
        ((LinearLayout)findViewById(R.id.info4)).setOnClickListener(this);((LinearLayout)findViewById(R.id.info5)).setOnClickListener(this);((LinearLayout)findViewById(R.id.info6)).setOnClickListener(this);
        ((LinearLayout)findViewById(R.id.info7)).setOnClickListener(this);((LinearLayout)findViewById(R.id.info8)).setOnClickListener(this);((LinearLayout)findViewById(R.id.info9)).setOnClickListener(this);((LinearLayout)findViewById(R.id.info10)).setOnClickListener(this);

        text1 = findViewById(R.id.text1);text2 = findViewById(R.id.text2);text3 = findViewById(R.id.text3);text4 = findViewById(R.id.text4);
        text5 = findViewById(R.id.text5);text6 = findViewById(R.id.text6);text7 = findViewById(R.id.text7);text8 = findViewById(R.id.text8);
        text9 = findViewById(R.id.text9);text10 = findViewById(R.id.text10);

        gestureDetector = new GestureDetector(this, new Information.SwipeGestureDetector());
        loaddb();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loaddb();  // finish()で戻った際に即座にデータベースを更新
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
                Intent intent = new Intent(Information.this, FeedTop.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                return true;
            }
            // 右から左へのスワイプ
            if (Math.abs(diffX) > Math.abs(diffY) && diffX < 0) {
                Intent intent = new Intent(Information.this, MypageTop.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            }

            return false;
        }
    }

    private void setTextColorBasedOnFlag(TextView textView, InformationEntity entity) {
        if (entity.getFlag() == 1) {
            textView.setTextColor(Color.GRAY); // 既読済み
        } else {
            textView.setTextColor(Color.BLACK); // 未読
        }
    }

    private void loaddb(){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable(){
            @Override
            public void run(){
                RoomFacilitiesDatabase db = Room.databaseBuilder(getApplicationContext(), RoomFacilitiesDatabase.class, "nishikan")
                        .fallbackToDestructiveMigration()
                        .build();
                try {
                    InformationDAO dao = db.informationdao();
                    list = dao.getAll();  // テーブルを取得

                    if(list.size() != 0){
                        runOnUiThread(() -> updateUI());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    db.close();
                }
            }
        });
    }

    private void updateUI() {
        int lastIndex = list.size() - 1;

        setTextViewData(text1, lastIndex, R.id.label1, R.id.time1);
        setTextViewData(text2, lastIndex - 1, R.id.label2, R.id.time2);
        setTextViewData(text3, lastIndex - 2, R.id.label3, R.id.time3);
        setTextViewData(text4, lastIndex - 3, R.id.label4, R.id.time4);
        setTextViewData(text5, lastIndex - 4, R.id.label5, R.id.time5);
        setTextViewData(text6, lastIndex - 5, R.id.label6, R.id.time6);
        setTextViewData(text7, lastIndex - 6, R.id.label7, R.id.time7);
        setTextViewData(text8, lastIndex - 7, R.id.label8, R.id.time8);
        setTextViewData(text9, lastIndex - 8, R.id.label9, R.id.time9);
        setTextViewData(text10, lastIndex - 9, R.id.label10, R.id.time10);
    }

    private void setTextViewData(TextView textView, int index, int labelId, int timeId) {
        if (index < 0 || index >= list.size()) return;

        InformationEntity entity = list.get(index);
        textView.setText(entity.getTitle());
        setTextColorBasedOnFlag(textView, entity);

        TextView labelView = findViewById(labelId);
        switch (entity.getLabel()) {
            case "重要":
                labelView.setBackgroundColor(Color.RED);
                break;
            case "イベント":
                labelView.setBackgroundColor(Color.GREEN);
                break;
            case "その他":
                labelView.setBackgroundColor(Color.GRAY);
                break;
        }
        labelView.setText(entity.getLabel());

        TextView timeView = findViewById(timeId);
        timeView.setText(entity.getTime());
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
        } else if(view.getId() == R.id.info1){
            String text = text1.getText().toString();
            Intent intent = new Intent(this, InformationDetail.class);
            intent.putExtra("inputText", text);
            startActivity(intent);
        } else if(view.getId() == R.id.info2){
            String text = text2.getText().toString();
            Intent intent = new Intent(this, InformationDetail.class);
            intent.putExtra("inputText", text);
            startActivity(intent);
        } else if(view.getId() == R.id.info3){
            String text = text3.getText().toString();
            Intent intent = new Intent(this, InformationDetail.class);
            intent.putExtra("inputText", text);
            startActivity(intent);
        } else if(view.getId() == R.id.info4){
            String text = text4.getText().toString();
            Intent intent = new Intent(this, InformationDetail.class);
            intent.putExtra("inputText", text);
            startActivity(intent);
        } else if(view.getId() == R.id.info5){
            String text = text5.getText().toString();
            Intent intent = new Intent(this, InformationDetail.class);
            intent.putExtra("inputText", text);
            startActivity(intent);
        } else if(view.getId() == R.id.info6){
            String text = text6.getText().toString();
            Intent intent = new Intent(this, InformationDetail.class);
            intent.putExtra("inputText", text);
            startActivity(intent);
        } else if(view.getId() == R.id.info7){
            String text = text7.getText().toString();
            Intent intent = new Intent(this, InformationDetail.class);
            intent.putExtra("inputText", text);
            startActivity(intent);
        } else if(view.getId() == R.id.info8){
            String text = text8.getText().toString();
            Intent intent = new Intent(this, InformationDetail.class);
            intent.putExtra("inputText", text);
            startActivity(intent);
        } else if(view.getId() == R.id.info9){
            String text = text9.getText().toString();
            Intent intent = new Intent(this, InformationDetail.class);
            intent.putExtra("inputText", text);
            startActivity(intent);
        } else if(view.getId() == R.id.info10){
            String text = text10.getText().toString();
            Intent intent = new Intent(this, InformationDetail.class);
            intent.putExtra("inputText", text);
            startActivity(intent);
        }
    }
}