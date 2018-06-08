package com.example.zhomart.oqyrman;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.zhomart.oqyrman.Fragments.DiscoverFragment;
import com.example.zhomart.oqyrman.Models.Book;
import com.example.zhomart.oqyrman.R;
import com.google.gson.Gson;

import org.json.JSONObject;
import org.w3c.dom.Text;

public class DetailsActivity extends AppCompatActivity {

    private static final String URL = DiscoverFragment.getURL();
    private int position;
    private ImageView backgroundImage;
    private TextView content;
    private FloatingActionButton fab;
    private String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
        language = intent.getStringExtra("language");

        backgroundImage = (ImageView) findViewById(R.id.backgroundImage);
        content = (TextView) findViewById(R.id.content);

        fetchDetails();
    }

    private void fetchDetails() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL + "/" + position + ".json",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.println(response);
                        Gson gson = new Gson();
                        final Book book = gson.fromJson(response.toString(), Book.class);
                        if (language.equals("es")){
                            Glide.with(getBaseContext())
                                    .load(URL + "/" + book.getImage_latin())
                                    .apply(new RequestOptions()
                                            .centerInside())
                                    .into(backgroundImage);

                            getSupportActionBar().setTitle(book.getTitle_latin());
                            content.setText(book.getDescription_latin());
                        } else{
                            Glide.with(getBaseContext())
                                    .load(URL + "/" + book.getImage())
                                    .apply(new RequestOptions()
                                            .centerInside())
                                    .into(backgroundImage);

                            getSupportActionBar().setTitle(book.getTitle());
                            content.setText(book.getDescription());
                        }

                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!book.getEpub_cyrillic().isEmpty()) {
                                    if (haveStoragePermission()) {
                                        String url = URL + "/epub/test.epub";
                                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                                        request.setDescription("Some description");
                                        request.setTitle("Some title");
                                        System.out.println(url);
                                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOCUMENTS, book.getEpub_latin());

                                        // get download service and enqueue file
                                        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                                        if (manager != null) {
                                            manager.enqueue(request);
                                        }
                                    } else {
                                        Toast.makeText(getBaseContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getBaseContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Toast.makeText(getBaseContext(), getString(R.string.error) + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }

    public  boolean haveStoragePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.e("Permission error","You have permission");
                return true;
            } else {
                Log.e("Permission error","You have asked for permission");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //you dont need to worry about these stuff below api level 23
            Log.e("Permission error","You already have the permission");
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.action_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, getSupportActionBar().getTitle());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                sendIntent.removeExtra(Intent.EXTRA_TEXT);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
