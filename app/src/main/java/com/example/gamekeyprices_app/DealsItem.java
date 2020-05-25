package com.example.gamekeyprices_app;

public class DealsItem {
    public String image_url, game_title, price_old, price_new, shop, cut, expire, favStatus, plain, shoplink;

    public DealsItem(){}

    // CONSTRUCTORS
    public DealsItem(String image_url, String game_title, String price_old, String price_new, String shop, String cut, String expire, String favStatus, String plain, String shoplink) {
        this.image_url = image_url;
        this.game_title = game_title;
        this.price_old = price_old;
        this.price_new = price_new;
        this.shop = shop;
        this.cut = cut;
        this.expire = expire;
        this.favStatus = favStatus;
        this.plain = plain;
        this.shoplink = shoplink;
    }

    //GET GAME IMAGE
    public String getImage_url() {return image_url;}

    //GET GAME_TITLE
    public String getGame_title() {return game_title;}

    //GET PRICE_HISTORIC_LOW
    public String getPrice_old() {return price_old;}

    //GET PRICE_LOW_NOW
    public String getPrice_new() {return price_new;}

    //GET CHEAPEST_SHOP_NOW
    public String getShop() {return shop;}

    //GET CUT_NOW
    public String getCut() {return cut;}

    //GET EXPIRE
    public String getExpire() {return  expire;}

    //GET FAV_STATUS
    public String getFavStatus() {return favStatus;}

    //SET FAV_STATUS
    public void setFavStatus(String favStatus) {
        this.favStatus = favStatus;
    }

    //GET PLAIN
    public String getPlain() {return plain;}

    //GET SHOP_LINK
    public String getShopLink() {return shoplink;}
}
