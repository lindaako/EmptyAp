package com.example.emptyapp;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emptyapp.R;
import com.example.emptyapp.ShowWebPage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ShowWebPage2 extends AppCompatActivity
{
    int line_counter = 0;
    int Availability = 0;
    String JK;

    int AV;
    String url;
    String results2[] = new String[500];
    int id;
    public Menu menu;
    MenuItem menuItem;
    String Request_Mark;

    ShowWebPage.Singleton MY_URL = ShowWebPage.Singleton.getInstance();
    ShowWebPage.Singleton AV_INFO = ShowWebPage.Singleton.getInstance();

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


        if (id == R.id.UseRobotButton)
        {

                AV = AV_INFO.getAV_INFO();


                if (AV>0)
                {
                    Toast.makeText(this, "Sending data...", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    Toast.makeText(this, "This function can only be used for 'Available' books, on the 2nd floor of the University Library!", Toast.LENGTH_LONG).show();
                }

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_web_page2);

        Bundle bundle = getIntent().getExtras();


        if (bundle != null)
        {
            url = bundle.getString("url");
            results2 = getIntent().getStringArrayExtra("RES");

        }

        else
        {
            Toast errorToast = Toast.makeText(ShowWebPage2.this, "Error sending data!", Toast.LENGTH_SHORT);
            errorToast.show();
        }

        WebView webView =  findViewById(R.id.webView);
        TextView contentView =  findViewById(R.id.contentView);


        WebSettings webSettings = webView.getSettings();

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


        class MyJavaScriptInterface
        {


            private TextView contentView;



            public MyJavaScriptInterface(TextView aContentView)
            {
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

                        String result[] = new String[500];
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

                        //Toast.makeText(MainActivity.this,result[62],Toast.LENGTH_LONG).show();

                        for(int cc = 0; cc < line_counter; cc++)
                        {


                            if (result[cc].contains("서명/저자\t"))
                            {
                                JK = result[cc].replace("서명/저자\t","");
                                JK=JK.substring(0,JK.indexOf(" /"));






                                for(int b = 0; b < results2.length; b++)
                                {
                                    if(results2[b] != null)
                                    {

                                        if (results2[b].contains(JK))
                                        {
                                            //Toast.makeText(ShowWebPage2.this, "found it at "+ b +" = " + results2[b], Toast.LENGTH_LONG).show();

                                            for(int bb = 0; bb < 5; bb++)
                                            {
                                                if ((results2[b+bb].contains("] Available")))
                                                {
                                                    invalidateOptionsMenu();
                                                    Availability++;
                                                    AV_INFO.setAV_INFO(Availability);
                                                    Toast.makeText(ShowWebPage2.this, "It is available!", Toast.LENGTH_LONG).show();
                                                    Request_Mark = result[cc + 1].replace("청구기호","");
                                                    Toast.makeText(ShowWebPage2.this, Request_Mark, Toast.LENGTH_LONG).show();
                                                    //Request_Mark = results2[b+bb].replace(" ] Available","");
                                                }

                                            }
                                        }

                                    }

                                }

                            }

                            else

                            {

                            }
                        }






                    }
                });
            }

            @SuppressWarnings("unused")
            @JavascriptInterface
            public void processHTML(String html)
            {
                //Html extract here

                final String content = html;
                contentView.post(new Runnable()
                {
                    public void run()
                    {
                        contentView.setText(content);
                        System.out.println(content);


                    }
                });

            }
        }




        webView.addJavascriptInterface(new MyJavaScriptInterface(contentView), "INTERFACE");
        webView.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageFinished(WebView view, String url)
            {
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


                view.loadUrl("javascript:window.INTERFACE.processContent(document.getElementsByTagName('body')[0].innerText);");

            }
        });

        webView.clearCache(true);
        webView.loadUrl(url);

    }
}
