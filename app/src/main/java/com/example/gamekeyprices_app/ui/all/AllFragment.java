package com.example.gamekeyprices_app.ui.all;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gamekeyprices_app.AllFragmentRecyclerAdapter;
import com.example.gamekeyprices_app.R;
import com.example.gamekeyprices_app.ListItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AllFragment extends Fragment {

    private AllViewModel allViewModel;
    private List<ListItem> game_list;
    private RecyclerView game_list_view;

    // ADAPTER
    private AllFragmentRecyclerAdapter allFragmentRecyclerAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //SET VIEW
        View view = inflater.inflate(R.layout.fragment_all, container, false);

        // INITIALIZE LAYOUT
        game_list = new ArrayList<>();
        game_list_view = view.findViewById(R.id.fragment_all);

        // INITIALIZE BlogRecyclerAdapter
        allFragmentRecyclerAdapter = new AllFragmentRecyclerAdapter(game_list, getContext());
        game_list_view.setLayoutManager(new LinearLayoutManager(container.getContext()));
        game_list_view.setAdapter(allFragmentRecyclerAdapter);

        String JSON_URL = "https://api.isthereanydeal.com/v01/game/lowest/?key=0dfaaa8b017e516c145a7834bc386864fcbd06f5&plains=europauniversalisiv&region=eu1&country=DE";


        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //hiding the progressbar after completion



                        try {

                            JSONObject obj = new JSONObject(response);


                            JSONObject res_data = new JSONObject(obj.get("data").toString());
                            JSONObject game = new JSONObject(res_data.get("europauniversalisiv").toString());

                            game_list.add(new ListItem("https://steamcdn-a.akamaihd.net/steam/apps/255163/header.jpg", game.toString(), "", "", "", ""));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //adding the string request to request queue
        requestQueue.add(stringRequest);

        final ListItem testItem = new ListItem("https://i.kym-cdn.com/photos/images/newsfeed/000/764/965/47a.jpg", "Spiele Titel", "1.0", "1.5", "steam","");
        final ListItem testItem2 = new ListItem("https://steamcdn-a.akamaihd.net/steam/apps/236850/header.jpg", "Spiele Titel 2", "1.0", "2.5", "steam","");


        //public ListItem(String image_url, String game_title, String price_historic_low, String price_now_low, String cheapest_shop_now)

        game_list.add(testItem2);
        game_list.add(testItem);
        game_list.add(testItem2);
        game_list.add(testItem2);
        game_list.add(testItem2);

        // Inflate the layout for this fragment
        return view;
                                                                             }

    }
