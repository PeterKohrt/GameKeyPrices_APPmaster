package com.example.gamekeyprices_app.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gamekeyprices_app.AllFragmentRecyclerAdapter;
import com.example.gamekeyprices_app.ListItem;
import com.example.gamekeyprices_app.R;
import com.example.gamekeyprices_app.ui.all.AllViewModel;

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

    private String mSearchText;
    private Map<String,ListItem> plainMap;

    // ADAPTER
    private AllFragmentRecyclerAdapter allFragmentRecyclerAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_search, container, false);
        // INITIALIZE LAYOUT
        search_list = new ArrayList<>();
        search_View = view.findViewById(R.id.searchView);
        search_list_view = view.findViewById(R.id.rView);

        // INITIALIZE RecyclerAdapter
        allFragmentRecyclerAdapter = new AllFragmentRecyclerAdapter(search_list, getContext());
        search_list_view.setLayoutManager(new LinearLayoutManager(container.getContext()));
        search_list_view.setAdapter(allFragmentRecyclerAdapter);

        // On Query Text Listener -> ON Text Submit is Query loaded
        search_View.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearchText = query; // contains user input
                Toast.makeText(getContext().getApplicationContext(),mSearchText , Toast.LENGTH_SHORT).show();
                loadQuery(mSearchText); //gives user input as string to loadQuery for Request
                return false; }

            @Override
            public boolean onQueryTextChange(String newText) {
                search_list.clear();  //clear text when text is changed
                return false; }});


        // Inflate the layout for this fragment
        return view; }


    private void loadQuery(String request_plain) {
        String JSON_URL = "https://api.isthereanydeal.com/v01/search/search/?key=0dfaaa8b017e516c145a7834bc386864fcbd06f5&limit=30&region=eu1&country=DE&limit=50&q="+request_plain; //TODO DEPENDS ON REGION SET

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response); //Complete JSONObject

                            JSONObject obj_obj = obj.getJSONObject("data");  //only Data Object from Response
                            JSONArray searchArray = obj_obj.getJSONArray("list"); //only list-Array in Data Object
                            JSONObject result = obj.getJSONObject("data");  //only Data Object from Response

                            String plainList = "";
                            plainMap = new HashMap<>();

                            for (int i = 0; i < searchArray.length(); i++) {
                                JSONObject searchObject = searchArray.getJSONObject(i); //for each entry in list-object get DATA

                                String gameTitle = searchObject.getString("title");
                                String plain = searchObject.getString("plain");


                                if (i == searchArray.length())
                                plainList = plainList + searchObject.getString("plain");
                                else plainList = plainList + searchObject.getString("plain") + ",";

                                plainMap.put(searchObject.getString("plain"),new ListItem("", gameTitle, "", "", "", "0", plain));
                            }

                            // if response contains no results
                            if (searchArray.length() <= 1){
                                Toast.makeText(search_list_view.getContext(), "no results found", Toast.LENGTH_LONG).show();
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

                                String INNER_JSON_REQUEST2 = "https://api.isthereanydeal.com/v01/game/overview/?key=0dfaaa8b017e516c145a7834bc386864fcbd06f5&plains="+plainList;

                                StringRequest stringRequest2 = new StringRequest(Request.Method.GET, INNER_JSON_REQUEST2,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONObject obj = new JSONObject(response); //Complete JSONObject
                                                    JSONObject obj_obj = obj.getJSONObject("data");  //only Data Object from Response
                                                    for (String plain : plainMap.keySet()){
                                                        JSONObject plainSearchResult = obj_obj.getJSONObject(plain); //only Data Object from Response
                                                        plainMap.get(plain).price_historic_low = plainSearchResult.getJSONObject("lowest").getString("price");
                                                        plainMap.get(plain).price_now_low = plainSearchResult.getJSONObject("price").getString("price");
                                                        plainMap.get(plain).cheapest_shop_now = plainSearchResult.getJSONObject("price").getString("store");
                                                        plainMap.get(plain).favStatus = "0";
                                                    }
                                                    //creating custom adapter object
                                                    AllFragmentRecyclerAdapter adapter = new AllFragmentRecyclerAdapter(new ArrayList<>(plainMap.values()), getContext());
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
                                requestQueue.add(stringRequest2);requestQueue.add(stringRequest);


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

    }

}