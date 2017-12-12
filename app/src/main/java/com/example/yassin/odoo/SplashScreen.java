package com.example.yassin.odoo;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!checkLogin()) {
                    Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(intent);
                }else{
                 //   Toast.makeText(SplashScreen.this, "1", Toast.LENGTH_SHORT).show();
                    if(USI.getSession(SplashScreen.this).getPermission().equals("product")) {
                       // Toast.makeText(SplashScreen.this, "2", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SplashScreen.this, ProductActivity.class);
                        startActivity(intent);
                    }else if(USI.getSession(SplashScreen.this).getPermission().equals("commande")){
                        //TODO : to remove comment
                       // Toast.makeText(SplashScreen.this, "3", Toast.LENGTH_SHORT).show();
                         Intent intent = new Intent(SplashScreen.this, CommandeActivity.class);
                        startActivity(intent);
                    }

                }
            }
        },5000);

    }

    private boolean checkLogin() {
        if(!USI.getSession(this).getEmail().equals("")){
            return true;
        }else{
            return false;
        }
    }
}
