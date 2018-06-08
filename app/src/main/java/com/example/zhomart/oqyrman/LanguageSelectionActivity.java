package com.example.zhomart.oqyrman;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class LanguageSelectionActivity extends AppCompatActivity implements View.OnClickListener {

    private Button eng, kaz, lat;
    private String language;
    private String LANG_CURRENT = "en";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_selection);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setTitle(getResources().getString(R.string.language_pick));

        eng = (Button) findViewById(R.id.button);
        kaz = (Button) findViewById(R.id.button2);
        lat = (Button) findViewById(R.id.button3);

        eng.setOnClickListener(this);
        kaz.setOnClickListener(this);
        lat.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String lang = "en";
        switch (view.getId()){
            case R.id.button:
                lang = "en";
                break;
            case R.id.button2:
                lang = "kaz";
                break;
            case R.id.button3:
                lang = "es";
                break;
        }

        finish();
        startActivity(new Intent(this, MainActivity.class).putExtra("language", lang));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        inflater.inflate(R.menu.menu_library, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
