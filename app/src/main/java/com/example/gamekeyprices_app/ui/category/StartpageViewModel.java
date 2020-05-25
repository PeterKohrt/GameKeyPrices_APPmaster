package com.example.gamekeyprices_app.ui.category;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StartpageViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public StartpageViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is startpage fragment");
    }
    public LiveData<String> getText() {
        return mText;
    }
}