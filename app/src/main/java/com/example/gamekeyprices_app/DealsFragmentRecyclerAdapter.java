package com.example.gamekeyprices_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class DealsFragmentRecyclerAdapter extends RecyclerView.Adapter <DealsFragmentRecyclerAdapter.ViewHolder> {

    public List <DealsItem> deals_list;
    public Context context;

    public DealsFragmentRecyclerAdapter(List<DealsItem> deals_list){
        this.deals_list = deals_list;

    }

    // METHODS FOR ADAPTER
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // INFLATE LAYOUT
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.deals_item, parent, false);

        //INITIALIZE CONTENT
        context = parent.getContext();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        // BIND GAME_TITLE
        String game_title_data = deals_list.get(position).getGame_title();
        holder.setGameTitle(game_title_data);

        // BIND PRICE_OLD
        String price_old = deals_list.get(position).getPrice_old();
        holder.setPriceOld(price_old);

        // BIND PRICE_NEW
        String price_new = deals_list.get(position).getPrice_new();
        holder.setPriceNew(price_new);

        // BIND SHOP
        String shop_data = deals_list.get(position).getShop();
        holder.setShop(shop_data);

        // BIND GAME_IMAGE
        String game_image_data = deals_list.get(position).getImage_url();
        holder.setGameImage(game_image_data);

        // BIND CUT
        String cut_data = deals_list.get(position).getCut();
        holder.setCut(cut_data);

        // BIND EXPIRE
        String expire_data = deals_list.get(position).getExpire();
        holder.setExpire(expire_data);
    }

    // METHOD for VIEWHOLDER
    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private ImageView gameImageView;

        private TextView game_title;
        private TextView price_new;
        private TextView price_old;
        private TextView shop_name;
        private TextView cut;
        private TextView expire;

        // SET mVIEW
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        // SET IMAGE LIST ITEM
        public void setGameImage(String downloadUri){
            gameImageView = mView.findViewById(R.id.game_image_deals);

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.list_item_placeholder);

            Glide.with(context).applyDefaultRequestOptions(requestOptions).load(downloadUri).into(gameImageView);
        }

        // SET GameTitle
        public void setGameTitle(String gameTitle){
            game_title = mView.findViewById(R.id.game_title_deals);
            game_title.setText(gameTitle);
        }

        // SET Price_Old
        public void setPriceOld(String priceOld){
            price_old = mView.findViewById(R.id.price_old_value);
            price_old.setText(priceOld);
        }

        // SET Price_New
        public void setPriceNew(String priceNew){
            price_new = mView.findViewById(R.id.price_new_value);
            price_new.setText(priceNew);
        }

        // SET Shop
        public void setShop(String shop){
            shop_name = mView.findViewById(R.id.shop);
            shop_name.setText(shop);
        }

        // SET CUT
        public void setCut(String cutValue){
            cut = mView.findViewById(R.id.cut_value);
            cut.setText(cutValue);
        }

        // SET EXPIRE
        public void setExpire(String expireDate){
            expire = mView.findViewById(R.id.expire_value);
            expire.setText(expireDate);
        }
    }

    @Override
    public int getItemCount() {
        return deals_list.size();
    }

}
