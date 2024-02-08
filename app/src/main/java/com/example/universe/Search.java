package com.example.universe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.SearchView;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.cursoradapter.widget.SimpleCursorAdapter;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Search extends AppCompatActivity implements View.OnClickListener {


    private Button tag1,tag2,tag3,tag4,tag5,tag6,tag7,tag8,tag9,tag10,tag11,tag12,tag13,tag14,tag15,tag16,tag17,tag18,tag19,tag20,tag21,tag22,tag23,tag24,tag25,tag26,tag27,tag28,tag29,tag30;
    private Button info1,info2,info3,info4;
    private List<InformationEntity> informationList;
    private List<Button> infoTagButtonList;
    private List<RoomEntity> list;
    private List<Button> tagButtonList;
    private GestureDetector gestureDetector;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ((ImageButton)findViewById(R.id.mapIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.searchIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.snsIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.infoIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.mypageIcon)).setOnClickListener(this);

        ((Button) findViewById(R.id.info1)).setOnClickListener(this);((Button) findViewById(R.id.info2)).setOnClickListener(this);((Button) findViewById(R.id.info3)).setOnClickListener(this);((Button) findViewById(R.id.info4)).setOnClickListener(this);

        tag1 = findViewById(R.id.tag1);tag2 = findViewById(R.id.tag2);tag3 = findViewById(R.id.tag3);tag4 = findViewById(R.id.tag4);tag5 = findViewById(R.id.tag5);
        tag6 = findViewById(R.id.tag6);tag7 = findViewById(R.id.tag7);tag8 = findViewById(R.id.tag8);tag9 = findViewById(R.id.tag9);tag10 = findViewById(R.id.tag10);
        tag11 = findViewById(R.id.tag11);tag12 = findViewById(R.id.tag12);tag13 = findViewById(R.id.tag13);tag14 = findViewById(R.id.tag14);tag15 = findViewById(R.id.tag15);
        tag16 = findViewById(R.id.tag16);tag17 = findViewById(R.id.tag17);tag18 = findViewById(R.id.tag18);tag19 = findViewById(R.id.tag19);tag20 = findViewById(R.id.tag20);
        tag21 = findViewById(R.id.tag21);tag22 = findViewById(R.id.tag22);tag23 = findViewById(R.id.tag23);tag24 = findViewById(R.id.tag24);tag25 = findViewById(R.id.tag25);
        tag26 = findViewById(R.id.tag26);tag27 = findViewById(R.id.tag27);tag28 = findViewById(R.id.tag28);tag29 = findViewById(R.id.tag29);tag30 = findViewById(R.id.tag30);
        info1 = findViewById(R.id.info1);info2 = findViewById(R.id.info2);info3 = findViewById(R.id.info3);info4 = findViewById(R.id.info4);

        setTagButtonOnClickListener(tag1);setTagButtonOnClickListener(tag2);setTagButtonOnClickListener(tag3);setTagButtonOnClickListener(tag4);setTagButtonOnClickListener(tag5);
        setTagButtonOnClickListener(tag6);setTagButtonOnClickListener(tag7);setTagButtonOnClickListener(tag8);setTagButtonOnClickListener(tag9);setTagButtonOnClickListener(tag10);
        setTagButtonOnClickListener(tag11);setTagButtonOnClickListener(tag12);setTagButtonOnClickListener(tag13);setTagButtonOnClickListener(tag14);setTagButtonOnClickListener(tag15);
        setTagButtonOnClickListener(tag16);setTagButtonOnClickListener(tag17);setTagButtonOnClickListener(tag18);setTagButtonOnClickListener(tag19);setTagButtonOnClickListener(tag20);
        setTagButtonOnClickListener(tag21);setTagButtonOnClickListener(tag22);setTagButtonOnClickListener(tag23);setTagButtonOnClickListener(tag24);setTagButtonOnClickListener(tag25);
        setTagButtonOnClickListener(tag26);setTagButtonOnClickListener(tag27);setTagButtonOnClickListener(tag28);setTagButtonOnClickListener(tag29);setTagButtonOnClickListener(tag30);
        infoTagButtonList = new ArrayList<>();
        infoTagButtonList.add(info1);infoTagButtonList.add(info2);infoTagButtonList.add(info3);infoTagButtonList.add(info4);

        tagButtonList = new ArrayList<>();
        tagButtonList.add(tag1);tagButtonList.add(tag2);tagButtonList.add(tag3);tagButtonList.add(tag4);tagButtonList.add(tag5);
        tagButtonList.add(tag6);tagButtonList.add(tag7);tagButtonList.add(tag8);tagButtonList.add(tag9);tagButtonList.add(tag10);
        tagButtonList.add(tag11);tagButtonList.add(tag12);tagButtonList.add(tag13);tagButtonList.add(tag14);tagButtonList.add(tag15);
        tagButtonList.add(tag16);tagButtonList.add(tag17);tagButtonList.add(tag18);tagButtonList.add(tag19);tagButtonList.add(tag20);
        tagButtonList.add(tag21);tagButtonList.add(tag22);tagButtonList.add(tag23);tagButtonList.add(tag24);tagButtonList.add(tag25);
        tagButtonList.add(tag26);tagButtonList.add(tag27);tagButtonList.add(tag28);tagButtonList.add(tag29);tagButtonList.add(tag30);

        gestureDetector = new GestureDetector(this, new Search.SwipeGestureDetector());

        searchView = findViewById(R.id.search_view);
        searchView.setIconified(false); //最初から拡張させる(ヒントテキストを表示)
        AutoCompleteTextView autoCompleteTextView = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setTextColor(Color.BLACK);
        autoCompleteTextView.clearFocus(); //キーボードを閉じる
        searchView.setQueryHint("キーワードを入力してください");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // ユーザーが検索ボタンを押すと呼ばれる
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // ユーザーがテキストを変更するたびに呼ばれる
                if (!newText.isEmpty()) {
                    getSuggestions(newText);
                }
                return false;
            }
        });

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // SearchViewの内部EditTextにフォーカスを与え、キーボードを表示する
                searchView.setIconified(false);
            }
        });

        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                // クリックされたサジェストのデータを取得
                Cursor cursor = searchView.getSuggestionsAdapter().getCursor();
                cursor.moveToPosition(position);
                String suggestion = cursor.getString(cursor.getColumnIndex("name"));

                // そのデータを使用して検索を行う
                performSearch(suggestion);

                // サジェストをクリックした後のデフォルトの動作（テキストを入力欄にセットする）を防ぐためにtrueを返す
                return true;
            }
        });

        loaddb();
    }

    private Cursor convertListToCursor(List<String> list) {
        MatrixCursor matrixCursor = new MatrixCursor(new String[] {"_id", "name"});
        long id = 0;
        for (String item : list) {
            matrixCursor.addRow(new Object[] {id++, item});
        }
        return matrixCursor;
    }

    private void getSuggestions(String query) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                RoomFacilitiesDatabase db = Room.databaseBuilder(getApplicationContext(), RoomFacilitiesDatabase.class, "nishikan").build();
                RoomDAO dao = db.roomdao();
                List<String> suggestions = dao.getNamesMatchingQuery("%" + query + "%");

                Cursor cursor = convertListToCursor(suggestions);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String[] from = new String[] {"name"};
                        int[] to = new int[] {android.R.id.text1};
                        CursorAdapter adapter = new SimpleCursorAdapter(Search.this,
                                android.R.layout.simple_dropdown_item_1line,
                                cursor, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
                        searchView.setSuggestionsAdapter(adapter);
                    }
                });
            }
        });
    }

    private void performSearch(String query) {
        if(!query.trim().isEmpty()){
            query = query.replace("'", "''").replace(",", "/");  // シングルクォートとカンマをエスケープ
            Intent intent = new Intent(this, SearchResult.class);
            intent.putExtra("inputText", query); // key-value pair
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "入力が空です", Toast.LENGTH_SHORT).show();  // エラーメッセージを表示
        }
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

            // 右から左へのスワイプ
            if (Math.abs(diffX) > Math.abs(diffY) && diffX < 0) {
                Intent intent = new Intent(Search.this, FeedTop.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            }

            // 左から右へのスワイプ
            if (Math.abs(diffX) > Math.abs(diffY) && diffX > 0) {
                Intent intent = new Intent(Search.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                return true;
            }

            return false;
        }
    }

    private void loaddb(){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable(){
            @Override
            public void run(){
                RoomFacilitiesDatabase db = Room.databaseBuilder(getApplicationContext(),RoomFacilitiesDatabase.class,"nishikan")
                        .fallbackToDestructiveMigration()
                        .build();
                RoomDAO dao = db.roomdao();
                InformationDAO infoDao = db.informationdao();

                list = dao.getall();
                informationList = infoDao.getInformationsWithNotEmptyName();

                if(list.size() > 0){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            HashSet<String> allTags = new HashSet<>();
                            for(RoomEntity entity : list) {
                                String tags = entity.getTag();
                                List<String> tagList = Arrays.asList(tags.split(","));
                                allTags.addAll(tagList); // allTagsにtagListの全ての要素を追加
                            }
                            int i = 0;
                            for(String tag : allTags) {
                                if(i >= 30) break; //30個のタグをセットしたら終了
                                tagButtonList.get(i).setText(tag);
                                i++;
                            }
                        }
                    });
                }
                if(informationList.size() > 0){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int startIndex = Math.max(informationList.size() - 4, 0); // 最後の4件を取得するための開始インデックス
                            int j = 0;
                            for(int i = informationList.size() - 1; i >= startIndex; i--) {
                                String infoTitle = informationList.get(i).getTitle();
                                infoTagButtonList.get(j).setText(infoTitle);
                                j++;
                            }
                        }
                    });
                }
            }
        });
    }

    private void setTagButtonOnClickListener(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Search.this, SearchResult.class);
                intent.putExtra("inputText", ((Button) v).getText().toString()); // ボタンのテキストを直接取得
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {
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
        } else if (view.getId() == R.id.info1 || view.getId() == R.id.info2 || view.getId() == R.id.info3 || view.getId() == R.id.info4) {

            Button clickedButton = (Button) view;
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    RoomFacilitiesDatabase db = Room.databaseBuilder(getApplicationContext(), RoomFacilitiesDatabase.class, "nishikan").build();
                    InformationDAO informationDao = db.informationdao();
                    String text = informationDao.findByTitle(clickedButton.getText().toString()).getName();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(Search.this, Facilities.class);
                            intent.putExtra("inputText", text);
                            startActivity(intent);
                        }
                    });
                }
            });
        }
    }
}