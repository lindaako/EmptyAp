package com.example.emptyapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class ShowWebPage extends AppCompatActivity
{
    WebView simpleWebView;
    String url;
    Document document;
    Toast myBook;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_web_page);

        simpleWebView = findViewById(R.id.simpleWebView);
        simpleWebView.setWebViewClient(new MyWebViewClient());

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

            if ( url.contains("https://pyxis.knu.ac.kr/en/#/search/detail/") )
            {
                Toast myToast = Toast.makeText(ShowWebPage.this, url, Toast.LENGTH_SHORT);
                myToast.show();

                try
                {
                    document = Jsoup.connect(url).get();

                    Element BookAvailabilityInfo = document.select("span.ikc-item-status").first();
                    String htmlString = BookAvailabilityInfo.text();

                    myBook = Toast.makeText(ShowWebPage.this, htmlString, Toast.LENGTH_SHORT);
                    myBook.show();

                    /*
                    if (htmlString.contains("Not Available") )
                    {
                        myBook = Toast.makeText(ShowWebPage.this, "Book is not Available", Toast.LENGTH_SHORT);
                        myBook.show();
                    }
                    else
                    {
                        myBook = Toast.makeText(ShowWebPage.this, "Book is Available", Toast.LENGTH_SHORT);
                        myBook.show();
                    }


                    for (Element urls : BookAvailabilityInfo)
                    {
                        if (((urls.text()).toString()).contains("Not Available") )
                        {
                            myBook = Toast.makeText(ShowWebPage.this, "Book is not Available", Toast.LENGTH_SHORT);
                            myBook.show();
                        }
                        else
                        {
                            myBook = Toast.makeText(ShowWebPage.this, "Book is Available", Toast.LENGTH_SHORT);
                            myBook.show();
                        }

                    }*/

                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    document = null;
                }
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
       /*
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            view.loadUrl(url);

            return true;
        }*/
    }
}
