package com.example.universe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.room.Room;

import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FeedMapLink extends AppCompatActivity implements View.OnClickListener{
    private GestureDetector gestureDetector;
    private SearchView searchView;
    private List<RoomEntity> list,alllist;
    private List<TextView> nameTextViewList;
    private List<ImageView> ImageViewList;
    private String inputText;
    private List<Integer> resultLayoutIds = Arrays.asList(
            R.id.result1,
            R.id.result2,
            R.id.result3,
            R.id.result4,
            R.id.result5,
            R.id.result6
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_map_link);

        Intent intent = getIntent();
        inputText = intent.getStringExtra("inputText");

        ((ImageButton)findViewById(R.id.mapIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.searchIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.snsIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.infoIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.mypageIcon)).setOnClickListener(this);

        ((ImageView) findViewById(R.id.backImage)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.result1)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.result2)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.result3)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.result4)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.result5)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.result6)).setOnClickListener(this);

        gestureDetector = new GestureDetector(this, new FeedMapLink.SwipeGestureDetector());
        initViews();

        searchView = findViewById(R.id.search_view);
        searchView.setIconified(false); //最初から拡張させる(ヒントテキストを表示)
        AutoCompleteTextView autoCompleteTextView = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setTextColor(Color.BLACK);
        autoCompleteTextView.clearFocus(); //キーボードを閉じる
        searchView.setQueryHint("キーワードを入力してください");
        searchView.setQuery(inputText, false);
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
                        CursorAdapter adapter = new SimpleCursorAdapter(FeedMapLink.this,
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
            Intent intent = new Intent(this, FeedMapLink.class);
            intent.putExtra("inputText", query); // key-value pair
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "入力が空です", Toast.LENGTH_SHORT).show();  // エラーメッセージを表示
        }
    }

    private void initViews() {
        searchView = findViewById(R.id.search_view);

        nameTextViewList = new ArrayList<>();
        nameTextViewList.add(findViewById(R.id.text1));
        nameTextViewList.add(findViewById(R.id.text2));
        nameTextViewList.add(findViewById(R.id.text3));
        nameTextViewList.add(findViewById(R.id.text4));
        nameTextViewList.add(findViewById(R.id.text5));
        nameTextViewList.add(findViewById(R.id.text6));

        ImageViewList = new ArrayList<>();
        ImageViewList.add(findViewById(R.id.image1));
        ImageViewList.add(findViewById(R.id.image2));
        ImageViewList.add(findViewById(R.id.image3));
        ImageViewList.add(findViewById(R.id.image4));
        ImageViewList.add(findViewById(R.id.image5));
        ImageViewList.add(findViewById(R.id.image6));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loaddb(){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable(){
            @Override
            public void run(){
                RoomFacilitiesDatabase db = Room.databaseBuilder(getApplicationContext(),RoomFacilitiesDatabase.class,"nishikan").build();
                RoomDAO dao = db.roomdao();

                list = dao.searchByName("%"+inputText+"%");  // nameによるテーブル検索
                list.addAll(dao.searchByTag("%"+inputText+"%"));  // tagによるテーブル検索結果を追加
                alllist = dao.getall();

                if(list.size() > 0){
                    List<String> names = new ArrayList<>(); // nameを保存するためのリストを作成
                    List<Integer> resourceIdList = new ArrayList<>(); // resourceIdを保存するためのリストを作成

                    for(RoomEntity entity : list) {
                        String name = entity.getName();
                        names.add(name); // nameをリストに追加

                        String imageName = entity.getImage();
                        if (imageName.length() > 0) {
                            imageName = imageName.substring(0, imageName.length() - 1); //"w101image2"->"w101image"
                        }
                        int resourceId = getResources().getIdentifier(imageName, "drawable", getPackageName());
                        resourceIdList.add(resourceId); // resourceIdをリストに追加
                    }

                    // UIの更新はメインスレッドで行う必要があるため、新たなRunnableを作成
                    Runnable updateUI = new Runnable() {
                        @Override
                        public void run() {
                            for(int i = 0; i < names.size() && i < nameTextViewList.size(); i++) {
                                nameTextViewList.get(i).setText(names.get(i));
                                ImageViewList.get(i).setImageResource(resourceIdList.get(i));
                            }

                            for(int i = names.size(); i < nameTextViewList.size(); i++) {
                                nameTextViewList.get(i).setText("");
                                ImageViewList.get(i).setImageDrawable(null);
                                findViewById(resultLayoutIds.get(i)).setVisibility(View.GONE);
                            }
                        }
                    };
                    runOnUiThread(updateUI);
                }else{
                    List<String> names = new ArrayList<>(); // nameを保存するためのリストを作成
                    List<Integer> resourceIdList = new ArrayList<>(); // resourceIdを保存するためのリストを作成

                    for(RoomEntity entity : alllist) {
                        String name = entity.getName();
                        names.add(name); // nameをリストに追加

                        String imageName = entity.getImage();
                        if (imageName.length() > 0) {
                            imageName = imageName.substring(0, imageName.length() - 1); //"w101image2"->"w101image"
                        }
                        int resourceId = getResources().getIdentifier(imageName, "drawable", getPackageName());
                        resourceIdList.add(resourceId); // resourceIdをリストに追加
                    }

                    // UIの更新はメインスレッドで行う必要があるため、新たなRunnableを作成
                    Runnable updateUI = new Runnable() {
                        @Override
                        public void run() {
                            for(int i = 0; i < names.size() && i < nameTextViewList.size(); i++) {
                                nameTextViewList.get(i).setText(names.get(i));
                                ImageViewList.get(i).setImageResource(resourceIdList.get(i));
                            }

                            for(int i = names.size(); i < nameTextViewList.size(); i++) {
                                nameTextViewList.get(i).setText("");
                                ImageViewList.get(i).setImageDrawable(null);
                                findViewById(resultLayoutIds.get(i)).setVisibility(View.GONE);
                            }
                        }
                    };
                    runOnUiThread(updateUI);
                }
            }
        });
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
    public void onClick(View view) {  //ボタンをクリックしたときの挙動
        if (view.getId() == R.id.mapIcon) {  //switch使うとエラー(if elseを使う)
            Intent intentlevel = new Intent(getApplication(), MainActivity.class); //画面遷移(MainActivity→GameLevel)
            startActivity(intentlevel);
        } else if (view.getId() == R.id.searchIcon) {
            Intent intent = new Intent(getApplication(), Search.class);
            startActivity(intent);
        } else if (view.getId() == R.id.snsIcon) {
            Intent intent = new Intent(getApplication(), FeedTop.class);
            startActivity(intent);
        } else if (view.getId() == R.id.infoIcon) {
            Intent intent = new Intent(getApplication(), Information.class);
            startActivity(intent);
        } else if (view.getId() == R.id.mypageIcon) {
            Intent intent = new Intent(getApplication(), MypageTop.class);
            startActivity(intent);
        } else if (view.getId() == R.id.backImage) {
            finish();
        } else if (view.getId() == R.id.result1) {
            TextView nameTextView = findViewById(R.id.text1);
            String inputText = nameTextView.getText().toString();
            Intent intent = new Intent(this, FeedUpload.class);
            intent.putExtra("inputText", inputText);
            startActivity(intent);
        } else if (view.getId() == R.id.result2) {
            TextView nameTextView = findViewById(R.id.text2);
            String inputText = nameTextView.getText().toString();
            Intent intent = new Intent(this, FeedUpload.class);
            intent.putExtra("inputText", inputText);
            startActivity(intent);
        } else if (view.getId() == R.id.result3) {
            TextView nameTextView = findViewById(R.id.text3);
            String inputText = nameTextView.getText().toString();
            Intent intent = new Intent(this, FeedUpload.class);
            intent.putExtra("inputText", inputText);
            startActivity(intent);
        } else if (view.getId() == R.id.result4) {
            TextView nameTextView = findViewById(R.id.text4);
            String inputText = nameTextView.getText().toString();
            Intent intent = new Intent(this, FeedUpload.class);
            intent.putExtra("inputText", inputText);
            startActivity(intent);
        } else if (view.getId() == R.id.result5) {
            TextView nameTextView = findViewById(R.id.text5);
            String inputText = nameTextView.getText().toString();
            Intent intent = new Intent(this, FeedUpload.class);
            intent.putExtra("inputText", inputText);
            startActivity(intent);
        } else if (view.getId() == R.id.result6) {
            TextView nameTextView = findViewById(R.id.text6);
            String inputText = nameTextView.getText().toString();
            Intent intent = new Intent(this, FeedUpload.class);
            intent.putExtra("inputText", inputText);
            startActivity(intent);
        }
    }
}