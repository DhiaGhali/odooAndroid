package com.example.yassin.odoo;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ProductActivity extends AppCompatActivity {

    private ImageView productBtn , arrangeBtn ;
    private FrameLayout frame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        productBtn = (ImageView) findViewById(R.id.reception);
        arrangeBtn = (ImageView) findViewById(R.id.move_to_stock);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.frame, new CheckProductOfCommande())  ;
        ft.commit();

        productBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productBtn.setImageResource(R.drawable.barcode_selected);
                arrangeBtn.setImageResource(R.drawable.arrange);
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.frame, new CheckProductOfCommande());
                ft.commit();
            }
        });
        arrangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrangeBtn.setImageResource(R.drawable.arrange_selected);
                productBtn.setImageResource(R.drawable.barcode);
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.frame, new ArrangeProduct());
                ft.commit();
            }
        });

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
