package com.example.universe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReplyUpload extends AppCompatActivity implements View.OnClickListener{
    private GestureDetector gestureDetector;
    private String inputText;
    private int postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_upload);

        inputText = getIntent().getStringExtra("inputText");
        postId = getIntent().getIntExtra("postId", -1);

        ((ImageButton)findViewById(R.id.mapIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.searchIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.snsIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.infoIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.mypageIcon)).setOnClickListener(this);

        ((ImageView) findViewById(R.id.backImage)).setOnClickListener(this);
        ((Button)findViewById(R.id.replyButton)).setOnClickListener(this);
        ((Button)findViewById(R.id.mapAddButton)).setOnClickListener(this);

        LinearLayout mapLayout = findViewById(R.id.map);

        if (inputText == null || inputText.trim().isEmpty()) {
            mapLayout.setVisibility(View.GONE);
        } else {
            addMapLink(inputText);
        }

        EditText editText = findViewById(R.id.content);

        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        }

        gestureDetector = new GestureDetector(this, new ReplyUpload.SwipeGestureDetector());
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

    private String getUsername() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        return sharedPreferences.getString("username", "");
    }

    private void addReplyAndNavigate() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            String content = ((EditText)findViewById(R.id.content)).getText().toString();
            String user = getUsername();
            String time = new SimpleDateFormat("HH:mm MM/dd", Locale.getDefault()).format(new Date());

            RoomFacilitiesDatabase db = Room.databaseBuilder(getApplicationContext(), RoomFacilitiesDatabase.class, "nishikan")
                    .fallbackToDestructiveMigration()
                    .build();
            RepliesDAO dao = db.repliesdao();
            RoomDAO roomdao = db.roomdao();
            RepliesEntity newReply;

            if (inputText == null || inputText.trim().isEmpty()) {
                newReply = new RepliesEntity(postId,user,content,"","",time);
            } else {
                String imageName = roomdao.getImageByName(inputText);
                if (imageName.length() > 0) {
                    imageName = imageName.substring(0, imageName.length() - 1); //"w101image2"->"w101image"
                }
                newReply = new RepliesEntity(postId,user,content,inputText,imageName,time);
            }

            dao.insert(newReply);

            Intent intent = new Intent(getApplication(), FeedDetail.class);
            intent.putExtra("postId", postId);
            startActivity(intent);
            finish();
        });
    }

    private void addMapLink(String inputText) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            RoomFacilitiesDatabase db = Room.databaseBuilder(getApplicationContext(), RoomFacilitiesDatabase.class, "nishikan")
                    .fallbackToDestructiveMigration()
                    .build();
            RoomDAO dao = db.roomdao();
            String imageName = dao.getImageByName(inputText);
            if (imageName.length() > 0) {
                imageName = imageName.substring(0, imageName.length() - 1); //"w101image2"->"w101image"
            }
            int resourceId = getResources().getIdentifier(imageName, "drawable", getPackageName());

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView nameTextView = findViewById(R.id.name);
                    ImageView roomimageView = findViewById(R.id.roomimage);
                    nameTextView.setText(inputText);
                    roomimageView.setImageResource(resourceId);
                }
            });
        });
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
        } else if(view.getId() == R.id.replyButton){
            addReplyAndNavigate();
        } else if(view.getId() == R.id.backImage){
            finish();
        } else if(view.getId() == R.id.mapAddButton){
            Intent intent = new Intent(getApplication(),ReplyMapLink.class);
            intent.putExtra("postId", postId);
            startActivity(intent);
        }
    }
}