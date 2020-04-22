package com.example.gamekeyprices_app.ui.category.strategy;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StrategyViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public StrategyViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Strategy fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}