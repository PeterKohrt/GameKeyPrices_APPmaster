package com.example.gamekeyprices_app.ui.category.strategy;

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

public class StrategyFragment extends Fragment {

    private StrategyViewModel strategyViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        strategyViewModel =
                ViewModelProviders.of(this).get(StrategyViewModel.class);
        View root = inflater.inflate(R.layout.fragment_strategy, container, false);
        final TextView textView = root.findViewById(R.id.text_strategy);
        strategyViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
