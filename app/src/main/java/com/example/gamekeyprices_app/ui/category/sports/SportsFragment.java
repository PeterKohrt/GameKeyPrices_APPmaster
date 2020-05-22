package com.example.gamekeyprices_app.ui.category.sports;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SportsFragment extends Fragment {

    private AllViewModel allViewModel;
    private List<ListItem> game_list;
    private RecyclerView game_list_view;

    // ADAPTER
    private AllFragmentRecyclerAdapter allFragmentRecyclerAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_sports, container, false);

        // INITIALIZE LAYOUT
        game_list = new ArrayList<>();
        game_list_view = view.findViewById(R.id.sports_recycler);

        // INITIALIZE RecyclerAdapter
        allFragmentRecyclerAdapter = new AllFragmentRecyclerAdapter(game_list, getContext());
        game_list_view.setLayoutManager(new LinearLayoutManager(container.getContext()));
        game_list_view.setAdapter(allFragmentRecyclerAdapter);

        loadQuery();

        // Inflate the layout for this fragment
        return view;
    }

    private void loadQuery() {

        //define STRING for URL GET REQUEST by Plain
        final String g1 = "rocketleague";
        final String g2 = "golfwithyourfriends";
        final String g3= "pipebybmxstreets";
        final String g4 = "descenders";
        final String g5 = "assettocorsa";
        final String g6 = "fiii0ii0";
        final String g7 = "dirtrally";
        final String g8 = "trackmaniaiistadium";
        final String g9 = "sessionskateboardingsimgame";
        final String g10 = "assettocorsacompetizione";

        //Transfer as one string
        String glist = ""+g1+"%2C"+g2+"%2C"+g3+"%2C"+g4+"%2C"+g5+"%2C"+g6+"%2C"+g7+"%2C"+g8+"%2C"+g9+"%2C"+g10;

        String JSON_URL = "https://api.isthereanydeal.com/v01/game/overview/?key=0dfaaa8b017e516c145a7834bc386864fcbd06f5&region=eu1&country=DE&plains="+glist; //GET REQUEST for 10 Preselected Games

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response); //Complete JSONObject
                            JSONObject obj_obj = obj.getJSONObject("data");  //only Data Object from Response

                            //Helper Array to get through JSON OBJECTS
                            String[] list = {g1,g2,g3,g4,g5,g6,g7,g8,g9,g10};

                            //Helper Array to get ID for Game-Image-URL
                            String[] picid = {"252950","431240","815780","681280","244210","1015500","310560","232910","861650","805550"};
                            String[] title = {"Rocket League","Golf With Your Friends","PIPE by BMX Streets","Descenders","Assetto Corsa","F1® 2020","DiRT Rally","TrackMania 2 Stadium","Session: Skateboarding Sim Game","Assetto Corsa Competizione"};

                            for (int i = 0; i<list.length; i++) {

                                JSONObject gArray = obj_obj.getJSONObject(list[i]);

                                // Maybe by string SPLIT TO GET APP-ID PROBLEM IS IF STEAM IS NOT CHEAPEST ATM OR IN PAST
                                //String string = "004-034556";
                                //String[] parts = string.split("-");
                                //String part1 = parts[0]; // 004
                                //String part2 = parts[1]; // 034556
                                //String helper = fpsArray.getJSONObject()

                                //add ID to Game-Image-URL
                                String game_image_url = "https://steamcdn-a.akamaihd.net/steam/apps/"+picid[i]+"/header.jpg"; //TODO IMAGE FOR GAME AUTOMATIKK

                                //set plain titel better than no title atm
                                String gameTitle = title[i];                                                                                          //TODO ADD NO-PLAIN TITLE
                                String price_historic_low = gArray.getJSONObject("lowest").getString("price")+" €";      //TODO DEPENDS ON REGION SET
                                String price_now_low = gArray.getJSONObject("price").getString("price")+" €";      //TODO DEPENDS ON REGION SET
                                String shop = gArray.getJSONObject("price").getString("store");
                                String plain = list[i];

                                game_list.add(new ListItem(game_image_url, gameTitle, price_historic_low, price_now_low, shop, "0", plain)); //CREATE ITEMS
                            }

                            //creating custom adapter object
                            AllFragmentRecyclerAdapter adapter = new AllFragmentRecyclerAdapter(game_list, getContext());
                            //adding the adapter to listview
                            game_list_view.setAdapter(adapter);

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