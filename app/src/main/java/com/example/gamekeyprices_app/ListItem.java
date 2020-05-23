package com.example.gamekeyprices_app;

public class ListItem {
    public String image_url, game_title, price_historic_low, price_now_low, cheapest_shop_now, favStatus, plain, shopLink;

    public ListItem(){}

    // CONSTRUCTORS
    public ListItem(String image_url, String game_title, String price_historic_low, String price_now_low, String cheapest_shop_now, String favStatus, String plain, String shopLink) {
        this.image_url = image_url;
        this.game_title = game_title;
        this.price_historic_low = price_historic_low;
        this.price_now_low = price_now_low;
        this.cheapest_shop_now = cheapest_shop_now;
        this.favStatus = favStatus;
        this.plain = plain;
        this.shopLink = shopLink;
    }

    //GET GAME IMAGE
    public String getImage_url() {return image_url;}

    //GET GAME_TITLE
    public String getGame_title() {return game_title;}

    //GET PRICE_HISTORIC_LOW
    public String getPrice_historic_low() {return price_historic_low;}

    //GET PRICE_LOW_NOW
    public String getPrice_now_low() {return price_now_low;}

    //GET CHEAPEST_SHOP_NOW
    public String getCheapest_shop_now() {return cheapest_shop_now;}

    //GET FAV_STATUS
    public String getFavStatus() {return favStatus;}

    //SET FAV_STATUS
    public void setFavStatus(String favStatus) {
        this.favStatus = favStatus;
    }

    //GET PLAIN
    public String getPlain() {return plain;}

    //GET SHOPLINK
    public String getShopLink() {return shopLink;}


}
