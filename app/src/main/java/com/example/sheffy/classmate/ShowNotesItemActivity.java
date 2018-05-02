package com.example.sheffy.classmate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ShowNotesItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_notes_item);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
