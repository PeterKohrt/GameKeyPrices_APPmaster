package com.mcvm_app.gamekeyprices_app.ui.category.adventure;


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
import com.mcvm_app.gamekeyprices_app.AllFragmentRecyclerAdapter;
import com.mcvm_app.gamekeyprices_app.ListItem;
import com.mcvm_app.gamekeyprices_app.MainActivity;
import com.mcvm_app.gamekeyprices_app.R;
import com.mcvm_app.gamekeyprices_app.ui.all.AllViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdventureFragment extends Fragment {
    //works like every category fragment -> comments like in action

    public MainActivity iCountry;
    public MainActivity iRegion;

    private AllViewModel allViewModel;
    private List<ListItem> game_list;
    private RecyclerView game_list_view;

    private ProgressBar adventure_progressbar;

    // ADAPTER
    private AllFragmentRecyclerAdapter allFragmentRecyclerAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_adventure, container, false);

        // INITIALIZE LAYOUT
        game_list = new ArrayList<>();
        game_list_view = view.findViewById(R.id.adventure_recycler);

        // INITIALIZE RecyclerAdapter
        allFragmentRecyclerAdapter = new AllFragmentRecyclerAdapter(game_list, getContext());
        game_list_view.setLayoutManager(new LinearLayoutManager(container.getContext()));
        game_list_view.setAdapter(allFragmentRecyclerAdapter);

        adventure_progressbar = view.findViewById(R.id.progressBar_adventure);
        adventure_progressbar.setVisibility(View.VISIBLE);

        iCountry = (MainActivity) getActivity();
        String setCountry = iCountry.mCountryFromMain;
        iRegion = (MainActivity) getActivity();
        String setRegion = iRegion.mRegionFromMain;

        loadQuery(setCountry, setRegion);

        // Inflate the layout for this fragment
        return view;
    }

    private void loadQuery(String county, String region) {

        //define STRING for URL GET REQUEST by Plain
        final String g1 = "reddeadredemptionii";
        final String g2 = "aplaguetaleinnocence";
        final String g3 = "shadowoftombraiderdefinitiveedition";
        final String g4 = "observation";
        final String g5 = "riseoftombraiderii0yearcelebration";
        final String g6 = "starwarsjedifallenorder";
        final String g7 = "arksurvivalevolved";
        final String g8 = "remnantfromashes";
        final String g9 = "theforest";
        final String g10 = "raft";

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

                            JSONObject obj_meta = obj.getJSONObject(".meta");
                            String currency = obj_meta.getString("currency");

                            //Helper Array to get through JSON OBJECTS
                            String[] list = {g1,g2,g3,g4,g5,g6,g7,g8,g9,g10};

                            //Helper Array to get ID for Game-Image-URL
                            String[] picid = {"1174180","752590","750920","906100","391220","1172380","346110","617290","242760","648800"};
                            String[] title = {
                                    "Red Dead Redemption 2",
                                    "A Plague Tale: Innocence",
                                    "Shadow Of The Tomb Raider - Definitive Edition",
                                    "Observation",
                                    "Rise of the Tomb Raider: 20 Year Celebration",
                                    "STAR WARS Jedi: Fallen Order™",
                                    "ARK: Survival Evolved",
                                    "Remnant: From the Ashes",
                                    "The Forest",
                                    "Raft"};

                            for (int i = 0; i<list.length; i++) {

                                JSONObject gArray = obj_obj.getJSONObject(list[i]);

                                //add ID to Game-Image-URL
                                String game_image_url = "https://steamcdn-a.akamaihd.net/steam/apps/"+picid[i]+"/header.jpg";

                                //set plain titel better than no title atm
                                String gameTitle = title[i];

                                //format price info
                                Double price_now_low_double = gArray.getJSONObject("price").getDouble("price");
                                String price_now_low = String.format("%.2f", price_now_low_double) + " " + currency;
                                Double price_historic_low_double = gArray.getJSONObject("lowest").getDouble("price");
                                String price_historic_low = String.format("%.2f", price_historic_low_double) + " " + currency;

                                String shop = gArray.getJSONObject("price").getString("store");
                                String plain = list[i];

                                String shopLink = gArray.getJSONObject("price").getString("url");

                                game_list.add(new ListItem(game_image_url, gameTitle, price_historic_low, price_now_low, shop, "0", plain, shopLink)); //CREATE ITEMS
                            }
                            adventure_progressbar.setVisibility(View.INVISIBLE);
                            //creating custom adapter object
                            AllFragmentRecyclerAdapter adapter = new AllFragmentRecyclerAdapter(game_list, getContext());
                            //adding the adapter to listview
                            game_list_view.setAdapter(adapter);

                        } catch (JSONException e) {
                            adventure_progressbar.setVisibility(View.INVISIBLE);
                            e.printStackTrace();
                        }

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        adventure_progressbar.setVisibility(View.INVISIBLE);
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