package com.example.gamekeyprices_app.ui.recent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RecentViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public RecentViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is recent fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}