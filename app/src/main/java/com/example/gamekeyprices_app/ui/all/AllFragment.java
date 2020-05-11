package com.example.gamekeyprices_app.ui.all;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.gamekeyprices_app.AllFragmentRecyclerAdapter;
import com.example.gamekeyprices_app.R;
import com.example.gamekeyprices_app.ListItem;

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
        allFragmentRecyclerAdapter = new AllFragmentRecyclerAdapter(game_list);
        game_list_view.setLayoutManager(new LinearLayoutManager(container.getContext()));
        game_list_view.setAdapter(allFragmentRecyclerAdapter);


        final ListItem testItem = new ListItem("https://i.kym-cdn.com/photos/images/newsfeed/000/764/965/47a.jpg", "Spiele Titel", "1.0", "0.5", "steam");
        final ListItem testItem2 = new ListItem("https://steamcdn-a.akamaihd.net/steam/apps/236850/header.jpg", "Spiele Titel 2", "1.0", "0.5", "steam");


        //public ListItem(String image_url, String game_title, String price_historic_low, String price_now_low, String cheapest_shop_now)

        game_list.add(testItem2);
        game_list.add(testItem);

        // Inflate the layout for this fragment
        return view;
                                                                             }

    }
