package com.example.gamekeyprices_app.ui.all;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.gamekeyprices_app.R;
import com.example.gamekeyprices_app.ListItem;

import java.util.ArrayList;
import java.util.List;

public class AllFragment extends Fragment {


    private AllViewModel allViewModel;
    private List<ListItem> game_list;
    private RecyclerView game_list_view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //SET VIEW
        View view = inflater.inflate(R.layout.fragment_all, container, false);

        // INITIALIZE LAYOUT
        game_list = new ArrayList<>();
        game_list_view = view.findViewById(R.id.fragment_all);

        // Inflate the layout for this fragment
        return view;
                                                                             }
    }
