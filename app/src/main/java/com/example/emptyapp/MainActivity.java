package com.example.emptyapp;

import android.Manifest;
import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.io.File;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import android.webkit.WebView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity

{
    Button mButton;
    EditText mEdit;
    public Document document = null;
    Toolbar myToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        final WebView browser = findViewById(R.id.browser);
        mButton = findViewById(R.id.button);
        mEdit   = findViewById(R.id.edittext);


        mButton.setOnClickListener
                (new View.OnClickListener()
                        {

                            @RequiresApi(api = Build.VERSION_CODES.N)
                            public void onClick(View view)
                            {

                                String query = mEdit.getText().toString();
                                String url = "https://pyxis.knu.ac.kr/en/#/search/ex?all=1%7Ck%7Ca%7C" + query + "&rq=BRANCH%3D1";

                                Intent intent = new Intent(getApplicationContext(), ShowWebPage.class);
                                intent.putExtra("url", url);
                                startActivity(intent);


                            }
                        });







    }




}