package com.mcvm_app.gamekeyprices_app.ui.favorites;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mcvm_app.gamekeyprices_app.AllFragmentRecyclerAdapter;
import com.mcvm_app.gamekeyprices_app.FavDB;
import com.mcvm_app.gamekeyprices_app.ListItem;
import com.mcvm_app.gamekeyprices_app.MainActivity;
import com.mcvm_app.gamekeyprices_app.R;

import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;


public class FavoritesFragment extends Fragment {

    private RecyclerView favourite_view;
    private FavDB favDB;
    private List<ListItem> favItemList = new ArrayList<>();
    private AllFragmentRecyclerAdapter allFragmentRecyclerAdapter;

    //variables for request
    private ArrayList<String> plainList = new ArrayList<String>();
    private ArrayList<String> plainListImage = new ArrayList<String>();
    private ArrayList<String> plainListTitle = new ArrayList<String>();

    //variables for location
    public MainActivity iCountry;
    public MainActivity iRegion;

    //progressbar
    private ProgressBar favorites_progressbar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        favDB = new FavDB(getActivity());
        favourite_view = view.findViewById(R.id.fav_recycler);
        //favourite_view.setHasFixedSize(true);
        favourite_view.setLayoutManager(new LinearLayoutManager(getActivity()));

        favorites_progressbar = view.findViewById(R.id.progressBar_favorites);
        favorites_progressbar.setVisibility(View.VISIBLE);

        //get country, region from main
        iCountry = (MainActivity) getActivity();
        String setCountry = iCountry.mCountryFromMain;
        iRegion = (MainActivity) getActivity();
        String setRegion = iRegion.mRegionFromMain;

        loadData(setCountry,setRegion);

        // TODO IMPLEMENT AGAIN -> WORKS WITHOUT JSON QUERY BEFORE
/*
        // add item touch helper
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(favourite_view); // set swipe to recyclerview
*/
        return view;
    }

    private void loadData(String country, String region) {
        if (favItemList != null) {
            //favorites_progressbar.setVisibility(View.INVISIBLE);
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
            }

            JSON_URL = JSON_URL.substring(0, JSON_URL.length() -1);
            String JSON_URLRegionized = JSON_URL+country+region;

            StringRequest request = new StringRequest(Request.Method.GET, JSON_URLRegionized,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject obj = new JSONObject(response); //whole response
                                JSONObject data = obj.getJSONObject("data"); //only object data from response

                                JSONObject obj_meta = obj.getJSONObject(".meta"); //meta info for currency USD EUR ...
                                String currency = obj_meta.getString("currency");

                                for(int i = 0; i<plainList.size(); i++) {
                                    JSONObject favObj = data.getJSONObject(plainList.get(i)); //data obj from request for every game in favorite list
                                    JSONObject priceObj = favObj.getJSONObject("price"); //json obj price now
                                    JSONObject lowPriceObj = favObj.optJSONObject("lowest"); //json obj historical low price

                                    //String lowest_price_now = priceObj.getString("price") + " " + currency;
                                    //String historical_price_low = lowPriceObj.getString("price")  + " " + currency;
                                    //Price 2 digits after .
                                    Double lowest_price_now_double = priceObj.getDouble("price");
                                    String lowest_price_now = String.format("%.2f", lowest_price_now_double) + " " + currency;
                                    Double historical_price_low_double = lowPriceObj.getDouble("price");
                                    String historical_price_low = String.format("%.2f", historical_price_low_double) + " " + currency;

                                    String cheapest_shop_now = priceObj.getString("store");

                                    String shopLink = priceObj.getString("url");

                                    favItemList.add(new ListItem(plainListImage.get(i),plainListTitle.get(i),historical_price_low,lowest_price_now,cheapest_shop_now,"0",plainList.get(i), shopLink));
                                }
                                //shut down progressbar
                                favorites_progressbar.setVisibility(View.INVISIBLE);
                                //creating custom adapter object
                                AllFragmentRecyclerAdapter adapter = new AllFragmentRecyclerAdapter(favItemList, getContext());
                                //adding the adapter to listview
                                favourite_view.setAdapter(adapter);

                            } catch (Exception e) {
                                //shut down progressbar
                                favorites_progressbar.setVisibility(View.INVISIBLE);
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            favorites_progressbar.setVisibility(View.INVISIBLE);
                            //displaying the error in toast if occurs
                            Toast.makeText(getActivity().getApplicationContext(), "no games on favorite list", Toast.LENGTH_SHORT).show();
                        }
                    });

            //creating a request queue
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

            //adding the string request to request queue
            requestQueue.add(request);

            //set timeout for response to prevent creating view before data is there
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

    //TODO IMPLEMENT LATER - WORKS WITHOUT REQUEST ONLY WITH DB DATA
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