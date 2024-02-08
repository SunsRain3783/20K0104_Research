package com.example.universe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GroupChat extends AppCompatActivity implements View.OnClickListener{
    private GestureDetector gestureDetector;
    private int groupId;
    private EditText inputChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        inputChat = findViewById(R.id.inputChat);

        ((ImageView) findViewById(R.id.backImage)).setOnClickListener(this);
        ((Button) findViewById(R.id.btnSend)).setOnClickListener(this);

        groupId = getIntent().getIntExtra("groupId", -1);

        gestureDetector = new GestureDetector(this, new GroupChat.SwipeGestureDetector());

        loadGroup();
        simulateInitialChat();
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
                TextView groupNameView = findViewById(R.id.groupname);
                TextView groupMembersView = findViewById(R.id.groupmembers);

                String[] members = groupEntity.getParticipants().split(",");
                String number = members.length + " 人のメンバー";
                groupMembersView.setText(number);
                groupNameView.setText(groupEntity.getGroupName());
            });
        });
    }

    private void simulateInitialChat() {
        String[] initialConversations = {
                "Computer B: xっていう二次元リストがあって，\nx[0],x[1],x[2]の長さを知りたいってことですか？",
                "Computer A: そうです！このように書いたら，x[i]ってなんや～っていうエラーが出てしまいまして．．．"
        };

        for (String conversation : initialConversations) {
            updateChat(conversation);
        }
    }

    private void updateChat(String message) {
        String[] parts = message.split(": ", 2);
        addMessageToChat(parts[0], parts[1]);
    }

    private void addMessageToChat(String sender, String message) {
        View chatView = getLayoutInflater().inflate(R.layout.chat_message_layout, null);
        ImageView chatIcon = chatView.findViewById(R.id.chatIcon);
        TextView chatMessage = chatView.findViewById(R.id.chatMessage);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) chatMessage.getLayoutParams();

        if ("Computer A".equals(sender)) {
            chatIcon.setImageResource(R.drawable.freeicon1);
        } else if ("Computer B".equals(sender)) {
            chatIcon.setImageResource(R.drawable.freeicon2);
        } else if ("User".equals(sender)) {
            chatIcon.setVisibility(View.GONE);
            params.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
            chatMessage.setBackgroundResource(R.drawable.chat_bg_right);
        } else {
            chatIcon.setImageResource(R.drawable.freeicon10);
        }

        chatMessage.setLayoutParams(params);
        chatMessage.setText(message);

        LinearLayout chatContainer = findViewById(R.id.chatContainer);
        chatContainer.addView(chatView);
    }





    @Override
    public void onClick(View view){  //ボタンをクリックしたときの挙動
        if(view.getId() == R.id.backImage){
            finish();
        } else if(view.getId() == R.id.btnSend) {
            String userMessage = inputChat.getText().toString().trim();
            if (!userMessage.isEmpty()) {
                updateChat("User: " + userMessage);
                inputChat.setText("");
                updateChat("Computer: 初めまして！よろしくお願いします！！");
            }
        }
    }
}