package com.example.yassin.odoo;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckProductOfCommandeSales extends Fragment {

        ListView listCommande, listProduit;
        List<Order> list;
        List<Product> listProd;
        ProgressBar pb;
        TextView ncp ;
        ImageView scanCode ;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
           View view = inflater.inflate(R.layout.fragment_check_product_of_commande, container, false);
           list = new ArrayList<Order>();
           listProd = new ArrayList<Product>();
           listCommande = (ListView) view.findViewById(R.id.listCommande);
           listProduit = (ListView) view.findViewById(R.id.listProduit);
           pb = (ProgressBar) view.findViewById(R.id.pb);
           ncp = (TextView) view.findViewById(R.id.nocp);
           scanCode =(ImageView) view.findViewById(R.id.scanbarcode);
           getOrders();

          listCommande.setOnItemClickListener(new AdapterView.OnItemClickListener() {
               @Override
               public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              //     listCommande.setVisibility(View.GONE);
            //       listProduit.setVisibility(View.VISIBLE);
             //      getProductOfCommand(list.get(i).getProductsID());
               }
           });
           scanCode.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   IntentIntegrator integrator = new IntentIntegrator(getActivity());
                   integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                   integrator.setPrompt("Scan");
                   integrator.setCameraId(0);
                   integrator.setBeepEnabled(false);
                   integrator.setBarcodeImageEnabled(false);
                   integrator.initiateScan();
               }
           });
           return view;
        }
        //TODO SEND REQUEST TO GET ORDERS AND PRODUCT DATA

    private void getOrders() {
        final String email = USI.getSession(getActivity()).getEmail();
        final String pass = USI.getSession(getActivity()).getPass();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, USI.urlToGetCommandeSales,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                      //  Toast.makeText(getActivity(), ""+response, Toast.LENGTH_SHORT).show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                              if(response.indexOf("noOrder")!=-1){
                                    pb.setVisibility(View.GONE);
                                    ncp.setVisibility(View.VISIBLE);
                                    ncp.setText(getResources().getString(R.string.no_commande));
                                }else{
                                    extraireOrder(response) ;
                                    pb.setVisibility(View.GONE);
                                    listCommande.setVisibility(View.VISIBLE);
                                }
                            }
                        },2000);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), ""+error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("pass", pass);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
    /*
    private void getProductOfCommand(final String products){
        List<Integer> idProduct = new ArrayList<Integer>();
        try{
            JSONArray jsonArray = new JSONArray(products);
            for(int i = 0;i<jsonArray.length();i++){
                idProduct.add(Integer.parseInt(jsonArray.getString(i)));
            }
            getProductInformation(idProduct);
        }catch (Exception e){
            Toast.makeText(getActivity(), "sj : "+e, Toast.LENGTH_SHORT).show();
        }
    }
    */
    //TODO EXTRAIRE LIST IDPRODUCT
/*
    private void getProductInformation(final List<Integer> idProduct) {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(boucle<idProduct.size()){
                    getInfo(idProduct.get(boucle));
                    boucle++;
                    handler.postDelayed(this,3000);
                }else{
                    setUpListProduct(listProd);
                }
            }
        },3000);

    }
  */
//TODO GET PRODUCT INFO
/*
    private void getInfo(final Integer integer) {
        final String email = USI.getSession(getActivity()).getEmail();
        final String pass = USI.getSession(getActivity()).getPass();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, USI.urlToGetProductSales,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        extraireProduct(response) ;
                        Toast.makeText(getActivity(), ""+response, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "error :"+error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("pass", pass);
                params.put("id",integer+"");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
    */
    //TODO EXTRAIRE JSON DATA

    private void extraireOrder(String commandeData) {
        try {
            JSONArray jsonArray = new JSONArray(commandeData);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String idCommande = jsonObject.getString("id");
                String nameCommande = jsonObject.getString("name");
                String etatCommande = jsonObject.getString("invoice_status");
                String dateCommande = jsonObject.getString("date_planned");
                String productsID = jsonObject.getString("order_line");
                Order order = new Order(idCommande,nameCommande,etatCommande,dateCommande,productsID);
                list.add(order);
            }
            setUpList(list);
        }catch (Exception e){
            Toast.makeText(getActivity(), ""+e, Toast.LENGTH_SHORT).show();
        }
    }
    /*private void extraireProduct(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String idProduct = jsonObject.getString("id");
            String nameProduct = jsonObject.getString("name");
            String codeBar = jsonObject.getString("codeBar");
           // String etatProduct = jsonObject.getString("etat");
            String imgProduct = jsonObject.getString("image");
            String quantity = jsonObject.getString("quantity");
            String desc = jsonObject.getString("description");
            String etat = checkProductScanned(idProduct);
            Product product = new Product(idProduct,nameProduct,codeBar,etat,imgProduct,quantity,desc);
            listProd.add(product);
        }catch (Exception e){
            Toast.makeText(getActivity(), ""+e, Toast.LENGTH_SHORT).show();
        }
    }
    */
    //TODO SETUP LIST ADAPTER

    private void setUpList(List<Order> list) {
        OrderAdapter orderAdapter = new OrderAdapter(getActivity(),list);
        listCommande.setAdapter(orderAdapter);
    }
/*    private void setUpListProduct(List<Product> listProd) {
        ProductAdapter productAdapter = new ProductAdapter(getActivity(),listProd);
        listProduit.setAdapter(productAdapter);
    }
    */
    //TODO CHECK PRODUCT SCANNED BEFORE OR NOT

    /*private String checkProductScanned(String idProduct) {
        String x = "";
        try{
            FileInputStream fi = getActivity().openFileInput(idProduct);
            int c;
            while((c= fi.read())!=-1){
                x+=Character.toString((char)c);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return x;
    }
    */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String barCodeTxt = data.getStringExtra("res");
        Toast.makeText(getActivity(), "Scanned", Toast.LENGTH_LONG).show();
        //checkInList(barCodeTxt);
    }
    //TODO check barcode in list
    /*
    private void checkInList(String barcode){
        boolean exist = false;
        int i = 0 ;
        do{
            if(listProd.get(i).getCodeBar().equals(barcode)){
                saveProduct(listProd.get(i).getIdProduct());
                listProd.get(i).setEtatProduct("scanned");
                //TODO not sure to work
                ProductAdapter productAdapter = new ProductAdapter(getActivity(),listProd);
                listProduit.setAdapter(productAdapter);
                productAdapter.notifyDataSetChanged();
                exist = true;
            }
        }while (!exist && i<listProd.size());

    }
    */
    //TODO saveProduct scanned
    /*
    private void saveProduct(String idp){
        String d = "scanned";
        try{
            FileOutputStream fo = getActivity().openFileOutput(idp, getActivity().MODE_PRIVATE);
            fo.write(d.getBytes());
            fo.close();
        }catch (Exception e){}
    }
    */

}
