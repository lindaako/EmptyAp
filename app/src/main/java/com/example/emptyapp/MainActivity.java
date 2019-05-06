package com.example.emptyapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.io.IOException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity
{

    Button mButton;
    EditText mEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mButton = findViewById(R.id.button);
        mEdit   = findViewById(R.id.edittext);


        mButton.setOnClickListener
                (
                     new View.OnClickListener()
                        {
                            public void onClick(View view)
                            {

                                String query = mEdit.getText().toString();
                                String url = "https://pyxis.knu.ac.kr/en/#/search/ex?all=1%7Ck%7Ca%7C" + query + "&rq=BRANCH%3D1";


                                Intent intent = new Intent(getApplicationContext(), ShowWebPage.class);
                                intent.putExtra("url", url);
                                startActivity(intent);

                                //Intent i = new Intent(Intent.ACTION_VIEW);
                                //i.setData(Uri.parse(url));
                                //startActivity(i);
                            }
                        });

    }


}

