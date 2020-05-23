package com.example.gamekeyprices_app.ui.favorites;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gamekeyprices_app.AllFragmentRecyclerAdapter;
import com.example.gamekeyprices_app.FavDB;
import com.example.gamekeyprices_app.ListItem;
import com.example.gamekeyprices_app.MainActivity;
import com.example.gamekeyprices_app.R;

import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;


public class FavoritesFragment extends Fragment {

    private RecyclerView favourite_view;
    private FavDB favDB;
    private List<ListItem> favItemList = new ArrayList<>();
    private AllFragmentRecyclerAdapter allFragmentRecyclerAdapter;

    private ArrayList<String> plainList = new ArrayList<String>();
    private ArrayList<String> plainListImage = new ArrayList<String>();
    private ArrayList<String> plainListTitle = new ArrayList<String>();
    private int ArrayListLength = 0;

    public MainActivity iCountry;
    public MainActivity iRegion;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        favDB = new FavDB(getActivity());
        favourite_view = view.findViewById(R.id.fav_recycler);
        //favourite_view.setHasFixedSize(true);
        favourite_view.setLayoutManager(new LinearLayoutManager(getActivity()));

        iCountry = (MainActivity) getActivity();
        String setCountry = iCountry.mCountryFromMain;
        iRegion = (MainActivity) getActivity();
        String setRegion = iRegion.mRegionFromMain;

        loadData(setCountry,setRegion);
/*
        // add item touch helper
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(favourite_view); // set swipe to recyclerview
*/
        return view;
    }

    private void loadData(String country, String region) {
        if (favItemList != null) {
            favItemList.clear();
        }
        SQLiteDatabase db = favDB.getReadableDatabase();
        Cursor cursor = favDB.select_all_favorite_list();

        String JSON_URL = "https://api.isthereanydeal.com/v01/game/overview/?key=0dfaaa8b017e516c145a7834bc386864fcbd06f5&plains=";

        try {
            // JSON URL WHILE
            while (cursor.moveToNext()) {
                String request_Plain = cursor.getString(cursor.getColumnIndex(FavDB.KEY_ID));
                String image = cursor.getString(cursor.getColumnIndex(FavDB.ITEM_IMAGE));
                String gameTitle = cursor.getString(cursor.getColumnIndex(FavDB.GAME_TITLE));
                JSON_URL = JSON_URL + request_Plain + ",";

                plainList.add(request_Plain);
                plainListImage.add(image);
                plainListTitle.add(gameTitle);
                ArrayListLength = ArrayListLength + 1;
            }

            JSON_URL = JSON_URL.substring(0, JSON_URL.length() -1);
            String JSON_URLRegionized = JSON_URL+country+region;

            StringRequest request = new StringRequest(StringRequest.Method.GET, JSON_URLRegionized,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                JSONObject data = obj.getJSONObject("data");

                                JSONObject obj_meta = obj.getJSONObject(".meta");
                                String currency = obj_meta.getString("currency");

                                for(int i = 0; i<ArrayListLength; i++) {
                                    JSONObject favObj = data.getJSONObject(plainList.get(i));
                                    JSONObject priceObj = favObj.getJSONObject("price");
                                    JSONObject lowPriceObj = favObj.optJSONObject("lowest");

                                    String lowest_price_now = priceObj.getString("price") + " " + currency;
                                    String cheapest_shop_now = priceObj.getString("store");
                                    String historical_price_low = lowPriceObj.getString("price")  + " " + currency;

                                    String shopLink = priceObj.getString("url");

                                    favItemList.add(new ListItem(plainListImage.get(i),plainListTitle.get(i),historical_price_low,lowest_price_now,cheapest_shop_now,"0",plainList.get(i), shopLink));
                                    //favItemList.add(new ListItem(plainListImage.get(i),plainListTitle.get(i),historical_price_low,lowest_price_now,cheapest_shop_now,"0",plainList.get(i));
                                }

                                //creating custom adapter object
                                AllFragmentRecyclerAdapter adapter = new AllFragmentRecyclerAdapter(favItemList, getContext());
                                //adding the adapter to listview
                                favourite_view.setAdapter(adapter);


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //displaying the error in toast if occurrs
                            Toast.makeText(getActivity().getApplicationContext(), "no games on favorite list", Toast.LENGTH_SHORT).show();
                        }
                    });

            //creating a request queue
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

            //adding the string request to request queue
            requestQueue.add(request);

            request.setRetryPolicy(new DefaultRetryPolicy(
                    50000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS));

        } finally {
            if (cursor != null && cursor.isClosed())
                cursor.close();
            db.close();
        }


    }
/*
    // remove item after swipe
    private ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition(); // get position which is swipe
            final ListItem listItem = favItemList.get(position);
            if (direction == ItemTouchHelper.RIGHT | direction == ItemTouchHelper.LEFT) { //if swipe left/right
                allFragmentRecyclerAdapter.notifyItemRemoved(position); // item removed from recyclerview
                favItemList.remove(position); //then remove item
                favDB.remove_fav(listItem.getPlain()); // remove item from database
            }
        }
    };
*/
}