package com.example.yassin.odoo;

/**
 * Created by Yassin on 2017-12-08.
 */

class Product {

    private String idProduct;
    private String nameProduct ;
    private String codeBar ;
    private String etatProduct ;
    private String imgProduct ;
    private String quantity ;
    private String desc ;
    public Product(String idProduct, String nameProduct, String codeBar, String etatProduct , String imgProduct ,
                   String quantity ,String desc ) {
        this.idProduct = idProduct;
        this.nameProduct =nameProduct;
        this.codeBar = codeBar;
        this.etatProduct = etatProduct;
        this.imgProduct = imgProduct ;
        this.quantity = quantity ;
        this.desc = desc;
    }

    public String getCodeBar() {
        return codeBar;
    }

    public String getDesc() {
        return desc;
    }

    public String getEtatProduct() {
        return etatProduct;
    }

    public void setEtatProduct(String etatProduct) {
        this.etatProduct = etatProduct;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public String getImgProduct() {
        return imgProduct;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public String getQuantity() {
        return quantity;
    }

}
