package com.example.gamekeyprices_app.ui.favorites;

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
import com.example.gamekeyprices_app.DealsFragmentRecyclerAdapter;
import com.example.gamekeyprices_app.DealsItem;
import com.example.gamekeyprices_app.ListItem;
import com.example.gamekeyprices_app.R;
import com.example.gamekeyprices_app.ui.all.AllViewModel;

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

public class FavoritesFragment extends Fragment {

    private AllViewModel allViewModel;
    private List<ListItem> favorite_list;
    private RecyclerView favorite_list_view;

    private Map<String,ListItem> plainMap;
    private String mFavPlains;

    // ADAPTER
    private AllFragmentRecyclerAdapter allFragmentRecyclerAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_favorites, container, false);
        // INITIALIZE LAYOUT
        favorite_list = new ArrayList<>();
        favorite_list_view = view.findViewById(R.id.fav_recycler);

        // INITIALIZE RecyclerAdapter
        allFragmentRecyclerAdapter = new AllFragmentRecyclerAdapter(favorite_list, getContext());
        favorite_list_view.setLayoutManager(new LinearLayoutManager(container.getContext()));
        favorite_list_view.setAdapter(allFragmentRecyclerAdapter);

        //TODO ADD PLAINS TO QUERY OVERVIEW + INFO
        //loadQuery(mFavPlains);

        final ListItem testItem = new ListItem("https://i.kym-cdn.com/photos/images/newsfeed/000/764/965/47a.jpg", "Spiele Titel", "1.0", "1.5", "steam","0","");
        final ListItem testItem2 = new ListItem("https://steamcdn-a.akamaihd.net/steam/apps/236850/header.jpg", "Spiele Titel 2", "1.0", "2.5", "steam","0","");


        //public ListItem(String image_url, String game_title, String price_historic_low, String price_now_low, String cheapest_shop_now)

        favorite_list.add(testItem2);
        favorite_list.add(testItem);
        favorite_list.add(testItem2);
        favorite_list.add(testItem2);
        favorite_list.add(testItem2);


        // Inflate the layout for this fragment
        return view; }


    private void loadQuery(String favPlains) {


    }

}
