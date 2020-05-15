package com.example.gamekeyprices_app.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamekeyprices_app.ListItem;
import com.example.gamekeyprices_app.R;
import com.example.gamekeyprices_app.ui.all.AllViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private AllViewModel allViewModel;
    private List<ListItem> game_list;
    private RecyclerView game_list_view;

    private SearchViewModel searchViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //SET VIEW
        View view = inflater.inflate(R.layout.fragment_all, container, false);

        // INITIALIZE LAYOUT
        game_list = new ArrayList<>();
        game_list_view = view.findViewById(R.id.fragment_all);

        // INITIALIZE BlogRecyclerAdapter
       // allFragmentRecyclerAdapter = new AllFragmentRecyclerAdapter(game_list);
       // game_list_view.setLayoutManager(new LinearLayoutManager(container.getContext()));
       //  game_list_view.setAdapter(allFragmentRecyclerAdapter);


        // Inflate the layout for this fragment
        return view;
    }
}