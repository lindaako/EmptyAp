package com.example.emptyapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity
{
    Button mButton = (Button)findViewById(R.id.button);
    EditText mEdit   = (EditText)findViewById(R.id.edittext);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mButton.setOnClickListener
                (
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        String query = mEdit.getText().toString();
                        String url = "https://pyxis.knu.ac.kr/en/#/search/ex?all=1%7Ck%7Ca%7C" + query;
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                });

    }
}
