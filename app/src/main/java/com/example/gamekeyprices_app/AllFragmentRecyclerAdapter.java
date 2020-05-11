package com.example.gamekeyprices_app;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

import org.jetbrains.annotations.Nullable;

public class AllFragmentRecyclerAdapter extends RecyclerView.Adapter <AllFragmentRecyclerAdapter.ViewHolder> {

    public List <ListItem> all_list;
    public Context context;

    public AllFragmentRecyclerAdapter(List<ListItem> all_list){
        this.all_list = all_list;

    }

    // METHODS FOR ADAPTER
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // INFLATE LAYOUT
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        //INITIALIZE CONTENT
        context = parent.getContext();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

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

    }

    // METHOD for VIEWHOLDER
    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private ImageView gameImageView;

        private TextView game_title;

        private TextView price_historic_low;
        private TextView price_now_low;
        private TextView cheapest_shop_now;

        // SET mVIEW
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        // SET IMAGE LIST ITEM
        public void setGameImage(String downloadUri){
            gameImageView = mView.findViewById(R.id.game_image);

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.list_item_placeholder);

            Glide.with(context).applyDefaultRequestOptions(requestOptions).load(downloadUri).into(gameImageView);
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


    }

    @Override
    public int getItemCount() {
        return all_list.size();
    }

}

