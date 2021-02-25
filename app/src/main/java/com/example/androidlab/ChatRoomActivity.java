package com.example.androidlab;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidlab.models.Message;
import com.example.androidlab.models.Message.Type;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomActivity extends AppCompatActivity {

    private final List<Message> messages = new ArrayList<>();
    MessageListAdapter messageListAdapter = new MessageListAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        setListeners();
    }

    private void setListeners() {
        ListView messageList = findViewById(R.id.theListView);
        messageList.setAdapter(messageListAdapter);
        EditText text = findViewById(R.id.textChatBox);
        findViewById(R.id.btnSend).setOnClickListener(v -> {
            Message message = new Message(text.getText().toString(), Type.SENT);
            messages.add(message);
            messageListAdapter.notifyDataSetChanged();
            text.setText("");
        });

        findViewById(R.id.btnReceive).setOnClickListener(v -> {
            Message message = new Message(text.getText().toString(), Type.RECEIVED);
            messages.add(message);
            messageListAdapter.notifyDataSetChanged();
            text.setText("");
        });

        // Set long click listener for each item in the row.
        messageList.setOnItemLongClickListener(((parent, view, position, id) -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.
                    setTitle(getString(R.string.delete))
                    .setMessage(
                            getString(R.string.row) + position + "\n" +
                                    getString(R.string.database) + messageListAdapter.getItemId(position)
                    )
                    .setPositiveButton(getString(R.string.yes), (click, arg) -> {
                        messages.remove(position);
                        messageListAdapter.notifyDataSetChanged();
                    })
                    .setNegativeButton(R.string.no, (click, arg) -> {
                    })
                    .show();

            return false;
        }));
    }

    private class MessageListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return messages.size();
        }

        @Override
        public Object getItem(int position) {
            return messages.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            Message.Type message = ((Message) getItem(position)).getType();
            if (message == Type.SENT) {
                return R.layout.row_sent;
            } else if (message == Type.RECEIVED) {
                return R.layout.row_received;
            } else {
                throw new UnsupportedOperationException("Invalid message type for layout.");
            }
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();

            View newView = inflater.inflate(getItemViewType(position), parent, false);

            TextView messageText = newView.findViewById(R.id.message);
            messageText.setText(messages.get(position).getText());

            return newView;
        }
    }
}
