package com.example.yassin.odoo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
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
import com.bumptech.glide.Glide;
import com.google.zxing.integration.android.IntentIntegrator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class CheckProductOfCommande extends Fragment {

        ListView listCommande, listProduit;
        List<Order> list;
        List<Product> listProd;
        ProgressBar pb;
        TextView ncp ;
        ImageView scanCode ;
        int boucle = 0;

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
                   listCommande.setVisibility(View.GONE);
                   pb.setVisibility(View.VISIBLE);
                //   getInfo(list.get(i).getProductsID());
                   getProductOfCommand(list.get(i).getProductsID());
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
           //TODO CHECK LIST DE PRODUIT ALL CHECKED TO UPDATE ORDER IN ODOO DATABASE invoice_state MISSING
        }

    private void getOrders() {
        final String email = USI.getSession(getActivity()).getEmail();
        final String pass = USI.getSession(getActivity()).getPass();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, USI.urlToGetCommande,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                      //  Toast.makeText(getActivity(), ""+response, Toast.LENGTH_SHORT).show();
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
    private void getProductOfCommand(final String products){
        List<String> l = new ArrayList<String>();
        try{
            JSONArray jsonArray = new JSONArray(products);
            for(int i = 0;i<jsonArray.length();i++){
                l.add(jsonArray.getString(i));
            }
            getInfo(l);
          //  l.clear();
        }catch (Exception e){
            Toast.makeText(getActivity(), "sj : "+e, Toast.LENGTH_SHORT).show();
        }
    }

    private void getInfo(/*final String productsID*/final List<String>l) {
        final String email = USI.getSession(getActivity()).getEmail();
        final String pass = USI.getSession(getActivity()).getPass();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, USI.urlToGetProduct,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        extraireProduct(response) ;
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
                for(int i = 0;i<l.size();i++) {
                    params.put(i+"",l.get(i));
                }
                params.put("size",l.size()+"");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void extraireProduct(String response) {
        try {
            JSONObject jsonOb = new JSONObject(response);
            JSONArray jsonArray = jsonOb.getJSONArray("data");
            for(int i =0; i<jsonArray.length();i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String idProduct = jsonObject.getString("id");
                String nameProduct = jsonObject.getString("name");
                String codeBar = jsonObject.getString("barcode");
                // String etatProduct = jsonObject.getString("etat");
                String imgProduct = jsonObject.getString("image");
                String quantity = "";//jsonObject.getString("quantity");
                String desc = jsonObject.getString("desc");
                String etat = checkProductScanned(idProduct);
                Product product = new Product(idProduct, nameProduct, codeBar, etat, imgProduct, quantity, desc);
                listProd.add(product);
            }
            setUpListProduct(listProd);
        }catch (Exception e){
            Toast.makeText(getActivity(), ""+e, Toast.LENGTH_SHORT).show();
        }
    }
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

    private void setUpList(List<Order> list) {
        OrderAdapter orderAdapter = new OrderAdapter(getActivity(),list);
        listCommande.setAdapter(orderAdapter);
    }
    private void setUpListProduct(List<Product> listProd) {
        ProductAdapter productAdapter = new ProductAdapter(getActivity(),listProd);
        listProduit.setAdapter(productAdapter);
        productAdapter.notifyDataSetChanged();
        listProduit.setVisibility(View.VISIBLE);
        pb.setVisibility(View.GONE);
        scanCode.setVisibility(View.VISIBLE);
    }

    private String checkProductScanned(String idProduct) {
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String barCodeTxt = data.getStringExtra("res");
        Toast.makeText(getActivity(), "Scanned", Toast.LENGTH_LONG).show();
        checkInList(barCodeTxt);
    }
    private void checkInList(String barcode){
        boolean exist = false;
        int i = 0 ;
        do{
            if(listProd.get(i).getCodeBar().equals(barcode)){
                saveProduct(listProd.get(i).getIdProduct());
                listProd.get(i).setEtatProduct("scanned");
                Toast.makeText(getActivity(), "exist", Toast.LENGTH_SHORT).show();
                //TODO not sure to work
                ProductAdapter productAdapter = new ProductAdapter(getActivity(),listProd);
                listProduit.setAdapter(productAdapter);
                productAdapter.notifyDataSetChanged();
                exist = true;
            }
            i++;
        }while (!exist && i<listProd.size());

    }
    private void saveProduct(String idp){
        String d = "scanned";
        try{
            FileOutputStream fo = getActivity().openFileOutput(idp, getActivity().MODE_PRIVATE);
            fo.write(d.getBytes());
            fo.close();
        }catch (Exception e){}
    }
}
class OrderAdapter extends ArrayAdapter<Order> {

    Activity context;
    List<Order>listOrder;
    public OrderAdapter(Activity context,List<Order>listOrder ) {
        super(context,R.layout.order_list_item, listOrder);
        this.context = context ;
        this.listOrder = listOrder;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.order_list_item,null,false);
        TextView tv = (TextView) view.findViewById(R.id.nameCommande);
        TextView dc = (TextView) view.findViewById(R.id.date_commande);
        ImageView cb = (ImageView) view.findViewById(R.id.checked);
        tv.setText(listOrder.get(position).getNameCommande());
        dc.setText(listOrder.get(position).getDateCommande().substring(0,10));
        if(listOrder.get(position).getEtatCommande().equals("to invoice")){
            cb.setImageResource(R.drawable.checked);
        }else if(listOrder.get(position).getEtatCommande().equals("no")){
            cb.setImageResource(R.drawable.notchecked);
        }
        return view;
    }
}
class ProductAdapter extends ArrayAdapter<Product>{
    Activity context;
    List<Product>listProduct;
    public ProductAdapter(Activity context,List<Product>listProduct ) {
        super(context,R.layout.product_list_item, listProduct);
        this.context = context ;
        this.listProduct = listProduct;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.product_list_item,null,false);
        ImageView imgProduct = (ImageView)view.findViewById(R.id.imgProduct);
        TextView nameProduct = (TextView) view.findViewById(R.id.nameProduct);
        TextView  barCode= (TextView) view.findViewById(R.id.barcode);
        ImageView cb = (ImageView) view.findViewById(R.id.checked);
      //  Glide.with(context).load(listProduct.get(position).getImgProduct()).bitmapTransform(new CropCircleTransformation(context)).into(imgProduct);
        nameProduct.setText(listProduct.get(position).getNameProduct());
        if(barCode.equals("false")){
            barCode.setText("No bar code");
        }else {
            barCode.setText(listProduct.get(position).getCodeBar());
        }
        if(listProduct.get(position).getEtatProduct().equals("scanned")){
            cb.setImageResource(R.drawable.checked);
        }else{
            cb.setImageResource(R.drawable.notchecked);
        }
        cb.setEnabled(false);
        return view;
    }
}
