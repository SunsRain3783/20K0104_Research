package com.example.universe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FeedTop extends AppCompatActivity implements View.OnClickListener{
    private TabHost tabHost;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_top);

        ((ImageButton)findViewById(R.id.mapIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.searchIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.snsIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.infoIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.mypageIcon)).setOnClickListener(this);

        ((LinearLayout)findViewById(R.id.post1)).setOnClickListener(this);((LinearLayout)findViewById(R.id.post2)).setOnClickListener(this);((LinearLayout)findViewById(R.id.post3)).setOnClickListener(this);
        ((LinearLayout)findViewById(R.id.post4)).setOnClickListener(this);((LinearLayout)findViewById(R.id.post5)).setOnClickListener(this);((LinearLayout)findViewById(R.id.post6)).setOnClickListener(this);
        ((LinearLayout)findViewById(R.id.post7)).setOnClickListener(this);((LinearLayout)findViewById(R.id.post8)).setOnClickListener(this);((LinearLayout)findViewById(R.id.post9)).setOnClickListener(this);
        ((LinearLayout)findViewById(R.id.post10)).setOnClickListener(this);((LinearLayout)findViewById(R.id.post11)).setOnClickListener(this);((LinearLayout)findViewById(R.id.post12)).setOnClickListener(this);
        ((LinearLayout)findViewById(R.id.post13)).setOnClickListener(this);((LinearLayout)findViewById(R.id.post14)).setOnClickListener(this);((LinearLayout)findViewById(R.id.post15)).setOnClickListener(this);
        ((LinearLayout)findViewById(R.id.post16)).setOnClickListener(this);((LinearLayout)findViewById(R.id.post17)).setOnClickListener(this);((LinearLayout)findViewById(R.id.post18)).setOnClickListener(this);
        ((LinearLayout)findViewById(R.id.post19)).setOnClickListener(this);((LinearLayout)findViewById(R.id.post20)).setOnClickListener(this);
        ((ImageView)findViewById(R.id.postAddButton)).setOnClickListener(this);

        ((LinearLayout)findViewById(R.id.groupresult1)).setOnClickListener(this);((LinearLayout)findViewById(R.id.groupresult2)).setOnClickListener(this);((LinearLayout)findViewById(R.id.groupresult3)).setOnClickListener(this);
        ((LinearLayout)findViewById(R.id.groupresult4)).setOnClickListener(this);((LinearLayout)findViewById(R.id.groupresult5)).setOnClickListener(this);((LinearLayout)findViewById(R.id.groupresult6)).setOnClickListener(this);
        ((LinearLayout)findViewById(R.id.par_groupresult)).setOnClickListener(this);

        tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.setup();
        TabWidget tabWidget = tabHost.getTabWidget();
        TabHost.TabSpec spec1 = tabHost.newTabSpec("Tab 1");
        spec1.setIndicator("フィード");
        spec1.setContent(R.id.tab1);
        tabHost.addTab(spec1);
        TabHost.TabSpec spec2 = tabHost.newTabSpec("Tab 2");
        spec2.setIndicator("グループ");
        spec2.setContent(R.id.tab2);
        tabHost.addTab(spec2);
        updateTabColors(tabHost); // 画面を最初に開いた時にも色を設定
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                updateTabColors(tabHost);
                switchVisibilityBasedOnCurrentTab(tabId);
            }
        });
        int selectedTab = getIntent().getIntExtra("selectedTab", 0);
        tabHost.setCurrentTab(selectedTab);

        gestureDetector = new GestureDetector(this, new FeedTop.SwipeGestureDetector());

        loadPosts();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadGroups();
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
                Intent intent = new Intent(FeedTop.this, Search.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                return true;
            }
            // 右から左へのスワイプ
            if (Math.abs(diffX) > Math.abs(diffY) && diffX < 0) {
                Intent intent = new Intent(FeedTop.this, Information.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            }

            return false;
        }
    }

    private void switchVisibilityBasedOnCurrentTab(String tabId) {
        ImageView postAddButton = findViewById(R.id.postAddButton);
        ImageView groupMakeButton = findViewById(R.id.groupMakeButton);

        if ("Tab 1".equals(tabId)) {
            postAddButton.setVisibility(View.VISIBLE);
            groupMakeButton.setVisibility(View.GONE);
        } else if ("Tab 2".equals(tabId)) {
            postAddButton.setVisibility(View.GONE);
            groupMakeButton.setVisibility(View.VISIBLE);
        }
    }

    private void loadPosts() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            RoomFacilitiesDatabase db = Room.databaseBuilder(getApplicationContext(), RoomFacilitiesDatabase.class, "nishikan")
                    .fallbackToDestructiveMigration()
                    .build();
            PostsDAO postsDAO = db.postsdao();
            List<PostsEntity> latestPosts = postsDAO.getLatestPosts();
            runOnUiThread(() -> {
                for (int i = 0; i < latestPosts.size(); i++) {
                    PostsEntity post = latestPosts.get(i);
                    String postIdViewName = "post" + (i + 1);
                    String usernameId = "username" + (i + 1);
                    String timeId = "time" + (i + 1);
                    String contentId = "content" + (i + 1);
                    String imageId = "image" + (i + 1);
                    String roomimageId = "roomimage" + (i + 1);
                    String nameId = "name" + (i + 1);
                    String likeId = "like" + (i + 1);
                    String resultId = "result" + (i + 1);

                    int resPostId = getResources().getIdentifier(postIdViewName, "id", getPackageName());
                    int resUsernameId = getResources().getIdentifier(usernameId, "id", getPackageName());
                    int resTimeId = getResources().getIdentifier(timeId, "id", getPackageName());
                    int resContentId = getResources().getIdentifier(contentId, "id", getPackageName());
                    int resImageId = getResources().getIdentifier(imageId, "id", getPackageName());
                    int resRoomimageId = getResources().getIdentifier(roomimageId, "id", getPackageName());
                    int resNameId = getResources().getIdentifier(nameId, "id", getPackageName());
                    int resLikeId = getResources().getIdentifier(likeId, "id", getPackageName());
                    int resResultId = getResources().getIdentifier(resultId, "id", getPackageName());

                    LinearLayout postView = findViewById(resPostId);
                    TextView usernameView = findViewById(resUsernameId);
                    TextView timeView = findViewById(resTimeId);
                    TextView contentView = findViewById(resContentId);
                    ImageView imageView = findViewById(resImageId);
                    ImageView roomimageView = findViewById(resRoomimageId);
                    TextView nameView = findViewById(resNameId);
                    TextView likeView = findViewById(resLikeId);
                    LinearLayout resultView = findViewById(resResultId);

                    postView.setTag(post.getId()); // エンティティのIDをタグとして設定

                    if(post.getImage().equals("")){
                        imageView.setVisibility(View.GONE);
                    } else{
                        int resourceImageId = getResources().getIdentifier(post.getImage(), "drawable", getPackageName());
                        imageView.setImageResource(resourceImageId);
                    }

                    if(post.getName().equals("")){
                        resultView.setVisibility(View.GONE);
                    } else{
                        int resourceRoomimageId = getResources().getIdentifier(post.getRoomimage(), "drawable", getPackageName());
                        roomimageView.setImageResource(resourceRoomimageId);
                    }

                    usernameView.setText(post.getUser());
                    //timeView.setText(post.getTime());
                    contentView.setText(post.getContent());
                    nameView.setText(post.getName());

                    likeView.setText(String.valueOf(post.getLike()));
                }
            });
        });
    }

    private void loadGroups() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            RoomFacilitiesDatabase db = Room.databaseBuilder(getApplicationContext(), RoomFacilitiesDatabase.class, "nishikan")
                    .fallbackToDestructiveMigration()
                    .build();
            GroupDAO groupDAO = db.groupdao();
            List<GroupEntity> groups = groupDAO.getAll();
            LinearLayout participateLayout = findViewById(R.id.participateLayout);

            runOnUiThread(() -> {
                int count = 0;
                for (int i = 0; i < groups.size() && i < 6; i++) { // 最大6項目まで考慮
                    GroupEntity group = groups.get(i);

                    String groupIdViewName = "groupresult" + (i + 1);
                    String groupImageIdName = "groupimage" + (i + 1);
                    String groupNameIdName = "groupname" + (i + 1);
                    String groupMembersIdName = "groupmembers" + (i + 1);

                    int resGroupId = getResources().getIdentifier(groupIdViewName, "id", getPackageName());
                    int resGroupImageId = getResources().getIdentifier(groupImageIdName, "id", getPackageName());
                    int resGroupNameId = getResources().getIdentifier(groupNameIdName, "id", getPackageName());
                    int resGroupMembersId = getResources().getIdentifier(groupMembersIdName, "id", getPackageName());

                    LinearLayout groupView = findViewById(resGroupId);
                    ImageView groupImageView = findViewById(resGroupImageId);
                    TextView groupNameView = findViewById(resGroupNameId);
                    TextView groupMembersView = findViewById(resGroupMembersId);

                    LinearLayout par_groupView = findViewById(R.id.par_groupresult);
                    ImageView par_groupImageView = findViewById(R.id.par_groupimage);
                    TextView par_groupNameView = findViewById(R.id.par_groupname);
                    TextView par_groupMembersView = findViewById(R.id.par_groupmembers);

                    String imageName = group.getImage();
                    imageName = imageName.substring(0, imageName.length() - 1); //"w101image2"->"w101image"
                    int resourceImageId = getResources().getIdentifier(imageName, "drawable", getPackageName());
                    String[] members = group.getParticipants().split(",");
                    String number = members.length + " 人のメンバー";

                    if(group.getParticipateFlag() == 1){ //グループに参加している
                        par_groupView.setTag(group.getId()); // エンティティのIDをタグとして設定
                        par_groupImageView.setImageResource(resourceImageId);
                        par_groupNameView.setText(group.getGroupName());
                        par_groupMembersView.setText(number);

                        groupView.setVisibility(View.GONE);

                        count++;
                    }else{ //参加していない
                        groupView.setTag(group.getId()); // エンティティのIDをタグとして設定
                        groupImageView.setImageResource(resourceImageId);
                        groupNameView.setText(group.getGroupName());
                        groupMembersView.setText(number);
                    }
                }
                if(count == 0){
                    participateLayout.setVisibility(View.GONE);
                }
            });
        });
    }

    private void updateTabColors(TabHost tabHost) {
        TabWidget tabWidget = tabHost.getTabWidget();

        for (int i = 0; i < tabWidget.getChildCount(); i++) {
            TextView tv = (TextView) tabWidget.getChildAt(i).findViewById(android.R.id.title);
            if (i == tabHost.getCurrentTab()) {
                tv.setTextColor(Color.parseColor("#333333")); // 選択中のタブの色
            } else {
                tv.setTextColor(Color.parseColor("#888888")); // その他のタブの色
            }
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
        } else if(view.getId() == R.id.post1 || view.getId() == R.id.post2 || view.getId() == R.id.post3 || view.getId() == R.id.post4 || view.getId() == R.id.post5 || view.getId() == R.id.post6
        || view.getId() == R.id.post7 || view.getId() == R.id.post8 || view.getId() == R.id.post9 || view.getId() == R.id.post10 || view.getId() == R.id.post11 || view.getId() == R.id.post12
        || view.getId() == R.id.post13 || view.getId() == R.id.post14 || view.getId() == R.id.post15 || view.getId() == R.id.post16 || view.getId() == R.id.post17 || view.getId() == R.id.post18 || view.getId() == R.id.post19 || view.getId() == R.id.post20){
            int entityId = (int) view.getTag(); // タグからエンティティのIDを取得
            Intent intent = new Intent(getApplication(), FeedDetail.class);
            intent.putExtra("postId", entityId);
            startActivity(intent);
        } else if(view.getId() == R.id.groupresult1 || view.getId() == R.id.groupresult2 || view.getId() == R.id.groupresult3 || view.getId() == R.id.groupresult4 || view.getId() == R.id.groupresult5 || view.getId() == R.id.groupresult6){
            int entityId = (int) view.getTag(); // タグからエンティティのIDを取得
            Intent intent = new Intent(getApplication(), GroupDetail.class);
            intent.putExtra("groupId", entityId);
            startActivity(intent);
        } else if(view.getId() == R.id.postAddButton){
            Intent intent = new Intent(getApplication(),FeedUpload.class);
            startActivity(intent);
        } else if(view.getId() == R.id.par_groupresult){
            int entityId = (int) view.getTag(); // タグからエンティティのIDを取得
            Intent intent = new Intent(getApplication(), GroupChat.class);
            intent.putExtra("groupId", entityId);
            startActivity(intent);
        }
    }
}