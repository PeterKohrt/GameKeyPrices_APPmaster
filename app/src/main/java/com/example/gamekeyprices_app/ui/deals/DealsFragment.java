package com.example.gamekeyprices_app.ui.deals;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamekeyprices_app.DealsFragmentRecyclerAdapter;
import com.example.gamekeyprices_app.DealsItem;
import com.example.gamekeyprices_app.R;

import java.util.ArrayList;
import java.util.List;

public class DealsFragment extends Fragment {

    private DealsViewModel dealsViewModel;
    private List<DealsItem> deals_list;
    private RecyclerView deals_list_view;

    // ADAPTER
    private DealsFragmentRecyclerAdapter dealsFragmentRecyclerAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //SET VIEW
        View view = inflater.inflate(R.layout.fragment_deals, container, false);

        // INITIALIZE LAYOUT
        deals_list = new ArrayList<>();
        deals_list_view = view.findViewById(R.id.fragment_deals);

        // INITIALIZE BlogRecyclerAdapter
        dealsFragmentRecyclerAdapter = new DealsFragmentRecyclerAdapter(deals_list);
        deals_list_view.setLayoutManager(new LinearLayoutManager(container.getContext()));
        deals_list_view.setAdapter(dealsFragmentRecyclerAdapter);

        // public DealsItem(String image_url, String game_title, String price_old, String price_new, String shop, String cut, String expire
        final DealsItem testItem = new DealsItem("https://steamcdn-a.akamaihd.net/steam/apps/505170/header.jpg", "Spieltitel", "100.000", "2.27", "steam", "99","01.01.2021");
        final DealsItem testItem2 = new DealsItem("https://steamcdn-a.akamaihd.net/steam/apps/440900/header.jpg", "Spieltitel 2", "0.99", "0.75", "humble","75","never");

        //public ListItem(String image_url, String game_title, String price_historic_low, String price_now_low, String cheapest_shop_now)
        deals_list.add(testItem2);
        deals_list.add(testItem);
        deals_list.add(testItem2);
        deals_list.add(testItem2);
        deals_list.add(testItem2);

        // Inflate the layout for this fragment
        return view;
    }

}
