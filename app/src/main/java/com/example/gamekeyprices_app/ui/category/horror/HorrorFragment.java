package com.example.gamekeyprices_app.ui.category.horror;

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

public class HorrorFragment extends Fragment {

    private HorrorViewModel horrorViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        horrorViewModel =
                ViewModelProviders.of(this).get(HorrorViewModel.class);
        View root = inflater.inflate(R.layout.fragment_horror, container, false);
        final TextView textView = root.findViewById(R.id.text_horror);
        horrorViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}