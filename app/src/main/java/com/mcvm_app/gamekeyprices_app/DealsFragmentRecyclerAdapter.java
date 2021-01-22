package com.mcvm_app.gamekeyprices_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

//import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DealsFragmentRecyclerAdapter extends RecyclerView.Adapter <DealsFragmentRecyclerAdapter.ViewHolder> {

    public List <DealsItem> deals_list;
    public Context mCtx;
    public FavDB favDB;

    public DealsFragmentRecyclerAdapter(List<DealsItem> deals_list, Context mCtx){
        this.mCtx = mCtx;
        this.deals_list = deals_list;

    }

    //methods for adapter
    //@NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        favDB = new FavDB(mCtx);

        //create table on first
        SharedPreferences prefs = mCtx.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);
        if (firstStart) {
            createTableOnFirstStart();
        }

        //inflate layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.deals_item, parent, false);

        //initialize content
        mCtx = parent.getContext();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final DealsItem dealsItem = deals_list.get(position);
        //TODO setIsRecycable not the best but easy way
        holder.setIsRecyclable(false);

        readCursorData(dealsItem, holder);

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

        // BIND URL
        String shop_URL = deals_list.get(position).getShopLink();
        holder.setURL(shop_URL);
    }

    // METHOD FOR VIEWHOLDER
    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private ImageView gameImageView;

        private TextView game_title;
        private TextView price_new;
        private TextView price_old;
        private TextView shop_name;
        private TextView cut;
        private TextView expire;
        private Button favBtn;
        private TextView deal_url;

        // SET mVIEW
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            favBtn = itemView.findViewById(R.id.fav_button_deal);

            favBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    DealsItem dealsItem = deals_list.get(position);

                   if (dealsItem.getFavStatus().equals("0")) {
                        dealsItem.setFavStatus("1");
                        favDB.insertIntoTheDatabase(dealsItem.getPlain(), dealsItem.getGame_title(), dealsItem.getImage_url(), dealsItem.getFavStatus());
                        favBtn.setBackgroundResource(R.drawable.fav_icon_checked);
                        favBtn.setSelected(true);
                    } else {
                        dealsItem.setFavStatus("0");
                        favDB.remove_fav(dealsItem.getPlain());
                        favBtn.setBackgroundResource(R.drawable.fav_icon_unchecked);
                        favBtn.setSelected(false);
                    }
                }
            });
        }

        // SET IMAGE LIST ITEM
        public void setGameImage(String downloadUri){
            gameImageView = mView.findViewById(R.id.game_image_deals);

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.ic_image_placeholder);

            Glide.with(mCtx).applyDefaultRequestOptions(requestOptions).load(downloadUri).into(gameImageView);
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

        //SET URL
        public void setURL(String shopURL){
            deal_url = mView.findViewById(R.id.url_field);
            deal_url.setText(shopURL);
        }
    }

    @Override
    public int getItemCount() {
        return deals_list.size();
    }

    private void createTableOnFirstStart() {
        favDB.insertEmpty();

        SharedPreferences prefs = mCtx.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
    }

    private void readCursorData(DealsItem dealsItem, ViewHolder viewHolder) {
        Cursor cursor = null;
        SQLiteDatabase db = favDB.getReadableDatabase();
        try {
            cursor = favDB.read_all_data(dealsItem.getPlain());
            while (cursor.moveToNext()) {
                //reading db column for column
                String item_fav_staus = cursor.getString(cursor.getColumnIndex(FavDB.FAVORITE_STATUS));
                dealsItem.setFavStatus(item_fav_staus);

                //check fav status and depending on that set icon
                if (item_fav_staus != null && item_fav_staus.equals("1")) {
                    viewHolder.favBtn.setBackgroundResource(R.drawable.fav_icon_checked);
                } else if (item_fav_staus != null && item_fav_staus.equals("0")) {
                    viewHolder.favBtn.setBackgroundResource(R.drawable.fav_icon_unchecked);
                }
            }
        }
        catch (Exception e){
        }

        finally {
            if (cursor != null && cursor.isClosed())
                cursor.close();
            db.close();
        }

    }

}

