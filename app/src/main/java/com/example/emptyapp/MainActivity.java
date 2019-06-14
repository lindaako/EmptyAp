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
import android.widget.Toolbar;

public class MainActivity extends AppCompatActivity

{

    Button mButton;
    EditText mEdit;
    public Document document = null;


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
/*

                                File External_File = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Myfile.txt");
                                String text = "Fizzle rocks";
                                FileOutputStream fos = null;

                                try
                                {
                                    fos = new FileOutputStream(External_File);
                                    //fos = openFileOutput(FILE_NAME, MODE_PRIVATE);

                                    fos.write(text.getBytes());

                                    Toast.makeText(MainActivity.this, "Saved to " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+ "/" + FILE_NAME, Toast.LENGTH_LONG).show()
                                    ;

                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } finally {
                                    if (fos != null) {
                                        try {
                                            fos.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }


                                FileInputStream fis = null;

                                try {

                                    fis = new FileInputStream(External_File);
                                    //DataInputStream in = new DataInputStream(fis);
                                    //BufferedReader br =new BufferedReader(new InputStreamReader(in));
                                    //String strLine;

                                    //fis = openFileInput(FILE_NAME);
                                    InputStreamReader isr = new InputStreamReader(fis);
                                    BufferedReader br = new BufferedReader(isr);
                                    StringBuilder sb = new StringBuilder();
                                    String text2;

                                    while ((text2 = br.readLine()) != null)
                                    {
                                        sb.append(text2).append("\n");
                                    }
                                    System.out.println(sb);

                                    Toast.makeText(MainActivity.this, sb, Toast.LENGTH_LONG).show()
                                    ;


                                }
                                catch (FileNotFoundException e)
                                        {
                                    e.printStackTrace();
                                }
                                catch (IOException e)
                                        {
                                    e.printStackTrace();
                                }
                                finally
                                        {
                                            if (fis != null)
                                            {
                                                try
                                                {
                                                    fis.close();
                                                }
                                                catch (IOException e)
                                                {
                                                    e.printStackTrace();
                                                }
                                             }
                                        }
*/