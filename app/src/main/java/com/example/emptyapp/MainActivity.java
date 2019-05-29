package com.example.emptyapp;

import android.Manifest;
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

public class MainActivity extends AppCompatActivity

{

    Button mButton;
    EditText mEdit;
    public Document document = null;
    String url_1 = "https://pyxis.knu.ac.kr/en/#/search/detail/";
    private static final String FILE_NAME = "Detail_Kyungpook.html";
    private static final int REQUEST_CODE = 2424;

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
            ActivityCompat.requestPermissions(MainActivity.this,permissions,REQUEST_CODE);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        verifyPermissions();
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final WebView browser = findViewById(R.id.browser);
        mButton = findViewById(R.id.button);
        mEdit   = findViewById(R.id.edittext);
        verifyPermissions();

        mButton.setOnClickListener
                (new View.OnClickListener()
                        {

                            @RequiresApi(api = Build.VERSION_CODES.N)
                            public void onClick(View view) {
                                /*
                                String query = mEdit.getText().toString();
                                String url = "https://pyxis.knu.ac.kr/en/#/search/ex?all=1%7Ck%7Ca%7C" + query + "&rq=BRANCH%3D1";

                                Intent intent = new Intent(getApplicationContext(), ShowWebPage.class);
                                intent.putExtra("url", url);
                                startActivity(intent);
                                */
                                File My_File = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), FILE_NAME);//if ready then create a file for external
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


                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } finally {
                                    if (fis != null) {
                                        try {
                                            fis.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
*/

                                Thread thread = new Thread(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        /* An instance of this class will be registered as a JavaScript interface */


                                        try {
                                            // Connect to the web site

                                            File input = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), FILE_NAME);
                                            Document mBlogDocument = Jsoup.parse(input, "UTF-8", url_1);
                                            //Document mBlogDocument = Jsoup.connect(url).get();
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


                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                    }


                                });

                                thread.start();



                            }
                        });







    }




}
