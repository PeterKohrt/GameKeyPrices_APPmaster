package com.mcvm_app.gamekeyprices_app.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
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

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import com.mcvm_app.gamekeyprices_app.AllFragmentRecyclerAdapter;
import com.mcvm_app.gamekeyprices_app.ListItem;
import com.mcvm_app.gamekeyprices_app.MainActivity;
import com.mcvm_app.gamekeyprices_app.R;
import com.mcvm_app.gamekeyprices_app.ui.all.AllViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchFragment extends Fragment {

    private AllViewModel allViewModel;
    private List<ListItem> search_list;
    private RecyclerView search_list_view;
    private SearchView search_View;

    //variables for request - user input - map for requests from start request to inner request
    private String mSearchText;
    private Map<String,ListItem> plainMap;
    private String plainList;

    //variables for location
    public MainActivity iCountry;
    public MainActivity iRegion;

    //progressbar
    private ProgressBar search_progressbar;

    //Interstitial AD
    public InterstitialAd interstitialAd;

    // ADAPTER
    private AllFragmentRecyclerAdapter allFragmentRecyclerAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //get country, region from main
        iCountry = (MainActivity) getActivity();
        final String setCountry = iCountry.mCountryFromMain;
        iRegion = (MainActivity) getActivity();
        final String setRegion = iRegion.mRegionFromMain;

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // INITIALIZE LAYOUT
        search_list = new ArrayList<>();
        search_View = view.findViewById(R.id.searchView);
        search_list_view = view.findViewById(R.id.rView);

        // INITIALIZE RecyclerAdapter
        allFragmentRecyclerAdapter = new AllFragmentRecyclerAdapter(search_list, getContext());
        search_list_view.setLayoutManager(new LinearLayoutManager(container.getContext()));
        search_list_view.setAdapter(allFragmentRecyclerAdapter);
        search_progressbar = view.findViewById(R.id.progressBar_search);

        search_progressbar.setVisibility(View.INVISIBLE);

        //INITIALIZE BANNER AD AND SEND REQUEST
        AdView mAdView = (AdView) view.findViewById(R.id.adView_search);
        AdRequest adRequestBanner = new AdRequest.Builder().build();
        mAdView.loadAd(adRequestBanner);
        //Toast.makeText(getContext().getApplicationContext(),mAdView.getAdUnitId().toString(),Toast.LENGTH_SHORT).show(); //| Контрольная работа

        // Create the InterstitialAd + set the adUnitId + load Ad.
        interstitialAd = new InterstitialAd(getContext());
        interstitialAd.setAdUnitId("ca-app-pub-6392169959833277/7884985419");
        //Toast.makeText(getContext().getApplicationContext(),interstitialAd.getAdUnitId().toString(),Toast.LENGTH_SHORT).show(); //| Контрольная работа
        AdRequest adRequestInterstitial = new AdRequest.Builder().build();
        interstitialAd.loadAd(adRequestInterstitial);

   /*     interstitialAd.setAdListener(
                new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        //Toast.makeText(getContext().getApplicationContext(), "onAdLoaded()", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        String error =
                                String.format("domain: %s, code: %d, message: %s", loadAdError.getDomain(), loadAdError.getCode(), loadAdError.getMessage());
                        //Toast.makeText(getContext().getApplicationContext(), "onAdFailedToLoad() with error: " + error, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAdClosed() {
                    }
                }); */

        // on Query Text Listener -> ON Text Submit Query is loaded
        search_View.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                // contains user input
                mSearchText = query;
                Toast.makeText(getContext().getApplicationContext(),mSearchText , Toast.LENGTH_SHORT).show();
                search_progressbar.setVisibility(View.VISIBLE);



                //gives user input as string to loadQuery for Request
                loadQuery(mSearchText, setCountry, setRegion);
                showInterstitial();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //clear list when text is changed
                search_list.clear();
                search_progressbar.setVisibility(View.INVISIBLE);
                //reset view
                AllFragmentRecyclerAdapter adapter = new AllFragmentRecyclerAdapter(search_list, getContext());
                search_list_view.setAdapter(adapter);
                return false;
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void loadQuery(String request_plain, final String country, final String region) {
         String JSON_URL = "https://api.isthereanydeal.com/v01/search/search/?key=0dfaaa8b017e516c145a7834bc386864fcbd06f5&limit=30&limit=100&q="+request_plain+country+region; //TODO DEPENDS ON REGION SET

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response); //Complete JSONObject

                            JSONObject obj_obj = obj.getJSONObject("data");  //only Data Object from Response
                            JSONArray searchArray = obj_obj.getJSONArray("list"); //only list-Array in Data Object
                            JSONObject result = obj.getJSONObject("data");  //only Data Object from Response

                            plainList = "";
                            plainMap = new HashMap<>();

                            for (int i = 0; i < searchArray.length(); i++) {
                                JSONObject searchObject = searchArray.getJSONObject(i); //for each entry in list-object get DATA

                                String gameTitle = searchObject.getString("title");
                                String plain = searchObject.getString("plain");

                                //adds every plain to the 2nd query url seperated by "," except the last element
                                if (i == searchArray.length())
                                plainList = plainList + searchObject.getString("plain");
                                else plainList = plainList + searchObject.getString("plain") + ",";

                                plainMap.put(searchObject.getString("plain"),new ListItem("", gameTitle, "", "", "", "0", plain, ""));
                            }

                            // if response contains no results
                            if (searchArray.length() <= 1){
                                search_progressbar.setVisibility(View.INVISIBLE);
                                Toast.makeText(search_list_view.getContext(), "no results found", Toast.LENGTH_LONG).show();
                            }

                            // if there are results
                            else {
                                // second request INFO with plains
                                String INNER_JSON_REQUEST = "https://api.isthereanydeal.com/v01/game/info/?key=0dfaaa8b017e516c145a7834bc386864fcbd06f5&plains="+plainList;

                                StringRequest stringRequest1 = new StringRequest(Request.Method.GET, INNER_JSON_REQUEST,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONObject obj = new JSONObject(response); //Complete JSONObject
                                                    JSONObject obj_obj = obj.getJSONObject("data");  //only Data Object from Response
                                                    for (String plain : plainMap.keySet()){
                                                        JSONObject plainSearchResult = obj_obj.getJSONObject(plain); //only Data Object from Response
                                                        plainMap.get(plain).image_url = plainSearchResult.getString("image");
                                                    }
                                                    // third request for price info and shop per game
                                                    String INNER_JSON_REQUEST2 = "https://api.isthereanydeal.com/v01/game/overview/?key=0dfaaa8b017e516c145a7834bc386864fcbd06f5&plains="+plainList+country+region;

                                                    StringRequest stringRequest2 = new StringRequest(Request.Method.GET, INNER_JSON_REQUEST2,
                                                            new Response.Listener<String>() {
                                                                @Override
                                                                public void onResponse(String response) {
                                                                    try {
                                                                        JSONObject obj = new JSONObject(response); //Complete JSONObject
                                                                        JSONObject obj_obj = obj.getJSONObject("data");  //only Data Object from Response

                                                                        JSONObject obj_meta = obj.getJSONObject(".meta"); //meta info for currency USD EUR ...
                                                                        String currency = obj_meta.getString("currency");

                                                                        for (String plain : plainMap.keySet()){
                                                                            JSONObject plainSearchResult = obj_obj.getJSONObject(plain); //only Data Object from Response

                                                                            //plainMap.get(plain).price_historic_low = plainSearchResult.getJSONObject("lowest").getString("price")+" "+currency;
                                                                            //plainMap.get(plain).price_now_low = plainSearchResult.getJSONObject("price").getString("price")+" "+currency;
                                                                            //Price 2 digits after "."
                                                                            Double price_historic_low_double = plainSearchResult.getJSONObject("lowest").getDouble("price");
                                                                            plainMap.get(plain).price_historic_low = String.format("%.2f", price_historic_low_double) + " " + currency;
                                                                            Double price_now_low_double = plainSearchResult.getJSONObject("price").getDouble("price");
                                                                            plainMap.get(plain).price_now_low = String.format("%.2f", price_now_low_double) + " " + currency;

                                                                            plainMap.get(plain).cheapest_shop_now = plainSearchResult.getJSONObject("price").getString("store");
                                                                            plainMap.get(plain).shopLink = plainSearchResult.getJSONObject("price").getString("url");
                                                                            plainMap.get(plain).favStatus = "0";
                                                                        }
                                                                        //creating custom adapter object
                                                                        AllFragmentRecyclerAdapter adapter = new AllFragmentRecyclerAdapter(new ArrayList<>(plainMap.values()), getContext());
                                                                        //make progressbar invisible
                                                                        search_progressbar.setVisibility(View.INVISIBLE);
                                                                        //adding the adapter to listview
                                                                        search_list_view.setAdapter(adapter);

                                                                    } catch (JSONException e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            },
                                                            new Response.ErrorListener() {
                                                                @Override
                                                                public void onErrorResponse(VolleyError error) {
                                                                    //displaying the error in toast if occurrs
                                                                    Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                                                }
                                                            });

                                                    //creating a request queue
                                                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

                                                    //adding the string request to request queue
                                                    requestQueue.add(stringRequest2);requestQueue.add(stringRequest2);

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                //displaying the error in toast if occurrs
                                                Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                //creating a request queue
                                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

                                //adding the string request to request queue
                                requestQueue.add(stringRequest1);

                                }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurs
                        Toast.makeText(getActivity().getApplicationContext(), "error: timeout", Toast.LENGTH_SHORT).show();
                        search_progressbar.setVisibility(View.INVISIBLE);
                    }
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //adding the string request to request queue
        requestQueue.add(stringRequest);

        //setting timeout let the server time to respond else the view is maybe empty
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS));
    }

    private void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and restart the game.
        if (interstitialAd != null && interstitialAd.isLoaded()) {
            interstitialAd.show();
        } else {
            Toast.makeText(getContext().getApplicationContext(), "Ad did not load"
                    , Toast.LENGTH_SHORT).show();
        }
    }


}