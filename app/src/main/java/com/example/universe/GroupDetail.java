package com.example.universe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GroupDetail extends AppCompatActivity implements View.OnClickListener{

    private GestureDetector gestureDetector;
    private int groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);

        ((ImageButton)findViewById(R.id.mapIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.searchIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.snsIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.infoIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.mypageIcon)).setOnClickListener(this);
        ((ImageView) findViewById(R.id.backImage)).setOnClickListener(this);
        ((Button) findViewById(R.id.groupentry)).setOnClickListener(this);

        groupId = getIntent().getIntExtra("groupId", -1);

        gestureDetector = new GestureDetector(this, new GroupDetail.SwipeGestureDetector());

        loadGroup();
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

    private void loadGroup() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            RoomFacilitiesDatabase db = Room.databaseBuilder(getApplicationContext(), RoomFacilitiesDatabase.class, "nishikan")
                    .fallbackToDestructiveMigration()
                    .build();
            GroupDAO groupDAO = db.groupdao();
            GroupEntity groupEntity = groupDAO.getGroupById(groupId);

            runOnUiThread(() -> {
                ImageView groupImageView = findViewById(R.id.groupimage);
                ImageView openImageView = findViewById(R.id.openImage);
                TextView openTextView = findViewById(R.id.openText);
                TextView groupNameView = findViewById(R.id.groupname);
                TextView groupMembersView = findViewById(R.id.groupmembers);
                TextView detailTextView = findViewById(R.id.detailText);
                Button groupEntry = findViewById(R.id.groupentry);

                if(groupEntity.getImage() != null && !groupEntity.getImage().isEmpty()){
                    int resourceImageId = getResources().getIdentifier(groupEntity.getImage(), "drawable", getPackageName());
                    groupImageView.setImageResource(resourceImageId);
                }

                if(groupEntity.getOpenFlag() == 1){
                    int resourceImageId = getResources().getIdentifier("closed", "drawable", getPackageName());
                    openImageView.setImageResource(resourceImageId);
                    openTextView.setText("このコミュニティに参加するには，リクエストの承認が必要です");
                    groupEntry.setText("参加リクエストをする");
                }

                if(groupEntity.getParticipateFlag() == 1){
                    groupEntry.setText("参加しています");
                }

                String[] members = groupEntity.getParticipants().split(",");
                String number = members.length + " 人のメンバー";
                groupMembersView.setText(number);
                groupNameView.setText(groupEntity.getGroupName());
                detailTextView.setText(groupEntity.getDetail());
            });
        });
    }

    private void groupEntry() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            RoomFacilitiesDatabase db = Room.databaseBuilder(getApplicationContext(), RoomFacilitiesDatabase.class, "nishikan")
                    .fallbackToDestructiveMigration()
                    .build();
            GroupDAO groupDAO = db.groupdao();
            GroupEntity groupEntity = groupDAO.getGroupById(groupId);

            if(groupEntity.getOpenFlag() == 0 && groupEntity.getParticipateFlag() == 0){ //オープン　かつ　参加していない
                groupEntity.setParticipateFlag(1);
                groupDAO.update(groupEntity);
                runOnUiThread(() -> {
                    Intent intent = new Intent(getApplication(),FeedTop.class);
                    intent.putExtra("selectedTab", 1);
                    startActivity(intent);
                });
            }
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
        } else if (view.getId() == R.id.backImage) {
            finish();
        } else if (view.getId() == R.id.groupentry) {
            groupEntry();
        }
    }
}