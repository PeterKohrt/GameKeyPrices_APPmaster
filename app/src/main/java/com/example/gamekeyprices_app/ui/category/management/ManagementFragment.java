package com.example.gamekeyprices_app.ui.category.management;

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

public class ManagementFragment extends Fragment {

    private ManagementViewModel managementViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        managementViewModel =
                ViewModelProviders.of(this).get(ManagementViewModel.class);
        View root = inflater.inflate(R.layout.fragment_management, container, false);
        final TextView textView = root.findViewById(R.id.text_management);
        managementViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}