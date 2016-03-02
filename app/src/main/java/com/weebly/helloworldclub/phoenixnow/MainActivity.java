package com.weebly.helloworldclub.phoenixnow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.titlepage);
    }
    public void loginClick(View view){
        Intent intent=new Intent(MainActivity.this,LoginActivity.class);
        startActivity(intent);
    }
    public void registerClick(View view){
        Intent intent =new Intent(MainActivity.this,RegisterActivity.class);
        startActivity(intent);
    }

}
