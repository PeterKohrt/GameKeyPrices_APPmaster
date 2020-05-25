package com.example.gamekeyprices_app.ui.category.fps;

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

public class FPSFragment extends Fragment {
    //works like every category fragment -> comments like in action

    public MainActivity iCountry;
    public MainActivity iRegion;

    private AllViewModel allViewModel;
    private List<ListItem> game_list;
    private RecyclerView game_list_view;

    private ProgressBar fps_progressbar;

    // ADAPTER
    private AllFragmentRecyclerAdapter allFragmentRecyclerAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_fps, container, false);

        // INITIALIZE LAYOUT
        game_list = new ArrayList<>();
        game_list_view = view.findViewById(R.id.fps_recycler);

        // INITIALIZE RecyclerAdapter
        allFragmentRecyclerAdapter = new AllFragmentRecyclerAdapter(game_list, getContext());
        game_list_view.setLayoutManager(new LinearLayoutManager(container.getContext()));
        game_list_view.setAdapter(allFragmentRecyclerAdapter);

        fps_progressbar = view.findViewById(R.id.progressBar_fps);
        fps_progressbar.setVisibility(View.VISIBLE);

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
        final String g1 = "halflifealyx";
        final String g2 = "dyinglight";
        final String g3 = "deeprockgalactic";
        final String g4 = "doom";
        final String g5 = "blackmesa";
        final String g6 = "ravenfield";
        final String g7 = "bloodandbacon";
        final String g8 = "residentevilviibiohazard";
        final String g9 = "insurgency";
        final String g10 = "halomasterchiefcollection";

        //Transfer as one string
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
                            String[] fpsList = {g1,g2,g3,g4,g5,g6,g7,g8,g9,g10};

                            //Helper Array to get ID for Game-Image-URL
                            String[] picid = {"546560","239140","548430","379720","362890","636480","434570","418370","222880","976730"};
                            String[] title = {"Half-Life: Alyx","Dying Light","Deep Rock Galactic","DOOM","Black Mesa","Ravenfield","Blood and Bacon","RESIDENT EVIL 7 biohazard","Insurgency","Halo: The Master Chief Collection"};

                            for (int i = 0; i<fpsList.length; i++) {

                                JSONObject fpsArray = obj_obj.getJSONObject(fpsList[i]);

                                //add ID to Game-Image-URL
                                String game_image_url = "https://steamcdn-a.akamaihd.net/steam/apps/"+picid[i]+"/header.jpg";

                                //set plain titel better than no title atm
                                String gameTitle = title[i];

                                //String price_historic_low = gArray.getJSONObject("lowest").getString("price")+ " " + currency;
                                //String price_now_low = gArray.getJSONObject("price").getString("price")+ " " + currency;
                                Double price_now_low_double = fpsArray.getJSONObject("price").getDouble("price");
                                String price_now_low = String.format("%.2f", price_now_low_double) + " " + currency;
                                Double price_historic_low_double = fpsArray.getJSONObject("lowest").getDouble("price");
                                String price_historic_low = String.format("%.2f", price_historic_low_double) + " " + currency;

                                String shop = fpsArray.getJSONObject("price").getString("store");
                                String plain = fpsList[i];

                                String shopLink = fpsArray.getJSONObject("price").getString("url");

                                game_list.add(new ListItem(game_image_url, gameTitle, price_historic_low, price_now_low, shop, "0", plain, shopLink)); //CREATE ITEMS
                            }
                            fps_progressbar.setVisibility(View.INVISIBLE);
                            //creating custom adapter object
                            AllFragmentRecyclerAdapter adapter = new AllFragmentRecyclerAdapter(game_list, getContext());
                            //adding the adapter to listview
                            game_list_view.setAdapter(adapter);

                        } catch (JSONException e) {
                            fps_progressbar.setVisibility(View.INVISIBLE);
                            e.printStackTrace();
                        }

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        fps_progressbar.setVisibility(View.INVISIBLE);
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