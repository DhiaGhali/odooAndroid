package com.example.yassin.odoo;

/**
 * Created by Yassin on 2017-12-08.
 */

class Order {
    private String idCommande,nameCommande , etatCommande, dateCommande,productsID;

    public Order(String idCommande, String nameCommande, String etatCommande , String dateCommande , String productsID) {
        this.idCommande = idCommande;
        this.nameCommande = nameCommande;
        this.etatCommande = etatCommande;
        this.dateCommande = dateCommande;
        this.productsID = productsID;
    }

    public String getEtatCommande() {
        return etatCommande;
    }

    public String getIdCommande() {
        return idCommande;
    }

    public String getNameCommande() {
        return nameCommande;
    }

    public String getDateCommande() {
        return dateCommande;
    }

    public String getProductsID() {
        return productsID;
    }
}
