package com.example.gamekeyprices_app.ui.category.rpg;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.gamekeyprices_app.R;

public class RPGFragment extends Fragment {

    private RPGViewModel rpgViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rpgViewModel =
                ViewModelProviders.of(this).get(RPGViewModel.class);
        View root = inflater.inflate(R.layout.fragment_rpg, container, false);
        final TextView textView = root.findViewById(R.id.text_rpg);
        rpgViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}