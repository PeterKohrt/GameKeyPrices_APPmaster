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
import java.util.List;

public class SearchFragment extends Fragment {

    private AllViewModel allViewModel;
    private List<ListItem> search_list;
    private RecyclerView search_list_view;
    private SearchView search_View;

    // Example for transferring var from activity to class
    // public MainActivity mQuery;

  //  private RecyclerView mListView;
  //  private String convert;
  //  private SearchView mSearchView;

    // ADAPTER
    private AllFragmentRecyclerAdapter allFragmentRecyclerAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_search, container, false);
        // INITIALIZE LAYOUT
        search_list = new ArrayList<>();
        search_list_view = view.findViewById(R.id.rView);
        search_View =view.findViewById(R.id.searchView);

        // INITIALIZE RecyclerAdapter
        allFragmentRecyclerAdapter = new AllFragmentRecyclerAdapter(search_list, getContext());
        search_list_view.setLayoutManager(new LinearLayoutManager(container.getContext()));
        search_list_view.setAdapter(allFragmentRecyclerAdapter);

        // Example for transferring var from activity to class
  /*    mQuery = (MainActivity) getActivity();
        CharSequence getQueryFromMain = mQuery.query;
        String request_plain = getQueryFromMain.toString(); */

        CharSequence searchQuery = search_View.getQuery();
        String request_plain = searchQuery.toString();

        // CONTROL VAR
        Toast.makeText(search_list_view.getContext(), request_plain , Toast.LENGTH_LONG).show();

        //TODO IMPLEMENT SEARCH 2 PLAIN
        loadQuery(request_plain);

        // Inflate the layout for this fragment
        return view;
    }



    private void loadQuery(String request_plain) {
        // TODO URI BUILDER
        String JSON_URL = "https://api.isthereanydeal.com/v01/search/search/?key=0dfaaa8b017e516c145a7834bc386864fcbd06f5&region=eu1&country=DE&q="+request_plain;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response); //Complete JSONObject

                            JSONObject obj_obj = obj.getJSONObject("data");  //only Data Object from Response
                            JSONArray searchArray = obj_obj.getJSONArray("list"); //only list-Array in Data Object
                            JSONObject result = obj.getJSONObject("data");  //only Data Object from Response

                            for (int i = 0; i < searchArray.length(); i++) {
                                JSONObject dealObject = searchArray.getJSONObject(i); //for each entry in list-object get DATA

                                String game_image_url = "https://www.uscustomstickers.com/wp-content/uploads//2018/10/STFU-Funny-Black-Sticker.png"; //TODO GAME-INFO REQUEST 4 PIC
                                String gameTitle = dealObject.getString("title");
                                String price_historic_low = dealObject.getString("price_old")+" €";      //TODO DEPENDS ON REGION SET
                                String price_now_low = dealObject.getString("price_new")+" €";      //TODO DEPENDS ON REGION SET

                                // shop is an separate object in list-array-object -> getJSONObject("shop) ...
                                String shop = dealObject.getJSONObject("shop").getString("name");

                                search_list.add(new ListItem(game_image_url, gameTitle, price_historic_low, price_now_low, shop));
                            }
                            // if response contains no results
                            if (searchArray.length() <= 1){
                                Toast.makeText(search_list_view.getContext(), "no results found", Toast.LENGTH_LONG).show();
                            }
                            // if there are results
                            else {
                            //creating custom adapter object
                            AllFragmentRecyclerAdapter adapter = new AllFragmentRecyclerAdapter(search_list, getContext());
                            //adding the adapter to listview
                            search_list_view.setAdapter(adapter);}

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