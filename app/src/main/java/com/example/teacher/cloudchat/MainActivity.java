package com.example.teacher.cloudchat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");
    ArrayList<String> chats;
    String nickname;
    ListView lv;
    ArrayAdapter adapter;
    EditText ed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ed = (EditText) findViewById(R.id.editText);
        chats = new ArrayList<>();
        lv = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, chats);
        lv.setAdapter(adapter);
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Gson gson = new Gson();
                chats = gson.fromJson(value, new TypeToken<ArrayList<String>>(){}.getType());
                // adapter.notifyDataSetChanged();

                adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, chats);
                lv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("CHAT", "Failed to read value.", error.toException());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sp = getSharedPreferences("chat", MODE_PRIVATE);
        nickname = sp.getString("nickname", "user");
    }

    public void clickSend(View v)
    {
        chats.add(nickname + ":" + ed.getText().toString());
        Gson gson = new Gson();
        String json = gson.toJson(chats);
        myRef.setValue(json);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Settings");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent it = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(it);
        return super.onOptionsItemSelected(item);
    }
}
