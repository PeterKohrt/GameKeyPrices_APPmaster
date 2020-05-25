package com.example.gamekeyprices_app.ui.category.racing;

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

public class RacingFragment extends Fragment {

    public MainActivity iCountry;
    public MainActivity iRegion;

    private AllViewModel allViewModel;
    private List<ListItem> game_list;
    private RecyclerView game_list_view;

    private ProgressBar racing_progressbar;

    // ADAPTER
    private AllFragmentRecyclerAdapter allFragmentRecyclerAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_racing, container, false);

        // INITIALIZE LAYOUT
        game_list = new ArrayList<>();
        game_list_view = view.findViewById(R.id.racing_recycler);

        // INITIALIZE RecyclerAdapter
        allFragmentRecyclerAdapter = new AllFragmentRecyclerAdapter(game_list, getContext());
        game_list_view.setLayoutManager(new LinearLayoutManager(container.getContext()));
        game_list_view.setAdapter(allFragmentRecyclerAdapter);

        racing_progressbar = view.findViewById(R.id.progressBar_racing);
        racing_progressbar.setVisibility(View.VISIBLE);

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
        final String g1 = "dirtrallyii0";
        final String g2 = "projectcarsii";
        final String g3 = "rfactorii";
        final String g4 = "crewii";
        final String g5 = "roadredemption";
        final String g6 = "nascarheativ";
        final String g7 = "rideiii";
        final String g8 = "motogpii0";
        final String g9 = "trackmaniaiistadium";
        final String g10 = "torquedrift";

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
                            String[] picid = {"690790","378860","365960","646910","300380","1127980","759740","1161490","232910","1029550"};
                            String[] title = {
                                    "DiRT Rally 2.0",
                                    "Project CARS 2",
                                    "rFactor 2",
                                    "The Crew 2",
                                    "Road Redemption",
                                    "NASCAR Heat 4",
                                    "RIDE 3",
                                    "MotoGP™20",
                                    "TrackMania 2 Stadium",
                                    "Torque Drift"};

                            for (int i = 0; i<list.length; i++) {

                                JSONObject gArray = obj_obj.getJSONObject(list[i]);

                                //add ID to Game-Image-URL
                                String game_image_url = "https://steamcdn-a.akamaihd.net/steam/apps/"+picid[i]+"/header.jpg";

                                //set plain titel better than no title atm
                                String gameTitle = title[i];

                                //String price_historic_low = gArray.getJSONObject("lowest").getString("price")+ " " + currency;
                                //String price_now_low = gArray.getJSONObject("price").getString("price")+ " " + currency;
                                Double price_now_low_double = gArray.getJSONObject("price").getDouble("price");
                                String price_now_low = String.format("%.2f", price_now_low_double) + " " + currency;
                                Double price_historic_low_double = gArray.getJSONObject("lowest").getDouble("price");
                                String price_historic_low = String.format("%.2f", price_historic_low_double) + " " + currency;

                                String shop = gArray.getJSONObject("price").getString("store");
                                String plain = list[i];

                                String shopLink = gArray.getJSONObject("price").getString("url");

                                game_list.add(new ListItem(game_image_url, gameTitle, price_historic_low, price_now_low, shop, "0", plain, shopLink)); //CREATE ITEMS
                            }
                            racing_progressbar.setVisibility(View.INVISIBLE);
                            //creating custom adapter object
                            AllFragmentRecyclerAdapter adapter = new AllFragmentRecyclerAdapter(game_list, getContext());
                            //adding the adapter to listview
                            game_list_view.setAdapter(adapter);

                        } catch (JSONException e) {
                            racing_progressbar.setVisibility(View.INVISIBLE);
                            e.printStackTrace();
                        }

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        racing_progressbar.setVisibility(View.INVISIBLE);
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