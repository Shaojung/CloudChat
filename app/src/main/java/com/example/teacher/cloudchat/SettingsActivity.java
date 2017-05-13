package com.example.teacher.cloudchat;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity {
    EditText ed2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ed2 = (EditText) findViewById(R.id.editText2);
        SharedPreferences sp = getSharedPreferences("chat", MODE_PRIVATE);
        String nickname = sp.getString("nickname", "user");
        ed2.setText(nickname);
    }
    public void clickOK(View v)
    {
        SharedPreferences sp = getSharedPreferences("chat", MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("nickname", ed2.getText().toString());
        ed.commit();
        finish();
    }
}
