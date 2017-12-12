package com.example.yassin.odoo;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class CommandeActivity extends AppCompatActivity {

    private ImageView ExpOrder , Order_ ;
    private FrameLayout frame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commande);

        ExpOrder = (ImageView) findViewById(R.id.reception);
        Order_ = (ImageView) findViewById(R.id.move_to_stock);
        frame = (FrameLayout) findViewById(R.id.frame);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.frame, new CheckProductOfCommandeSales())  ;
        ft.commit();

     /*   ExpOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExpOrder.setImageResource(R.drawable.barcode_selected);
                Order_.setImageResource(R.drawable.arrange);
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.frame, new CheckProductOfCommande());
                ft.commit();
            }
        });
        Order_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Order_.setImageResource(R.drawable.arrange_selected);
                ExpOrder.setImageResource(R.drawable.barcode);
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.frame, new ArrangeProduct());
                ft.commit();
            }
        }); */

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if(scanResult != null){
            Fragment fragment = getFragmentManager().findFragmentById(R.id.frame);
            intent.putExtra("res",scanResult.getContents());
            fragment.onActivityResult(requestCode, resultCode,intent);
        }
    }

    @Override
    public void onBackPressed(){}
}
