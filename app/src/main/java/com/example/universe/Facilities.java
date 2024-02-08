package com.example.universe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Facilities extends AppCompatActivity implements View.OnClickListener{
    private Button tag1,tag2,tag3,tag4,tag5,tag6,tag7;
    private List<Button> tagBtnList = new ArrayList<>();
    private List<String> tagList;
    private TabHost tabHost;
    private TextView label,title,time,topname,seatinfo,timeinfo,elecinfo,detailText;
    private ImageView imageView;
    private LinearLayout info;
    private RoomFacilitiesDatabase dbInstance;
    private GestureDetector gestureDetector;
    private String inputText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facilities);

        ((ImageButton)findViewById(R.id.mapIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.searchIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.snsIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.infoIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.mypageIcon)).setOnClickListener(this);

        ((ImageView) findViewById(R.id.backImage)).setOnClickListener(this);
        ((Button) findViewById(R.id.tag1)).setOnClickListener(this);((Button) findViewById(R.id.tag2)).setOnClickListener(this);((Button) findViewById(R.id.tag3)).setOnClickListener(this);((Button) findViewById(R.id.tag4)).setOnClickListener(this);
        ((Button) findViewById(R.id.tag5)).setOnClickListener(this);((Button) findViewById(R.id.tag6)).setOnClickListener(this);((Button) findViewById(R.id.tag7)).setOnClickListener(this);((Button) findViewById(R.id.addtag)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.info)).setOnClickListener(this);((LinearLayout) findViewById(R.id.post)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.share)).setOnClickListener(this);((Button) findViewById(R.id.map)).setOnClickListener(this);
        title = findViewById(R.id.title);
        topname = findViewById(R.id.topname);
        seatinfo = findViewById(R.id.seatinfo);
        timeinfo = findViewById(R.id.timeinfo);
        elecinfo = findViewById(R.id.elecinfo);
        detailText = findViewById(R.id.detail);
        imageView = findViewById(R.id.image);
        tag1 = findViewById(R.id.tag1);tagBtnList.add(tag1);tag2 = findViewById(R.id.tag2);tagBtnList.add(tag2);tag3 = findViewById(R.id.tag3);tagBtnList.add(tag3);tag4 = findViewById(R.id.tag4);tagBtnList.add(tag4);
        tag5 = findViewById(R.id.tag5);tagBtnList.add(tag5);tag6 = findViewById(R.id.tag6);tagBtnList.add(tag6);tag7 = findViewById(R.id.tag7);tagBtnList.add(tag7);
        setTagButtonOnClickListener(tag1);setTagButtonOnClickListener(tag2);setTagButtonOnClickListener(tag3);setTagButtonOnClickListener(tag4);
        setTagButtonOnClickListener(tag5);setTagButtonOnClickListener(tag6);setTagButtonOnClickListener(tag7);

        tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.setup();
        TabWidget tabWidget = tabHost.getTabWidget();
        TabHost.TabSpec spec1 = tabHost.newTabSpec("Tab 1");
        spec1.setIndicator("概要");
        spec1.setContent(R.id.tab1);
        tabHost.addTab(spec1);
        TabHost.TabSpec spec2 = tabHost.newTabSpec("Tab 2");
        spec2.setIndicator("アクティビティ");
        spec2.setContent(R.id.tab2);
        tabHost.addTab(spec2);
        updateTabColors(tabHost); // 画面を最初に開いた時にも色を設定
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                updateTabColors(tabHost); // タブが変わった時にも色を設定
            }
        });

        dbInstance = Room.databaseBuilder(getApplicationContext(), RoomFacilitiesDatabase.class, "nishikan")
                .fallbackToDestructiveMigration()
                .build();

        gestureDetector = new GestureDetector(this, new Facilities.SwipeGestureDetector());

        inputText = getIntent().getStringExtra("inputText");

        init();
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

    private void init() {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            loadroomdb();
            loadinfodb();
            loadPosts();
        });
    }

    private void loadroomdb(){

        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            RoomDAO dao = dbInstance.roomdao();
            RoomEntity entity = dao.findByName(inputText);
            String tags = entity.getTag();
            String seat = entity.getSeat();
            String time = entity.getTime();
            String elec = entity.getElec();
            String detail = entity.getDetail();
            String imageName = entity.getImage();
            int resourceId = getResources().getIdentifier(imageName, "drawable", getPackageName());
            tagList = new ArrayList<>(Arrays.asList(tags.split(","))); //tagをカンマ区切りでリストに保存->先頭からtag1,tag2,...にセット

            if (entity != null) {
                runOnUiThread(() -> {
                    topname.setText(inputText);
                    for (int i = 0; i < 7; i++) {
                        tagBtnList.get(i).setText(tagList.get(i));
                    }
                    imageView.setImageResource(resourceId);
                    seatinfo.setText("席数\n" + seat);
                    timeinfo.setText("利用時間\n" + time);
                    elecinfo.setText("コンセント\n" + elec);
                    detailText.setText(detail);
                });
            }
        });
    }

    private void loadinfodb() {
        InformationDAO dao = dbInstance.informationdao();
        InformationEntity entity = dao.getByName(inputText); //entityは一つしかない想定(リストx)

        if(entity != null){
            runOnUiThread(() -> {
                label = findViewById(R.id.label);
                switch (entity.getLabel()) {
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
                label.setText(entity.getLabel());

                title = findViewById(R.id.title);
                title.setText(entity.getTitle());

                time = findViewById(R.id.time);
                time.setText(entity.getTime());

            });
        }else{
            info = findViewById(R.id.info);
            info.setVisibility(View.GONE);
        }
    }

    private void loadPosts() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            RoomFacilitiesDatabase db = Room.databaseBuilder(getApplicationContext(), RoomFacilitiesDatabase.class, "nishikan")
                    .fallbackToDestructiveMigration()
                    .build();
            PostsDAO postsDAO = db.postsdao();
            PostsEntity matchingPost = postsDAO.getPostByFacilityName(inputText);

            runOnUiThread(() -> {
                LinearLayout postLayout = findViewById(R.id.post);
                if (matchingPost != null) {
                    postLayout.setTag(matchingPost.getId()); // エンティティのIDをタグとして設定
                    TextView usernameView = findViewById(R.id.post_username);
                    TextView timeView = findViewById(R.id.post_time);
                    TextView contentView = findViewById(R.id.post_content);

                    usernameView.setText(matchingPost.getUser());
                    timeView.setText(matchingPost.getTime());
                    contentView.setText(matchingPost.getContent());
                } else{
                    postLayout.setVisibility(View.GONE);
                }
            });
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbInstance.close(); // データベースを閉じる
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

    private void setTagButtonOnClickListener(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Facilities.this, SearchResult.class);
                intent.putExtra("inputText", ((Button) v).getText().toString()); // ボタンのテキストを直接取得
                startActivity(intent);
            }
        });
    }

    private void showTagDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("タグの登録");
        builder.setMessage("タグを入力してください (10文字以内)");

        // 入力フィールドの設定
        final EditText input = new EditText(this);
        builder.setView(input);

        // ポジティブボタン
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String inputTag = input.getText().toString();
                if (!inputTag.trim().isEmpty() && inputTag.length() <= 10) {
                    addtagdb(inputTag);
                    Toast.makeText(Facilities.this, "タグ「" + inputTag + "」を追加しました", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(Facilities.this, "タグは10文字以内で入力してください", Toast.LENGTH_SHORT).show();
                    showTagDialog();  // 入力が無効な場合は、ダイアログを再表示
                }
            }
        });

        // ネガティブボタン
        builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void addtagdb(String tag) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                RoomDAO dao = dbInstance.roomdao();
                RoomEntity entity = dao.findByName(inputText);

                if(entity != null){
                    String tags = entity.getTag();
                    String newTags = tag + "," + tags;
                    entity.setTag(newTags);
                    dao.update(entity);  // update the entity to the database
                    runOnUiThread(() -> {
                        loadroomdb();
                    });
                }
            }
        });
    }

    private void openMap(){
        if(inputText.equals("W101(西館1F)") || inputText.equals("W102(西館1F)") || inputText.equals("W103(西館1F)") || inputText.equals("ロビー(西館1F)") || inputText.equals("情報科学図書室(西館1F)")){
            Intent intent = new Intent(getApplication(), nishi_1f.class);
            startActivity(intent);
        }else if(inputText.equals("GBC(西館1F)") || inputText.equals("学生ラウンジ(西館1F)") || inputText.equals("ミュージアム小金井(西館1F)") || inputText.equals("視聴覚室(西館1F)")){
            Intent intent = new Intent(getApplication(), nishi_1f_2.class);
            startActivity(intent);
        }else if(inputText.equals("W211(西館2F)") || inputText.equals("W212(西館2F)") || inputText.equals("W213(西館2F)") || inputText.equals("学生ラウンジ(西館2F)")){
            Intent intent = new Intent(getApplication(), nishi_2f.class);
            startActivity(intent);
        }else if(inputText.equals("アクティブラーニングラボ(西館3F)") || inputText.equals("共通実験室(西館3F)") || inputText.equals("W311(西館3F)")){
            Intent intent = new Intent(getApplication(), nishi_3f.class);
            startActivity(intent);
        }else if(inputText.equals("キャリアセンター(管理棟2F)")){
            Intent intent = new Intent(getApplication(), kanri_2f.class);
            startActivity(intent);
        }else if(inputText.equals("食堂(管理棟3F)") || inputText.equals("購買(管理棟3F)")){
            Intent intent = new Intent(getApplication(), kanri_3f.class);
            startActivity(intent);
        }else if(inputText.equals("ベンディングコーナー(西館2F)") || inputText.equals("W201(西館2F)") || inputText.equals("W202(西館2F)") || inputText.equals("W203(西館2F)") || inputText.equals("W204(西館2F)")){
            Intent intent = new Intent(getApplication(), nishi_2f_2.class);
            startActivity(intent);
        }else if(inputText.equals("マルチユースホール(東館1F)")){
            Intent intent = new Intent(getApplication(), higashi_1f.class);
            startActivity(intent);
        }else if(inputText.equals("食堂(東館B1)") || inputText.equals("購買書籍部(東館B1)")){
            Intent intent = new Intent(getApplication(), higashi_b1.class);
            startActivity(intent);
        }else if(inputText.equals("小金井図書館(南館1F)") || inputText.equals("ラーニングコモンズ(南館1F)")){
            Intent intent = new Intent(getApplication(), minami_1f.class);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View view) {
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
        } else if (view.getId() == R.id.backImage) {
            finish();
        } else if (view.getId() == R.id.info) {
            String text = title.getText().toString();
            Intent intent = new Intent(this, InformationDetail.class);
            intent.putExtra("inputText", text);
            startActivity(intent);
        } else if (view.getId() == R.id.addtag) {
            showTagDialog();
        } else if (view.getId() == R.id.post) {
            int entityId = (int) view.getTag(); // タグからエンティティのIDを取得
            Intent intent = new Intent(getApplication(), FeedDetail.class);
            intent.putExtra("postId", entityId);
            startActivity(intent);
        } else if (view.getId() == R.id.map) {
            openMap();
        } else if (view.getId() == R.id.share) {
            Intent intent = new Intent(this, FeedUpload.class);
            intent.putExtra("inputText", inputText);
            startActivity(intent);
        }
    }
}