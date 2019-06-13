package com.example.emptyapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
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
import java.io.StringReader;
import java.lang.reflect.Method;


public class ShowWebPage extends AppCompatActivity
{
    int AV;
    WebView simpleWebView;
    TextView contentView ;
    String url;
    String url_1 = "https://pyxis.knu.ac.kr/en/#/search/detail/";
    private static final String FILE_NAME = "Detail_Kyungpook.html";
    private static final int REQUEST_CODE = 2424;
    int line_counter = 0;
    int Availability = 0;
    int id;
    public Menu menu;
    MenuItem menuItem;
    String result[] = new String[500];


    public static class Singleton
    {
        private  static Singleton instance = null;
        private String MY_URL;
        private int AV_INFO;

        protected Singleton()
        {

            // Exists only to defeat instantiation.
        }

        public void setString(String s)
        {
            this.MY_URL=s;
        }
        public void setAV_INFO(int av)
        {
            this.AV_INFO=av;
        }

        public String getString()
        {
            return this.MY_URL;
        }
        public int getAV_INFO()
    {
        return this.AV_INFO;
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
    Singleton AV_INFO = Singleton.getInstance();

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

        AV = AV_INFO.getAV_INFO();


        if (AV>0)
        {
            menuItem.setIcon(ContextCompat.getDrawable(this, R.drawable.robotvectorfacetransparent));
            System.out.println(" Button Functionality ON !");

        }

        return super.onCreateOptionsMenu(menu);
    }



    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        id = item.getItemId();
        String Book_url = MY_URL.getString();

        Toast.makeText(this, "Please Select a Book!", Toast.LENGTH_LONG).show();
                // do something here


        return super.onOptionsItemSelected(item);
    }

    public class MyJavaScriptInterface
    {
        private TextView contentView ;

        public MyJavaScriptInterface(TextView aContentView)
        {
            contentView = aContentView;
        }

        @SuppressWarnings("unused")
        @JavascriptInterface
        public void processContent(String aContent)
        {
            final String content = aContent;
            contentView.post(new Runnable()
            {
                public void run()
                {


                    contentView.setText(content);
                    System.out.print(content);

                    BufferedReader br = new BufferedReader(new StringReader(content));


                    String line = "";


                    while (true)
                    {
                        try
                        {
                            if (!((line = br.readLine()) != null))
                                break;
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }


                        result[line_counter] = line;
                        line_counter++;
                    }



                }
            });
        }


    }



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_web_page);

        simpleWebView = findViewById(R.id.browser);
        contentView =  findViewById(R.id.contentView);

        verifyPermissions();
        AV_INFO.setAV_INFO(0);

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



        MY_URL.setString(url);

        WebSettings webSettings = simpleWebView.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setJavaScriptEnabled(true);
        try {
            Method m = WebSettings.class.getMethod("setMixedContentMode", int.class);
            if ( m == null )
            {
                Log.e("WebSettings", "Error getting setMixedContentMode method");
            }
            else {
                m.invoke(webSettings, 2); // 2 = MIXED_CONTENT_COMPATIBILITY_MODE
                Log.i("WebSettings", "Successfully set MIXED_CONTENT_COMPATIBILITY_MODE");
            }
        }
        catch (Exception ex) {
            Log.e("WebSettings", "Error calling setMixedContentMode: " + ex.getMessage(), ex);
        }




        simpleWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        simpleWebView.addJavascriptInterface(new MyJavaScriptInterface(contentView), "INTERFACE");
        simpleWebView.setWebViewClient(new MyWebViewClient());
        simpleWebView.clearCache(true);
        MY_URL.setString(url);
        simpleWebView.loadUrl(url); // load a web page in a web view



    }

    public  class MyWebViewClient extends WebViewClient
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


            MY_URL.setString(url);

            try
            {
                synchronized(this)
                {
                    wait(1000);
                }
            }

            catch(InterruptedException ex)
            {

            }

            simpleWebView.loadUrl("javascript:window.INTERFACE.processContent(document.getElementsByTagName('body')[0].innerText);");

            if ( url.contains(url_1) )

            {
                Intent intent = new Intent(getApplicationContext(), ShowWebPage2.class);
                intent.putExtra("url", url);
                intent.putExtra("RES", result);
                startActivity(intent);
                //Toast.makeText(ShowWebPage.this, "Found", Toast.LENGTH_LONG).show();


            }
            else
                {
                    Availability = 0;
                    AV_INFO.setAV_INFO(Availability);
                    invalidateOptionsMenu();
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
