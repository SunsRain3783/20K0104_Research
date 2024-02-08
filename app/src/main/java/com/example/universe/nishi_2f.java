package com.example.universe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class nishi_2f extends AppCompatActivity implements View.OnClickListener {

    private GestureDetector gestureDetector;
    private LocationManager locationManager;
    private ImageView userIcon;
    private Location previousLocation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nishi2f);

        ((ImageButton)findViewById(R.id.mapIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.searchIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.snsIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.infoIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.mypageIcon)).setOnClickListener(this);
        ((Button)findViewById(R.id.button1)).setOnClickListener(this);
        ((Button)findViewById(R.id.button2)).setOnClickListener(this);
        ((Button)findViewById(R.id.button3)).setOnClickListener(this);
        ((Button)findViewById(R.id.button4)).setOnClickListener(this);

        Button buttonTo2fRikou = findViewById(R.id.buttonTo2fRikou);
        Button button3f = findViewById(R.id.button3f);
        Button button1f = findViewById(R.id.button1f);
        Button button2f = findViewById(R.id.button2f);

        buttonTo2fRikou.setOnClickListener(this);
        button1f.setOnClickListener(this);
        button2f.setOnClickListener(this);
        button3f.setOnClickListener(this);

        userIcon = findViewById(R.id.usericon);  // アイコンの取得
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // 位置情報の権限確認
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 権限がない場合、リクエスト
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        } else {
            // 位置情報の取得
            requestLocationUpdates();
        }

        gestureDetector = new GestureDetector(this, new nishi_2f.SwipeGestureDetector());
    }

    private void requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0.2f, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (previousLocation != null) {
                    int dpMove = convertToDp(4);
                    ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) userIcon.getLayoutParams();

                    params.leftMargin += (location.getLongitude() - previousLocation.getLongitude() > 0) ? dpMove : -dpMove;
                    params.topMargin += (location.getLatitude() - previousLocation.getLatitude() > 0) ? -dpMove : dpMove;

                    userIcon.setLayoutParams(params);
                }
                previousLocation = location;
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) { }

            @Override
            public void onProviderEnabled(String provider) { }

            @Override
            public void onProviderDisabled(String provider) { }
        });
    }

    private int convertToDp(float meters) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        return (int) (meters * metrics.density);
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

            // 右から左へのスワイプ（nishi_2f_2へ遷移)
            if (Math.abs(diffX) > Math.abs(diffY) && diffX < 0) {
                Intent intent = new Intent(nishi_2f.this, nishi_2f_2.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            }
            // 上から下へのスワイプ（nishi_3fへ遷移）
            if (Math.abs(diffY) > Math.abs(diffX) && diffY > 0) {
                Intent intent = new Intent(nishi_2f.this, nishi_3f.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_up);
                return true;
            }

            // 下から上へのスワイプ（nishi_1fへ遷移）
            if (Math.abs(diffY) > Math.abs(diffX) && diffY < 0) {
                Intent intent = new Intent(nishi_2f.this, nishi_1f.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down);
                return true;
            }

            return false;
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Intent intent;

        if(id == R.id.mapIcon) {
            intent = new Intent(getApplication(),MainActivity.class);
        } else if(view.getId() == R.id.searchIcon){
            intent = new Intent(getApplication(),Search.class);
        } else if(id == R.id.snsIcon){
            intent = new Intent(getApplication(),FeedTop.class);
        } else if(id == R.id.infoIcon){
            intent = new Intent(getApplication(),Information.class);
        } else if(id == R.id.mypageIcon){
            intent = new Intent(getApplication(),MypageTop.class);
        } else if (id == R.id.buttonTo2fRikou) {
            intent = new Intent(this, nishi_2f_2.class);
        } else if (id == R.id.button1f) {
            intent = new Intent(this, nishi_1f.class);
        } else if (id == R.id.button2f) {
            intent = new Intent(this, nishi_2f.class);
        } else if (id == R.id.button3f) {
            intent = new Intent(this, nishi_3f.class);
        } else if (id == R.id.button1) {
            intent = new Intent(this, Facilities.class);
            intent.putExtra("inputText", "W211(西館2F)");
        } else if (id == R.id.button2) {
            intent = new Intent(this, Facilities.class);
            intent.putExtra("inputText", "W212(西館2F)");
        } else if (id == R.id.button3) {
            intent = new Intent(this, Facilities.class);
            intent.putExtra("inputText", "W213(西館2F)");
        } else if (id == R.id.button4) {
            intent = new Intent(this, Facilities.class);
            intent.putExtra("inputText", "学生ラウンジ(西館2F)");
        } else {
            return;
        }

        startActivity(intent);
    }

}
