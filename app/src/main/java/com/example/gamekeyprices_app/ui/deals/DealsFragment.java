package com.example.gamekeyprices_app.ui.deals;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.gamekeyprices_app.DealsFragmentRecyclerAdapter;
import com.example.gamekeyprices_app.DealsItem;
import com.example.gamekeyprices_app.MainActivity;
import com.example.gamekeyprices_app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DealsFragment extends Fragment {

    public MainActivity iCountry;
    public MainActivity iRegion;

    private List<DealsItem> deals_list;
    private RecyclerView deals_list_view;


    private Map<String, DealsItem> plainMap;

    // ADAPTER
    private DealsFragmentRecyclerAdapter dealsFragmentRecyclerAdapter;
    private Context context;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //SET VIEW
        View view = inflater.inflate(R.layout.fragment_deals, container, false);

        // INITIALIZE LAYOUT
        deals_list = new ArrayList<>();
        deals_list_view = view.findViewById(R.id.fragment_deals);

        // INITIALIZE RecyclerAdapter
        dealsFragmentRecyclerAdapter = new DealsFragmentRecyclerAdapter(deals_list, getContext());
        deals_list_view.setLayoutManager(new LinearLayoutManager(container.getContext()));
        deals_list_view.setAdapter(dealsFragmentRecyclerAdapter);

        iCountry = (MainActivity) getActivity();
        String setCountry = iCountry.mCountryFromMain;
        iRegion = (MainActivity) getActivity();
        String setRegion = iRegion.mRegionFromMain;
        
       loadQuery(setCountry, setRegion);

        // Inflate the layout for this fragment
        return view;
    }

    private void loadQuery(String county, String region) {
        String JSON_URL = "https://api.isthereanydeal.com/v01/deals/list/?key=0dfaaa8b017e516c145a7834bc386864fcbd06f5&limit=100"+county+region;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response); //Complete JSONObject

                            JSONObject obj_obj = obj.getJSONObject("data");  //only Data Object from Response
                            JSONArray gameDealArray = obj_obj.getJSONArray("list"); //only list-Array in Data Object

                            JSONObject obj_meta = obj.getJSONObject(".meta");
                            String currency = obj_meta.getString("currency");

                            String plainList = "";
                            plainMap = new HashMap<>();

                            for (int i = 0; i < gameDealArray.length(); i++) {
                                JSONObject dealObject = gameDealArray.getJSONObject(i); //for each entry in list-object get DATA

                                String gameTitle = dealObject.getString("title");
                                String price_old = dealObject.getString("price_old") + " " + currency;      //TODO DEPENDS ON REGION SET
                                String price_new = dealObject.getString("price_new") + " " + currency;      //TODO DEPENDS ON REGION SET
                                String cut = dealObject.getString("price_cut")+" %";
                                String plain = dealObject.getString("plain");

                                // shop is an separate object in list-array-object -> getJSONObject("shop) ...
                                String shop = dealObject.getJSONObject("shop").getString("name");

                                //date is unix timestamp -> format in date-only - if/else cause respond can be "null"
                                String expire_string =  dealObject.getString("expiry");
                                String output_expiry = "";

                                //expiry-JSON = "null"
                                if (expire_string == "null"){
                                    output_expiry ="n.a.";
                                }
                                //expiry-JSON = "unix-timestamp"
                                else {
                                    Long c = Long.parseLong(expire_string);
                                    Instant instant = Instant.ofEpochSecond(c);
                                    ZoneId z = ZoneId.of("Europe/Berlin");
                                    ZonedDateTime zdt = instant.atZone(z);
                                    LocalDate ld = zdt.toLocalDate();
                                    //set Date Format-Output
                                    output_expiry = ld.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));
                                }


                                if (i == gameDealArray.length())
                                    plainList = plainList + dealObject.getString("plain");
                                else plainList = plainList + dealObject.getString("plain") + ",";

                                plainMap.put(dealObject.getString("plain"),new DealsItem("", gameTitle, price_old, price_new, shop, cut, output_expiry, "0", plain));

                            }

                            // if response contains no results
                            if (gameDealArray.length() <= 1) {
                                Toast.makeText(deals_list_view.getContext(), "no results found", Toast.LENGTH_LONG).show();
                            }
                            // if there are results
                            else {
                                // second request INFO with plains
                                String INNER_JSON_REQUEST = "https://api.isthereanydeal.com/v01/game/info/?key=0dfaaa8b017e516c145a7834bc386864fcbd06f5&plains="+plainList;

                                StringRequest stringRequest = new StringRequest(Request.Method.GET, INNER_JSON_REQUEST,
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
                                                    //creating custom adapter object
                                                    DealsFragmentRecyclerAdapter adapter = new DealsFragmentRecyclerAdapter(new ArrayList<>(plainMap.values()), getContext());
                                                    //adding the adapter to listview
                                                    deals_list_view.setAdapter(adapter);

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
                                requestQueue.add(stringRequest);
                            }
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
        requestQueue.add(stringRequest);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS));

    }
}

