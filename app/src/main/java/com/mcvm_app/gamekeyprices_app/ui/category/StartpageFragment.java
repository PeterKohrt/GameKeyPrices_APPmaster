package com.mcvm_app.gamekeyprices_app.ui.category;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.mcvm_app.gamekeyprices_app.R;

public class StartpageFragment extends Fragment {

    private StartpageViewModel startpageViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        startpageViewModel =
                ViewModelProviders.of(this).get(StartpageViewModel.class);
        View root = inflater.inflate(R.layout.fragment_startpage, container, false);
        return root;
    }
}