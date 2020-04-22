package com.example.gamekeyprices_app.ui.category.fps;

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

public class FPSFragment extends Fragment {

    private FPSViewModel fpsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        fpsViewModel =
                ViewModelProviders.of(this).get(FPSViewModel.class);
        View root = inflater.inflate(R.layout.fragment_fps, container, false);
        final TextView textView = root.findViewById(R.id.text_fps);
        fpsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}