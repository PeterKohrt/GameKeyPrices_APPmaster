package com.example.gamekeyprices_app;

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

import java.util.List;

public class AllFragmentRecyclerAdapter extends RecyclerView.Adapter <AllFragmentRecyclerAdapter.ViewHolder> {

    public List <ListItem> all_list;
    public Context mCtx;
    public FavDB favDB;

    public AllFragmentRecyclerAdapter(List<ListItem> all_list, Context mCtx){
        this.mCtx = mCtx;
        this.all_list = all_list;

    }

    // METHODS FOR ADAPTER
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        favDB = new FavDB(mCtx);
        //create table on first
        SharedPreferences prefs = mCtx.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);
        if (firstStart) {
            createTableOnFirstStart();
        }

        // INFLATE LAYOUT
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        // INITIALIZE CONTENT
        mCtx = parent.getContext();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final ListItem listItem = all_list.get(position);
        //TODO setIsRecycable not the best but easy way
        holder.setIsRecyclable(false);

        readCursorData(listItem, holder);

        // BIND GAME_TITLE
        String game_title_data = all_list.get(position).getGame_title();
        holder.setGameTitle(game_title_data);

        // BIND PRICE_HISTORIC_LOW
        String price_historic_now = all_list.get(position).getPrice_historic_low();
        holder.setPriceHistoricLow(price_historic_now);

        // BIND PRICE_NOW_LOW
        String price_now_low = all_list.get(position).getPrice_now_low();
        holder.setPriceNowLow(price_now_low);

        // BIND CHEAPEST_SHOP_NOW
        String cheapest_shop_now = all_list.get(position).getCheapest_shop_now();
        holder.setCheapestShopNow(cheapest_shop_now);

        // BIND GAME_IMAGE
        String game_image_data = all_list.get(position).getImage_url();
        holder.setGameImage(game_image_data);

        //BIND URL
        String shop_URL = all_list.get(position).getShopLink();
        holder.setURL(shop_URL);

    }

    // METHOD for VIEWHOLDER
    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private ImageView gameImageView;

        private TextView game_title;

        private TextView price_historic_low;
        private TextView price_now_low;
        private TextView cheapest_shop_now;
        private TextView deal_url;
        private Button favBtn;

        // SET mVIEW
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            favBtn = itemView.findViewById(R.id.fav_button_list);

            favBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    ListItem listItem = all_list.get(position);

                    if (listItem.getFavStatus().equals("0")) {
                        listItem.setFavStatus("1");
                        favDB.insertIntoTheDatabase(listItem.getPlain(), listItem.getGame_title(), listItem.getImage_url(), listItem.getFavStatus());
                        favBtn.setBackgroundResource(R.drawable.fav_icon_checked);
                        favBtn.setSelected(true);
                    } else {
                        listItem.setFavStatus("0");
                        favDB.remove_fav(listItem.getPlain());
                        favBtn.setBackgroundResource(R.drawable.fav_icon_unchecked);
                        favBtn.setSelected(false);
                    }
                }
            });
        }

        // SET IMAGE LIST ITEM
        public void setGameImage(String downloadUri){
            gameImageView = mView.findViewById(R.id.game_image);

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.ic_image_placeholder);

            Glide.with(mCtx).applyDefaultRequestOptions(requestOptions).load(downloadUri).into(gameImageView);
        }

        // SET GameTitle
        public void setGameTitle(String gameTitle){
            game_title = mView.findViewById(R.id.game_title);
            game_title.setText(gameTitle);
        }

        // SET Price_Historic_low
        public void setPriceHistoricLow(String priceHistoricLow){
            price_historic_low = mView.findViewById(R.id.historic_low_value);
            price_historic_low.setText(priceHistoricLow);
        }

        // SET Price_Now_low
        public void setPriceNowLow(String priceNowLow){
            price_now_low = mView.findViewById(R.id.now_low_value);
            price_now_low.setText(priceNowLow);
        }

        // SET Price_Now_low
        public void setCheapestShopNow(String cheapestShopNow){
            cheapest_shop_now = mView.findViewById(R.id.cheapest_shop_now);
            cheapest_shop_now.setText(cheapestShopNow);
        }

        // SET URL
        public void setURL(String shopURL){
            deal_url = mView.findViewById(R.id.url_field2);
            deal_url.setText(shopURL);
        }
    }

    @Override
    public int getItemCount() {
        return all_list.size();
    }

    private void createTableOnFirstStart() {
        favDB.insertEmpty();

        SharedPreferences prefs = mCtx.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
    }

    //like in DealsFragmentRecyclerAdapter
    private void readCursorData(ListItem listItem, AllFragmentRecyclerAdapter.ViewHolder viewHolder) {
        Cursor cursor = null;
        SQLiteDatabase db = favDB.getReadableDatabase();
        try {
            cursor = favDB.read_all_data(listItem.getPlain());
            while (cursor.moveToNext()) {
                String item_fav_staus = cursor.getString(cursor.getColumnIndex(FavDB.FAVORITE_STATUS));
                listItem.setFavStatus(item_fav_staus);

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

