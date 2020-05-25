package com.example.gamekeyprices_app.ui.category.action;

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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gamekeyprices_app.AllFragmentRecyclerAdapter;
import com.example.gamekeyprices_app.ListItem;
import com.example.gamekeyprices_app.MainActivity;
import com.example.gamekeyprices_app.R;
import com.example.gamekeyprices_app.ui.all.AllViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActionFragment extends Fragment {

    //variables for location
    public MainActivity iCountry;
    public MainActivity iRegion;

    private AllViewModel allViewModel;
    private List<ListItem> game_list;
    private RecyclerView game_list_view;

    //progressbar variable
    private ProgressBar action_progressbar;

    // ADAPTER
    private AllFragmentRecyclerAdapter allFragmentRecyclerAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_action, container, false);

        // INITIALIZE LAYOUT
        game_list = new ArrayList<>();
        game_list_view = view.findViewById(R.id.action_recycler);

        // INITIALIZE RecyclerAdapter
        allFragmentRecyclerAdapter = new AllFragmentRecyclerAdapter(game_list, getContext());
        game_list_view.setLayoutManager(new LinearLayoutManager(container.getContext()));
        game_list_view.setAdapter(allFragmentRecyclerAdapter);

        action_progressbar = view.findViewById(R.id.progressBar_action);
        action_progressbar.setVisibility(View.VISIBLE);

        //get country, region from main
        iCountry = (MainActivity) getActivity();
        String setCountry = iCountry.mCountryFromMain;
        iRegion = (MainActivity) getActivity();
        String setRegion = iRegion.mRegionFromMain;

        loadQuery(setCountry, setRegion);

        //inflate the layout for this fragment
        return view;
    }

    private void loadQuery(String county, String region) {

        //define STRING for URL GET REQUEST by Plain - ten preselected 4 each view
        final String g1 = "nierautomata";
        final String g2 = "monsterhunterworld";
        final String g3 = "totalwarthreekingdoms";
        final String g4 = "mordhau";
        final String g5 = "huntshowdown";
        final String g6 = "bioshockiiremastered";
        final String g7 = "control";
        final String g8 = "gearsv";
        final String g9 = "blasphemous";
        final String g10 = "tomclancysghostreconwildlands";

        String glist = ""+g1+"%2C"+g2+"%2C"+g3+"%2C"+g4+"%2C"+g5+"%2C"+g6+"%2C"+g7+"%2C"+g8+"%2C"+g9+"%2C"+g10;

        String JSON_URL_C_R = "https://api.isthereanydeal.com/v01/game/overview/?key=0dfaaa8b017e516c145a7834bc386864fcbd06f5"+county+region;
        String JSON_URL = JSON_URL_C_R + "&plains=" + glist; //GET REQUEST for 10 Preselected Games

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response); //Complete JSONObject
                            JSONObject obj_obj = obj.getJSONObject("data");  //only Data Object from Response

                            JSONObject obj_meta = obj.getJSONObject(".meta"); //meta info for currency USD EUR ...
                            String currency = obj_meta.getString("currency");

                            //Helper Array to get through JSON OBJECTS
                            String[] list = {g1,g2,g3,g4,g5,g6,g7,g8,g9,g10};

                            //Helper Array to get ID for Game-Image-URL
                            String[] picid = {"524220","582010","779340","629760","594650","409720","870780","1097840","774361","460930"};
                            String[] title = {"NieR: Automata","Monster Hunter: World","Total War: Three Kingdoms","Mordhau","Hunt: Showdown","BioShock 2 Remastered","Control","Gears 5","Blasphemous","Tom Clancy´s Ghost Recon Wildlands"};

                            for (int i = 0; i<list.length; i++) {

                                JSONObject gArray = obj_obj.getJSONObject(list[i]);

                                //add ID to Game-Image-URL
                                String game_image_url = "https://steamcdn-a.akamaihd.net/steam/apps/"+picid[i]+"/header.jpg";

                                //set plain title better than no title atm
                                String gameTitle = title[i];

                                //format price info
                                Double price_now_low_double = gArray.getJSONObject("price").getDouble("price");
                                String price_now_low = String.format("%.2f", price_now_low_double) + " " + currency;
                                Double price_historic_low_double = gArray.getJSONObject("lowest").getDouble("price");
                                String price_historic_low = String.format("%.2f", price_historic_low_double) + " " + currency;

                                //shop in extra object (url and name) so get json obj price first
                                String shop = gArray.getJSONObject("price").getString("store");
                                String shopLink = gArray.getJSONObject("price").getString("url");

                                String plain = list[i];

                                game_list.add(new ListItem(game_image_url, gameTitle, price_historic_low, price_now_low, shop, "0", plain, shopLink)); //CREATE ITEMS
                            }
                            //make progressbar invisible
                            action_progressbar.setVisibility(View.INVISIBLE);
                            //creating custom adapter object
                            AllFragmentRecyclerAdapter adapter = new AllFragmentRecyclerAdapter(game_list, getContext());
                            //adding the adapter to listview
                            game_list_view.setAdapter(adapter);

                        } catch (JSONException e) {
                            //make progressbar invisible
                            action_progressbar.setVisibility(View.INVISIBLE);
                            e.printStackTrace();
                        }

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //make progressbar invisible
                        action_progressbar.setVisibility(View.INVISIBLE);
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