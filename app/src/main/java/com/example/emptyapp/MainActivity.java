package com.example.emptyapp;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
//public class MainActivity extends AsyncTask<String, Void,>
{

    Button mButton;
    EditText mEdit;
    Toast myBook;
    private Document document = null;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mButton = findViewById(R.id.button);
        mEdit   = findViewById(R.id.edittext);


        mButton.setOnClickListener
                (new View.OnClickListener()
                        {
                            public void onClick(View view)
                            {
                                final String url = "https://pyxis.knu.ac.kr/en/#/search/detail/4500295";

                                Document doc = null;
                                try
                                {
                                    doc = Jsoup.connect(url).get();
                                } catch (IOException e)
                                {
                                    e.printStackTrace();
                                }


                                if(doc != null)
                                {
                                    Elements spanTags = doc.getElementsByTag("span");
                                    for (Element spanTag : spanTags) {
                                        String text = spanTag.ownText();
                                        System.out.println(text);
                                    }

                                }

/*
                                Thread thread = new Thread(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {

                                        try
                                        {

                                            Log.d("MainActivity", " trying... ");
                                           document = Jsoup.connect(url)
                                            //document = Jsoup.connect("http://example.com")
                                                    .data("query","Java")
                                                    .userAgent("Mozilla")
                                                    .cookie("auth","token")
                                                    .timeout(6000)
                                                    .post();

                                            Log.d("MainActivity", " Done! ");

                                            if (document == null)
                                            {
                                                Log.d("MainActivity", "\n The Document is empty!\n");
                                            }

                                            if(document != null)
                                            {
                                                Elements top = document.select("tbody");
                                                Elements top_1 = top.select("tr");
                                                Elements top_2 = top_1.select("td");
                                                Elements top_3 = top_2.select("span");
                                                //Elements options = document.getElementsByClass("font");
                                                //Elements options = document.getElementsByTag("span.ikc-item-status");
                                                 int i = 1;
                                                for (Element element : top_3)
                                                {
                                                    //Log.d("MainActivity", "\nDone\n");
                                                    //Log.d("MainActivity", element.text());
                                                    System.out.println("string " + i + ": " + element.text());
                                                    i++;
                                                }

                                                Elements myElements = document.getElementsByTag("td")
                                                        .first().getElementsByTag("span");
                                                for (Element element : myElements)
                                                {
                                                    if (element.className().contains("ikc-item-status"))
                                                    {
                                                        String Content = element.ownText();


                                                        System.out.println("   " + Content + " " );
                                                    }
                                                }


                                            }


                                        }
                                        catch (IOException e)
                                        {
                                            Log.d("MainActivity", "ERROR!\n");
                                            e.printStackTrace();
                                            document = null;


                                        }


                                    }


                                });

                                thread.start();*/


                            }
                        });







    }


}

/*
                                if (document != null)
                                {
                                    Elements options = document.select("td>span.ikc-item-status");
                                    int i = 1;
                                    for (Element element : options)
                                    {
                                        //System.out.println("string"+i+element.text());
                                        myBook = Toast.makeText(MainActivity.this, element.text(), Toast.LENGTH_SHORT);
                                        myBook.show();
                                        i++;
                                    }
                                }
                                else if (document == null)
                                {
                                    myBook = Toast.makeText(MainActivity.this, "empty documnet error", Toast.LENGTH_SHORT);
                                    myBook.show();
                                }


                                String query = mEdit.getText().toString();
                                String url = "https://pyxis.knu.ac.kr/en/#/search/ex?all=1%7Ck%7Ca%7C" + query + "&rq=BRANCH%3D1";


                                Intent intent = new Intent(getApplicationContext(), ShowWebPage.class);
                                intent.putExtra("url", url);
                                startActivity(intent);
                                */