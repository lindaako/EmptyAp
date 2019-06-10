package com.example.emptyapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.content.res.AppCompatResources;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;



public class ShowWebPage extends AppCompatActivity
{
    WebView simpleWebView;
    String url;
    String url_1 = "https://pyxis.knu.ac.kr/en/#/search/detail/";
    Document document = null;
    Toast myBook;
    private static final String FILE_NAME = "Detail_Kyungpook.html";
    private static final int REQUEST_CODE = 2424;


    int Availability;
    int id;
    public Menu menu;


    MenuItem menuItem;


    public static class Singleton
    {
        private  static Singleton instance = null;
        private String MY_URL;

        protected Singleton()
        {

            // Exists only to defeat instantiation.
        }

        public void setString(String s)
        {
            this.MY_URL=s;
        }

        public String getString()
        {
            return this.MY_URL;
        }

        public static Singleton getInstance()
        {
            if(instance == null)
            {
                instance = new Singleton();
            }
            return instance;
        }
    }

    Singleton MY_URL = Singleton.getInstance();

    private void verifyPermissions()
    {
        Log.d("This App", "verifyPermissions: Asking user fo permissions");
        String[] permissions =
                {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                };

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[0]) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this.getApplicationContext(),
                        permissions[1]) == PackageManager.PERMISSION_GRANTED)
        {

        }

        else
        {
            ActivityCompat.requestPermissions(ShowWebPage.this,permissions,REQUEST_CODE);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        verifyPermissions();
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.my_menu, menu);
         this.menu = menu;

        menuItem = menu.findItem(R.id.UseRobotButton);

        if (menuItem != null)
        {
            //tintMenuIcon(ShowWebPage.this, menuItem, android.R.color.holo_purple);
            //menuItem.setIcon(ContextCompat.getDrawable(this, R.drawable.robotvectorfacetransparent));
            if (Availability == 1)
                System.out.println(" Robot button found !");

        }
        return super.onCreateOptionsMenu(menu);
    }

/*
    public void doThis(MenuItem item)
    {
        Toast.makeText(this, "This function is only available for the 5th floor!", Toast.LENGTH_LONG).show();
    }*/




    public void SetMenuIcon(MenuItem item)
    {
        id = item.getItemId();

        if (id == R.id.UseRobotButton)
        {
            Toast.makeText(this, "Robot button found !", Toast.LENGTH_LONG).show();
            menuItem.setIcon(ContextCompat.getDrawable(this, R.drawable.robotvectorfacetransparent));
            // do something here
        }

    }



    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        id = item.getItemId();


            if (id == R.id.UseRobotButton)
            {
                if ( url.contains(url_1) )
                    Toast.makeText(this, "This function is only available for the 5th floor!", Toast.LENGTH_LONG).show();

                else
                    Toast.makeText(this, url, Toast.LENGTH_LONG).show();
                // do something here
            }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_web_page);

        simpleWebView = findViewById(R.id.browser);
        simpleWebView.setWebViewClient(new MyWebViewClient());
        verifyPermissions();

        Bundle bundle = getIntent().getExtras();

        if (bundle != null)
        {
            url = bundle.getString("url");
        }

        else
        {
            Toast errorToast = Toast.makeText(ShowWebPage.this, "Error sending data!", Toast.LENGTH_SHORT);
            errorToast.show();
        }

        simpleWebView.getSettings().setJavaScriptEnabled(true);
        simpleWebView.loadUrl(url); // load a web page in a web view



    }

    private class MyWebViewClient extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon)
        {
            super.onPageStarted(view, url, favicon);

            Log.d("WebView", "your current url when webpage loading.." + url);
        }

        @Override
        public void onPageFinished(WebView view, String url)
        {
            Log.d("WebView", "your current url when webpage loading.. finish " + url);


            if ( url.contains(url_1) )
            {
                Toast myToast = Toast.makeText(ShowWebPage.this, url, Toast.LENGTH_SHORT);
                myToast.show();



                //Then download the webpage and call it FILE_NAME = "Detail_Kyungpook.html";

                File My_File = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), FILE_NAME);//if ready then create a file for external


                Thread thread = new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        /* An instance of this class will be registered as a JavaScript interface */


                        try
                        {
                            // Connect to the web site

                            File input = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), FILE_NAME);
                            Document mBlogDocument = Jsoup.parse(input, "UTF-8", url_1);
                            //System.out.println(mBlogDocument);

                            // Using Elements to get the Meta data
                            Elements mElementDataSize = mBlogDocument.select("span[class=ikc-item-status]");
                            //System.out.println(mElementDataSize);
                            // Locate the content attribute
                            int mElementSize = mElementDataSize.size();
                            System.out.println(mElementSize);


                            for (int i = 0; i < mElementSize; i++)
                            {
                                Element BookStatusData = mBlogDocument.select("span.ikc-item-status").get(i);
                                System.out.println(BookStatusData.text());
                                if(BookStatusData.text().equals("Available"))
                                {
                                    Availability++;
                                    if (Availability == 1)
                                    {

                                                //tintMenuIcon(ShowWebPage.this, menuItem, android.R.color.holo_purple);
                                                //menuItem.setIcon(ContextCompat.getDrawable(ShowWebPage.this, R.drawable.robotvectorfacetransparent));

                                                //SetMenuIcon();

                                                //homie = findViewById(id);
                                                //System.out.println(" Robot button found !" + homie);



                                    }

                                }

                            }

                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }

                        System.out.println(Availability + " Available books!");
                    }


                });

                thread.start();



            }


            super.onPageFinished(view, url);
        }

        @Override
        public void onLoadResource(WebView view, String url)
        {
            // TODO Auto-generated method stub
            super.onLoadResource(view, url);
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            System.out.println("when you click on any interlink on webview that time you got url :-" + url);

            return super.shouldOverrideUrlLoading(view, url);
        }

    }
}
