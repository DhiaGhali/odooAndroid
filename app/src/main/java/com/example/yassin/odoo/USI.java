package com.example.yassin.odoo;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yassin on 2017-12-01.
 */

public class USI {

    //public final static String HOST ="http://192.168.1.68";
    public final static String HOST ="http://192.168.43.118";
    public final  static String PROJECT  = "/odoo";
    public final static String urlToConnect = HOST+PROJECT+"/connect.php" ;
    public final static String urlToGetCommande = HOST+PROJECT+"/getAllPurchaseOrder.php" ;
    public final static String urlToGetProduct = HOST+PROJECT+"/getAllProductsofOrder.php" ;
    public final static String urlToGetCommandeSales = HOST+PROJECT+"/getAllPurchaseOrderSales.php" ;
    public final static String urlToGetProductSales = HOST+PROJECT+"/getAllProductsofOrderSales.php" ;


    public static boolean checkInternet (Activity context){
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo() != null;
    }

    public static void createSession(Activity context , String email ,String name, String pass, String permission){
        SharedPreferences sharedPreferences = context.getSharedPreferences("session",context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email",email);
        editor.putString("name",name);
        editor.putString("pass",pass);
        editor.putString("permission",permission);
        editor.commit();
    }
    public static User getSession(Activity context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("session",context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email","");
        String name = sharedPreferences.getString("name","");
        String pass = sharedPreferences.getString("pass","");
        String permission= sharedPreferences.getString("permission","");
        User user = new User(email,name,pass,permission);
        return user;
    }
}
