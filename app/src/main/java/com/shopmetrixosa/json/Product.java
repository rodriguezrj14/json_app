package com.shopmetrixosa.json;

/**
 * Created by td-engagia
 */
public class Product {
    private String name, price, uom;

    public Product(){

    }

    public Product(String name, String price, String uom){
        this.name=name;
        this.price=price;
        this.uom=uom;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }
}
