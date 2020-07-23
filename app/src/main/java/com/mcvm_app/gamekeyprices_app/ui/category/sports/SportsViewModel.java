package com.mcvm_app.gamekeyprices_app.ui.category.sports;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SportsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SportsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Sports fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}