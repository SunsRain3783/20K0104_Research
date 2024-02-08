package com.example.universe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class QustionChat extends AppCompatActivity implements View.OnClickListener{
    private GestureDetector gestureDetector;
    private EditText inputChat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qustion_chat);
        inputChat = findViewById(R.id.inputChat);

        ((ImageView) findViewById(R.id.backImage)).setOnClickListener(this);
        ((Button) findViewById(R.id.btnSend)).setOnClickListener(this);

        gestureDetector = new GestureDetector(this, new QustionChat.SwipeGestureDetector());
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
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                return true;
            }

            return false;
        }
    }

    private void simulateInitialChat() {
        String[] initialConversations = {
                "User: 1階の言語L1について，例えばF(help)=(18K1000,18K2000)の場合，18K2000→18K1000の解釈はできますか？",
                "Computer A: いいえ，できません．左側が助ける人，右側が助けられる人です．",
                "Computer B: M=(D,F)の場合，D→Fの関係のみ成り立ちます！！",
                "User: ありがとうございます！",
                "Computer A: いえいえ～",
                "Computer: 質問です．再帰とはどういう意味ですか？"
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
                updateChat("Computer: ありがとうございます！！");
            }
        }
    }
}