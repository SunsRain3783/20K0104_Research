package com.example.universe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FeedDetail extends AppCompatActivity implements View.OnClickListener{
    private int postId;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_detail);

        ((ImageButton)findViewById(R.id.mapIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.searchIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.snsIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.infoIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.mypageIcon)).setOnClickListener(this);
        ((ImageView) findViewById(R.id.backImage)).setOnClickListener(this);
        ((ImageView) findViewById(R.id.hearticon)).setOnClickListener(this);
        ((ImageView) findViewById(R.id.replyicon)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.result)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.rep_result)).setOnClickListener(this);
        ((ImageView)findViewById(R.id.postAddButton)).setOnClickListener(this);

        gestureDetector = new GestureDetector(this, new FeedDetail.SwipeGestureDetector());

        postId = getIntent().getIntExtra("postId", -1);
        loadPosts();
        loadReplies();
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

    private void loadPosts() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            RoomFacilitiesDatabase db = Room.databaseBuilder(getApplicationContext(), RoomFacilitiesDatabase.class, "nishikan")
                    .fallbackToDestructiveMigration()
                    .build();
            PostsDAO postsDAO = db.postsdao();
            PostsEntity post = postsDAO.getPostById(postId);

            runOnUiThread(() -> {
                if (post != null) {
                    TextView usernameView = findViewById(R.id.username);
                    TextView timeView = findViewById(R.id.time);
                    TextView contentView = findViewById(R.id.content);
                    ImageView imageView = findViewById(R.id.image);
                    ImageView roomimageView = findViewById(R.id.roomimage);
                    TextView nameView = findViewById(R.id.name);
                    TextView likeView = findViewById(R.id.like);
                    LinearLayout resultView = findViewById(R.id.result);

                    if (post.getImage() != null && !post.getImage().equals("")) {
                        int resourceImageId = getResources().getIdentifier(post.getImage(), "drawable", getPackageName());
                        imageView.setImageResource(resourceImageId);
                    } else {
                        imageView.setVisibility(View.GONE);
                    }

                    if (post.getName() != null && !post.getName().equals("")) {
                        int resourceRoomimageId = getResources().getIdentifier(post.getRoomimage(), "drawable", getPackageName());
                        roomimageView.setImageResource(resourceRoomimageId);
                    } else {
                        roomimageView.setVisibility(View.GONE);
                        resultView.setVisibility(View.GONE);
                    }

                    usernameView.setText(post.getUser());
                    timeView.setText(post.getTime());
                    contentView.setText(post.getContent());
                    nameView.setText(post.getName());
                    likeView.setText(String.valueOf(post.getLike()));
                }
            });
        });
    }



    private void loadReplies() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            RoomFacilitiesDatabase db = Room.databaseBuilder(getApplicationContext(), RoomFacilitiesDatabase.class, "nishikan")
                    .fallbackToDestructiveMigration()
                    .build();

            RepliesDAO repliesDAO = db.repliesdao();
            List<RepliesEntity> repliesList = repliesDAO.getRepliesForPost(postId);
            runOnUiThread(() -> {
                if (repliesList != null && !repliesList.isEmpty()) {
                    RepliesEntity reply = repliesList.get(repliesList.size() - 1); // 最後のリプライを取得
                    TextView usernameView = findViewById(R.id.rep_username);
                    TextView timeView = findViewById(R.id.rep_time);
                    TextView contentView = findViewById(R.id.rep_content);
                    ImageView roomimageView = findViewById(R.id.rep_roomimage);
                    TextView nameView = findViewById(R.id.rep_name);
                    LinearLayout resultView = findViewById(R.id.rep_result);

                    usernameView.setText(reply.getUser());
                    timeView.setText(reply.getTime());
                    contentView.setText(reply.getContent());
                    nameView.setText(reply.getName());

                    if(!reply.getName().equals("")) {
                        int resourceRoomimageId = getResources().getIdentifier(reply.getRoomimage(), "drawable", getPackageName());
                        roomimageView.setImageResource(resourceRoomimageId);
                    } else {
                        resultView.setVisibility(View.GONE);
                    }

                    if(postId == 18 || postId == 16 || postId == 20){
                        LinearLayout replyLayout2 = findViewById(R.id.reply2);
                        replyLayout2.setVisibility(View.GONE);
                    }
                } else {
                    LinearLayout replyLayout = findViewById(R.id.reply);
                    LinearLayout replyLayout2 = findViewById(R.id.reply2);
                    replyLayout.setVisibility(View.GONE);
                    replyLayout2.setVisibility(View.GONE);
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
        } else if(view.getId() == R.id.backImage){
            finish();
        } else if(view.getId() == R.id.result){
            TextView nameView = findViewById(R.id.name);
            String text = nameView.getText().toString();
            Intent intent = new Intent(FeedDetail.this, Facilities.class);
            intent.putExtra("inputText", text);
            startActivity(intent);
        } else if(view.getId() == R.id.rep_result){
            TextView nameView = findViewById(R.id.rep_name);
            String text = nameView.getText().toString();
            Intent intent = new Intent(FeedDetail.this, Facilities.class);
            intent.putExtra("inputText", text);
            startActivity(intent);
        } else if (view.getId() == R.id.hearticon) {
            ImageView heartView = findViewById(R.id.hearticon);
            TextView likeView = findViewById(R.id.like);
            String likes = likeView.getText().toString();
            int x = Integer.parseInt(likes);

            if ("active".equals(heartView.getTag())) {
                int resourceImageId = getResources().getIdentifier("hearticon", "drawable", getPackageName());
                heartView.setImageResource(resourceImageId);
                heartView.setTag("inactive");
                x = x - 1;
            } else {
                int resourceImageId = getResources().getIdentifier("hearticon2", "drawable", getPackageName());
                heartView.setImageResource(resourceImageId);
                heartView.setTag("active");
                x = x + 1;
            }
            likeView.setText(String.valueOf(x));
        } else if(view.getId() == R.id.postAddButton || view.getId() == R.id.replyicon){
            Intent intent = new Intent(getApplication(),ReplyUpload.class);
            intent.putExtra("postId", postId);
            startActivity(intent);
        }
    }
}