package com.weebly.helloworldclub.phoenixnow;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BackEnd backend=new BackEnd(null,null,null);
        if(backend.getToken()==null){
            setContentView(R.layout.titlepage);
        } else if(backend.getToken()!=null){
            setContentView(R.layout.homepage);
            getSupportActionBar().setSubtitle("Signed in with \""+backend.getEmail()+"\"");
            Log.d("MainActivityToken",backend.getToken());
        }

        TextView txt = (TextView) findViewById(R.id.phoenix);
        Typeface font = Typeface.createFromAsset(getAssets(), "BrushScriptStd.otf");
        txt.setTypeface(font);


    }
    public void loginActivity(View view){
        Intent intent=new Intent(MainActivity.this,LoginActivity.class);
        startActivity(intent);
    }
    public void registerActivity(View view){
        Intent intent =new Intent(MainActivity.this,RegisterActivity.class);
        startActivity(intent);
    }
    public void signIn(){
        
    }

}
